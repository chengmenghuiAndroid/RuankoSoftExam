package com.itee.exam.app.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rkcoe on 2016/10/26.
 */

public class Courses implements Serializable {
    private static final long serialVersionUID = -7437829190230693908L;

    /**
     * coursesId : 1
     * coursesRuankoId : ERQFSDGYRTSDFG2343645
     * coursesImg : http://static.ruanko.com/course/2015-08-19/admin/centerTask/thumbnail/0a100e3a036e4eaa9503889170451396
     * coursesIntro : 第一部分：进行软考科普
     主要了解的是：软考包过班介绍、国家软考介绍、软考报名流程、全国各地报名地址及电话

     第二部分：直播试听
     选取各资格考试中两场直播供各位备考人员试听。在线直播主要是通过腾讯课堂讲解，在软酷网官网上会有录播视频。直播课中通过直播讲解备考的知识点及与学员互动，解答备考当中的相关问题。

     第三部分：真题视频解析
     选取各资格考试最新下午卷(实践能力考核)考题进行详细视频解析，讲解如何读懂题、分析业务和知识、掌握解题思路和技巧、正确解题，以达到举一反三、触类旁通之功效。
     * coursesTags : 精品课
     * coursesTitle : 软酷软考大型公开课
     * coursesPrice : 100
     * countHits : 500
     * coursesPraise : 5
     * lastUpdatetime : 1477375951000
     */

    private int coursesId;
    private String coursesRuankoId;
    private String coursesImg;
    private String coursesIntro;
    private String coursesTags;
    private String coursesTitle;
    private Double coursesPrice;
    private int countHits;
    private String coursesPraise;
    private String lastUpdatetime;

    public String getLastUpdatetime() {
        return lastUpdatetime;
    }

    public void setLastUpdatetime(String lastUpdatetime) {
        this.lastUpdatetime = lastUpdatetime;
    }

    private List<ChaptersList> chaptersList;

    public List<ChaptersList> getChaptersList() {
        return chaptersList;
    }

    public void setChaptersList(List<ChaptersList> chaptersList) {
        this.chaptersList = chaptersList;
    }

    public int getCoursesId() {
        return coursesId;
    }

    public void setCoursesId(int coursesId) {
        this.coursesId = coursesId;
    }

    public String getCoursesRuankoId() {
        return coursesRuankoId;
    }

    public void setCoursesRuankoId(String coursesRuankoId) {
        this.coursesRuankoId = coursesRuankoId;
    }

    public String getCoursesImg() {
        return coursesImg;
    }

    public void setCoursesImg(String coursesImg) {
        this.coursesImg = coursesImg;
    }

    public String getCoursesIntro() {
        return coursesIntro;
    }

    public void setCoursesIntro(String coursesIntro) {
        this.coursesIntro = coursesIntro;
    }

    public String getCoursesTags() {
        return coursesTags;
    }

    public void setCoursesTags(String coursesTags) {
        this.coursesTags = coursesTags;
    }

    public String getCoursesTitle() {
        return coursesTitle;
    }

    public void setCoursesTitle(String coursesTitle) {
        this.coursesTitle = coursesTitle;
    }

    public Double getCoursesPrice() {
        return coursesPrice;
    }

    public void setCoursesPrice(Double coursesPrice) {
        this.coursesPrice = coursesPrice;
    }

    public int getCountHits() {
        return countHits;
    }

    public void setCountHits(int countHits) {
        this.countHits = countHits;
    }

    public String getCoursesPraise() {
        return coursesPraise;
    }

    public void setCoursesPraise(String coursesPraise) {
        this.coursesPraise = coursesPraise;
    }

}
