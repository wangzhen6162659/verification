package com.wz.modular;

import com.wz.Exception.VerifyException;
import com.wz.Exception.VerifyExceptionEnum;
import com.wz.base.BaseVerifyModular;
import com.wz.entity.ImgDTO;
import com.wz.entity.PicVerifyObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.wz.base.CommonMethod.generator;

/**
 * 图形识别验证码生成模型
 *
 * @author wz
 * @createTime 2018-11-06 16:03
 */
public class PicVerifyModular extends BaseVerifyModular {
    private List<ImgDTO> dtos = new ArrayList<>();
    private String[] tipsArr;
    private final int TITLE_HEIGHT = 60;
    private final Integer WIDTH_DEFULT = 4;
    private final Integer HEIGHT_DEFULT = 2;

    /**
     * 图片识别默认资源,文件夹内为具体图片文件(尺寸限制200*200)
     */
    public void initImgObejct() {
        dtos.addAll(commonMethod.loadResource(commonMethod.PATH_DEFULT_IMG));
    }

    /**
     * 自定义+图片识别默认资源
     *
     * @param path
     */
    public void initImgObejct(String path) {
        dtos.addAll(commonMethod.loadResource(commonMethod.PATH_DEFULT_IMG));
        if (!StringUtils.isEmpty(path)) {
            dtos.addAll(commonMethod.loadResource(path));
        }
    }

    /**
     * 自定义资源
     *
     * @param path
     * @param useDefult
     */
    public void initImgObejct(String path, boolean useDefult) {
        if (useDefult) {
            dtos.addAll((commonMethod.loadResource(commonMethod.PATH_DEFULT_IMG)));
        }
        if (!StringUtils.isEmpty(path)) {
            dtos.addAll(commonMethod.loadResource(path));
        }
    }

    /**
     * 获取图片识别验证码
     *
     * @param w
     * @param h
     * @return
     */
    @Override
    public PicVerifyObject getCode(int w, int h) {
        return getPicVerifyAndPath(dtos, w, h);
    }

    /**
     * 默认 4*2
     *
     * @return
     */
    @Override
    public PicVerifyObject getCode() {
        return getPicVerifyAndPath(dtos, WIDTH_DEFULT, HEIGHT_DEFULT);
    }

