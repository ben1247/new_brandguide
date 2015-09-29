package com.shuyun.brandguide.utils.props;

import org.apache.commons.lang3.StringUtils;

/**
 * Component:
 * Description:
 * Date: 15/9/28
 *
 * @author yue.zhang
 */
public class MicroPropsUtil {

    public static String getEnv(String key, String defValue) {
        String env = System.getenv(key);
        if (StringUtils.isNotEmpty(env)) {
            return env;
        }
        return defValue;
    }

    public static String getEnv(MicroPropsKey key, String defValue) {
        return getEnv(key.value(), defValue);
    }

    public static String getSys(String key) {
        return System.getProperty(key);
    }

    public static String getSys(MicroPropsKey key) {
        return getSys(key.value());
    }

    public static boolean getBooleanSys(String key) {
        return "true".equalsIgnoreCase(getSys(key));
    }

    public static boolean getBooleanSys(MicroPropsKey key) {
        return getBooleanSys(key.value());
    }

    public static int getIntSys(String key, int def) {
        try {
            return Integer.valueOf(getSys(key));
        } catch (Exception e) {
        }
        return def;
    }

    public static int getIntSys(MicroPropsKey key, int def) {
        return getIntSys(key.value(), def);
    }

    public static void setSys(String key, String value) {
        if (null != value) {
            System.setProperty(key, value);
        }
    }

    public static void setSys(MicroPropsKey key, String value) {
        setSys(key.value(), value);
    }

    public static void copyEnvToSys(String key, String defValue) {
        setSys(key, getEnv(key, defValue));
    }

    public static void copyEnvToSys(MicroPropsKey key, String defValue) {
        copyEnvToSys(key.value(), defValue);
    }

}
