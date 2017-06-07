package Servidor;

import Cliente.Cliente;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Servidor extends Remote{
    public void conectar(String usuario, Cliente cliente)throws RemoteException;

    public void mandarMensaje(String usuario, String mensaje) throws RemoteException;

    public void desconectar(String usuario)throws RemoteException;
}
