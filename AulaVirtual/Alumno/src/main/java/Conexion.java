import Cliente.Cliente;
import Servidor.Servidor;

import java.rmi.Naming;

/**
 * Creado por Mauricio el 27/05/2017.
 **/

public class Conexion {
    private  Servidor servidor;

    public Conexion() {
        try {
            this.servidor = (Servidor) Naming.lookup("rmi://localhost:7890/AulaVirtual");
        }catch (Exception e){
            System.out.println("Error al crear conexión");
        }
    }

    public void conectar(Cliente cliente){
        try {
            this.servidor.conectar(EscenaPrincipal.usuario, cliente);
        }catch (Exception e){
            System.out.println("Error al conectarse");
        }
    }

    public void mandarMensaje(String mensaje){
        try {
            this.servidor.mandarMensaje(EscenaPrincipal.usuario, mensaje);
        }catch (Exception e){
            System.out.println("Error al mandar Mensaje");
        }
    }

    public void mandarParticipacion(String mensaje){
        try {
            this.servidor.mandarMensaje("Participación de "+ EscenaPrincipal.usuario, mensaje);
        }catch (Exception e){
            System.out.println("Error al mandar Mensaje");
        }
    }


    public void desconectarse(){
        try {
          this.servidor.desconectar(EscenaPrincipal.usuario);
        }catch (Exception e){
            System.out.println("Error al desconectarse");
        }
    }

}
