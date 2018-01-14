import java.io.DataInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * Hilo para leer mensajes
 * 
 * @author Ismael MArtin
 *
 * !!CLASE NO UTLILIZADA!!
 */
public class HiloLeerMensaje implements Runnable {
	DataInputStream flujoEntrada;
	Scanner teclado;
	String mensaje;

	public HiloLeerMensaje(DataInputStream flujoEntrada) {
		this.flujoEntrada = flujoEntrada;
		teclado = new Scanner(System.in);
		}
	@Override
	public void run() {
			try {
				mensaje = flujoEntrada.readUTF();
			} catch (IOException e) {		
			}			
			System.out.println("\t-[Lector]: " + mensaje);
			mensaje = "";	
		
}

	public DataInputStream getFlujoEntrada() {
		return flujoEntrada;
	}

	public void setFlujoEntrada(DataInputStream flujoEntrada) {
		this.flujoEntrada = flujoEntrada;
	}

	public Scanner getTeclado() {
		return teclado;
	}

	public void setTeclado(Scanner teclado) {
		this.teclado = teclado;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
}// Fin de la clase