/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextArea;

/**
 *
 * @author victor
 */
public class RefrescadorMensajes implements Runnable {

    private BufferedReader in;
    private TextArea areaMensajes;

    public RefrescadorMensajes(BufferedReader in, TextArea areaMensajes) {
        this.in = in;
        this.areaMensajes = areaMensajes;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String mensaje = in.readLine();
                areaMensajes.appendText(mensaje + "\n");
            } catch (IOException ex) {
                Logger.getLogger(RefrescadorMensajes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
