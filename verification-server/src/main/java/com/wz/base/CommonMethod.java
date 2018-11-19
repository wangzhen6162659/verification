package com.wz.base;

import com.wz.Exception.VerifyException;
import com.wz.Exception.VerifyExceptionEnum;
import com.wz.entity.ImgDTO;
import com.wz.util.VerificationCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.awt.Color;
import java.awt.Graphics;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 绘图基本方法
 *
 * @author wz
 * @createTime 2018-11-06 16:20
 */
@Slf4j
public class CommonMethod {
    public static Random generator = new Random();
    public final String PATH_DEFULT_IMG = "img_distinguish";

    /**
     * 获得随机颜色
     *
     * @param begin 范围起点
     * @param end   范围末
     * @return
     */
    public Color getRandColor(int begin, int end) {
        if (begin > 255)
            begin = 255;
        if (end > 255)
            end = 255;
        int r = begin + generator.nextInt(end - begin);
        int g = begin + generator.nextInt(end - begin);
        int b = begin + generator.nextInt(end - begin);
        return new Color(r, g, b);
    }

    /**
     * byte转is
     *
     * @param buf
     * @return
     */
    public final InputStream byte2Input(byte[] buf) {
        return new ByteArrayInputStream(buf);
    }

    /**
     * byte转is(集合)
     *
     * @param bufs
     * @return
     */
    public final List<InputStream> byte2Input(List<byte[]> bufs) {
        List<InputStream> inputStreams = new ArrayList<>();
        for (int i = 0; i < bufs.size(); i++) {
            inputStreams.add(byte2Input(bufs.get(i)));
        }
        return inputStreams;
    }

    /**
     * is转byte[]
     *
     * @param inStream
     * @return
     * @throws IOException
     */
    public final byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    /**
     * 获取无序String集合
     *
     * @param strs
     * @return
     */
    public String getRandom(List<String> strs) {
        return strs.get(generator.nextInt(strs.size()));
    }

    /**
     * 获取一个范围内随机值
     *
     * @param size
     * @return
     */
    public Integer getRandom(int size) {
        int result = generator.nextInt(size);
        return result;
    }

    /**
     * 图像偏移
     *
     * @param g      纹理
     * @param beginX 偏移起始点
     * @param w1     x偏移量
     * @param h1     y偏移量
     * @param value  偏移权值
     */
    public void shear(Graphics g, int beginX, int w1, int h1, Float value) {
        value = value == null ? 1 : value;
        shearX(g, beginX, w1, h1, value);
        shearY(g, beginX, w1, h1, value);
    }

