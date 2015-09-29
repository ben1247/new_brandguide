package com.shuyun.brandguide.utils.props;

/**
 * Component:
 * Description:
 * Date: 15/9/28
 *
 * @author yue.zhang
 */
public enum MicroPropsKey{

    LILY_POS_APP_KEY("lily.pos.app.key"),
    LILY_POS_APP_SECRET("lily.pos.app.secret"),
    LILY_POS_SERVER_URL("lily.pos.server.url");

    private final String value;

    MicroPropsKey(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
