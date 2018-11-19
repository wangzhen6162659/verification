package com.wz.util;

import com.wz.entity.OperationVerifyObject;
import com.wz.entity.PicVerifyObject;
import com.wz.modular.OperationVerifyModular;
import com.wz.modular.PicVerifyModular;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class VerificationCodeUtils {
    private PicVerifyModular picVerifyModular = new PicVerifyModular();
    private OperationVerifyModular operationVerifyModular = new OperationVerifyModular();

    /**
     * 获取图形识别验证码
     *
     * @param widthNum  自定义列数
     * @param heightNum 自定义行数
     * @return
     */
    public PicVerifyObject getPicVerifyCode(int widthNum, int heightNum) {
        return picVerifyModular.getCode(widthNum, heightNum);
    }

    /**
     * 获取图形识别验证码
     *
     * @return
     */
    public PicVerifyObject getPicVerifyCode() {
        return picVerifyModular.getCode();
    }

    /**
     * 图形分类资源加载-默认资源 默认资源仅适用于springboot
     */
    public void initImgObejct() {
        picVerifyModular.initImgObejct();
    }

    /**
     * 图形分类资源加载-自定义+默认资源
     *
     * @param path
     */
    public void initImgObejct(String path) {
        picVerifyModular.initImgObejct(path);
    }

    /**
     * 图形分类资源加载-自定义资源
     *
     * @param path
     * @param useDefult
     */
    public void initImgObejct(String path, boolean useDefult) {
        picVerifyModular.initImgObejct(path, useDefult);
    }

    /**
     * 获取运算符验证码
     *
     * @return
     */
    public OperationVerifyObject getOperationVerifyCode() {
        return operationVerifyModular.getCode();
    }

    /**
     * 获取运算符验证码,字体会随h变化,尽量 w = 4 * h
     *
     * @param w 自定义长
     * @param h 自定义宽
     * @return
     */
    public OperationVerifyObject getOperationVerifyCode(int w, int h) {
        return operationVerifyModular.getCode(w, h);
    }


    /**
     * 验证用户传入坐标是否正确
     *
     * @param cache    生成验证图片时的缓存坐标
     * @param locals   用户传进来的坐标
     * @param widthNum 验证图片的横向个数
     * @param widthVal 验证图片的边长
     * @return
     */
    public boolean DistinguishPicVerifyCode(List<Integer> cache, List<Integer> locals, Integer widthNum, Float widthVal) {
        return picVerifyModular.DistinguishPicVerifyCode(cache, locals, widthNum, widthVal);
    }
}
