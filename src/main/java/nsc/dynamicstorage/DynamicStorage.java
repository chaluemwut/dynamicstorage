package nsc.dynamicstorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@Component
@PropertySource("classpath:application.properties")
public class DynamicStorage {
    @Autowired
    private Environment env;

    @Resource(name = "google")
    private DynamicStorageInterface dsGoogle;

    @Resource(name = "native")
    private DynamicStorageInterface dsNative;

    public void save(MultipartFile file, String path) throws IOException {
        DynamicStorageInterface dynamicStorageInterface = getFSInterface();
        dynamicStorageInterface.save(file, path);
    }

    public byte[] get(String path) throws IOException {
        DynamicStorageInterface dynamicStorageInterface = getFSInterface();
        return dynamicStorageInterface.getContent(path);
    }

    public void remove(String path) {
        DynamicStorageInterface dynamicFsInterface = getFSInterface();
        dynamicFsInterface.remove(path);
    }

    private DynamicStorageInterface getFSInterface() {
        DynamicStorageInterface dynamicStorageInterface = null;
        String fsName = env.getProperty("dynamicstorage.provider");
        if (fsName == null) {
            new RuntimeException("Please config dynamicstorage.provider in application.properties");
        }
        if ("google".equals(fsName)) {
            dynamicStorageInterface = dsGoogle;
        } else if ("native".equals(fsName)) {
            dynamicStorageInterface = dsNative;
        }
        return dynamicStorageInterface;
    }
}
