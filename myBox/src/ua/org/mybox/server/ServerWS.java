package ua.org.mybox.server;

import javax.activation.DataHandler;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import ua.org.mybox.common.Credential;
import ua.org.mybox.common.FileInfo;

@WebService
@SOAPBinding(style = Style.RPC)
public interface ServerWS extends Server {
    public DataHandler downloadFile(Credential credential, FileInfo info);
}
