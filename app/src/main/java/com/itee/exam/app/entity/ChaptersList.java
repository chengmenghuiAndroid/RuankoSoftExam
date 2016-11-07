package com.itee.exam.app.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rkcoe on 2016/10/26.
 */

public class ChaptersList implements Serializable {

    private static final long serialVersionUID = -3935391663323767111L;


    /**
     * chaptersId : 1
     * chaptersRuankoId : QWEQWEQE21313
     * chaptersSort : 1
     * chaptersTags : 软考
     * chaptersTitle : 软考介绍
     * chaptersType : 1
     * coursesId : 1
     */

    private int chaptersId;
    private String chaptersRuankoId;
    private int chaptersSort;
    private String chaptersTags;
    private String chaptersTitle;
    private String chaptersType;
    private int coursesId;
    private List<SectionsList> sectionsList;

    public List<SectionsList> getSectionsList() {
        return sectionsList;
    }

    public void setSectionsList(List<SectionsList> sectionsList) {
        this.sectionsList = sectionsList;
    }

    public int getChaptersId() {
        return chaptersId;
    }

    public void setChaptersId(int chaptersId) {
        this.chaptersId = chaptersId;
    }

    public String getChaptersRuankoId() {
        return chaptersRuankoId;
    }

    public void setChaptersRuankoId(String chaptersRuankoId) {
        this.chaptersRuankoId = chaptersRuankoId;
    }

    public int getChaptersSort() {
        return chaptersSort;
    }

    public void setChaptersSort(int chaptersSort) {
        this.chaptersSort = chaptersSort;
    }

    public String getChaptersTags() {
        return chaptersTags;
    }

    public void setChaptersTags(String chaptersTags) {
        this.chaptersTags = chaptersTags;
    }

    public String getChaptersTitle() {
        return chaptersTitle;
    }

    public void setChaptersTitle(String chaptersTitle) {
        this.chaptersTitle = chaptersTitle;
    }

    public String getChaptersType() {
        return chaptersType;
    }

    public void setChaptersType(String chaptersType) {
        this.chaptersType = chaptersType;
    }

    public int getCoursesId() {
        return coursesId;
    }

    public void setCoursesId(int coursesId) {
        this.coursesId = coursesId;
    }
}
