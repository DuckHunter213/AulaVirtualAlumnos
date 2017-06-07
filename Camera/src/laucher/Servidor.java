package laucher;

import us.sosia.video.stream.agent.StreamServer;

public class Servidor {

    public Servidor(String host,int port) {
        StreamServer streamServer = new StreamServer(host,port);
    }
    
    
}
