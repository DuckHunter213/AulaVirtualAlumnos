import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;


public class EscenaPrincipal extends Application {
    public static int numeroDeDiapositiva = 1;
    public static String usuario = "Maestro";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Escena.fxml"));
        primaryStage.setTitle("Aula Virtual");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("Imagenes/logo.png"))));
        primaryStage.setOnCloseRequest(Event ->{
            new Conexion().desconectarse();
            System.exit(0);
        });
        Scene scene = new Scene(root, 900-10, 700-10);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
