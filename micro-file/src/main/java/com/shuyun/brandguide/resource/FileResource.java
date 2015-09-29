package com.shuyun.brandguide.resource;


import com.google.common.io.ByteStreams;
import com.shuyun.brandguide.AppConfig;
import com.shuyun.brandguide.model.FileUploadRequest;
import com.shuyun.brandguide.utils.FileUtil;
import com.shuyun.brandguide.utils.net.MicroResponse;
import com.shuyun.brandguide.utils.upyun.UpYunService;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.*;

/**
 * Component: 文件上传资源接口
 * Description:
 * Date: 15/9/18
 *
 * @author yue.zhang
 */
@Path("files")
public class FileResource {

    private static Logger LOGGER = LoggerFactory.getLogger(FileResource.class);

    private UpYunService upYunService = new UpYunService();

    /**
     * 上传图片
     * @param form
     * @param zip
     * @return
     */
    @POST
    @Path("upload/images")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadImages(@NotNull FormDataMultiPart form,@QueryParam("zip")String zip){

        Map<String,Object> resMap = new HashMap<String, Object>();
        resMap.put("error",1);

        try{
            List<BodyPart> bodyParts = form.getBodyParts();

            // 验证图片
            String errorMessage = checkImage(bodyParts);
            if(errorMessage!=null){
                resMap.put("message",errorMessage);

            }else{
                List<String> urls = new ArrayList<String>();
                for(BodyPart bodyPart : bodyParts){
                    // 压缩尺寸
                    int zipQuality = FileUtil.getZipQuality(zip, ByteStreams.toByteArray(((BodyPartEntity) bodyPart.getEntity()).getInputStream()).length);
                    String url = upload(bodyPart,FileUtil.IMAGE_GROUP_NAME, zipQuality);
                    if(url != null) urls.add(url);
                }

                // 上传成功
                resMap.put("error",0);
                resMap.put("urls",urls);
            }

        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            resMap.put("message","服务器内部错误");
        }

        return MicroResponse.OK(resMap);
    }

    /**
     * 上传文件
     * @param form
     * @param format
     * @return
     */
    @POST
    @Path("upload/files")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFiles(@NotNull FormDataMultiPart form , @NotNull @QueryParam("format") String format){
        Map<String,Object> resMap = new HashMap<String, Object>();
        resMap.put("error",1);

        try{

            List<BodyPart> bodyParts = form.getBodyParts();

            // 验证文件
            String errorMessage = checkFile(bodyParts, format);
            if(errorMessage!=null){
                resMap.put("message",errorMessage);
            }else{
                List<String> urls = new ArrayList<String>();

                for(BodyPart bodyPart : bodyParts){
                    // 压缩尺寸(文件都不压缩)
                    int zipQuality = 100;
                    String url = upload(bodyPart,FileUtil.FILE_GROUP_NAME,zipQuality);
                    if(url != null) urls.add(url);
                }

                // 上传成功
                resMap.put("error",0);
                resMap.put("urls",urls);
            }

        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            resMap.put("message", "服务器内部错误");
        }

        return MicroResponse.OK(resMap);
    }

    /**
     * 通过url进行文件上传
     * @return
     */
    @POST
    @Path("upload/url")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadWithUrl(FileUploadRequest fileUploadRequest){

        Map<String,Object> resMap = new HashMap<String, Object>();
        resMap.put("result", "failure");
        if(fileUploadRequest.getUrl() == null || fileUploadRequest.getSuffix() == null){
            return MicroResponse.BAD_REQUEST(resMap);
        }

        try {
            long fileSize = ByteStreams.toByteArray(new URL(fileUploadRequest.getUrl()).openConnection().getInputStream()).length;
            if (fileSize > FileUtil.URL_MAX_SIZE) {
                LOGGER.error(String.format("%s %s", fileUploadRequest.getUrl(), FileUtil.URL_OUT_OF_SIZE));
                return MicroResponse.BAD_REQUEST(resMap);
            }
            int zipQuality = FileUtil.getZipQuality(FileUtil.ZIP_BIG, fileSize);
            String url = upload(fileUploadRequest.getUrl(),FileUtil.URL_GROUP_NAME,zipQuality,fileUploadRequest.getSuffix());
            if(url != null){
                resMap.put("result", "success");
                resMap.put("url",url);
            }

        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            resMap.put("message", "服务器内部错误");
        }
        return MicroResponse.OK(resMap);
    }