    /**
     * 图像偏移X
     *
     * @param g
     * @param beginX
     * @param w1
     * @param h1
     */
    public void shearX(Graphics g, int beginX, int w1, int h1, float vlaue) {
        float tag = generator.nextFloat();
        int negativeTag = tag > 0.5 ? 1 : -1;

        int period = (int) (generator.nextFloat() * 4 * (-negativeTag));

        int frames = 1;
        int phase = 1;

        for (int i = 0; i < h1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / frames);
            g.copyArea(beginX, i, w1, 1, (int) (d * negativeTag * vlaue), 0);
        }
    }

    /**
     * 图像偏移Y
     *
     * @param g
     * @param beginX
     * @param w1
     * @param h1
     */
    public void shearY(Graphics g, int beginX, int w1, int h1, float vlaue) {
        float tag = generator.nextFloat();
        int negativeTag = tag > 0.5 ? 1 : -1;

        int period = generator.nextInt(3) + 10;

        int frames = 1;
        int phase = 1;
        for (int i = 0; i < w1 + 100 - beginX; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / (double) frames);
            g.copyArea(beginX + i, 0, 1, h1 - 5, 0, (int) (d * negativeTag * vlaue));
        }
    }

    /**
     * 设置干扰线 修正
     *
     * @param g   纹理
     * @param w   x范围值(0~w)
     * @param h   y范围值(0~h)
     * @param dx  x范围值修正
     * @param dy  y范围值修正
     * @param num 生成干扰线数量
     */
    public void setInterferenceLine(Graphics g, int w, int h, int dx, int dy, int num) {
        //干扰线
        for (int i = 0; i < num; i++) {
            g.setColor(new Color(generator.nextInt(255) + 1, generator.nextInt(255) + 1, generator.nextInt(255) + 1));
            g.drawLine(generator.nextInt(w) + dx, generator.nextInt(h) + dy,
                    generator.nextInt(w) + dx, generator.nextInt(h) + dy);
        }
    }

    /**
     * 设置干扰线
     *
     * @param g   纹理
     * @param w   x范围值(0~w)
     * @param h   y范围值(0~h)
     * @param num 生成干扰线数量
     */
    public void setInterferenceLine(Graphics g, int w, int h, int num) {
        //干扰线
        for (int i = 0; i < num; i++) {
            g.setColor(new Color(generator.nextInt(255) + 1, generator.nextInt(255) + 1, generator.nextInt(255) + 1));
            g.drawLine(generator.nextInt(w), generator.nextInt(h),
                    generator.nextInt(w), generator.nextInt(h));
        }
    }

    /**
     * 获取jar内容
     *
     * @param imgPath
     * @return
     */
    List<ImgDTO> getFilesByJar(String imgPath) {
        Enumeration<JarEntry> jarEntrys = getJarEntrys(imgPath);
        if (jarEntrys == null || !jarEntrys.hasMoreElements()) {
            return null;
        }
        List<ImgDTO> dtos = new ArrayList<>();
        boolean tag = false;
        while (jarEntrys.hasMoreElements()) {
            JarEntry entry = jarEntrys.nextElement();
            String name = entry.getName();
            if (entry.isDirectory() && name.startsWith(imgPath, name.indexOf(imgPath)) && !name.endsWith(imgPath + "/")) {
                ImgDTO imgDTO = new ImgDTO();
                String tipName = name.substring(name.indexOf(imgPath) + imgPath.length() + 1, name.length() - 1);
                imgDTO.setName(tipName);
                getChildFilesByJar(imgDTO.getChilds(), name.substring(0, name.length() - 1));
                dtos.add(imgDTO);
                tag = true;
            }
        }
        if (!tag) {
            log.info(imgPath + VerifyExceptionEnum.PATH_RESOURCE_NOT_EFFECTIVE.getMsg());
        }
        return dtos;
    }

    /**
     * 获取子jar内容
     *
     * @param imgPath
     */
    private void getChildFilesByJar(List<ImgDTO> dtos, String imgPath) {
        Enumeration<JarEntry> jarEntrys = getJarEntrys(imgPath);
        if (jarEntrys == null || !jarEntrys.hasMoreElements()) {
            return;
        }
        while (jarEntrys.hasMoreElements()) {
            JarEntry entry = jarEntrys.nextElement();
            String name = entry.getName();
            if (!entry.isDirectory() && name.startsWith(imgPath, name.indexOf(imgPath))) {
                ImgDTO imgDTO = new ImgDTO();
                String tipName = name.substring(name.indexOf(imgPath) + imgPath.length() + 1, name.lastIndexOf("."));
                imgDTO.setName(tipName);
                imgDTO.setFile(getFile(imgPath + "/" + name.substring(name.indexOf(imgPath) + imgPath.length() + 1, name.length())));
                try {
                    imgDTO.setBytes(input2byte(imgDTO.getFile()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dtos.add(imgDTO);
            }
        }
    }

    /**
     * 获得jar中映射元素
     *
     * @param imgPath
     * @return
     */
    private Enumeration<JarEntry> getJarEntrys(String imgPath) {
        URL url = VerificationCodeUtils.class.getClassLoader().getResource("");
        String jarPath = url.toString();

        if (imgPath.indexOf(PATH_DEFULT_IMG) != -1) {
            String classPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            if (!classPath.endsWith("!/")) {
                classPath = "file:" + classPath + "!/";
            }
            jarPath = "jar:" + classPath;
        }

        jarPath = jarPath + imgPath;

        JarURLConnection jarCon = null;
        Enumeration<JarEntry> jarEntrys = null;

        try {
            URL urlObj = new URL(jarPath);
            URLConnection urlConnection = urlObj.openConnection();
            if (urlConnection instanceof JarURLConnection) {
                jarCon = (JarURLConnection) urlConnection;
                if (jarCon.getJarFile() == null) {
                    return null;
                }
                JarFile jarFile = jarCon.getJarFile();
                jarEntrys = jarFile.entries();
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new VerifyException(VerifyExceptionEnum.PATH_READ_FAIL, jarPath);
        }
        return jarEntrys;
    }

    /**
     * 获取目标流
     *
     * @param imgPath
     * @return
     */
    private InputStream getFile(String imgPath) {
        log.info("加载:" + imgPath);
        InputStream inputStream = VerificationCodeUtils.class.getClassLoader().getResourceAsStream(imgPath);
        if (inputStream == null) {
            log.info("错误资源链接！：" + imgPath);
        }
        return inputStream;
    }

    /**
     * 读取磁盘某个文件夹下的所有文件
     *
     * @param filepath
     * @return
     */
    List<ImgDTO> getFileByDisk(String filepath) {
        List<ImgDTO> list = new ArrayList<>();
        Resource fileRource = new ClassPathResource(filepath);

        try {
            if (fileRource.getFile() == null) {
                return new ArrayList<>();
            }
            if (fileRource.getFile().isDirectory()) {
                String[] filelist = fileRource.getFile().list();
                for (int i = 0; i < filelist.length; i++) {
                    fileRource = new ClassPathResource(filepath + "\\" + filelist[i]);
                    if (fileRource.getFile().isDirectory()) {
                        ImgDTO dto = new ImgDTO(filelist[i], null, null);
                        getFileChildByDisk(filepath + "\\" + filelist[i], dto.getChilds());
                        list.add(dto);
                    }
                }
            }
        } catch (IOException e) {
            throw new VerifyException(VerifyExceptionEnum.PATH_READ_FAIL, filepath);
        }
        return list;
    }

    /**
     * 读取磁盘某个文件夹下的所有文件
     *
     * @param filepath 当前递归路径
     * @param list     文件二维集合
     */
    private void getFileChildByDisk(String filepath, List<ImgDTO> list) {
        Resource fileRource = new ClassPathResource(filepath);
        try {
            if (fileRource.getFile().isDirectory()) {
                String[] filelist = fileRource.getFile().list();
                for (int i = 0; i < filelist.length; i++) {
                    fileRource = new ClassPathResource(filepath + "\\" + filelist[i]);
                    if (!fileRource.getFile().isDirectory()) {
                        log.info("加载:" + filepath + "/" + fileRource.getFile().getName());
                        list.add(new ImgDTO(fileRource.getFile().getName(), input2byte(fileRource.getInputStream()), fileRource.getInputStream()));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 资源加载
     *
     * @param path path之下应为分类文件夹
     */
    public List<ImgDTO> loadResource(String path) {
        List<ImgDTO> files;
        files = getFilesByJar(path);
        return files != null ? files : getFileByDisk(path);
    }
}
