package resources;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Component:
 * Description:
 * Date: 15/9/24
 *
 * @author yue.zhang
 */
public class FileResourceTest {

    @Test
    public void fileName() throws UnsupportedEncodingException {
        String fileName = "crt%E9%85%8D%E7%BD%AE%E5%86%85%E7%BD%91%E6%95%B0%E6%8D%AE%E5%BA%93%E5%AF%B9%E5%A4%96%E8%AE%BF%E9%97%AE.png";
//        String newFileName = new String(fileName.getBytes("GB2312"), "UTF-8");
        String newFileName = URLDecoder.decode(fileName, "UTF-8");
        System.out.println(newFileName);
    }

    @Test
    public void random(){
        System.out.println( LocalDateTime.of(2015, 1, 1, 0, 0).until(LocalDateTime.now(), ChronoUnit.MICROS) + RandomStringUtils.random(3, false, true));
    }
}
