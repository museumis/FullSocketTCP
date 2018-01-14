import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Scanner;

/**
 * Clase que actua como servidor
 * 
 * @author Ismael Martin
 *
 * <pre>
 *El puerto 80  HTTP (Para transferencia de Páginas web).
 *El puerto 20  Datos para FTP (Para transferencia de archivos).
 *El puerto 21  Control para FTP (Para transferencia de archivos).
 *El puerto 22  SSH (Comunicación segura).
 *El puerto 23  Telnet (Comunicación no segura).
 * </pre>
 *
 */
public class Servidor {
	// Puerto por el que escuchar
	private static int PUERTO = 8000;
	// Socket
	private static ServerSocket servidor;
	private static Socket cliente;
	// Flujos
	private static DataInputStream flujoEntrada;
	private static DataOutputStream flujoSalida;
	// Variable
	private static String mensaje;
	private static int NUMEROCLIENTES = 1;

	/*
	 * Clase que inicia el servidor
	 */
	public static void main(String[] args) {
		// System.out.println("Host del servidor -> "+obtenerIpv4());
		serverMultiCliente();
	}

	/**
	 * Plantilla basica de server con varios clientes que aceptar
	 * 
	 * @param numeroClientes que serán aceptados en el servidor
	 */
	public static void serverMultiCliente() {
		activarServidor();
		for (int i = 0; i < NUMEROCLIENTES; i++) {
			// Preparativos
			conectarCliente();
			crearFlujosDatosPrimitivos();
			// Comunicacion
			// variosMensajeConRespuesta02(flujoEntrada, flujoSalida);
			// mensajeConRespuestaServerContinua03(flujoEntrada, flujoSalida);
			// mensajesClienteServidorPorTurnos04(flujoEntrada, flujoSalida);
			mensajesClienteServidor05(flujoEntrada, flujoSalida);

			// Cerrar cliente y flujo
			cerrarTodoMenosServidor();
		}
		cerrarServer();
	}// Fin de server secuencial

	/**
	 * Plantilla basica
	 */
	public static void serverConUnCliente() {
		// Preparativos
		activarServidor();
		conectarCliente();
		crearFlujosDatosPrimitivos();
		// Comunicacion
		// recibirDatosPrimitivos(flujoEntrada, flujoSalida);
		// mensajeConRespuesta01(flujoEntrada, flujoSalida);
		// variosMensajeConRespuesta02(flujoEntrada, flujoSalida);
		// Cerrar
		cerrarTodo();
	}// Fin de plantilla basica

