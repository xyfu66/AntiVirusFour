package com.jinyun.antivirusfour.health.cook.entity;

/**
 *
 * 菜谱的类别信息
 */
public class CookMenu {
    private int id;
    private String name;
    private int imgId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
