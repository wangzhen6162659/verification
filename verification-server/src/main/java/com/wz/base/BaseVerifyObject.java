package com.wz.base;

import lombok.Data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Data
public class BaseVerifyObject {
    /**
     * 验证图片
     */
    private BufferedImage img;

    public BaseVerifyObject() {
    }

    public BaseVerifyObject(BufferedImage img) {
        this.img = img;
    }


    /**
     * 读取BufferedImage 并返回图片到前端
     *
     * @param os
     */
    public void retBufferImg(OutputStream os) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            ImageIO.write(this.img, "png", out);
            os.write(out.toByteArray());
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
