package laucher;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.InetSocketAddress;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.sosia.video.stream.agent.StreamClient;
import us.sosia.video.stream.agent.StreamClientAgent;
import us.sosia.video.stream.agent.ui.VideoPanel;
import us.sosia.video.stream.handler.StreamFrameListener;
import laucher.Servidor;

public class SwingNodeExample extends Application {
    
    static VideoPanel panel = new VideoPanel();
    private final static Dimension dimension = new Dimension(400, 462);
    protected final static Logger logger = LoggerFactory.getLogger(StreamClient.class);

    @Override
    public void start(Stage stage) {
        Servidor servidor = new Servidor("localhost", 20000);
        final SwingNode swingNode = new SwingNode();
        createAndSetSwingContent(swingNode);

        Pane pane = new Pane();
        pane.getChildren().add(swingNode); // Adding swing node

        CamaraWebPane camaraWebPane = new CamaraWebPane("localhost", 20000);
        stage.setScene(new Scene(camaraWebPane.getPanelVideo(), 400, 462));
        stage.show();
    }

    private void createAndSetSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JPanel panel2 = new JPanel();
                panel2.setSize(dimension);
                panel2.setPreferredSize(dimension);

                logger.info("setup dimension :{}",dimension);
                StreamClientAgent clientAgent = new StreamClientAgent(new StreamFrameListenerIMPL(), dimension);
                clientAgent.connect(new InetSocketAddress("localhost", 20000));
                
                panel.setSize(dimension);
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
            logger.info("frame received :{}",count++);
            panel.updateImage(image);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}