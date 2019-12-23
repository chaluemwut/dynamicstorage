package nsc.dynamicstorage;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Google cloud storage
 */
@Component("google")
@PropertySource("classpath:application.properties")
public class GoogleCloudStorage implements DynamicStorageInterface {
    private Storage storage = null;
    @Autowired
    private Environment env;

    @Value("classpath:credentials.json")
    private Resource myFile;

    private void setStorage() {
        if (storage == null) {
            try {
                Credentials credentials = GoogleCredentials.fromStream(myFile.getInputStream());
                storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void save(MultipartFile multipartFile, String destinationPath) throws IOException {
        setStorage();
        List<Acl> acls = new ArrayList<>();
        acls.add(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        Blob blob =
                storage.create(
                        BlobInfo.newBuilder(getBucketName(), destinationPath).setAcl(acls).setContentType("image/png").build(),
                        multipartFile.getBytes());
    }

    @Override
    public void remove(String path) {
        Blob b = getBlob(path);
        b.delete();
    }

    private Blob getBlob(String path) {
        setStorage();
        return storage.get(getBucketName(), path);
    }

    @Override
    public byte[] getContent(String path) {
        Blob b = getBlob(path);
        return b.getContent();
    }

    @Override
    public boolean isExist(String path) {
        Blob b = getBlob(path);
        return b.exists();
    }

    private String getBucketName() {
        String bucketName = env.getProperty("dynamicstorage.google.bucketname");
        if (bucketName == null) {
            new RuntimeException("Please config dynamicstorage.google.bucketname in application.properties");
        }
        return bucketName;
    }

}
