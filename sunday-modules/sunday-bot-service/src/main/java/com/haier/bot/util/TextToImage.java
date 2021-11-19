package com.haier.bot.util;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class TextToImage {

    /**
     * 根据str,font的样式等生成图片
     * https://blog.csdn.net/sinat_28505133/article/details/54669111
     *
     * @param message
     * @throws Exception
     */
    // 标点符号也算一个字,字体宽度
    private static final int stringWidth;
    // 图片字体
    private static final Font font = new Font("微软雅黑", Font.PLAIN, 22);

    static {
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
        stringWidth = (int) (font.getStringBounds("字", frc).getWidth());
    }

    public static boolean createImage(String message, String path) {
        // 图片宽度
        int width = 800;

        // 每行或者每个文字的高度
        int lineHeight = 30;
        String[] strArr = message.split("\n");
        // 每行应该多少个字符
        int lineStringNum = width % stringWidth == 0 ? (width / stringWidth) : ((width / stringWidth) + 1);

        List<String> newList = new ArrayList<>();
        int length = strArr.length;
        for (int i = 0; i < length; i++) {
            String s = strArr[i];
            int j = s.length() / lineStringNum;
            int p = s.length() % lineStringNum;
            int start = 0;
            for (int k = 0; k < j; k++) {
                newList.add(s.substring(start, (k + 1) * lineStringNum));
                start = (k + 1) * lineStringNum;
            }
            if (p != 0) {
                newList.add(s.substring(start));
            }
        }

        // 图片总高度
        int height = newList.size() * lineHeight + 10;
        File outFile = new File(path);
        // 创建图片
        int leftMargin = 25;
        int rightMargin = 40;
        int newWidth = width + leftMargin + rightMargin;
        BufferedImage image = new BufferedImage(newWidth, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, newWidth, height);
        g.setColor(Color.white); // 背景色白色
        g.fillRect(0, 0, newWidth, height);
        g.setColor(Color.black);//  字体颜色黑色
        g.setFont(font);// 设置画笔字体
        // 写入图片
        for (int i = 0; i < newList.size(); i++) {
            g.drawString(newList.get(i), leftMargin, lineHeight * (i + 1));
        }
        g.dispose();
        try {
            // 输出png图片
            ImageIO.write(image, "png", outFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}