    /**
     * 合成验证图片
     *
     * @param finalImages 所需图片集合
     * @param widthNum    横坐标量
     * @param heightNum   纵坐标量
     * @throws IOException
     */
    @Override
    protected BufferedImage imageCode(List<?> finalImages, Integer widthNum, Integer heightNum) {

        //初始化
        BufferedImage mergeImage = new BufferedImage(widthNum * 200, TITLE_HEIGHT + heightNum * 200, BufferedImage.TYPE_INT_BGR);

        int index = 0;
        for (int i = 0; i < heightNum; i++) {
            for (int j = 0; j < widthNum; j++) {
                index = i * widthNum + j;
                InputStream images = (InputStream) finalImages.get(index);
                BufferedImage bufferedImage = null;
                try {
                    bufferedImage = ImageIO.read(images);
                    if (bufferedImage == null) {
                        throw new VerifyException(VerifyExceptionEnum.PIC_READ_FAIL);
                    }
                    if (bufferedImage.getWidth() > 200 || bufferedImage.getHeight() > 200) {
                        throw new VerifyException(VerifyExceptionEnum.PIC_SIZE_OUT);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int width = bufferedImage.getWidth();
                int height = bufferedImage.getHeight();
                //从图片中读取RGB
                int[] imageBytes = new int[width * height];
                imageBytes = bufferedImage.getRGB(0, 0, width, height, imageBytes, 0, width);
                mergeImage.setRGB(j * 200, TITLE_HEIGHT + i * 200, width, height, imageBytes, 0, width);
            }
        }

        //读取头文字信息
        mergeImage = imageCode(mergeImage, widthNum * 200, null);


        return mergeImage;
    }

    /**
     * 创建横幅-这里的横幅为了方便，定死为60的高
     *
     * @param image  图片buffer
     * @param width  宽
     * @param height 高
     * @return
     */
    @Override
    protected BufferedImage imageCode(BufferedImage image, Integer width, Integer height) {
        // 获取图形上下文
        Graphics g = image.getGraphics();
        // 设定背景色
        g.setColor(new Color(250, 250, 250));
        g.fillRect(0, 0, width, TITLE_HEIGHT);
        // 设定字体
        int fontSize = 30;
        g.setFont(new Font("宋体", Font.ITALIC, fontSize));

        //预设标题
        String title = "请识别出下列所有的:";
        g.setColor(new Color(0, 0, 0));
        g.drawString(title, 20, (TITLE_HEIGHT + fontSize) / 2);
        int spacing = 0;
        int titleSize = title.length() * fontSize + 10;
        for (int i = 0; i < tipsArr.length; i++) {
            // 将tip添加在标题后面
            int lastTipLocal = spacing + titleSize;
            g.setColor(commonMethod.getRandColor(0, 150));
            g.drawString(String.valueOf(tipsArr[i]), titleSize + spacing, (TITLE_HEIGHT + fontSize) / 2);
            commonMethod.shear(g, lastTipLocal, titleSize + spacing, TITLE_HEIGHT, 1f);
            spacing += tipsArr[i].length() * fontSize + 20 * (i + 1);
        }
        //生成干扰线
        commonMethod.setInterferenceLine(g, width - titleSize, TITLE_HEIGHT, titleSize, 0, generator.nextInt(5) + 10);
        // 图象生效
        g.dispose();
        return image;
    }

    /**
     * 组装实体
     *
     * @param widthNum
     * @param heightNum
     * @return
     */
    private PicVerifyObject getPicVerifyAndPath(List<ImgDTO> dtos, int widthNum, int heightNum) {
        //获取默认路径下图形集合

        int childSize = dtos.stream().map(ImgDTO::getChilds).collect(Collectors.toList())
                .stream().mapToInt(List::size).sum();
        if (childSize < widthNum * heightNum) {
            throw new VerifyException(VerifyExceptionEnum.OBJ_READ_FAIL, childSize + "<" + widthNum * heightNum);
        }
        List<Object> object = getEightImages(dtos, widthNum * heightNum);

        //获取随机图形集合
        List<InputStream> finalImages = (List<InputStream>) object.get(0);

        //获得随机图形集合的分类名
        String[] tips = (String[]) object.get(1);
        //所有key的图片位置,即用户必须要选的图片
        List<Object> locations = getLocation(Arrays.asList(tips));

        //获取随机分类的下标
        tipsArr = (String[]) locations.get(0);
        locations.remove(0);
        List<Integer> locationsIndex = new ArrayList<>();

        locations.forEach(obj -> {
            locationsIndex.add((Integer) obj);
        });

        PicVerifyObject picVerifyObject = new PicVerifyObject();
        picVerifyObject.setClickNum(locations.size());
        picVerifyObject.setFinalImages(finalImages);
        picVerifyObject.setLocations(locationsIndex);
        picVerifyObject.setImg(imageCode(finalImages, widthNum, heightNum));

        return picVerifyObject;
    }

    /**
     * 获取随机分类标识
     *
     * @param strs
     * @return
     */
    private String[] getTip(List<String> strs) {
        int num = 2;
        String[] tips = new String[num];
        for (int i = 0; i < num; i++) {
            tips[i] = strs.get(generator.nextInt(strs.size()));
        }
        return tips;
    }

    /**
     * 遍历获取文件
     *
     * @param dtos
     * @param name
     * @return
     */
    private ImgDTO getFiles(List<ImgDTO> dtos, String name) {
        return (ImgDTO) dtos.stream().filter(dto -> name.equals(dto.getName())).toArray()[0];
    }

    /**
     * 随机选取N个图片
     *
     * @param dtoList ImgDTO 二维集合,[分类][图片]
     * @param imgNum  N
     * @return
     */
    private List<Object> getEightImages(List<ImgDTO> dtoList, Integer imgNum) {
        List<InputStream> finalImages = new ArrayList<>();
        List<Object> object = new ArrayList<Object>();
        List<String> isEx = new ArrayList<>();

        //保存tips
        String[] tips = new String[imgNum];
        int i = 0;
        List<String> imgNames = dtoList.stream().map(ImgDTO::getName).collect(Collectors.toList());
        while (i < imgNum) {
            //获取随机的二级目录
            String dirIndexName = commonMethod.getRandom(imgNames);
            ImgDTO secondaryDir = getFiles(dtoList, dirIndexName);


            //获取二级图片目录下的文件
            List<ImgDTO> dtosChild = secondaryDir.getChilds();
            List<byte[]> imagesBytes = dtosChild.stream().map(ImgDTO::getBytes).collect(Collectors.toList());
            List<InputStream> images = commonMethod.byte2Input(imagesBytes);
            List<String> names = dtosChild.stream().map(ImgDTO::getName).collect(Collectors.toList());
            int imageIndex = commonMethod.getRandom(images.size());

            //去除重复图像
            InputStream image = images.get(imageIndex);
            if (isEx.contains(secondaryDir.getName() + "." + names.get(imageIndex))) {
                continue;
            }

            //随机到的文件夹名称保存到tips中
            tips[i] = secondaryDir.getName();
            finalImages.add(image);
            isEx.add(secondaryDir.getName() + "." + names.get(imageIndex));
            i++;
        }
        object.add(finalImages);
        object.add(tips);
        return object;
    }

    /**
     * 获取预选标识位置
     *
     * @param tips 标识集合
     * @return
     */
    private List<Object> getLocation(List<String> tips) {
        List<Object> locations = new ArrayList<Object>();

        //获取Key分类
        Set<String> tipSet = new HashSet<String>(Arrays.asList(getTip(tips)));
        String[] tipArr = new String[tipSet.size()];
        new ArrayList<String>(tipSet).toArray(tipArr);
        locations.add(tipArr);

        int length = tips.size();
        for (int i = 0; i < length; i++) {
            if (tipSet.contains(tips.get(i))) {
                locations.add(i + 1);
            }
        }

        return locations;
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
        //验证是否符合坐标参数
        if (locals.size() != cache.size() * 2) {
            return false;
        }

        int indexX = 0;
        int indexY = 0;

        //预防remove错误,逆向遍历,首先遍历每个缓存坐标
        for (int i = cache.size() - 1; i >= 0; i--) {
            //匹配传入坐标参数,这里图片行数是同最大列数相关,所以只需知道列数即可
            for (int j = locals.size() - 1; j >= 0; j--) {
                //列数下标
                indexX = (cache.get(i) - 1) % (widthNum);
                //行数下标
                indexY = (cache.get(i) - 1) / (widthNum);
                if (locals.get(j - 1) >= (indexX) * widthVal && locals.get(j) >= indexY * widthVal
                        && locals.get(j - 1) <= (indexX + 1) * widthVal && locals.get(j) <= (indexY + 1) * widthVal) {
                    cache.remove(cache.get(i));
                    break;
                }
                j--;
            }
        }
        if (cache.size() == 0) {
            return true;
        }
        return false;
    }
}
