package com.mawujun.utils.file;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mawujun.utils.string.StringUtils;

/**
 * http://www.cnblogs.com/digdeep/p/4829471.html Java 图片处理——如何生成高清晰度而占有磁盘小的缩略图
 * http://chenhua-1984.iteye.com/blog/1870812 java 图片缩略图的两种方法
 * 
 * @author mawujun qq:16064988 mawujun1234@163.com
 *
 */
public class ThumbUtils {
	/**
     * 按指定高度 等比例缩放图片
     * 
     * @param imageFile
     * @param newPath
     * @param newWidth 新图的宽度
     * @throws IOException
     */
    public static void zoomImageScale(File imageFile, String newPath, int newWidth) throws IOException {
         if(!imageFile.canRead())
             return;
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        if (null == bufferedImage) 
            return;
        
        int originalWidth = bufferedImage.getWidth();
        int originalHeight = bufferedImage.getHeight();
        double scale = (double)originalWidth / (double)newWidth;    // 缩放的比例
        
        int newHeight =  (int)(originalHeight / scale);

        zoomImageUtils(imageFile, newPath, bufferedImage, newWidth, newHeight);
    }
    private static void zoomImageUtils(File imageFile, String newPath, BufferedImage bufferedImage, int width, int height)
            throws IOException{
        
         String suffix = StringUtils.substringAfterLast(imageFile.getName(), ".");
        
         // 处理 png 背景变黑的问题
        if(suffix != null && (suffix.trim().toLowerCase().endsWith("png") || suffix.trim().toLowerCase().endsWith("gif"))){
            BufferedImage to= new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
            Graphics2D g2d = to.createGraphics(); 
            to = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT); 
            g2d.dispose(); 
            
            g2d = to.createGraphics(); 
            Image from = bufferedImage.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING); 
            g2d.drawImage(from, 0, 0, null);
            g2d.dispose(); 
            
            ImageIO.write(to, suffix, new File(newPath));
        }else{
            // 高质量压缩，其实对清晰度而言没有太多的帮助
//            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//            tag.getGraphics().drawImage(bufferedImage, 0, 0, width, height, null);
//
//            FileOutputStream out = new FileOutputStream(newPath);    // 将图片写入 newPath
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//            JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
//            jep.setQuality(1f, true);    //压缩质量, 1 是最高值
//            encoder.encode(tag, jep);
//            out.close();
            
            BufferedImage newImage = new BufferedImage(width, height, bufferedImage.getType());
            Graphics g = newImage.getGraphics();
            g.drawImage(bufferedImage, 0, 0, width, height, null);
            g.dispose();
            ImageIO.write(newImage, suffix, new File(newPath));
        }
    }
    
//    /**
//     * 等比例改变图片尺寸,本方法也不能处理 png 图片：
//     * @param nw 新图片的宽度
//     * @param oldImage 原图片
//     * @throws IOException
//     */
//    public static void constrainProportios(int nw, String oldImage) throws IOException {
//        AffineTransform transform = new AffineTransform();
//        BufferedImage bis = ImageIO.read(new File(oldImage));
//        int w = bis.getWidth();
//        int h = bis.getHeight();
//        int nh = (nw * h) / w;
//        double sx = (double) nw / w;
//        double sy = (double) nh / h;
//        transform.setToScale(sx, sy);
//        AffineTransformOp ato = new AffineTransformOp(transform, null);
//        BufferedImage bid = new BufferedImage(nw, nh, BufferedImage.TYPE_3BYTE_BGR);
//        ato.filter(bis, bid);
//        
//        String newPath = StringUtils.substringBeforeLast(oldImage,".")+"_3."+StringUtils.substringAfterLast(oldImage,".");
//        ImageIO.write(bid, "jpeg", new File(newPath));
////        ImageIO.write(bid, "jpeg", response.getOutputStream());
//    }
    
    /**
     * 按尺寸缩放图片
     * 
     * @param imageFile
     * @param newPath
     * @param times
     * @throws IOException
     */
    public static void zoomImage(File imageFile, String newPath, int width, int height) throws IOException {
        if (imageFile != null && !imageFile.canRead())
            return;
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        if (null == bufferedImage) 
            return;

        zoomImageUtils(imageFile, newPath, bufferedImage, width, height);
    }

}
