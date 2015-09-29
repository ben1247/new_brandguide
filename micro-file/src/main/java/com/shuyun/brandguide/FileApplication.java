package com.shuyun.brandguide;

import com.shuyun.brandguide.yml.CoreYmlAction;
import com.shuyun.motor.dropwizard.MotorApplication;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import java.io.File;

/**
 * Component:
 * Description: 启动示例：java -jar  service.jar  server  /path/sample.yml
 * Date: 15/9/18
 *
 * @author yue.zhang
 */
public class FileApplication extends MotorApplication<MicroConfiguration> {

    @Override
    protected void runApp(MicroConfiguration microConfiguration, Environment environment) {
        environment.jersey().packages("com.shuyun.brandguide");
        environment.jersey().register(MultiPartFeature.class);

        AppConfig.init(microConfiguration);
    }

    public static void main(String [] args) throws Exception{

        String environment = "test";
        String coreYmlPath = FileApplication.class.getClassLoader().getResource("micro-core.yml").getPath();
        CoreYmlAction.readCoreYmlToStoreSystem(environment,coreYmlPath);


        String microYml = "micro-file.yml";
        if (args == null || args.length == 0) {
            String path = FileApplication.class.getClassLoader().getResource(microYml).getPath();
            args = new String[]{"server", path};
        }else{
            args = new String[]{"server", args[0]+ File.separator + microYml};
        }

        new FileApplication().run(args);
    }

}
