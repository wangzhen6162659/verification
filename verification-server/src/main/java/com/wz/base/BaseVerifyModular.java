package com.wz.base;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * 验证码生成基本模型
 * <p>
 * 涵盖了一个对外的验证实体基本获取方法
 *
 * @author wz
 * @createTime 2018-11-07 15:03
 */
public abstract class BaseVerifyModular {
    protected CommonMethod commonMethod = new CommonMethod();

    public abstract <T extends BaseVerifyObject> T getCode();

    public abstract <T extends BaseVerifyObject> T getCode(int w, int h);

    /**
     * 带组合因子集合的绘图
     *
     * @param list 组合因子集合
     * @param w    绘图尺寸w
     * @param h    绘图尺寸h
     * @return
     */
    protected BufferedImage imageCode(List<?> list, Integer w, Integer h) {
        return null;
    }

    /**
     * 不含组合因子的绘图
     *
     * @param w
     * @param h
     * @return
     */
    protected BufferedImage imageCode(Integer w, Integer h) {
        return null;
    }

    /**
     * 继承绘图
     *
     * @param w
     * @param h
     * @return
     */
    protected BufferedImage imageCode(BufferedImage bufferedImage, Integer w, Integer h) {
        return bufferedImage;
    }
}
