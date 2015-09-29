package com.shuyun.brandguide.model;

/**
 * Component:
 * Description:
 * Date: 15/9/25
 *
 * @author yue.zhang
 */
public class FileUploadRequest {
    private String url;
    private String suffix;

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
