package laucher;

import us.sosia.video.stream.agent.StreamServer;

public class Servidor {

    public Servidor(String host,int port) {
        String[] args = new String[2];
        args[0] = host;
        args[1] = String.valueOf(port);
        StreamServer.main(args);
    }
}
