package laucher;

import com.github.sarxos.webcam.Webcam;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.InetSocketAddress;
import javafx.embed.swing.SwingNode;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.sosia.video.stream.agent.StreamClient;
import us.sosia.video.stream.agent.StreamClientAgent;
import us.sosia.video.stream.agent.ui.VideoPanel;
import us.sosia.video.stream.handler.StreamFrameListener;

import javafx.scene.layout.Pane;

public class CamaraWebPane extends Pane {

    int port;
    String host;
    Webcam webcam;
    static VideoPanel panel = new VideoPanel();
    private final static Dimension dimension = new Dimension(320, 240);
    protected final static Logger logger = LoggerFactory.getLogger(StreamClient.class);

    public CamaraWebPane(String host,int port) {
        final SwingNode swingNode = new SwingNode();
        createAndSetSwingContent(swingNode);
        this.host = host;
        this.port = port;
    }    
    
    private void createAndSetSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JPanel panel2 = new JPanel();
                panel2.setSize(dimension);
                panel2.setSize(320, 240);
                panel2.setPreferredSize(dimension);

                logger.info("setup dimension :{}", dimension);
                StreamClientAgent clientAgent = new StreamClientAgent(new StreamFrameListenerIMPL(), dimension);
                clientAgent.connect(new InetSocketAddress(host, port));

                panel.setSize(320, 240);
                panel.setPreferredSize(dimension);

                panel2.add(panel);
                swingNode.setContent(panel2);
            }
        });
    }

    protected static class StreamFrameListenerIMPL implements StreamFrameListener {

        private volatile long count = 0;

        @Override
        public void onFrameReceived(BufferedImage image) {
            logger.info("frame received :{}", count++);
            panel.updateImage(image);
        }
    }
}
