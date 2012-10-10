package ua.org.mybox.common;

import java.util.Date;

public class FileInfo {

    private String name = null;
    private String md5Hash = null;
    private String path = null;
    private Date date = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMd5Hash() {
        return md5Hash;
    }

    public void setMd5Hash(String md5Hash) {
        this.md5Hash = md5Hash;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "File name = " + name + "; MD5 hash = " + md5Hash
                + "; Relative path = " + path + "; Modification date = " + date;
    }

}
