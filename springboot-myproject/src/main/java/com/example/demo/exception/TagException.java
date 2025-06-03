package com.example.demo.exception;

//標籤重複例外
public class TagException extends RuntimeException{
	public TagException(String message) {
		super(message);
	}
}
