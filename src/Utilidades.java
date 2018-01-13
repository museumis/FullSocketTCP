import java.util.Scanner;

/**
 * Clase para metodos basicos
 * @author IsmA
 *
 */
public class Utilidades {

	/**
	 * Procedimiento que duerme el programa
	 */
	public static void hacerSleep(int tiempo) {
		try {
			Thread.sleep(tiempo);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}//Fin de hacerSleep
	
	/**
	 * Pedir texto por pantalla
	 * @param pregunta
	 * @param entrada scanner
	 * @return respuesta
	 */
	public static String pedirTexto(String pregunta, Scanner teclado) {
		System.out.print(pregunta);
		return teclado.nextLine();
	}
}
