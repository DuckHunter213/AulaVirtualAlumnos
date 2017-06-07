package us.sosia.video.stream.agent;

import com.github.sarxos.webcam.Webcam;
import java.awt.Dimension;
import java.net.InetSocketAddress;


public class StreamServer {
    
    public static void main(String[] args) {
        Webcam.setAutoOpenMode(true);
        Webcam webcam = Webcam.getDefault();
        Dimension dimension = new Dimension(320,240);
        webcam.setViewSize(dimension);

        StreamServerAgent serverAgent = new StreamServerAgent(webcam, dimension);
        //serverAgent.start(new InetSocketAddress(args[0], Integer.valueOf(args[1])));
        serverAgent.start(new InetSocketAddress("localhost", 20000));
        System.out.println("Video iniciado");
    }

}
