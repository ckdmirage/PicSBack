# PICS論壇專案  正在編寫中...
---
這是一個創作者分享論壇網站，使用者可以上傳以及刪除自己的作品，也可以按讚、收藏以及檢舉其他用戶的作品，或者與其他用戶進行追蹤交互。其他還附有標籤分類，關鍵字查詢等功能。

![示意圖](https://github.com/ckdmirage/PicSReact/blob/main/%E5%B1%95%E7%A4%BA%E7%94%A8%E5%9C%96%E7%89%87/1.png?raw=true)

## 系統架構
本系統採用前後端分離架構，後端使用Java、SpringBoot框架，採用了Spring MVC架構。資料庫方面使用JPA實現後端與資料庫的連結。 前端使用了ReactJS，前後端之間使用RESTful API實現資料交互。
此外還根據Spring Security 實作JWT，實現網站的安全管控。並根據JWT實現分流，完成用戶從低到高逐步的權限分級。
![示意圖](https://github.com/ckdmirage/PicSReact/blob/main/%E5%B1%95%E7%A4%BA%E7%94%A8%E5%9C%96%E7%89%87/2.png?raw=true)

## 資料表結構
PicS專案的資料表設計遵循第三正規化（3NF），以確保資料結構清晰、可維護性高、避免重複與異常更新。
本系統以「用戶」與「作品」為核心模組，後續依據功能需求，逐步擴充了使用者互動（追蹤、按讚、收藏）、作品內容延伸（標籤、檢舉）及輔助功能（消息通知、驗證信）等資料表設計。
![示意圖](https://github.com/ckdmirage/PicSReact/blob/main/%E5%B1%95%E7%A4%BA%E7%94%A8%E5%9C%96%E7%89%87/3.png?raw=true)

## 論壇主頁
用戶可在主頁瀏覽其他創作者已經上傳的作品，可以通過列表右上角的選擇欄選擇排序方式（上傳時間從新到舊/上傳時間從舊到新/點讚數最多）.



