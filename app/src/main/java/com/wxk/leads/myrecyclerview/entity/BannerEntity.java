package com.wxk.leads.myrecyclerview.entity;

/**
 * Created by Administrator on 2017/3/19
 */

public class BannerEntity {

    private Integer img;
    private String desc;

    public BannerEntity(Integer img, String desc) {
        this.img = img;
        this.desc = desc;
    }

    public BannerEntity() {
    }

    @Override
    public String toString() {
        return "BannerEntity{" +
                "img='" + img + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
