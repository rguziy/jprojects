package ua.org.mybox.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.ws.soap.MTOM;

import com.sun.xml.internal.ws.developer.StreamingAttachment;
import com.sun.xml.internal.ws.developer.StreamingDataHandler;

import ua.org.mybox.common.Credential;
import ua.org.mybox.common.FileInfo;
import ua.org.mybox.common.Util;

@MTOM
@StreamingAttachment(parseEagerly = true, memoryThreshold = 40000L)
@WebService(endpointInterface = "ua.org.mybox.server.ServerWS")
public class ServerWSImpl implements ServerWS {

    public final static String BASE_PATH = "E:/temp/flash/";

    // Use @XmlMimeType to map to DataHandler on the client side
    public void uploadFile(Credential credential, FileInfo info,
            @XmlMimeType("application/octet-stream") DataHandler data)
            throws IOException {
        StreamingDataHandler dataHandler = (StreamingDataHandler) data;
        File file = new File(BASE_PATH + info.getPath());
        dataHandler.moveTo(file);
        dataHandler.close();
    }

    @Override
    @XmlMimeType("application/octet-stream")
    @WebMethod
    public DataHandler downloadFile(Credential credential, FileInfo info) {
        return new DataHandler(new FileDataSource(BASE_PATH + info.getPath()));
    }

    @Override
    @WebMethod
    public FileInfo[] getInfos(Credential credential) throws IOException {
        // Return result can not be null. This is BP 1.1 R2211 violation.
        FileInfo[] infoArray = new FileInfo[0];
        List<FileInfo> infos = null;
        Collection<File> files = Util.listFiles(BASE_PATH);
        if (files != null && files.size() > 0) {
            infos = new ArrayList<FileInfo>();
            for (File file : files) {
                infos.add(Util.getInfo(BASE_PATH, file));
            }
            if (infos.size() > 0) {
                infoArray = infos.toArray(new FileInfo[0]);
            }
        }
        return infoArray;
    }
}
