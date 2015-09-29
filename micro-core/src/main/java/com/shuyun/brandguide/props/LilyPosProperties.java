package com.shuyun.brandguide.props;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Component: Lily POS 接口属性
 * Description:
 * Date: 15/9/28
 *
 * @author yue.zhang
 */
public class LilyPosProperties {

    private String appKey;
    private String appSecret;
    private String serverUrl;

    @JsonProperty
    public String getAppKey() {
        return appKey;
    }

    @JsonProperty
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @JsonProperty
    public String getAppSecret() {
        return appSecret;
    }

    @JsonProperty
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    @JsonProperty
    public String getServerUrl() {
        return serverUrl;
    }

    @JsonProperty
    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
