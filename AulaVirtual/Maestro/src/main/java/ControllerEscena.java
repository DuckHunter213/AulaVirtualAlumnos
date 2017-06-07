import Cliente.Cliente;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class ControllerEscena implements Initializable, Cliente {
    @FXML
    private AnchorPane panelChat;
    @FXML
    private VBox panelVideo;
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
    private Label tituloDiapositivas;
    @FXML
    private Button buscar;
    @FXML
    private Pane panelDeDibujo;


    /************************************************************/
    private ConversationView conversacion;
    private List<Usuario> usuarios;
    private Map<Integer,Image> listaDeDiapositivas;
    private Map<Integer, byte[]> diapositivas;
    private int total = 0;
    private Integer diapositivaActual = 0;
    private String ruta = null;
    private double progreso = 0;
    private Servicios servicios = null;
    private Path path;
    /***********************************************************/
    private void conectar() {
        try {
            servicios = new Servicios(this.diapositivas);
            new Thread(new SegundoPlano(this.servicios)).start();
            Thread.sleep(2_000);
            UnicastRemoteObject.exportObject(this, 0);
            new Conexion().conectar(this);
        } catch (Exception e) {
            System.out.println("Error al inicializar");
        }
    }

    public void actualizarListaDeUsuarios(){
        this.tablaDeUsuarios.setItems(FXCollections.observableArrayList(this.usuarios));
    }

    public void mostrarDispositiva(int numeroDeDiapositiva){
        this.presentacion.setImage(this.listaDeDiapositivas.get(numeroDeDiapositiva));
        this.numeroDeDiapositiva.setText("Diapositiva "+numeroDeDiapositiva+" de "+this.total);
    }

    public void actualizarIndicador(){
        Platform.runLater(()->{
            Indicador.setProgress(progreso);
        });
    }

    /*************************************************************/
    @FXML
    public void verDiapositivaAnterior(){
        if(this.diapositivaActual>1){
            this.diapositivaActual--;
            this.mostrarDispositiva(this.diapositivaActual);
            EscenaPrincipal.numeroDeDiapositiva = this.diapositivaActual;
            this.servicios.actualizarNumeroDeDiapositiva();
        }
    }

    @FXML
    public void verDiapositivaSiguiente(){
        if(this.diapositivaActual<this.total){
            this.diapositivaActual++;
            this.mostrarDispositiva(this.diapositivaActual);
            EscenaPrincipal.numeroDeDiapositiva = this.diapositivaActual;
            this.servicios.actualizarNumeroDeDiapositiva();
        }
    }

    @FXML
    public void buscarDiapositivas(){
        FileChooser explorador = new FileChooser();
        explorador.setTitle("Buscar Diapositivas");
        explorador.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        try {
            this.ruta = explorador.showOpenDialog(new Stage()).getPath();
        } catch (Exception e) {
            System.out.println("Error al buscar diapositivas en PDF");
        }

        if(this.ruta!=null){
            this.tituloDiapositivas.setText("Cargando Diapositivas");
            this.tituloDiapositivas.setLayoutX(208);//88 Seleccione las diapositivas para iniciar la clase
            this.Indicador.setVisible(true);
            this.buscar.setVisible(false);
            new Thread(){
                @Override
                public void run() {
                    double add = 0;
                    try {
                        String sourceDir = ruta;
                        String destinationDir = "C:\\Users\\ossiel\\Desktop\\PDFTemporal\\";
                        File sourceFile = new File(sourceDir);
                        File destinationFile = new File(destinationDir);
                        if (!destinationFile.exists()) {
                            destinationFile.mkdir();
                        }
                        if (sourceFile.exists()) {
                            PDDocument document = PDDocument.load(sourceDir);
                            List<PDPage> list = document.getDocumentCatalog().getAllPages();
                            total = list.size();
                            add = (double) 1/(total*2);
                            int pageNumber = 1;
                            for (PDPage page : list) {
                                BufferedImage image = page.convertToImage();
                                File outputfile = new File(destinationDir + pageNumber +".png");
                                ImageIO.write(image, "png", outputfile);
                                pageNumber++;
                                progreso += add;
                                actualizarIndicador();
                            }
                            document.close();
                        }
                        add = add/2;
                        for (int i = 1; i <= total; i++) {
                            File ruta = new File(destinationDir+i+".png");
                            FileInputStream fileFoto = new FileInputStream(ruta);
                            BufferedImage bufferedImage = ImageIO.read(fileFoto);
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            ImageIO.write(bufferedImage, "png", out);
                            out.flush();
                            diapositivas.put(i,out.toByteArray());
                            progreso += add;
                            actualizarIndicador();
                        }
                        for (int i = 1; i <= total; i++) {
                            Image image = null;
                            try {
                                ByteArrayInputStream in = new ByteArrayInputStream(diapositivas.get(i));
                                BufferedImage read = ImageIO.read(in);
                                image = SwingFXUtils.toFXImage(read, null);
                            }catch (Exception e){
                                System.out.println("Error al recibir diapositiva no. "+i);
                            }
                            listaDeDiapositivas.put(i, image);
                            progreso += add;
                            actualizarIndicador();
                        }
                        progreso += 1;
                        actualizarIndicador();
                    }catch (Exception e){
                        e.printStackTrace();
                        System.out.println("Error al cargar el PDF");
                    }
                    Platform.runLater(()->{
                        diapositivaActual = 1;
                        mostrarDispositiva(1);
                        Indicador.setVisible(false);
                        tituloDiapositivas.setVisible(false);
                        conectar();
                        path = new Path();
                        path.setStrokeWidth(3);
                        path.setStroke(Color.BLACK);
                        panelDeDibujo.setOnMouseClicked(mouseHandler);
                        panelDeDibujo.setOnMouseDragged(mouseHandler);
                        panelDeDibujo.setOnMouseEntered(mouseHandler);
                        panelDeDibujo.setOnMouseExited(mouseHandler);
                        panelDeDibujo.setOnMouseMoved(mouseHandler);
                        panelDeDibujo.setOnMousePressed(mouseHandler);
                        panelDeDibujo.setOnMouseReleased(mouseHandler);
                        panelDeDibujo.getChildren().add(path);

                    });
                }
            }.start();
        }
    }

    @FXML
    public void limpiarAreaDePintar(){
        if(this.path!=null){
            this.path.getElements().clear();
            this.servicios.actualizarAreaDeDibujo(3,0.0,0.0);
        }
    }

    /***********************************************************/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.conversacion = new ConversationView("Bienvenido " + EscenaPrincipal.usuario);
        this.conversacion.setPrefSize(450, 225);
        this.panelChat.getChildren().add(conversacion);
        this.Indicador.setProgress(-1);
        this.Indicador.setVisible(false);
        this.listaDeDiapositivas = new HashMap<>();
        this.diapositivas = new HashMap<>();
        this.usuarios=new ArrayList<>();
        this.columnaNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();
        webEngine.load("http://192.168.0.18:8080");
        panelVideo.getChildren().addAll(browser);

    }

    EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
                double x = mouseEvent.getX();
                double y = mouseEvent.getY();
                if(x>5 && x <550 && y>5 && y<310){
                    path.getElements().add(new MoveTo(mouseEvent.getX(), mouseEvent.getY()));
                    servicios.actualizarAreaDeDibujo(1,x,y);
                }
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                double x = mouseEvent.getX();
                double y = mouseEvent.getY();
                if(x>5 && x <550 && y>5 && y<310) {
                    path.getElements().add(new LineTo(mouseEvent.getX(), mouseEvent.getY()));
                    servicios.actualizarAreaDeDibujo(2,x,y);
                }
            }
        }
    };

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
        //TODO No necesario
    }

    @Override
    public void diapositivaActual(int numero) throws RemoteException {
        //TODO NO necesario
    }

    @Override
    public void actualizarAreaDeDibujo(int operacion, double x, double y) throws RemoteException {
        //TODO No necesario
    }
}
