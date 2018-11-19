package com.wz.entity;

import com.wz.base.BaseVerifyObject;
import lombok.Data;

import java.awt.image.BufferedImage;

@Data
public class OperationVerifyObject extends BaseVerifyObject {
    /**
     * 验证公式
     */
    private String formula;
    /**
     * 验证值
     */
    private String result;

    /**
     * @param formula
     * @param result
     * @param img
     */
    public OperationVerifyObject(String formula, String result, BufferedImage img) {
        super(img);
        this.formula = formula;
        this.result = result;
    }

}
