package nsc.dynamicstorage;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Component("native")
public class NativeStorage implements DynamicStorageInterface {
    @Override
    public void save(MultipartFile multipartFile, String destinationPath) throws IOException {
        String arr[] = destinationPath.split("/");
        String destPath = "";
        for (int i = 0; i < arr.length - 1; i++) {
            if (!arr[i].isEmpty()) {
                destPath = destPath + "/" + arr[i];
            }
        }
        File f = new File(destPath);
        if (!f.exists()) {
            f.mkdirs();
        }

        File fileContent = new File(destinationPath);
        BufferedOutputStream buf = new BufferedOutputStream(new FileOutputStream(fileContent));
        byte[] bytes = multipartFile.getBytes();
        buf.write(bytes);
        buf.close();
    }

    @Override
    public void remove(String path) {
        File f = new File(path);
        f.delete();
    }

    @Override
    public byte[] getContent(String path) throws IOException {
        InputStream in = new FileInputStream(new File(path));
        return IOUtils.toByteArray(in);
    }

    @Override
    public boolean isExist(String path) {
        File f = new File(path);
        return f.exists();
    }

}
