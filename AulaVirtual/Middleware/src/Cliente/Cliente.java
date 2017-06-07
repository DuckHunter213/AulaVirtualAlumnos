package Cliente;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Cliente extends Remote{
    public void recibirMensaje(String usuario, String mensaje) throws RemoteException;
    public void listaDeConectados(ArrayList<String> usuarios) throws RemoteException;
    public void diapositivas(int numero, byte[] diapositiva) throws RemoteException;
    public void diapositivaActual(int numero) throws RemoteException;
    public void actualizarAreaDeDibujo(int operacion, double x, double y) throws RemoteException;
}
