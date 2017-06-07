package laucher;


import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import java.awt.Color;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SwingNodeExample extends Application {

    Webcam webcam;
    WebcamPanel panel;

    @Override
    public void start(Stage stage) {
        final SwingNode swingNode = new SwingNode();
        createAndSetSwingContent(swingNode);

        Pane pane = new Pane();
        pane.getChildren().add(swingNode); // Adding swing node

        stage.setScene(new Scene(pane, 100, 50));
        stage.show();
    }

    private void createAndSetSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JPanel panel2 = new JPanel();
                webcam = Webcam.getDefault();
                System.out.println(webcam.getName());
                webcam.setViewSize(webcam.getViewSizes()[0]);
                panel = new WebcamPanel(webcam, false);
                panel.setPreferredSize(webcam.getViewSize());
                panel.setBackground(Color.black);
                panel.setOpaque(true);
                panel.setBounds(0, 0, 400, 300);
                panel.setFPSDisplayed(true);
		
		panel.setImageSizeDisplayed(true);
		panel.setMirrored(true);
                webcam.open();
                panel.setImageSizeDisplayed(true);
                panel2.add(panel);
                panel.start();
                swingNode.setContent(panel2);                
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
