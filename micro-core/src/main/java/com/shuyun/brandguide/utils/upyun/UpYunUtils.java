package com.shuyun.brandguide.utils.upyun;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class UpYunUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpYunUtils.class);

    /**
     * 绑定的域名
     */
    private static String URL = null;
    private static UpYun upyun = null;

    static {
        InputStream is = null;
        try {

            String confPath = "/upyun.properties";
            Properties prop = new Properties();
            is = UpYunUtils.class.getResourceAsStream(confPath);
            prop.load(is);
            String BUCKET_NAME = prop.getProperty("BUCKET_NAME").trim();
            String USER_NAME = prop.getProperty("USER_NAME").trim();
            String USER_PWD = prop.getProperty("USER_PWD").trim();
            upyun = new UpYun(BUCKET_NAME, USER_NAME, USER_PWD);
            URL = "http://" + BUCKET_NAME + ".b0.upaiyun.com";
        } catch (Exception e) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e2) {
                    throw new RuntimeException("初始化UpYun客户端关闭失败", e2);
                }
            }
            throw new RuntimeException("初始化UpYun客户端失败", e);
        }
    }


    /**
     * 上传原图
     * @param groupName
     * @param file
     * @return
     * @throws IOException
     */
    public static String upload(String groupName,File file,String suffix) throws IOException{
        upyun.setContentMD5(UpYun.md5(file));
        FileInputStream inputStream = new FileInputStream(file);
        return upload(groupName, IOUtils.toByteArray(inputStream),suffix);
    }
    public static String upload(String groupName, byte[] data,String suffix) throws IOException {
        String filePath = "/" + groupName + "/" + System.currentTimeMillis() + "_" + nex() + "." + suffix;
//        upyun.setFileSecret("bac");

        // 上传文件，并自动创建父级目录（最多10级）
        boolean result = upyun.writeFile(filePath, data, true);

        LOGGER.info(filePath + " 上传" + isSuccess(result));

        if(result){
            // 获取上传文件后的信息（仅图片空间有返回数据）
            LOGGER.info("****** " + filePath + " 的图片信息 *******");

            LOGGER.info(URL + filePath);
            return URL + filePath;
        }else{
            return null;
        }

    }

    /**
     * 上传缩略图
     */
    public static String uploadGmkerl(String groupName,File file,String suffix,int quality) throws IOException{
        upyun.setContentMD5(UpYun.md5(file));
        FileInputStream inputStream = new FileInputStream(file);
        byte[] data = IOUtils.toByteArray(inputStream);

        String filePath = "/" + groupName + "/" + System.currentTimeMillis() + "_" + nex() + "." + suffix;

        // 设置缩略图的参数
        Map<String, String> params = new HashMap<String, String>();
        // 设置缩略图类型，必须搭配缩略图参数值（KEY_VALUE）使用，否则无效
        params.put(UpYun.PARAMS.KEY_X_GMKERL_TYPE.getValue(), UpYun.PARAMS.VALUE_FIX_BOTH.getValue());
        // 设置缩略图的质量
        params.put(UpYun.PARAMS.KEY_X_GMKERL_QUALITY.getValue(), String.valueOf(quality));

        // 设置缩略图的锐化，默认锐化（true）
        params.put(UpYun.PARAMS.KEY_X_GMKERL_UNSHARP.getValue(), "true");

        // 若在 upyun 后台配置过缩略图版本号，则可以设置缩略图的版本名称
        // 注意：只有存在缩略图版本名称，才会按照配置参数制作缩略图，否则无效
        params.put(UpYun.PARAMS.KEY_X_GMKERL_THUMBNAIL.getValue(), "small");

        // 上传文件，并自动创建父级目录（最多10级）
        boolean result = upyun.writeFile(filePath, file, true, params);

        LOGGER.info(filePath + " 上传" + isSuccess(result));

        if(result){
            // 获取上传文件后的信息（仅图片空间有返回数据）
            LOGGER.info("****** " + filePath + " 的图片信息，压缩 " + quality + " % *******");

            LOGGER.info(URL + filePath);
            return URL + filePath;
        }else{
            return null;
        }
    }

    /**
     * 删除文件
     *
     * @param groupName      组名
     * @param remoteFileName 文件名
     * @return
     * @throws IOException
     */
    public static int delete(String groupName, String remoteFileName) throws IOException {
        int result = 0;
        remoteFileName = "/" + remoteFileName.substring(remoteFileName.indexOf(groupName), remoteFileName.length());
        result = upyun.deleteFile(remoteFileName) == true ? 1 : 0;
        LOGGER.info("删除图片成功 " + remoteFileName + " 的图片信息 *******");
        return result;
    }

    private static String nex() {
        String re = "";
        while (re.length() < 6)
            re += (int) (Math.random() * 10);
        return re;
    }

    private static String isSuccess(boolean result) {
        return result ? " 成功" : " 失败";
    }


    public static void main(String[] args) throws Exception {

        String groupName = "fileUpload";
        File file = new File(System.getProperty("user.dir") + "/micro-files/test.png");
        UpYunUtils.upload(groupName,file,".png");

    }
}
