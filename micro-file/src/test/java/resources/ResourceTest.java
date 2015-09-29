package resources;

import beanParam.MyBeanParam;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Map;

/**
 * Component:
 * Description:
 * Date: 15/9/23
 *
 * @author yue.zhang
 */
public class ResourceTest {

    /**
     * For header and cookie parameters the following
     * @param hh
     * @return
     */
    @GET
    public String get(@Context HttpHeaders hh) {
        MultivaluedMap<String, String> headerParams = hh.getRequestHeaders();
        Map<String, Cookie> pathParams = hh.getCookies();
        return null;
    }

    /**
     * Obtaining general map of URI path and/or query parameters
     * @param ui
     * @return
     */
    @GET
    public String get(@Context UriInfo ui) {
        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
        MultivaluedMap<String, String> pathParams = ui.getPathParameters();
        return null;
    }

    /**
     * Processing POSTed HTML form
     * @param name
     */
    @POST
    @Consumes("application/x-www-form-urlencoded")
    public void post(@FormParam("name") String name) {
        // Store the message
    }

    /**
     * Obtaining general map of form parameters
     * @param formParams
     */
    @POST
    @Consumes("application/x-www-form-urlencoded")
    public void post(MultivaluedMap<String, String> formParams) {
        // Store the message
    }

    /**
     * Example of the bean which will be used as @BeanParam
     * Injection of MyBeanParam as a method parameter:
     * @param beanParam
     * @param entity
     */
    @POST
    public void post(@BeanParam MyBeanParam beanParam, String entity) {
        final String pathParam = beanParam.getPathParam(); // contains injected path parameter "p"

    }



}
