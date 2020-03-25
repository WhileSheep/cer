package com.example.cer.utils;

import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import org.apache.batik.transcoder.TranscoderException;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;

@Component
public class PdfUtil {

    /**
     * Svg文件转为Png图片
     */
    public static String SvgToPng(String url,String pdfName,String root) {
        pdfName = FileUtil.getFileNameNoEx(pdfName);
        String pngUrl = null;
        try {
            String rootPath = new File("").getCanonicalPath();
            InputStream inputStream = ImageUtil.getImageStream(url);
            String filePath = rootPath + "/signature/primitive/" + root + "/" + pdfName + "/";
            File fileDir = new File(filePath);
            if (!fileDir.exists()){
                fileDir.mkdirs();//创建文件夹
            }
            pngUrl = filePath + (int)((Math.random()*9+1)*10000000) + ".png";
            ImageUtil.convertSvg2Png(inputStream,new File(pngUrl));
            if (inputStream != null) {
                inputStream.close();//关闭流
            }
        } catch (IOException | TranscoderException e) {
            e.printStackTrace();
        }
        return pngUrl;
    }

    /**
     * iTextPdf7的imageData目前做到无法旋转任意图片，旋转图片的逻辑需要使用java手动完成
     */
    public static File RotationImage(String imgUrl,String pdfName,String root){
        pdfName = FileUtil.getFileNameNoEx(pdfName);
        BufferedImage sourceImg;
        File file = null;
        try {
            String rootPath = new File("").getCanonicalPath();
            File picture = new File(imgUrl);
            InputStream inputStream1 = new FileInputStream(picture);
            InputStream inputStream2 = new FileInputStream(imgUrl);
            sourceImg = ImageIO.read(inputStream1);
            float width = sourceImg.getWidth();
            float height = sourceImg.getHeight();
            if (width < height){//图片是竖着的  需要旋转
                sourceImg = ImageUtil.rotateImage(sourceImg,-90);
                //获取图片名称
                File tempFile =new File(imgUrl.trim());
                String pngName = tempFile.getName();
                //获取图片后缀名
                List<String> list = ImageUtil.getImageFormat(inputStream2);
                String formatName = list.get(0);
                String filePath = rootPath + "/signature/rotation/" + root +  "/" + pdfName +"/";
                File fileDir = new File(filePath);
                if (!fileDir.exists()){
                    fileDir.mkdirs();//创建文件夹
                }
                file = new File( filePath + pngName);
                ImageIO.write(sourceImg,formatName,file);
            }else {//图片是横着的  不需要旋转
                file = new File(imgUrl);
            }
            inputStream1.close();
            inputStream2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 插入图片（按图片域缩放图片长宽）
     */
    public void Image(PdfDocument pdfDocument, PdfFormField formField, String imgAbsolutePath) {
        try {
            Rectangle rectangle = formField.getWidgets().get(0).getRectangle().toRectangle(); // 获取表单域的xy坐标
            PdfCanvas canvas = new PdfCanvas(pdfDocument.getPage(1));
            ImageData image = ImageDataFactory.create(imgAbsolutePath);
            //将pdf页旋转90度
//            pdfDocument.getPage(1).setRotation(pdfDocument.getPage(1).getRotation() + 90);

            float imageWidth = image.getWidth();//图片的宽
            float imageHeight = image.getHeight();//图片的长
            float rectangleWidth = rectangle.getWidth();//图片域的宽
            float rectangleHeight = rectangle.getHeight();//图片域的长

            float tempWidth = 0;
            float tempHeight = 0;

            int result = 1; // 压缩宽度
            if (imageWidth > rectangleWidth) {
                tempHeight = imageHeight * rectangleWidth / imageWidth;//预处理
                if (tempHeight > rectangleHeight) {
                    tempHeight = rectangleHeight;
                    result = 2; // 压缩高度
                } else {
                    tempWidth = rectangleWidth;
                    tempHeight = imageHeight * rectangleWidth / imageWidth;
                }
            } else {
                if (imageHeight > rectangleHeight) {
                    tempHeight = rectangleHeight;
                    result = 2;
                } else {
                    result = 3;
                }
            }

            float y = 0;

            if (result == 1) { // 压缩宽度
                y = rectangleHeight - tempHeight;
            } else if (result == 3) { // 不压缩
                y = rectangleHeight - imageHeight;
            }


//             y/=2; // 如果想要图片在表单域的上下对齐，这个值除以2就行。同理可以计算x的偏移
            if (result == 1) {
                canvas.addImage(image, rectangle.getX(), rectangle.getY() + y, tempWidth, false);
            } else if (result == 2) {
                canvas.addImage(image, rectangle.getX(), rectangle.getY(), tempHeight, false, false);
            } else {
                canvas.addImage(image, rectangle.getX(), rectangle.getY() + y, false);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}

