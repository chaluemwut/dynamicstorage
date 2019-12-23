package nsc.dynamicstorage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Interface
 */
public interface DynamicStorageInterface {

    public void save(MultipartFile multipartFile, String destinationPath) throws IOException;

    public void remove(String path);

    public byte[] getContent(String path) throws IOException;

    public boolean isExist(String path);

}
