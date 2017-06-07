/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static javafx.application.Application.launch;

/**
 *
 * @author gerar
 */
public class Servidor extends Application {
    

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("MarcoPrincipal.fxml"));
        Parent pantallaPrincipal = (Parent) myLoader.load();
        stage.setTitle("Ventana Maestro");
        Scene scene = new Scene(pantallaPrincipal);
        stage.setScene(scene);
        stage.show();
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
