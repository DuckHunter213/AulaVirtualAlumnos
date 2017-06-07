import Cliente.Cliente;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.List;

public class ControllerEscena implements Initializable, Cliente {
    @FXML
    private AnchorPane panelChat;
    @FXML
    private TitledPane Diapositivas;
    @FXML
    private ProgressIndicator Indicador;
    @FXML
    private TableView<Usuario> tablaDeUsuarios;
    @FXML
    private TableColumn columnaNombre;
    @FXML
    private ImageView presentacion;
    @FXML
    private Label numeroDeDiapositiva;
    @FXML
    private Label descargando;
    @FXML
    private Label notificacion;
    @FXML
    private ProgressIndicator notificacionContorno;
    @FXML
    private Pane areaDeDibujo;
    @FXML
    private VBox panelVideo;

    /************************************************************/
    private ConversationView conversacion;
    private List<Usuario> usuarios;
    private Map<Integer,Image> listaDeDiapositivas;
    private double progreso = 0;
    private double add = 0;
    private int total = 0;
    private Integer diapositivaActual = 0;
    private Integer diapositivaActualDeMaestro = 5;
    private Path path;
    /***********************************************************/

    private void conectar() {
        try {
            UnicastRemoteObject.exportObject(this, 0);
            new Conexion().conectar(this);
        } catch (Exception e) {
            System.out.println("Error al inicializar");
        }
    }

    public void actualizarIndicador(){
        Platform.runLater(()->{
            this.Indicador.setProgress(progreso);
        });
    }

    public  void actualizarDiapositiva(){
        if(this.diapositivaActual == this.diapositivaActualDeMaestro){
            this.notificacion.setVisible(false);
            this.notificacionContorno.setVisible(false);
        }else {
            this.notificacion.setVisible(true);
            this.notificacionContorno.setVisible(true);
        }
    }

    public void actualizarListaDeUsuarios(){
        this.tablaDeUsuarios.setItems(FXCollections.observableArrayList(this.usuarios));
    }

    public void mostrarDispositiva(int numeroDeDiapositiva){
        this.presentacion.setImage(this.listaDeDiapositivas.get(numeroDeDiapositiva));
        this.numeroDeDiapositiva.setText("Diapositiva "+numeroDeDiapositiva+" de "+this.total);
    }

    /*************************************************************/
    @FXML
    public void verDiapositivaAnterior(){
        if(this.diapositivaActual>1){
            this.diapositivaActual--;
            this.mostrarDispositiva(this.diapositivaActual);
            this.actualizarDiapositiva();
        }
    }

    @FXML
    public void verDiapositivaSiguiente(){
        if(this.diapositivaActual<this.total){
            this.diapositivaActual++;
            this.mostrarDispositiva(this.diapositivaActual);
            this.actualizarDiapositiva();
        }
    }

    @FXML
    public void verDiapositivaActual(){
        this.diapositivaActual = this.diapositivaActualDeMaestro;
        this.mostrarDispositiva(this.diapositivaActual);
        this.actualizarDiapositiva();
    }

    /***********************************************************/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.conversacion = new ConversationView("Bienvenido " + EscenaPrincipal.usuario);
        this.conversacion.setPrefSize(450, 225);
        this.panelChat.getChildren().add(conversacion);
        this.Indicador.setProgress(-1);
        this.listaDeDiapositivas = new HashMap<>();
        this.usuarios=new ArrayList<>();
        this.columnaNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        path = new Path();
        path.setStrokeWidth(3);
        path.setStroke(Color.BLACK);
        areaDeDibujo.getChildren().add(path);
        this.conectar();
        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();
        webEngine.load("http://192.168.0.18:8080");
        panelVideo.getChildren().addAll(browser);
    }

    @Override
    public void recibirMensaje(String usuario, String mensaje) throws RemoteException {
        Platform.runLater(() -> {
            if (EscenaPrincipal.usuario.equals(usuario)) {
                this.conversacion.sendMessage(usuario + ":\n" + mensaje);
            } else {
                this.conversacion.receiveMessage(usuario + ":\n" + mensaje);
            }
        });
    }

    @Override
    public void listaDeConectados(ArrayList<String> usuarios) throws RemoteException {
        this.usuarios.clear();
        for (String nombre: usuarios){
            this.usuarios.add(new Usuario(nombre));
        }
        this.actualizarListaDeUsuarios();
    }

    @Override
    public void diapositivas(int numero, byte[] diapositiva) throws RemoteException {
        if (diapositiva == null){
            this.total = numero;
            this.add = (double) 1/numero;
            this.progreso = 0;
            this.actualizarIndicador();
        }else{
            Image image = null;
            try {
                ByteArrayInputStream in = new ByteArrayInputStream(diapositiva);
                BufferedImage read = ImageIO.read(in);
                image = SwingFXUtils.toFXImage(read, null);
            }catch (Exception e){
                System.out.println("Error al recibir diapositiva no. "+numero);
            }
            this.listaDeDiapositivas.put(numero, image);
            this.progreso += this.add;
            this.actualizarIndicador();
            if(numero==total){
                this.descargando.setVisible(false);
                this.Indicador.setVisible(false);
                this.diapositivaActual = 1;
                this.mostrarDispositiva(1);
                this.actualizarDiapositiva();
            }
        }
    }

    @Override
    public void diapositivaActual(int numero) throws RemoteException {
        this.diapositivaActualDeMaestro = numero;
        this.actualizarDiapositiva();
    }

    @Override
    public void actualizarAreaDeDibujo(int operacion, double x, double y) throws RemoteException {
        Platform.runLater(()->{
            switch (operacion){
                case 1:
                    this.path.getElements().add(new MoveTo(x,y));
                    break;
                case 2:
                    this.path.getElements().add(new LineTo(x,y));
                    break;
                case 3:
                    this.path.getElements().clear();
                    break;
            }
        });
    }
}
