package com.wz.modular;

import com.wz.Exception.VerifyException;
import com.wz.Exception.VerifyExceptionEnum;
import com.wz.base.BaseVerifyModular;
import com.wz.entity.OperationVerifyObject;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.wz.base.CommonMethod.generator;

/**
 * 运算验证码生成模型
 *
 * @author wz
 * @createTime 2018-11-06 16:03
 */
public class OperationVerifyModular extends BaseVerifyModular {
    private int PIC_HEIGHT = 20;
    private final int MAX_INT = 20;
    private final int FONT_SIZE = 20;
    private int MAX_NUM = 3;
    private String[] OPERATOR = {"*", "+", "-"};

    /**
     * 获取验证码实体,默认160 * 40
     *
     * @return
     */
    @Override
    public OperationVerifyObject getCode() {
        OperationVerifyObject object = controller(null, null);
        return object;
    }

    /**
     * 获取验证码实体
     *
     * @return
     */
    @Override
    public OperationVerifyObject getCode(int w, int h) {
        OperationVerifyObject object = controller(w, h);
        return object;
    }

    /**
     * 组装验证码实体
     *
     * @return
     */
    private OperationVerifyObject controller(Integer w, Integer h) {
        //获取公式list
        List<String> formulaList = getFormula();
        BufferedImage image = imageCode(formulaList, w, h);

        //获取公式字符串
        String formula = getStrByList(formulaList);

        String result = getOperationResult(formula);

        return new OperationVerifyObject(formula, result, image);
    }

    /**
     * 获得一个随机的公式拆分元素list
     *
     * @return
     */
    private List<String> getFormula() {
        List<String> strs = new ArrayList<String>();
        int rand = generator.nextInt(MAX_NUM - 1) + 2;
        for (int i = 0; i < rand; i++) {
            strs.add(generator.nextInt(MAX_INT) + "");
            if (i + 1 < rand) {
                strs.add(OPERATOR[generator.nextInt(OPERATOR.length)]);
            }
        }
        return strs;
    }

    /**
     * List拼接String
     *
     * @param strList
     * @return
     */
    private String getStrByList(List<?> strList) {
        StringBuffer stringBuffer = new StringBuffer();
        strList.forEach(obj -> {
            stringBuffer.append(obj);
        });
        return stringBuffer.toString();
    }

    /**
     * 生成验证码图形
     *
     * @param formulaList
     * @return
     */
    @Override
    protected BufferedImage imageCode(List<?> formulaList, Integer w, Integer h) {
        //初始化
        BufferedImage mergeImage;
        int tempHeight = PIC_HEIGHT;
        int tempFontSize = FONT_SIZE;
        if (w != null && h != null) {
            tempHeight = h;
            tempFontSize = (h) / 2;
            mergeImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_BGR);
        } else {
            mergeImage = new BufferedImage((MAX_NUM * 2 + (MAX_NUM - 1)) * FONT_SIZE, FONT_SIZE + PIC_HEIGHT, BufferedImage.TYPE_INT_BGR);
        }
        //替换一些显示字符
        formulaList = formulaList.stream().map(obj -> {
            if (obj.equals("*")) {
                return "x";
            } else return obj;
        }).collect(Collectors.toList());

        Graphics g = mergeImage.getGraphics();
        // 设定背景色
        g.setColor(commonMethod.getRandColor(200, 255));
        g.fillRect(0, 0, mergeImage.getWidth(), mergeImage.getHeight());
        g.setFont(new Font("宋体", Font.ITALIC, tempFontSize));

        //绘制基本验证图
        int spacing = 0;
        int centValue = (mergeImage.getWidth() - tempFontSize - getStrByList(formulaList).length() * tempFontSize) / 2;
        for (int i = 0; i < formulaList.size(); i++) {
            g.setColor(commonMethod.getRandColor(0, 150));
            g.drawString(String.valueOf(formulaList.get(i)), centValue + tempFontSize + spacing, (tempFontSize + tempHeight) / 2);
            commonMethod.shear(g, spacing, tempFontSize / 2 + spacing, tempFontSize + tempHeight, 0.3f);
            spacing += ((String) formulaList.get(i)).length() * tempFontSize;
        }

        //干扰线
        commonMethod.setInterferenceLine(g, mergeImage.getWidth(), mergeImage.getHeight(), generator.nextInt(5) + 4);

        // 图象生效
        g.dispose();
        return mergeImage;
    }

    /**
     * 获得公式计算结果
     *
     * @param formula
     * @return
     * @throws ScriptException
     */
    private String getOperationResult(String formula) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Object result = null;
        try {
            result = engine.eval(formula);
            return result.toString();
        } catch (ScriptException e) {
            throw new VerifyException(VerifyExceptionEnum.OPERRA_CAN_NOT_DO);
        }
    }
}
