package com.itee.exam.app.entity;

import java.io.Serializable;

/**
 * Created by rkcoe on 2016/10/26.
 */
public class SectionsList implements Serializable {
    private static final long serialVersionUID = -2063008418984078050L;

    /**
     * sectionsId : 3
     * sectionsRuankoId : WEGJSGDHHIJTYUEWE3
     * sectionsSort : 1
     * sectionsTitle : 第3课
     * sectionsType : 1
     * sectionsUrl : http://v.ruanko.com/fd39f42585ad45a88fbf42167bd6983c_original.mp4?e=1477395081&token=kEUFey_36xJqHaOhr44fNd8eNUo4a9TLtMJSrqhu:53u0MmjSvoyr8jA0_FktvIlCQps=
     * chaptersId : 2
     */

    private int sectionsId;
    private String sectionsRuankoId;
    private int sectionsSort;
    private String sectionsTitle;
    private String sectionsType;
    private String sectionsFluentUrl;
    private String sectionsSDUrl;
    private String sectionsHDUrl;
    private int chaptersId;

    public String getSectionsFluentUrl() {
        return sectionsFluentUrl;
    }

    public void setSectionsFluentUrl(String sectionsFluentUrl) {
        this.sectionsFluentUrl = sectionsFluentUrl;
    }

    public String getSectionsHDUrl() {
        return sectionsHDUrl;
    }

    public void setSectionsHDUrl(String sectionsHDUrl) {
        this.sectionsHDUrl = sectionsHDUrl;
    }

    public String getSectionsSDUrl() {
        return sectionsSDUrl;
    }

    public void setSectionsSDUrl(String sectionsSDUrl) {
        this.sectionsSDUrl = sectionsSDUrl;
    }

    public int getSectionsId() {
        return sectionsId;
    }

    public void setSectionsId(int sectionsId) {
        this.sectionsId = sectionsId;
    }

    public String getSectionsRuankoId() {
        return sectionsRuankoId;
    }

    public void setSectionsRuankoId(String sectionsRuankoId) {
        this.sectionsRuankoId = sectionsRuankoId;
    }

    public int getSectionsSort() {
        return sectionsSort;
    }

    public void setSectionsSort(int sectionsSort) {
        this.sectionsSort = sectionsSort;
    }

    public String getSectionsTitle() {
        return sectionsTitle;
    }

    public void setSectionsTitle(String sectionsTitle) {
        this.sectionsTitle = sectionsTitle;
    }

    public String getSectionsType() {
        return sectionsType;
    }

    public void setSectionsType(String sectionsType) {
        this.sectionsType = sectionsType;
    }

    public int getChaptersId() {
        return chaptersId;
    }

    public void setChaptersId(int chaptersId) {
        this.chaptersId = chaptersId;
    }
}
