import Cliente.Cliente;
import java.util.Map;

public class MandarDiapositivas implements Runnable {
    private final Map<Integer, byte[]> diapositivas;
    private final Cliente cliente;

    public MandarDiapositivas(Map<Integer, byte[]> diapositivas, Cliente cliente) {
        this.diapositivas = diapositivas;
        this.cliente = cliente;
    }

    @Override
    public void run() {
        try{
            Thread.sleep(1000);
            this.cliente.diapositivas(diapositivas.size(),null);
            this.diapositivas.forEach((k, v) -> {
                try {
                    cliente.diapositivas(k, v);
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            //TODO corregir numero actual de maestro
            this.cliente.diapositivaActual(EscenaPrincipal.numeroDeDiapositiva);
        }catch (Exception e){
            System.out.println("Error al mandar diapositivas");
        }
    }
}
