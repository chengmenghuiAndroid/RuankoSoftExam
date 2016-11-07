package com.itee.exam.vo;

import java.io.Serializable;

/**
 * 专家查询过滤条件
 * Created by xin on 2015-07-06.
 */
public class GeometryQuery implements Serializable {
    public static final double border = 0.00001f;

    /**
     * 中心点的经纬度
     */
    private double lng;
    private double lat;

    /**
     * 过滤的半径范围
     */
    private int radius;

    /**
     * 选择服务的ID
     */
    private int serviceId;

    /**
     * 选择的技术方向的ID
     */
    private int[] techId;

    private int customerId;

    // 辅助字段
    /**
     * 选择的技术分类
     */
    private int categoryId;

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int[] getTechId() {
        return techId;
    }

    public void setTechId(int[] techId) {
        this.techId = techId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public boolean isLocation() {
        return lat > border|| lng > border;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
