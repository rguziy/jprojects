package ua.org.mybox.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class Util {

    public static FileInfo getInfo(String basePath, File file)
            throws IOException {
        FileInfo info = null;
        if (basePath != null && file != null) {
            info = new FileInfo();
            info.setName(file.getName());
            info.setPath(getRelativePath(basePath, file));
            info.setDate(new Date(file.lastModified()));
            info.setMd5Hash(getMD5Hash(file));
        }
        return info;
    }

    public static String getMD5Hash(File file) throws IOException {
        String md5 = null;
        FileInputStream fis = null;
        if (file != null) {
            fis = new FileInputStream(file);
            md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
        }
        return md5;
    }

    public static String getRelativePath(String basePath, File file)
            throws IOException {
        String relativePath = null;
        if (basePath != null) {
            File baseFolder = new File(basePath);
            if (baseFolder.exists() && baseFolder.isDirectory()
                    && file.exists() && file.isFile()) {
                String baseFolderPath = baseFolder.getCanonicalFile().toURI()
                        .getPath();
                String filePath = file.getCanonicalFile().toURI().getPath();
                if (filePath.startsWith(baseFolderPath)) {
                    relativePath = filePath.substring(baseFolderPath.length());
                }
            }
        }
        return relativePath;
    }

    public static Collection<File> listFiles(String basePath) {
        Collection<File> files = FileUtils.listFiles(new File(basePath),
                TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        return files;
    }

    public static void createFile(String path) throws IOException {
        FileUtils.touch(new File(path));
    }

}
