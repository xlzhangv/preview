package com.xlzhang.preview.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PDFchangToImage {
    private static final Logger LOGGER = Logger.getLogger(PDFchangToImage.class);
    /**
     * 功能: TODO.<br/>
     * date: 2016年8月1日 下午5:39:17 <br/>
     *
     * @author xlzhang@wisdombud.com
     * @param absolutePath 文件存放绝对位置<br>
     * @param relativePath 文件存放相对位置<br>
     * @param pdfUrl pdf真实路径<br>
     * @param event pdf2image 操作<br>
     */
    public static void changePdfToImg(String absolutePath, String relativePath, String pdfUrl, PDF2ImageEvent event) {
        String baseImgDir = absolutePath + "\\" + relativePath;
        long startTime = System.currentTimeMillis();
        try {

            PDDocument document = new PDDocument();
            document = PDDocument.load(new File(pdfUrl), "");

            for (int i = 0; i < document.getNumberOfPages(); i++) {
                
                BufferedImage image = new PDFRenderer(document).renderImageWithDPI(i, 512, ImageType.RGB);

                String destFile = baseImgDir + "\\" + i + ".png";

                // 目标路径不存在则建立目标路径
                File dest = new File(destFile);
                if (!dest.getParentFile().exists())
                    dest.getParentFile().mkdirs();
                FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
                ImageIO.write(image, "png", out);
//                
//                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//                JPEGEncodeParam param2 = encoder.getDefaultJPEGEncodeParam(image);
//                param2.setQuality(1f, false);// 1f是提高生成的图片质量
//                encoder.setJPEGEncodeParam(param2);
//                encoder.encode(image); // JPEG编码
                out.close();
                event.after(i, relativePath + "\\" + i + ".png");
            }
            document.close();
            LOGGER.info("完成:pdf 2 image finish,cost:" + (System.currentTimeMillis() - startTime) + "ms");
        } catch (Throwable e) {
            LOGGER.error("pdf 2 image error", e);
        }

    }
}