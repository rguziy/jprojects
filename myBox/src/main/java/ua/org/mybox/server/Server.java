package ua.org.mybox.server;

import java.io.IOException;

import ua.org.mybox.common.Credential;
import ua.org.mybox.common.FileInfo;

public interface Server {

    public FileInfo[] getInfos(Credential credential) throws IOException;

}
