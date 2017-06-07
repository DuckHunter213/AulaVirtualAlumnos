import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class EscenaPrincipal extends Application {

    public static String usuario = "OZZ";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nombre de Usuario");
        dialog.setHeaderText("Si no ingresa el nombre se creará un nombre random");
        dialog.setContentText("Nombre de usuario: ");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().equals("")){
            EscenaPrincipal.usuario = result.get();
        }else {
            EscenaPrincipal.usuario ="Usuario-" + ThreadLocalRandom.current().nextInt(0,1_000);
        }


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
