package com.hhly.utils.file;

import com.hhly.utils.ValueUtil;
import com.hhly.utils.LogUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.process.ArrayListOutputConsumer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 图片处理工具类(压缩)
 */
public class Im4JavaUtil {

    /**
     * 获取图片文件的大小
     * @param imagePath  文件地址
     */
    public int getSize(String imagePath){
        try (FileInputStream inputStream = new FileInputStream(imagePath)) {
            return inputStream.available();  // 获取文件大小
        } catch (FileNotFoundException e){
            LogUtil.ROOT_LOG.info("文件未找到");
        } catch(IOException io){
            LogUtil.ROOT_LOG.info("读取文件大小错误");
        }
        return 0;
    }

    /**
     * 获取文件宽度
     * @param imagePath 图片路径
     */
    public static int getWidth(String imagePath){
        try {
            IMOperation op = new IMOperation();
            op.format("%w"); // 设置获取高度的参数
            op.addImage(1);
            IdentifyCmd cmd = new IdentifyCmd();

            ArrayListOutputConsumer outputConsumer = new ArrayListOutputConsumer();
            cmd.setOutputConsumer(outputConsumer);
            cmd.run(op,imagePath);

            ArrayList<String> cmdOutPut = outputConsumer.getOutput();
            return NumberUtils.toInt(cmdOutPut.get(0));
        } catch (Exception e){
            LogUtil.ROOT_LOG.info("运行命令出错：", e);
        }
        return 0;
    }

    /**
     * 获取文件高度
     * @param imagePath 图片路径
     */
    public static int getHeight(String imagePath){
        try {
            IMOperation op = new IMOperation();
            op.format("%h"); // 设置获取高度的参数
            op.addImage(1);
            IdentifyCmd cmd = new IdentifyCmd();

            ArrayListOutputConsumer outputConsumer = new ArrayListOutputConsumer();
            cmd.setOutputConsumer(outputConsumer);
            cmd.run(op,imagePath);

            ArrayList<String> cmdOutPut = outputConsumer.getOutput();
            return NumberUtils.toInt(cmdOutPut.get(0));
        } catch (Exception e){
            LogUtil.ROOT_LOG.info("运行命令出错：", e);
        }
        return 0;
    }

    /**
     * 获取图片信息
     * @param imagePath 图片路径
     */
    public static String getImageInfo(String imagePath){
        String info = null;
        try {
            IMOperation op = new IMOperation();
            op.format("width:%w,height:%h,path:%d%f,size:%b%[EXIF:DateTimeOriginal]");
            op.addImage(1);

            IdentifyCmd cmd = new IdentifyCmd();
            ArrayListOutputConsumer outputConsumer = new ArrayListOutputConsumer();
            cmd.setOutputConsumer(outputConsumer);
            cmd.run(op,imagePath);

            ArrayList<String> cmdOutPut = outputConsumer.getOutput();

            assert cmdOutPut.size() == 1;

            info =cmdOutPut.get(0);
        } catch (Exception e){
            LogUtil.ROOT_LOG.info("获取图片信息异常：", e);
        }
        return info;
    }

    /**
     * 根据尺寸裁剪图片
     * @param imagePath
     *        源图片路径
     * @param newPath
     *        裁剪后图片路径
     * @param x
     *        起始X坐标
     * @param y
     *        起始Y坐标
     * @param width
     *        裁剪宽度
     * @param height
     *        裁剪高度
     * @return 返回true说明裁剪成功
     */
    public static boolean cutImage(String imagePath, String newPath, int x, int y,int width, int height){
        try {
            IMOperation op = new IMOperation();
            op.addImage(imagePath);

            op.crop(width,height,x,y);
            op.addImage(newPath);
            ConvertCmd cmd = new ConvertCmd();
            cmd.run(op);
            return true;
        } catch (Exception e) {
            LogUtil.ROOT_LOG.info("根据尺寸裁剪图片时异常", e);
        }
        return false;
    }

    /**
     * 按比例缩放图片
     * @param imagePath
     *        源图片路径
     * @param newPath
     *        缩放后图片路径
     * @param width
     *        缩放后图片宽度
     * @param height
     *        缩放后图片高度
     * @return  处理成功返回true
     */
    public static boolean zoomImage(String imagePath, String newPath, Integer width, Integer height){
        try {
            IMOperation op = new IMOperation();
            op.addImage(imagePath);
            op.quality(80.0);
            if (ValueUtil.less0(width) && !ValueUtil.less0(height)) {// 根据高度缩放图片 宽度为null
                op.resize(null, height);
            } else if (ValueUtil.less0(height) && !ValueUtil.less0(width)) {// 根据宽度缩放图片 高度为null
                op.resize(width);
            } else if(ValueUtil.less0(height) && ValueUtil.less0(width)){ // 长宽都为空  则按原比例缩放
                op.resize(getWidth(imagePath), getHeight(imagePath));
            } else {
                op.resize(width, height);
            }
            op.addImage(newPath);
            ConvertCmd convert = new ConvertCmd();
            convert.run(op);
            return true;
        } catch (IOException | InterruptedException | IM4JavaException e) {
            LogUtil.ROOT_LOG.info("按比例缩放图片时异常", e);
        }
        return false;
    }

}
