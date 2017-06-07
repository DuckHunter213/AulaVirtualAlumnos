/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import cliente.Cliente;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import laucher.CamaraWebPane;
import util.FXGenerico;
import laucher.Servidor;
import org.slf4j.LoggerFactory;
import us.sosia.video.stream.agent.StreamClient;
import us.sosia.video.stream.agent.StreamClientAgent;
import us.sosia.video.stream.agent.ui.VideoPanel;
import us.sosia.video.stream.handler.StreamFrameListener;

/**
 * FXML Controller class
 *
 * @author gerar
 */
public class MarcoPrincipalController extends FXGenerico implements Initializable {

    static VideoPanel panel = new VideoPanel();
    private final static Dimension dimension = new Dimension(320,240);
    protected final static org.slf4j.Logger logger = LoggerFactory.getLogger(StreamClient.class);

    @FXML
    private ScrollPane scrollPaneListaAlumnos;
    @FXML
    private Pane panelChat;
    @FXML
    private Pane panelVideo;
    @FXML
    private Pane panelDiapositivas;
    @FXML
    private AnchorPane rootPanel;
    @FXML
    private TextArea areaMensajes;
    @FXML
    private TextField textFieldMensaje;
    @FXML
    private static ListView<String> listaAlumnosConectados = new ListView<>();

    public Pane root;
    private static ArrayList<PrintWriter> flujosSalida;
    public static Map<String, PrintWriter> hm;
    private static PrintWriter out;
    private static ListProperty<String> listProperty = new SimpleListProperty<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        root = (Pane) this.rootPanel;
        Thread hiloServidor = iniciarServidor();
        Thread hiloVideo = iniciarTransmisionVideo();
        hiloServidor.start();
        hiloVideo.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(MarcoPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        final SwingNode swingNode = new SwingNode();
        createAndSetSwingContent(swingNode);
        
        panelVideo.getChildren().add(swingNode); //AQUÍ ESTOY HACIENDO ALGO ASÍ PERO SEPARADO :/

        

        //Así recibe el mensaje de los alumnos
        cliente();
    }

    private void createAndSetSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JPanel panel2 = new JPanel();
                panel2.setSize(dimension);
                panel2.setPreferredSize(dimension);

                logger.info("setup dimension :{}", dimension);
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
            logger.info("frame received :{}", count++);
            panel.updateImage(image);
        }
    }

    private static Thread iniciarTransmisionVideo() {
        return new Thread() {
            @Override
            public void run() {
                laucher.Servidor video = new Servidor("localhost", 20000);
            }
        };
    }

    private static Thread iniciarServidor() {
        return new Thread() {
            @Override
            public void run() {
                // TODO code application logic here
                int puerto = 8080;
                MarcoPrincipalController.flujosSalida = new ArrayList<>();

                ServerSocket serv = null;

                flujosSalida = new ArrayList<>();
                hm = new HashMap<>();
                try {
                    serv = new ServerSocket(puerto);
                    while (true) {  //esperando conexiones nuevas
                        Socket cliente = serv.accept();
                        //BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                        out = new PrintWriter(cliente.getOutputStream(), true);
                        MarcoPrincipalController.flujosSalida.add(out);

                        //crear hilo
                        Servicio atencion = new Servicio(cliente);
                        new Thread(atencion).start();

                    }
                } catch (IOException ex) {
                    Logger.getLogger(MarcoPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
    }

    private void cliente() {
        Socket cliente = conectarServidor();
        try {
            out = new PrintWriter(cliente.getOutputStream(), true);
            areaMensajes.setText("Bienvenid@!");

            BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            cliente.RefrescadorMensajes rm = new cliente.RefrescadorMensajes(in, areaMensajes);
            new Thread(rm).start();

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static Socket conectarServidor() {
        Socket cliente = null;
        try {
            cliente = new Socket("127.0.0.1", 8080);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cliente;
    }

    public static void broadcast(String nick, String mensaje) {
        synchronized (MarcoPrincipalController.flujosSalida) {
            String mensajeCompleto = "<" + nick + ">: " + mensaje;
            for (PrintWriter pr : MarcoPrincipalController.flujosSalida) {
                pr.println(mensajeCompleto);
                pr.flush();
            }
        }
    }

    @FXML
    private void enviarMensaje(MouseEvent event) {
        String mensaje = this.textFieldMensaje.getText().trim();
        this.textFieldMensaje.setText("");
        if (mensaje.equals("")) {
            return;
        }
        broadcast("Maestro", mensaje);
    }

}
