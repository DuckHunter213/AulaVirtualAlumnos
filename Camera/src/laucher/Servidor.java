package laucher;

import us.sosia.video.stream.agent.StreamServer;

public class Servidor {

    StreamServer streamServer;
    
    public Servidor(String host,int port) {
        streamServer = new StreamServer(host,port);
        streamServer.correrServidor();
    }
}
