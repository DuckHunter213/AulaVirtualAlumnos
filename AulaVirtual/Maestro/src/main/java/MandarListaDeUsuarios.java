import Cliente.Cliente;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public class MandarListaDeUsuarios implements Runnable{
    private final Map<String, Cliente> clientes;
    private ArrayList<String> usuarios;

    public MandarListaDeUsuarios(Map<String, Cliente> clientes) {
        this.clientes = clientes;
        this.usuarios = new ArrayList<>();
    }

    @Override
    public void run() {
        this.clientes.forEach((k,v)->this.usuarios.add(k));
        this.clientes.forEach((k, v) -> {
            try {
                v.listaDeConectados(this.usuarios);
            } catch (RemoteException e) {
                System.out.println("Error al enviar lista de usuarios a "+k);
            }
        });
    }
}
