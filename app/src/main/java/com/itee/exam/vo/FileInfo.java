package com.itee.exam.vo;

import java.io.Serializable;

/**
 * Created by gonglei on 2015-07-20.
 */
public class FileInfo implements Serializable {

    private String fileName;

    private String filePath;

    public FileInfo(){}

    public FileInfo(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
