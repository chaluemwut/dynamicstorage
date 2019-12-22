package nsc.dynamicstorage;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component("google")
@PropertySource("classpath:application.properties")
public class GoogleCloudStorage implements DynamicStorageInterface {
    private Storage storage = null;
    @Autowired
    private Environment env;

    public GoogleCloudStorage() {
        try {
            File file = new ClassPathResource("credentials.json").getFile();
            Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(file));
            storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(MultipartFile multipartFile, String destinationPath) throws IOException {
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
        return storage.get(getBucketName(), path);
    }

    @Override
    public void createFolder(String path) {
        new UnsupportedOperationException("Do not necessary in Google cloud storage");
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

    @Override
    public String getInfo(String path) {
        return null;
    }

    @Override
    public String getURL(String path) {
        return null;
    }

    private String getBucketName(){
        String bucketName = env.getProperty("dynamicstorage.google.bucketname");
        if(bucketName == null){
            new RuntimeException("Please config dynamicstorage.google.bucketname in application.properties");
        }
        return bucketName;
    }

}
