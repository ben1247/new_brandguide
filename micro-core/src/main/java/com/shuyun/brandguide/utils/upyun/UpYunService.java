package com.shuyun.brandguide.utils.upyun;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Component:
 * Description:
 * Date: 15/9/24
 *
 * @author yue.zhang
 */
public class UpYunService {

    private static Logger LOGGER = LoggerFactory.getLogger(UpYunService.class);

    /**
     * 上传图片
     * @param groupName 组名
     * @param file      文件
     * @param suffix    后缀名
     * @param quality   压缩质量
     * @return
     */
    public String upload(String groupName , File file , String suffix , int quality){
        try {
            if(quality>=100){
                return UpYunUtils.upload(groupName,file,suffix);
            }else{
                return UpYunUtils.uploadGmkerl(groupName,file,suffix,quality);
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return null;
        }
    }

}
