import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * Hilo para escribir mensajes
 * 
 * @author Ismael Martin
 *
 */
public class HiloEscribirMensaje implements Runnable {
	DataOutputStream flujoSalida;
	Scanner teclado;
	String mensaje;
	
	public HiloEscribirMensaje(DataOutputStream flujoSalida) {
		this.flujoSalida = flujoSalida;
		teclado = new Scanner(System.in);
	}
	@Override
	public void run() {
		while (true) {
			// Enviar mensaje 
			mensaje = Utilidades.pedirTexto("-> ", teclado);
			try {
				flujoSalida.writeUTF(mensaje);
			} catch (IOException e) {
				// System.out.println("\n***EL HILO DE ESCRITURA falló al ENVIAR UnSTRING\n");
				// e.printStackTrace();
			}
			System.out.println("\t+[Escritor]: " + mensaje);
			
		
		
		}
	}

}