    @GET
    @Path("lily/pos/config")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,String> getTest(){
        Map<String,String> resMap = new HashMap<String, String>();
        resMap.put("appKey",AppConfig.getLilyPosAppKey());
        resMap.put("appSecret",AppConfig.getLilyPosAppSecret());
        resMap.put("serverUrl",AppConfig.getLilyPosServerUrl());
        return resMap;
    }

    /**
     * 检测图片
     * @param bodyParts
     * @return message 有值为错误信息 ， null则验证通过
     * @throws Exception
     */
    private String checkImage(List<BodyPart> bodyParts) throws IOException {
        for(BodyPart bodyPart : bodyParts){
            // 文件格式是
            String fileName = URLDecoder.decode(bodyPart.getContentDisposition().getFileName(), "UTF-8");
            if(!FileUtil.checkIsImageFormat(bodyPart.getMediaType())){
                return fileName + FileUtil.IMAGE_FORMAT_ERROR;
            }
            // 文件大小
            BodyPartEntity bodyPartEntity = (BodyPartEntity)bodyPart.getEntity();
            long fileSize = ByteStreams.toByteArray(bodyPartEntity.getInputStream()).length;
            LOGGER.info(String.format("%s 文件大小: %s",fileName,fileSize));
            if(fileSize > FileUtil.IMAGE_MAX_SIZE){
                bodyPartEntity.close();
                return fileName + FileUtil.IMAGE_OUT_OF_SIZE;
            }

        }
        return null;
    }

    /**
     * 检测附件
     * @param bodyParts
     * @param format
     * @return
     */
    private String checkFile(List<BodyPart> bodyParts,String format) throws IOException {
        for(BodyPart bodyPart : bodyParts){
            // 文件格式是
            String fileName = URLDecoder.decode(bodyPart.getContentDisposition().getFileName(), "UTF-8");
            String suffix = fileName.split("\\.")[1];
            if(!FileUtil.checkIsFileFormat(suffix, format)){
                return String.format("%s 不是%s格式",fileName,format);
            }
            // 文件大小
            BodyPartEntity bodyPartEntity = (BodyPartEntity)bodyPart.getEntity();
            long fileSize = ByteStreams.toByteArray(bodyPartEntity.getInputStream()).length;
            LOGGER.info(String.format("%s 文件大小: %s",fileName,fileSize));
            if(fileSize > FileUtil.FILE_MAX_SIZE){
                bodyPartEntity.close();
                return fileName + FileUtil.FILE_OUT_OF_SIZE;
            }
        }
        return null;
    }


    /**
     * 上传文件
     * @param bodyPart          文件详细信息对象
     * @param zipQuality        压缩质量比
     * @return
     * @throws IOException
     */
    private String upload(BodyPart bodyPart,String groupName,int zipQuality) throws IOException {
        String fileName = URLDecoder.decode(bodyPart.getContentDisposition().getFileName(), "UTF-8");
        File f = File.createTempFile(groupName, fileName);
        FileUtils.copyInputStreamToFile(((BodyPartEntity) bodyPart.getEntity()).getInputStream(), f);
        String url = uploadToUpYun(groupName, f, fileName, zipQuality);
        Files.delete(f.toPath());
        return url;
    }

    private String upload(String url ,String groupName,int zipQuality,String suffix) throws IOException {
        URLConnection urlConnection = new URL(url).openConnection();
        return upload(urlConnection.getInputStream(),groupName,zipQuality,suffix);
    }

    private String upload(InputStream inputStream,String groupName,int zipQuality,String suffix) throws IOException {
        String fileName = FileUtil.getRandomFileName(suffix);
        File f = File.createTempFile(groupName, fileName);
        FileUtils.copyInputStreamToFile(inputStream, f);
        String url = uploadToUpYun(groupName, f, fileName, zipQuality);
        Files.delete(f.toPath());
        return url;
    }

    private String uploadToUpYun(String groupName,File f,String fileName, int zipQuality){
        long st = System.currentTimeMillis();
        String url = upYunService.upload(groupName, f, fileName.split("\\.")[1], zipQuality);
        long et = System.currentTimeMillis();
        LOGGER.info(String.format("%s上传至又拍云所花费时间为：%s ms", fileName, (et - st)));
        return url;
    }





}
