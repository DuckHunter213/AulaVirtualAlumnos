/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import util.FXGenerico;

/**
 * FXML Controller class
 *
 * @author gerar
 */
public class MarcoPrincipalController extends FXGenerico implements Initializable{

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
    private ListView<?> listaAlumnosConectados;

    public Pane root;
    private static ArrayList<PrintWriter> flujosSalida;
    public static Map<String, PrintWriter> hm;
    private static PrintWriter out;
    @FXML
    private Button botonEnviar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        root = (Pane) this.rootPanel;
        cliente();
    }
    
    private void cliente() {
        Socket cliente = conectarServidor();
        try {
            out = new PrintWriter(cliente.getOutputStream(), true);
            areaMensajes.setText("Bienvenid@!");

            BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            RefrescadorMensajes rm = new RefrescadorMensajes(in, areaMensajes);
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

    @FXML
    private void enviarMensaje(MouseEvent event) {
        String mensaje = textFieldMensaje.getText().trim();
        textFieldMensaje.setText("");
        if (mensaje.equals("")) {
            return;
        }
        out.println(mensaje);
        System.out.println(mensaje);
        out.flush();
    }

}
