package com.shuyun.brandguide.utils;

import org.apache.commons.lang.RandomStringUtils;

import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Component:
 * Description:
 * Date: 15/9/23
 *
 * @author yue.zhang
 */
public class FileUtil {

    public final static String IMAGE_FORMAT_ERROR = "不是图片格式";
    public final static String IMAGE_OUT_OF_SIZE = "超过5M";
    public final static String FILE_OUT_OF_SIZE = "超过5M";
    public final static String URL_OUT_OF_SIZE = "超过5M";
    public final static long IMAGE_MAX_SIZE = 5000000; // 5M
    public final static long FILE_MAX_SIZE = 5000000; // 5M
    public final static long URL_MAX_SIZE = 5000000; // 5M

    public final static String ZIP_BIG = "big"; // 压得大（也就是文件压缩不厉害）
    public final static String ZIP_SMALL = "small"; // 压得小 （也就是文件压缩厉害）
    public final static String ZIP_NONE = "none"; // 不压缩 （原图上传）

    public final static String IMAGE_GROUP_NAME = "images";
    public final static String FILE_GROUP_NAME = "files";
    public final static String URL_GROUP_NAME = "url";

    private final static String IMAGE_MEDIA_TYPE = "image";

    /**
     * 检测是否是图片格式
     * @param mediaType
     * @return
     */
    public static boolean checkIsImageFormat(MediaType mediaType){
        if(mediaType==null){
            return false;
        }
        if(!mediaType.getType().equals(FileUtil.IMAGE_MEDIA_TYPE)){
            return false;
        }
        return true;
    }

    /**
     * 检测文件后缀名是否是指定的文件格式
     * @param suffix    文件后缀名
     * @param format    指定的文件格式
     * @return
     */
    public static boolean checkIsFileFormat(String suffix,String format){
        if(suffix == null || format == null){
            return false;
        }
        return format.contains(suffix);
    }

    /**
     * 获取压缩尺寸质量
     * @param zip
     * @return
     */
    public static int getZipQuality(String zip,long fileSize){
        // 不压缩
        if(FileUtil.ZIP_NONE.equals(zip)){
            return 100;
        }
        // 压小
        else if(FileUtil.ZIP_SMALL.equals(zip)){
            return FileUtil.getZipSmallQuality(fileSize);
        }
        // 压大
        else {
            return FileUtil.getZipBigQuality(fileSize);
        }
    }

    /**
     * 生成随机文件名
     * @return
     */
    public static String getRandomFileName(String suffix){
        long temp = LocalDateTime.of(2015, 1, 1, 0, 0).until(LocalDateTime.now(), ChronoUnit.MICROS);
        return RandomStringUtils.random(3,false,true) + temp + "." + suffix;
    }

    /**
     * 获得质量压缩尺寸
     *  2M-5M：压缩到5%；
     * 1M-2M：压缩到10%；
     * 500K-1M：压缩到20%；
     * 200K-500K：压缩到50%；
     * 100K-200K：压缩到80%；
     * 100K以下：不压缩。
     * @param size
     * @return
     */
    private static int getZipBigQuality(long size){
        if(size >= 2000000 && size <= IMAGE_MAX_SIZE){
            return 5;
        }else if(size >= 1000000 && size < 2000000){
            return 10;
        }else if(size >= 500000 && size < 1000000){
            return 20;
        }else if(size >= 200000 && size < 500000){
            return 50;
        }else if(size >= 100000 && size < 200000){
            return 80;
        }else if(size < 100000){
            return 100;
        }else{
            return -1;
        }
    }

    /**
     *  获得质量压缩尺寸
     *  1、2M及以上：压缩到1%；
     * 2、1M-2M：压缩到2%；
     * 3、500K-1M：压缩到4%；
     * 4、200K-500K：压缩到10%；
     * 5、100K-200K：压缩到20%；
     * 6、50K-100K：压缩到50%；
     * 7、50K以下：不压缩。
     * @param size
     * @return
     */
    private static int getZipSmallQuality(long size){
        if(size >= 2000000 && size <= IMAGE_MAX_SIZE){
            return 1;
        }else if(size >= 1000000 && size < 2000000){
            return 2;
        }else if(size >= 500000 && size < 1000000){
            return 4;
        }else if(size >= 200000 && size < 500000){
            return 10;
        }else if(size >= 100000 && size < 200000){
            return 20;
        }else if(size >= 50000 && size < 100000){
            return 50;
        }else if(size < 50000){
            return 100;
        }else{
            return -1;
        }
    }

}
