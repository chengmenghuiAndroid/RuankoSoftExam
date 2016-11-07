package com.itee.exam.app.entity;

import java.io.Serializable;

/**
 * Created by rkcoe on 2016/11/3.
 */

public class VideoUrlBean implements Serializable {

    /**
     * image : ed3b6c02f3974736a110dc0b72513c51_image.jpg
     * low : http://v.ruanko.com/ed3b6c02f3974736a110dc0b72513c51_low.mp4?e=1478152847&token=kEUFey_36xJqHaOhr44fNd8eNUo4a9TLtMJSrqhu:yK6jQBNhwV7U4wXRLg5vlp6Sq0k=
     * normal : http://v.ruanko.com/ed3b6c02f3974736a110dc0b72513c51_normal.mp4?e=1478152847&token=kEUFey_36xJqHaOhr44fNd8eNUo4a9TLtMJSrqhu:xxQpFSym2aZ0GbPOyWBP9S2weuA=
     * original : http://v.ruanko.com/ed3b6c02f3974736a110dc0b72513c51_original.mp4?e=1478152847&token=kEUFey_36xJqHaOhr44fNd8eNUo4a9TLtMJSrqhu:tMmflGGihsArk1Gik9Szfo9jaR8=
     * result : 1
     */

    private String image;
    private String low;
    private String normal;
    private String original;
    private int result;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getNormal() {
        return normal;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
