package com.example.demo.service.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.PasswordInvalidException;
import com.example.demo.exception.UserException;
import com.example.demo.exception.UserNoFoundException;
import com.example.demo.exception.UserRegisterException;
import com.example.demo.exception.UserUpdateException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.dto.PasswordChangeDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.dto.userdto.UserDto;
import com.example.demo.model.dto.userdto.UserLoginDto;
import com.example.demo.model.dto.userdto.UserManageDto;
import com.example.demo.model.dto.userdto.UserRegisterDto;
import com.example.demo.model.dto.userdto.UserUpdateNameDto;
import com.example.demo.model.entity.User;
import com.example.demo.model.entity.VerificationToken;
import com.example.demo.model.enums.NotificationMessageType;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerificationTokenRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.NotificationService;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private EmailService emailService;

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	UserMapper userMapper;

	@Autowired
	private NotificationService notificationService;

	// 獲取單個用戶
	@Override
	public UserDto getUserDto(Integer userId) {
		Optional<User> optUser = userRepository.findById(userId);
		return userMapper.toDto(optUser.orElseThrow(() -> new UserNoFoundException("用戶不存在")));
	}

	// 模糊搜索用戶
	public List<UserDto> searchByKeyword(String keyword) {
		List<User> users = userRepository.findByUsernameContainingIgnoreCase(keyword);
		return users.stream().map(userMapper::toDto).toList();
	}

	// -----------登入----------
	@Override
	public UserCertDto login(UserLoginDto userLoginDto) {
		// 驗證密碼
		User user = userRepository.getUser(userLoginDto.getUsername())
				.orElseThrow(() -> new UserNoFoundException("用戶不存在"));
		if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPasswordHash())) {
			throw new PasswordInvalidException("密碼錯誤");
		}
		if(!user.getVerified()) {
			throw new UserException("您還未通過郵箱驗證");
		}
		if ("BAN".equalsIgnoreCase(user.getRole())) {
	        throw new UserException("您的帳號已被管理員封鎖，請等待解封");
	    }

		// 生成token回傳UserCertDto權限驗證
		String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
		return new UserCertDto(user.getId(), user.getUsername(), user.getRole(), token);
	}

	// -----------註冊----------
	@Override
	@Transactional
	public void addUser(UserRegisterDto userRegisterDto) {
		validateEmailFormat(userRegisterDto.getEmail());
		// 用戶保存
		User user = saveUser(userRegisterDto);

		// 產token驗證與發送
		createAndSendVerificationToken(user);
	}

	// 註冊子方法-用戶保存
	private User saveUser(UserRegisterDto userRegisterDto) {
		if (userRepository.existsByUsername(userRegisterDto.getUsername())) {
			throw new UserRegisterException("用戶名已被註冊");
		}
		String passwordHash = passwordEncoder.encode(userRegisterDto.getPassword());

		User user = new User(null, userRegisterDto.getUsername(), userRegisterDto.getEmail(), passwordHash, false,
				userRegisterDto.getCreated(), userRegisterDto.getRole(),
				"http://localhost:8081/myprojectImg/avatar/default.png", new ArrayList<>(), new ArrayList<>());
		return userRepository.save(user); // 這樣就含有id了
	}

	// 註冊子方法-郵箱token發送
	private void createAndSendVerificationToken(User user) {
		verificationTokenRepository.deleteByUser_Id(user.getId());
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
		verificationToken.setNewEmail(null);
		verificationToken.setNewPasswordHash(null);
		verificationTokenRepository.save(verificationToken);

		//
		String verifyLink = buildFrontendVerifyLink("register", token);
		emailService.sendEmail(user.getEmail(), "請驗證您的帳號", "請點擊以下連結完成驗證：\n" + verifyLink);
	}

	// 註冊郵箱token驗證
	@Override
	@Transactional
	public void verifyUserRegister(String token) {
		VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
				.orElseThrow(() -> new UserUpdateException("Token無效或不存在"));

		if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			throw new UserUpdateException("Token 已過期");
		}
		String newEmail = verificationToken.getNewEmail();
		String newPasswordHash = verificationToken.getNewPasswordHash();
		if (newEmail != null && newPasswordHash != null) {
			throw new UserUpdateException("Token 並非用於帳號註冊");
		}
		// 驗證成功,修改用戶郵箱驗證:
		User user = verificationToken.getUser();
		user.setVerified(true);
		userRepository.save(user);
		// 刪除token
		verificationTokenRepository.delete(verificationToken);
	}

	// 子方法 驗證郵箱格式
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

	private void validateEmailFormat(String email) {
		if (!EMAIL_PATTERN.matcher(email).matches()) {
			throw new UserUpdateException("請輸入合法的信箱格式");
		}
	}

	// 子方法 郵件發送路徑
	private String buildFrontendVerifyLink(String type, String token) {
		String frontendBaseUrl = "http://localhost:5173"; // TODO: 正式上線時改為正式網址
		return frontendBaseUrl + "/verify/result?type=" + type + "&token=" + token;
	}

	// 修改頭像
	@Override
	@Transactional
	public String updateAvatar(MultipartFile file, UserCertDto userCertDto) {
		try {
			// 存新頭像
			String imageUrl = fileStorageService.storeFile(file, "avatar");
			// 查使用者
			User user = userRepository.findById(userCertDto.getUserId())
					.orElseThrow(() -> new UserNoFoundException("用戶不存在"));
			// 判斷非default，刪除舊圖
			String oldAvatarUrl = user.getAvatarUrl();
			String baseUrl = "http://localhost:8081/myprojectImg/avatar/";
			String localDir = "E:/HTMLCSSJavaScript/myprojectImg/avatar/"; // E:/HTMLCSSJavaScript/myprojectImg/
																			// D:/myprojectImg/avatar/
			if (oldAvatarUrl != null && oldAvatarUrl.startsWith(baseUrl) && !oldAvatarUrl.contains("/default.png")) {
				String relativePath = oldAvatarUrl.substring(baseUrl.length());
				File oldFile = new File(localDir + relativePath);
				if (oldFile.exists()) {
					boolean deleted = oldFile.delete();
					if (!deleted) {
						System.err.println("刪除失敗：" + oldFile.getAbsolutePath());
					}
				}
			}
			// 更新頭像
			user.setAvatarUrl(imageUrl);
			userRepository.save(user);
			return imageUrl;
		} catch (IOException e) {
			throw new RuntimeException("頭像儲存失敗: " + e.getMessage(), e);
		}
	}

	// 修改用戶名
	@Override
	@Transactional
	public UserCertDto updateName(Integer userId, UserUpdateNameDto userUpdateNameDto) {
		String newUsername = userUpdateNameDto.getUsername();
		// 檢查空值或空白
		if (newUsername == null || newUsername.isBlank()) {
			throw new UserUpdateException("名稱不能為空");
		}
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNoFoundException("用戶不存在"));
		// 檢查無效修改
		if (newUsername.equals(user.getUsername())) {
			throw new UserUpdateException("無法輸入重複名字");
		}
		// 檢查重名
		boolean exists = userRepository.existsByUsername(newUsername);
		if (exists) {
			throw new UserUpdateException("該名稱已被使用");
		}
		// 更新名稱
		user.setUsername(newUsername);
		userRepository.save(user);
		// 重新產生token回傳
		String token = jwtUtil.generateToken(user.getId(), newUsername, user.getRole());
		return new UserCertDto(user.getId(), newUsername, user.getRole(), token);
	}

	// 修改郵箱-token發送
	@Override
	@Transactional
	public void requestEmailChange(Integer userId, String newEmail) {
		verificationTokenRepository.deleteByUser_Id(userId);
		validateEmailFormat(newEmail);
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNoFoundException("查無此用戶"));

		// 檢查無效修改
		if (newEmail.equals(user.getEmail())) {
			throw new UserUpdateException("新信箱與目前信箱相同");
		}
		// 非demo還要考慮新郵箱唯一性驗證

		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationToken.setExpiryDate(LocalDateTime.now().plusHours(1));
		verificationToken.setNewEmail(newEmail);
		verificationToken.setNewPasswordHash(null);
		verificationTokenRepository.save(verificationToken);

		String verifyLink = buildFrontendVerifyLink("email", token);
		emailService.sendEmail(newEmail, "請驗證您的新信箱", "請點擊以下連結完成信箱修改：\n" + verifyLink);
	}

	// 修改郵箱-token驗證
	@Override
	@Transactional
	public void verifyEmailChange(String token) {
		VerificationToken vt = verificationTokenRepository.findByToken(token)
				.orElseThrow(() -> new UserUpdateException("Token 無效或不存在"));

		if (vt.getExpiryDate().isBefore(LocalDateTime.now())) {
			throw new UserUpdateException("Token 已過期");
		}

		String newEmail = vt.getNewEmail();
		if (newEmail == null || newEmail.isBlank()) {
			throw new UserUpdateException("Token 並非用於信箱修改");
		}

		User user = vt.getUser();
		user.setEmail(newEmail);
		userRepository.save(user);

		verificationTokenRepository.delete(vt);
	}

	// 修改密碼-token發送
	@Override
	@Transactional
	public void requestPasswordChange(Integer userId, PasswordChangeDto passwordChangeDto) {
		verificationTokenRepository.deleteByUser_Id(userId);
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNoFoundException("查無此用戶"));

		// 兩次密碼不一致判斷
		if (!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmPassword())) {
			throw new UserUpdateException("兩次輸入的密碼不一致");
		}
		// 檢查無效修改
		if (passwordEncoder.matches(passwordChangeDto.getNewPassword(), user.getPasswordHash())) {
			throw new UserUpdateException("新密碼不能與舊密碼相同");
		}

		// 3. 加密新密碼
		String newPassword = passwordEncoder.encode(passwordChangeDto.getNewPassword());

		// 4. 建立 token
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationToken.setExpiryDate(LocalDateTime.now().plusHours(1));
		verificationToken.setNewEmail(null);
		verificationToken.setNewPasswordHash(newPassword);

		verificationTokenRepository.save(verificationToken);

		// 5. 寄信
		String verifyLink = buildFrontendVerifyLink("password", token);
		emailService.sendEmail(user.getEmail(), "請驗證您的密碼修改", "請點擊以下連結完成密碼變更：\n" + verifyLink);
	}

	// 修改密碼-token驗證
	@Override
	@Transactional
	public void verifyPasswordChange(String token) {
		VerificationToken vt = verificationTokenRepository.findByToken(token)
				.orElseThrow(() -> new RuntimeException("Token 無效或不存在"));

		if (vt.getExpiryDate().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("Token 已過期");
		}

		String newPasswordHash = vt.getNewPasswordHash();
		if (newPasswordHash == null || newPasswordHash.isBlank()) {
			throw new RuntimeException("Token 並非用於密碼修改");
		}

		User user = vt.getUser();
		user.setPasswordHash(newPasswordHash);
		userRepository.save(user);

		verificationTokenRepository.delete(vt);
	}

	// 管理員權限：用戶列表
	@Override
	public List<UserManageDto> getAllUsersForAdmin(UserCertDto operator) {
		List<User> users;

		if ("ROOT".equals(operator.getRole())) {
			// 🔓 ROOT 可看所有（包含其他 ADMIN）
			users = userRepository.findByRoleNotIn(List.of("ROOT"));
		} else if ("ADMIN".equals(operator.getRole())) {
			// 🔒 ADMIN 不能看其他 ADMIN 或 ROOT
			users = userRepository.findByRoleNotIn(List.of("ADMIN", "ROOT"));
		} else {
			throw new UserException("權限受限！");
		}

		return users.stream().map(user -> new UserManageDto(user.getId(), user.getUsername(), user.getRole(),
				user.getAvatarUrl(), user.getCreated())).toList();
	}

	// 管理員權限：處理用戶權限
	@Override
	@Transactional
	public void updateUserRole(Integer userId, String newRole, UserCertDto operator) {
		String normalizedRole = newRole == null ? "" : newRole.trim().toUpperCase();
		if (!List.of("USER", "BAN", "ADMIN").contains(normalizedRole)) {
			throw new IllegalArgumentException("角色錯誤");
		}

		if (Objects.equals(userId, operator.getUserId())) {
			throw new UserException("不能更改自己的角色");
		}

		User target = userRepository.findById(userId).orElseThrow(() -> new UserException("目標用戶不存在"));

		String oldRole = target.getRole();

		// 🔐 權限驗證
		switch (operator.getRole()) {
		case "ROOT" -> {
			// OK
		}
		case "ADMIN" -> {
			if ("ADMIN".equals(normalizedRole)) {
				throw new UserException("您沒有權限指派管理員");
			}
			if ("ADMIN".equals(oldRole)) {
				throw new UserException("您沒有權限修改管理員的角色");
			}
		}
		default -> throw new UserException("您沒有權限更改他人角色");
		}

		// ✅ 執行更新
		target.setRole(normalizedRole);
		userRepository.save(target);

		// 📩 通知：只有涉及 admin 權限變化才通知
		if (!oldRole.equals(normalizedRole)) {
			boolean isAdminChange = "ADMIN".equals(oldRole) || "ADMIN".equals(normalizedRole);
			if (isAdminChange) {
				User sender = userRepository.findById(operator.getUserId())
						.orElseThrow(() -> new UserException("操作者不存在"));

				NotificationMessageType type = switch (normalizedRole) {
				case "ADMIN" -> NotificationMessageType.ROLE_UPGRADED;
				case "USER" -> NotificationMessageType.ROLE_DOWNGRADED;
				default -> null;
				};

				if (type != null) {
					notificationService.sendNotification(sender, target, type);
				}
			}
		}
	}

}