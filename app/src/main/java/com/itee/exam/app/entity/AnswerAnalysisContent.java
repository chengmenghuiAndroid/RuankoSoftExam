package com.itee.exam.app.entity;

import java.io.Serializable;

/**
 * Created by rkcoe on 2016/9/21.
 */

public class AnswerAnalysisContent implements Serializable {
    private String analysisContent;
    private String fileUrl = "";

    public String getAnalysisContent() {
        return analysisContent;
    }

    public void setAnalysisContent(String analysisContent) {
        this.analysisContent = analysisContent;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}