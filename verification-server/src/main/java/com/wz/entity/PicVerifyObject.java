package com.wz.entity;

import com.wz.base.BaseVerifyObject;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Data
public class PicVerifyObject extends BaseVerifyObject {
    /**
     * 用户需要点击次数
     */
    private int clickNum;
    /**
     * 后端验证点击下标集合
     */
    private List<Integer> locations = new ArrayList<>();
    /**
     * 验证图片集合
     */
    private List<InputStream> finalImages;

    public PicVerifyObject(BufferedImage img, int clickNum, List<Integer> locations, List<InputStream> finalImages) {
        super(img);
        this.clickNum = clickNum;
        this.locations = locations;
        this.finalImages = finalImages;
    }

    public PicVerifyObject() {
        super();
    }
}
