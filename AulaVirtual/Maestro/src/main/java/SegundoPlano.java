import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class SegundoPlano implements Runnable {
    private Servicios servicios;

    public SegundoPlano(Servicios servicios) {
        this.servicios = servicios;
    }

    public void run() {
        try {
            Runtime.getRuntime().exec("rmiregistry");
            String url = "rmi://localhost:7890/AulaVirtual";
            LocateRegistry.createRegistry(7890);
            Naming.rebind(url, servicios);
            System.out.println("Servidor corriendo en: " + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
