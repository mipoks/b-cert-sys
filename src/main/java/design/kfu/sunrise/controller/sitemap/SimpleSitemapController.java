package design.kfu.sunrise.controller.sitemap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import jakarta.annotation.security.PermitAll;
import java.io.*;

/**
 * @author Daniyar Zakiev
 */
@Slf4j
@RestController
@RequestMapping
public class SimpleSitemapController {

    @PermitAll
    @GetMapping("/health")
    public Boolean isServerHealth() {
        return true;
    }

    @PermitAll
    @GetMapping(value = "/robots.txt", produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<StreamingResponseBody> getRobotsTxt() throws FileNotFoundException {
        File file = new File("robots.txt").getAbsoluteFile();
        InputStream videoFileStream = new FileInputStream(file);
        return ResponseEntity.ok((os) -> readAndWrite(videoFileStream, os));
    }

    //Spring генерирует SitemapController
//    @PermitAll
//    @PutMapping(value = "/sitemap.xml", produces = {MediaType.APPLICATION_XML_VALUE})
//    public StreamingResponseBody getSitemap() throws FileNotFoundException {
//        File file = new File("sitemap.xml").getAbsoluteFile();
//        InputStream videoFileStream = new FileInputStream(file);
//        return (os) -> {
//            readAndWrite(videoFileStream, os);
//        };
//    }

    private void readAndWrite(InputStream is, OutputStream os)
            throws IOException {
        byte[] data = new byte[2048];
        int read = 0;
        while ((read = is.read(data)) > 0) {
            os.write(data, 0, read);
        }
        os.flush();
    }
}
