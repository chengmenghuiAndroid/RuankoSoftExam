package com.itee.exam.vo;

import java.io.Serializable;

public class HttpMessage<T> implements Serializable {

	private static final long serialVersionUID = -2999571571280318844L;
	private String result = "success";
	private int generatedId;
	private String messageInfo;
	
	private T object;
	
	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public String getMessageInfo() {
		return messageInfo;
	}

	public void setMessageInfo(String messageInfo) {
		this.messageInfo = messageInfo;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getGeneratedId() {
		return generatedId;
	}

	public void setGeneratedId(int generatedId) {
		this.generatedId = generatedId;
	}
	
	
}
