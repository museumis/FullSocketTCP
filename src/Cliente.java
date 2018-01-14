import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Clase que actua como cliente
 * 
 * @author Ismael Martin
 *
 */
public class Cliente {
	// Puerto por el que escuchar
	private static int PUERTO = 8000;
	private static String HOST = "localhost";// Consola//ipconfig//Wi-fi//IPv4

	// Socket
	private static Socket cliente;
	// Flujos
	private static DataInputStream flujoEntrada;
	private static DataOutputStream flujoSalida;
	// Variable
	private static String mensaje;

	/**
	 * Clase que inicia el cliente
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Preparativos
		activarCliente();
		crearFlujosDatosPrimitivos();
		// Comunicacion
		// enviarDatosPrimitivos(flujoEntrada, flujoSalida, "Hola server");
		// recibirDatosPrimitivos(flujoEntrada, flujoSalida);
		// mensajeConRespuesta01(flujoEntrada, flujoSalida);
		// variosMensajeConRespuesta02(flujoEntrada, flujoSalida);
		// mensajeConRespuestaServerContinua03(flujoEntrada, flujoSalida);
		// mensajesClienteServidorPorTurnos04(flujoEntrada, flujoSalida);
		mensajesClienteServidor05(flujoEntrada, flujoSalida);

		// Cerrar
		cerrarTodo();

	}

	// ----------------------------------------------------------------------
	// PLANTILLA de COMUNICACION entre CLIENTE-SERVIDOR
	// ----------------------------------------------------------------------

	/**
	 * Plantilla de comunicacion con datos primitivos
	 * 
	 * @param flujoEntrada
	 * @param flujoSalida
	 */
	public static void comunicacion(DataInputStream flujoEntrada, DataOutputStream flujoSalida) {
	}// Fin de Comunicacion XXXXX

	// ----------------------------------------------------------------------
	// Metodos de COMUNICACION entre CLIENTE-SERVIDOR
	// ----------------------------------------------------------------------
	/**
	 * Metodo que comunica el servidor y el cliente mediante mensajes
	 * 
	 * @param flujoEntrada
	 * @param flujoSalida
	 */
	public static void mensajesClienteServidor05(DataInputStream flujoEntrada, DataOutputStream flujoSalida) {
		HiloEscribirMensaje escribir = new HiloEscribirMensaje(flujoSalida);
		new Thread(escribir).start();
		while(true) {
			recibirDatosPrimitivos(flujoEntrada, flujoSalida);
		}
		
		

	}// Fin de mensajes Cliente Servidor

	/**
	 * Metodo que comunica el servidor y el cliente en mensajes por turno
	 * 
	 * @param flujoEntrada
	 * @param flujoSalida
	 */

	public static void mensajesClienteServidorPorTurnos04(DataInputStream flujoEntrada, DataOutputStream flujoSalida) {
		String mensajeQueEnviar = "";
		Scanner teclado = new Scanner(System.in);
		while (true) {
			// Enviar sms al servidor
			mensajeQueEnviar = Utilidades.pedirTexto("-> ", teclado);
			enviarDatosPrimitivos(flujoEntrada, flujoSalida, mensajeQueEnviar);
			// Recibir sms al servidor
			recibirDatosPrimitivos(flujoEntrada, flujoSalida);

		}
	}// Fin de Comunicacion mensajesClienteServidorPorTurnos

	/**
	 * Plantilla de comunicacion con datos primitivos
	 * 
	 * @param flujoEntrada
	 * @param flujoSalida
	 */
	public static void mensajeConRespuestaServerContinua03(DataInputStream flujoEntrada, DataOutputStream flujoSalida) {
		String mensajeQueEnviar = "";
		Scanner teclado = new Scanner(System.in);
		boolean salir = false;
		System.out.println("¡Transforma un cadena!El server se encarga.");
		while (!salir) {
			// Enviar mensaje
			mensajeQueEnviar = Utilidades.pedirTexto("~Palabra: ", teclado);
			enviarDatosPrimitivos(flujoEntrada, flujoSalida, mensajeQueEnviar);
			// Control de final del programa
			if (mensaje.equalsIgnoreCase("fin")) {
				salir = true;
			} else {
				// Obtener mensaje del Servidor
				mensaje = "";
				try {
					mensaje = flujoEntrada.readUTF();
				} catch (IOException e) {
					System.out.println("\n***El CLIENTE falló al RECIBIR UnSTRING\n");
					e.printStackTrace();
				}
				System.out.println("\t+[DiosServer]: " + mensaje);
			}

		} // Fin del bucle
	}// Fin de Comunicacion continua con el servidor

	/**
	 * Comunicacion en la que el cliente y el servidor se corresponden mensajes con
	 * espera responde
	 * 
	 * @param flujoEntrada
	 * @param flujoSalida
	 */
	public static void variosMensajeConRespuesta02(DataInputStream flujoEntrada, DataOutputStream flujoSalida) {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		enviarDatosPrimitivos(flujoEntrada, flujoSalida, "Digo 1");
		recibirDatosPrimitivos(flujoEntrada, flujoSalida);
		Utilidades.hacerSleep(5000);
		enviarDatosPrimitivos(flujoEntrada, flujoSalida, "Pues yo 3");
		recibirDatosPrimitivos(flujoEntrada, flujoSalida);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

	}// enviar y recibir sms del servidor

