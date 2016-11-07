package com.itee.exam.app.entity;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class QuestionContent implements Serializable{

	private String title;
	private String titleImg = "";
	private LinkedHashMap<String, String> choiceList;
	private LinkedHashMap<String, String> choiceImgList;

	private String fileUrl = "";
	private String spaceCount = "1";

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getSpaceCount() {
		return spaceCount;
	}

	public void setSpaceCount(String spaceCount) {
		this.spaceCount = spaceCount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleImg() {
		return titleImg;
	}

	public void setTitleImg(String titleImg) {
		this.titleImg = titleImg;
	}

	public LinkedHashMap<String, String> getChoiceList() {
		return choiceList;
	}

	public void setChoiceList(LinkedHashMap<String, String> choiceList) {
		this.choiceList = choiceList;
	}

	public LinkedHashMap<String, String> getChoiceImgList() {
		return choiceImgList;
	}

	public void setChoiceImgList(LinkedHashMap<String, String> choiceImgList) {
		this.choiceImgList = choiceImgList;
	}

}
