/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import com.sun.imageio.plugins.jpeg.JPEG;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author victor
 */
public class Cliente extends Application {
    
    private static ArrayList<PrintWriter> flujosSalida;
    public static Map<String, PrintWriter> hm;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("MarcoPrincipal.fxml"));
        Parent pantallaPrincipal = (Parent) myLoader.load();
        stage.setTitle("Ventana Alumno");
        Scene scene = new Scene(pantallaPrincipal);

        stage.setScene(scene);
        stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

}
