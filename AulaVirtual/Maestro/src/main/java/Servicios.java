import Cliente.Cliente;
import Servidor.Servidor;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class Servicios extends UnicastRemoteObject implements Servidor {
    private Map<String, Cliente> clientes;
    private Map<Integer, byte[]> diapositivas;

    public Servicios(Map<Integer, byte[]> diapositivas) throws RemoteException {
        this.clientes = new HashMap<>();
        this.diapositivas = diapositivas;
    }

    public void conectar(String usuario, Cliente cliente) throws RemoteException {
        this.clientes.put(usuario, cliente);
        new Thread(new MandarListaDeUsuarios(clientes)).start();
        new Thread(new MandarDiapositivas(this.diapositivas,cliente)).start();
    }

    public void mandarMensaje(final String usuario, final String mensaje) throws RemoteException {
        this.clientes.forEach((k, v) -> {
            try {
                v.recibirMensaje(usuario, mensaje);
            } catch (RemoteException e) {
                System.out.println("Error al enviar mensaje\nUsuario:"+usuario+"\nMensaje;"+mensaje);
            }
        });
    }

    public void desconectar(String usuario) throws RemoteException {
        this.clientes.remove(usuario);
        new Thread(new MandarListaDeUsuarios(clientes)).start();
    }

    public void actualizarNumeroDeDiapositiva(){
        this.clientes.forEach((k, v) -> {
            try {
                v.diapositivaActual(EscenaPrincipal.numeroDeDiapositiva);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void actualizarAreaDeDibujo(int operacion, double x, double y){
        this.clientes.forEach((k, v) -> {
            try {
                v.actualizarAreaDeDibujo(operacion, x, y);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }
}
