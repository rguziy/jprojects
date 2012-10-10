package sample.ws.server;

import javax.xml.ws.Endpoint;

//Endpoint publisher
public class HelloWorldPublisher {

    private static final String ENDPOINT_ADDRESS = "http://localhost:9999/ws/hello";

    public static void main(String[] args) {
        Endpoint.publish(ENDPOINT_ADDRESS, new HelloWorldImpl());
    }

}
