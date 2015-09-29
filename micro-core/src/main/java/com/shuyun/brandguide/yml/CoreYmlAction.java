package com.shuyun.brandguide.yml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Component:
 * Description:
 * Date: 15/9/29
 *
 * @author yue.zhang
 */
public class CoreYmlAction {

    private static Logger LOGGER = LoggerFactory.getLogger(CoreYmlAction.class);

    /**
     *  读取yml文件并保存到系统变量中
     * @param environment 运行环境 test、pre、product
     * @param ymlPath   yml文件路径
     */
    public static void readCoreYmlToStoreSystem(String environment,String ymlPath){
        try{
            File file = new File(ymlPath);
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            JsonNode node = mapper.readTree(file).get(environment);
            storeLoggingPropertiesToSystem(node.get("logging"));
            storeDatabasePropertiesToSystem(node.get("database"));
            storeLilyPosPropertiesToSystem(node.get("pos"));
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
        }
    }

    /**
     * log
     * @param logNode
     */
    private static void storeLoggingPropertiesToSystem(JsonNode logNode){
        System.setProperty(LOGGING_LEVEL,logNode.get("level").asText());
        System.setProperty(LOGGING_MICRO_FILE_CURRENTLOGFILENAME,logNode.get("microFileCurrentLogFilename").asText());
        System.setProperty(LOGGING_MICRO_FILE_ARCHIVEDLOGFILENAMEPATTERN,logNode.get("microFileArchivedLogFilenamePattern").asText());
    }

    /**
     * database
     * @param dbNode
     */
    private static void storeDatabasePropertiesToSystem(JsonNode dbNode){
        System.setProperty(DB_DRIVER_CLASS,dbNode.get("driverClass").asText());
        System.setProperty(DB_USERNAME,dbNode.get("username").asText());
        System.setProperty(DB_PASSWORD,dbNode.get("password").asText());
        System.setProperty(DB_URL,dbNode.get("url").asText());
    }

    /**
     * lily pos
     * @param posNode
     */
    private static void storeLilyPosPropertiesToSystem(JsonNode posNode){
        System.setProperty(LILY_POS_APPKEY,posNode.get("appKey").asText());
        System.setProperty(LILY_POS_APPSECRET,posNode.get("appSecret").asText());
        System.setProperty(LILY_POS_SERVERURL,posNode.get("serverUrl").asText());
    }


    //系统属性前缀
    private final static String PREFIX = "BG_";

    // 日志
    public final static String LOGGING_LEVEL = PREFIX + "LOGGING_LEVEL";
    public final static String LOGGING_MICRO_FILE_CURRENTLOGFILENAME = PREFIX + "LOGGING_MICRO_FILE_CURRENTLOGFILENAME";
    public final static String LOGGING_MICRO_FILE_ARCHIVEDLOGFILENAMEPATTERN = PREFIX + "LOGGING_MICRO_FILE_ARCHIVEDLOGFILENAMEPATTERN";

    // 数据库
    public final static String DB_DRIVER_CLASS = PREFIX + "DB_DRIVER_CLASS";
    public final static String DB_USERNAME = PREFIX + "DB_USERNAME";
    public final static String DB_PASSWORD = PREFIX + "DB_PASSWORD";
    public final static String DB_URL = PREFIX + "DB_URL";

    // Lily POS
    public final static String LILY_POS_APPKEY = PREFIX + "LILY_POS_APPKEY";
    public final static String LILY_POS_APPSECRET = PREFIX + "LILY_POS_SECRET";
    public final static String LILY_POS_SERVERURL = PREFIX + "LILY_POS_SERVERURL";

}
