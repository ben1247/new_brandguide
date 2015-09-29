package com.shuyun.brandguide;

import com.shuyun.brandguide.props.LilyPosProperties;
import com.shuyun.brandguide.utils.props.MicroPropsKey;
import com.shuyun.brandguide.utils.props.MicroPropsUtil;

/**
 * Component:
 * Description:
 * Date: 15/9/28
 *
 * @author yue.zhang
 */
public class AppConfig {

    /**
     * lily pos app_key
     * @return
     */
    public static String getLilyPosAppKey() {
        return MicroPropsUtil.getSys(MicroPropsKey.LILY_POS_APP_KEY);
    }
    public static void setLilyPosAppKey(String appKey) {
        MicroPropsUtil.setSys(MicroPropsKey.LILY_POS_APP_KEY, appKey);
    }

    /**
     * lily pos app_secret
     * @return
     */
    public static String getLilyPosAppSecret(){
        return MicroPropsUtil.getSys(MicroPropsKey.LILY_POS_APP_SECRET);
    }
    public static void setLilyPosAppSecret(String appSecret){
        MicroPropsUtil.setSys(MicroPropsKey.LILY_POS_APP_SECRET,appSecret);
    }

    /**
     * lily pos server url
     * @return
     */
    public static String getLilyPosServerUrl(){
        return MicroPropsUtil.getSys(MicroPropsKey.LILY_POS_SERVER_URL);
    }
    public static void setLilyPosServerUrl(String serverUrl){
        MicroPropsUtil.setSys(MicroPropsKey.LILY_POS_SERVER_URL,serverUrl);
    }

    /**
     * 初始化
     * @param configuration
     */
    public static void init(MicroConfiguration configuration){
        LilyPosProperties lilyPosProperties = configuration.getLilyPosProperties();
        if(lilyPosProperties != null){
            setLilyPosAppKey(lilyPosProperties.getAppKey());
            setLilyPosAppSecret(lilyPosProperties.getAppSecret());
            setLilyPosServerUrl(lilyPosProperties.getServerUrl());
        }
    }

}