	// ----------------------------------------------------------------------
	// PLANTILLA de COMUNICACION entre SERVIDOR-CLIENTE
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
	// Metodos de COMUNICACION entre SERVIDOR-CLIENTE
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
			

	}// Fin de mensajes ClienteServidor

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
			// Recibir mensaje del cliente
			recibirDatosPrimitivos(flujoEntrada, flujoSalida);
			// Enviar mensaje al cliente
			mensajeQueEnviar = Utilidades.pedirTexto("-> ", teclado);
			enviarDatosPrimitivos(flujoEntrada, flujoSalida, mensajeQueEnviar);

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
		boolean cerrarServidor = false;
		while (!cerrarServidor) {
			mensajeQueEnviar = "";
			// Obtener mensaje del cliente
			mensaje = "";
			try {
				mensaje = flujoEntrada.readUTF();
			} catch (IOException e) {
				System.out.println("\n***El SERVIDOR falló al RECIBIR UnSTRING\n");
				e.printStackTrace();
			}
			// Control de final del programa
			if (mensaje.equalsIgnoreCase("fin")) {
				cerrarServidor = true;
			} else {
				// Codificacion del mensaje
				System.out.print("~[DiosSever]Recibe: " + mensaje);
				for (int i = mensaje.length() - 1; i >= 0; i--) {
					mensajeQueEnviar += String.valueOf(mensaje.charAt(i));

				} // Fin de codificar mensaje
				System.out.println(" / Envía: " + mensajeQueEnviar + " ");
				// Enviar al cliente
				enviarDatosPrimitivos(flujoEntrada, flujoSalida, mensajeQueEnviar);
			}
		} // Fin del bucle
	}// Fin de Comunicacion continua con el cliente

	/**
	 * Comunicacion en la que el cliente y el servidor se corresponden mensajes con
	 * espera responde
	 * 
	 * @param flujoEntrada
	 * @param flujoSalida
	 */
	public static void variosMensajeConRespuesta02(DataInputStream flujoEntrada, DataOutputStream flujoSalida) {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		recibirDatosPrimitivos(flujoEntrada, flujoSalida);
		Utilidades.hacerSleep(5000);
		enviarDatosPrimitivos(flujoEntrada, flujoSalida, "Yo 2");
		recibirDatosPrimitivos(flujoEntrada, flujoSalida);
		Utilidades.hacerSleep(5000);
		enviarDatosPrimitivos(flujoEntrada, flujoSalida, "Soy Dios Server, se acabó.");
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
		recibirDatosPrimitivos(flujoEntrada, flujoSalida);
		enviarDatosPrimitivos(flujoEntrada, flujoSalida, "Hola cliente, soy tu servidor.");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

	}// enviar y recibir sms del servidor
		// ----------------------------------------------------------------------

	/**
	 * Metodo para enviar mensajes al cliente: SERVIDOR->envía sms->CLIENTE
	 * 
	 * @param flujoEntrada
	 * @param flujoSalida
	 * @param mensajeQueEnviar mensaje que se desea enviar el cliente
	 */
	public static void enviarDatosPrimitivos(DataInputStream flujoEntrada, DataOutputStream flujoSalida,
			String mensajeQueEnviar) {
		mensaje = "";
		mensaje = mensajeQueEnviar;
		try {
			flujoSalida.writeUTF(mensaje);
		} catch (IOException e) {
			System.out.println("\n***El SERVIDOR falló al ENVIAR UnSTRING\n");
			e.printStackTrace();
		}
		System.out.println("\t+[DiosServer]: " + mensaje);
	}// Fin de enviar al cliente

	/**
	 * Metodo para recibir mensajes del cliente: CLIENTE->envia sms ->SERVIDOR
	 * 
	 * @param flujoEntrada
	 * @param flujoSalida
	 */
	public static void recibirDatosPrimitivos(DataInputStream flujoEntrada, DataOutputStream flujoSalida) {
		mensaje = "";
		try {
			mensaje = flujoEntrada.readUTF();
		} catch (IOException e) {
			System.out.println("\n***El SERVIDOR falló al RECIBIR UnSTRING\n");
			e.printStackTrace();
		}
		System.out.println("\t-[Cliente]: " + mensaje);
	}// Fin de recibir del cliente

	/**
	 * Metodo para recibir String con return del cliente: CLIENTE->envia sms
	 * ->SERVIDOR
	 * 
	 * @param flujoEntrada
	 * @param flujoSalida
	 * @return mensaje mensaje recibido de parte del cliente
	 * 
	 */
	public static String recibirStringDatosPrimitivosReturn(DataInputStream flujoEntrada,
			DataOutputStream flujoSalida) {
		mensaje = "";
		try {
			mensaje = flujoEntrada.readUTF();
		} catch (IOException e) {
			System.out.println("\n***El SERVIDOR falló al RECIBIR UnSTRING\n");
			e.printStackTrace();
		}
		System.out.println("\t-[Cliente]: " + mensaje);
		return mensaje;
	}// Fin de recibirString del cliente
		// ----------------------------------------------------------------------
		// Metodos iniciar el servidor, cerrarle y preparar la comunicacion
		// ----------------------------------------------------------------------

	/*
	 * Clase que conecta el servidor y procesa los clientes
	 */
	public static void activarServidor() {
		// Activar Sevidor
		servidor = null;
		try {
			servidor = new ServerSocket(PUERTO);
		} catch (IOException e) {
			System.out.println("\n***El SERVIDOR falló al conectarse\n");
			e.printStackTrace();
		}
		System.out.println("~~~ S E R V I D O R ~~~");
	}// Fin de activar servidor

	/*
	 * Metodo que conecta un cliente al servidor.
	 */
	public static void conectarCliente() {
		// Conectar el Cliente
		cliente = null;
		try {
			cliente = servidor.accept();
		} catch (IOException e) {
			System.out.println("\n***El CLIENTE falló al conectarse al servidor.\n");
			e.printStackTrace();
		}
		System.out.println("~~Un Cliente conectado~~");
	}// Fin de conectar un cliente

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
	 * Cierra todos los flujos abierto
	 */
	public static void cerrarTodo() {
		if (flujoSalida != null) {
			try {
				flujoSalida.close();
			} catch (IOException e) {
				System.out.println("\n***Fallo al CERRAR FLUJO SALIDA\n");
				e.printStackTrace();
			}
		}
		if (flujoEntrada != null) {
			try {
				flujoEntrada.close();
			} catch (IOException e) {
				System.out.println("\n***Fallo al CERRAR FLUJO ENTRADA\n");
				e.printStackTrace();
			}
		}
		if (cliente != null) {
			try {
				cliente.close();
			} catch (IOException e) {
				System.out.println("\n***Fallo al CERRAR el CLIENTE en el servidor\n");
				e.printStackTrace();
			}
		}
		if (servidor != null) {
			try {
				servidor.close();
			} catch (IOException e) {
				System.out.println("\n***Fallo al CERRAR el SERVIDOR\n");
				e.printStackTrace();
			}
		}
	}// Fin de cerrar todo

	/**
	 * Cierra todos los flujos abierto menos el servidor
	 */
	public static void cerrarTodoMenosServidor() {
		if (flujoSalida != null) {
			try {
				flujoSalida.close();
			} catch (IOException e) {
				System.out.println("\n***Fallo al CERRAR FLUJO SALIDA\n");
				e.printStackTrace();
			}
		}
		if (flujoEntrada != null) {
			try {
				flujoEntrada.close();
			} catch (IOException e) {
				System.out.println("\n***Fallo al CERRAR FLUJO ENTRADA\n");
				e.printStackTrace();
			}
		}
		if (cliente != null) {
			try {
				cliente.close();
			} catch (IOException e) {
				System.out.println("\n***Fallo al CERRAR el CLIENTE en el servidor\n");
				e.printStackTrace();
			}
		}
	}// Fin de cerrar menos Server

	/**
	 * Cierra el servidor
	 */
	public static void cerrarServer() {

		if (servidor != null) {
			try {
				servidor.close();
			} catch (IOException e) {
				System.out.println("\n***Fallo al CERRAR el SERVIDOR\n");
				e.printStackTrace();
			}
		}
	}// Fin de cerrar servidor

	public static String obtenerIpv4() {
		String ip;
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface iface = interfaces.nextElement();
				// filters out 127.0.0.1 and inactive interfaces
				if (iface.isLoopback() || !iface.isUp())
					continue;

				Enumeration<InetAddress> addresses = iface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress addr = addresses.nextElement();

					// *EDIT*
					if (addr instanceof Inet6Address)
						continue;

					ip = addr.getHostAddress();
					// System.out.println(iface.getDisplayName() + " " + ip);
					return ip;
				}

			}

		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

}
