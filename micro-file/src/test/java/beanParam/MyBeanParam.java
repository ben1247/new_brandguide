package beanParam;

import javax.ws.rs.*;

/**
 * Component:
 * Description:
 * Date: 15/9/23
 *
 * @author yue.zhang
 */
public class MyBeanParam {

    @PathParam("p")
    private String pathParam;

    @MatrixParam("m")
    @Encoded
    @DefaultValue("default")
    private String matrixParam;

    @HeaderParam("header")
    private String headerParam;

    private String queryParam;

    public MyBeanParam(@QueryParam("q") String queryParam) {
        this.queryParam = queryParam;
    }

    public String getPathParam() {
        return pathParam;
    }

}
