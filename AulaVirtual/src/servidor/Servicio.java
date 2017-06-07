/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author victor
 */
public class Servicio implements Runnable {

    private BufferedReader in;
    Socket cliente;

    public Servicio(Socket cliente) {
        this.cliente = cliente;
        try {
            this.in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(Servicio.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        try {
            String nick = String.valueOf(MarcoPrincipalController.hm.size());
            MarcoPrincipalController.hm.put(nick, new PrintWriter(this.cliente.getOutputStream(), true));
            while (true) {
                String mensaje = this.in.readLine();
                System.out.println(mensaje);
                MarcoPrincipalController.broadcast(nick, mensaje);
            }
        } catch (Exception e) {

        } finally {
            try {
                cliente.close();
            } catch (IOException ex) {
                Logger.getLogger(Servicio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    

}
