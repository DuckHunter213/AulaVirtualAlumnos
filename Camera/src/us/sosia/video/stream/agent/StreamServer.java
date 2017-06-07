package us.sosia.video.stream.agent;

import java.awt.Dimension;
import java.net.InetSocketAddress;

import com.github.sarxos.webcam.Webcam;

public class StreamServer {

    static String host;
    static int port;
    
    public StreamServer(String host,int port) {
        this.host = host;
        this.port = port;
    }
    
    public void correrServidor() {
        Webcam.setAutoOpenMode(true);
        Webcam webcam = Webcam.getDefault();
        Dimension dimension = new Dimension(320, 240);
        webcam.setViewSize(dimension);

        StreamServerAgent serverAgent = new StreamServerAgent(webcam, dimension);
        serverAgent.start(new InetSocketAddress(host, port));
        System.out.println("Video iniciado");
    }

}
