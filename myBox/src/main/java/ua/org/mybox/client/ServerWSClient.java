package ua.org.mybox.client;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import javax.activation.DataHandler;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import com.sun.xml.internal.ws.Closeable;
import com.sun.xml.internal.ws.developer.JAXWSProperties;

import ua.org.mybox.common.Credential;
import ua.org.mybox.common.FileInfo;
import ua.org.mybox.common.Util;
import ua.org.mybox.server.ServerWS;

public class ServerWSClient {

    public final static String BASE_PATH = "C:/temp/";

    public final static String WSDL_URL = "http://localhost:9999/ws/server?wsdl";
    public final static String WS_NAME_SPACE = "http://server.mybox.org.ua/";
    public final static String WS_LOCAL_PART = "ServerWSImplService";

    public ServerWS getServerStub() throws MalformedURLException {
        URL url = new URL(WSDL_URL);
        QName qname = new QName(WS_NAME_SPACE, WS_LOCAL_PART);
        Service service = Service.create(url, qname);
        ServerWS serverWS = service.getPort(ServerWS.class);
        // Enable MTOM in client
        BindingProvider bindingProvider = (BindingProvider) serverWS;
        SOAPBinding binding = (SOAPBinding) bindingProvider.getBinding();
        binding.setMTOMEnabled(true);
        // Enable HTTP chunking mode, otherwise HttpURLConnection buffers
        Map<String, Object> ctxt = bindingProvider.getRequestContext();
        ctxt.put(JAXWSProperties.HTTP_CLIENT_STREAMING_CHUNK_SIZE, 8192);
        return serverWS;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        ServerWSClient client = new ServerWSClient();
        long startTime = new Date().getTime();
        try {
            ServerWS serverWS = client.getServerStub();

            System.out.println("Get infos.......");
            FileInfo[] infos = serverWS.getInfos(new Credential());
            if (infos != null && infos.length > 0) {
                System.out.println("count of files = " + infos.length);
            }
            long finishTime = new Date().getTime();
            System.out.println("working time = " + (finishTime - startTime)
                    / 1000 + " seconds");
            System.out.println("--------------\n");

            System.out.println("Get files........");
            DataHandler dataHandler = null;
            FileOutputStream outputStream = null;
            startTime = new Date().getTime();
            for (FileInfo info : infos) {
                dataHandler = serverWS.downloadFile(new Credential(), info);
                String fullPath = BASE_PATH + info.getPath();
                Util.createFile(fullPath);
                outputStream = new FileOutputStream(fullPath);
                dataHandler.writeTo(outputStream);
                outputStream.close();
            }
            finishTime = new Date().getTime();
            System.out.println("working time = " + (finishTime - startTime)
                    / 1000 + " seconds");
            System.out.println("--------------\n");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
