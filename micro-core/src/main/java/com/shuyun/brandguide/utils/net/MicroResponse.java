package com.shuyun.brandguide.utils.net;

import javax.ws.rs.core.Response;

/**
 * Component:
 * Description:
 * Date: 15/9/25
 *
 * @author yue.zhang
 */
public class MicroResponse {

    /**
     * 200
     * @param entity
     * @return
     */
    public static Response OK(Object entity){
        return getResponse(Response.Status.OK.getStatusCode(),entity);
    }

    /**
     * 400
     * @param entity
     * @return
     */
    public static Response BAD_REQUEST(Object entity){
        return getResponse(Response.Status.BAD_REQUEST.getStatusCode(),entity);
    }

    private static Response getResponse(int statusCode,Object entity){
        return Response.status(statusCode).entity(entity).encoding("UTF-8").build();
    }

}