	/**
	 * Comunicacion en la que el cliente envia un mensaje al servidor y este
	 * responde
	 * 
	 * @param flujoEntrada
	 * @param flujoSalida
	 */
	public static void mensajeConRespuesta01(DataInputStream flujoEntrada, DataOutputStream flujoSalida) {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		enviarDatosPrimitivos(flujoEntrada, flujoSalida, "Hola server, contestame.");
		recibirDatosPrimitivos(flujoEntrada, flujoSalida);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}// enviar y recibir sms del servidor

	/**
	 * Metodo para enviar mensajes al servidor: CLIENTE->envía sms->SERVIDOR
	 * 
	 * @param flujoEntrada
	 * @param flujoSalida
	 * @param mensajeQueEnviar mensaje que se desea enviar al servidor
	 */
	public static void enviarDatosPrimitivos(DataInputStream flujoEntrada, DataOutputStream flujoSalida,
			String mensajeQueEnviar) {
		mensaje = "";
		mensaje = mensajeQueEnviar;
		try {
			flujoSalida.writeUTF(mensaje);
		} catch (IOException e) {
			System.out.println("\n***El CLIENTE falló al ENVIAR UnSTRING\n");
			e.printStackTrace();
		}
		System.out.println("\t-[Cliente]: " + mensaje);
	}// Fin de enviar al servidor

	/**
	 * Metodo para recibir mensajes del servidor: SERVIDOR->envia sms ->CLIENTE
	 * 
	 * @param flujoEntrada
	 * @param flujoSalida
	 */
	public static void recibirDatosPrimitivos(DataInputStream flujoEntrada, DataOutputStream flujoSalida) {
		mensaje = "";
		try {
			mensaje = flujoEntrada.readUTF();
		} catch (IOException e) {
			System.out.println("\n***El CLIENTE falló al RECIBIR UnSTRING\n");
			e.printStackTrace();
		}
		System.out.println("\t+[DiosServer]: " + mensaje);
	}// Fin de recibir del servidor

	/**
	 * Metodo para recibir String con return del servidor: SERVIDOR->envia sms
	 * ->CLIENTE
	 * 
	 * @param flujoEntrada
	 * @param flujoSalida
	 * @return mensaje mensaje recibido de parte del servidor
	 * 
	 */
	public static String recibirStringDatosPrimitivosReturn(DataInputStream flujoEntrada,
			DataOutputStream flujoSalida) {
		mensaje = "";
		try {
			mensaje = flujoEntrada.readUTF();
		} catch (IOException e) {
			System.out.println("\n***El CLIENTE falló al RECIBIR UnSTRING\n");
			e.printStackTrace();
		}
		System.out.println("\t-[Servidor]: " + mensaje);
		return mensaje;
	}// Fin de recibir String del servidor

	// ----------------------------------------------------------------------
	// Metodos iniciar el cliente, cerrarle y preparar la comunicacion
	// ----------------------------------------------------------------------
	/**
	 * Metodo para activar el cliente
	 */
	public static void activarCliente() {
		cliente = null;
		try {
			cliente = new Socket(HOST, PUERTO);
		} catch (UnknownHostException e) {
			System.out.println("\n***No se encontró el HOST del SERVIDOR[El cliente tiene un host equivocado.]\n");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("\n***El CLIENTE falló al conectarse\n");
			e.printStackTrace();
		}
		System.out.println("~~~ C L I E N T E ~~~");
		// datosDelCliente();
	}// Fin de activar cliente

	/**
	 * Crea flujos de datos primitivos utilizados en la comunicacionF
	 */
	public static void crearFlujosDatosPrimitivos() {
		// ***Flujo de entrada
		flujoEntrada = null;
		try {
			flujoEntrada = new DataInputStream(cliente.getInputStream());
		} catch (IOException e) {
			System.out.println("\n***El FLUJO de ENTRADA falló al crearse\n");
			e.printStackTrace();
		}
		// **** Flujo Salida
		flujoSalida = null;
		try {
			flujoSalida = new DataOutputStream(cliente.getOutputStream());
		} catch (IOException e) {
			System.out.println("\n***El FLUJO de SALIDA falló al crearse\n");
			e.printStackTrace();
		}
	}// Fin de abrir flujos

	/**
	 * Metodo para mostrar la informacion del cliente
	 */
	public static void datosDelCliente() {
		InetAddress i = cliente.getInetAddress();
		System.out.println("Puerto local:  " + cliente.getLocalPort());
		System.out.println("Puerto remoto:  " + cliente.getPort());
		System.out.println("Nombre host remoto:  " + i.getHostName());
		System.out.println("IP host remoto:  " + i.getHostAddress());
		System.out.println("~~~                  ~~~");

	}// Informacion del cliente

	/**
	 * Cierra todos los flujos abierto
	 */
	public static void cerrarTodo() {
		if (flujoSalida != null) {
			try {
				flujoSalida.close();
			} catch (IOException e) {
				System.out.println("\n***Fallo al CERRAR FLUJO SALIDA del cliente\n");
				e.printStackTrace();
			}
		}
		if (flujoEntrada != null) {
			try {
				flujoEntrada.close();
			} catch (IOException e) {
				System.out.println("\n***Fallo al CERRAR FLUJO ENTRADA del cliente\n");
				e.printStackTrace();
			}
		}
		if (cliente != null) {
			try {
				cliente.close();
			} catch (IOException e) {
				System.out.println("\n***Fallo al CERRAR el CLIENTE en el cliente\n");
				e.printStackTrace();
			}
		}
	}// Fin de cerrar todo
}
