package ua.org.mybox.server;

import javax.xml.ws.Endpoint;

public class ServerWSPublisher {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:9999/ws/server", new ServerWSImpl());
    }

}
