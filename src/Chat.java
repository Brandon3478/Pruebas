
/**
 * 
 * EQUIPO 3 - GRUPO 4CV11
 * 
 * Contreras Barrita Jose Roberto
 * Contreras Mendez Brandon
 * Covarrubias Sanchez Daniel
 * 
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.DatagramSocket;
import java.util.Scanner;

public class Chat {
    static class Worker extends Thread{
    
        public void run() {
            // En un ciclo infinito se recibirán los mensajes enviados al 
            // grupo 230.0.0.0 a través del puerto 50000 y se desplegarán en la pantalla.
            for (;;) {
                try {
                    InetAddress grupo = InetAddress.getByName("230.0.0.0");
                    MulticastSocket socket = new MulticastSocket(50000);
                    socket.joinGroup(grupo);
                    
                    byte[] buffer = recibe_mensaje_multicast(socket, 100);
                    System.out.println(new String(buffer,"Windows-1252").trim());
    
                    socket.leaveGroup(grupo);
                    socket.close();
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) throws Exception{
    
        Worker w = new Worker();
        w.start();
        String nombre = args[0];
        Scanner scanner = new Scanner(System.in, "Windows-1252");        
        // En un ciclo infinito se leerá cada mensaje del teclado y se enviará el mensaje al
        // grupo 230.0.0.0 a través del puerto 50000.
        while(true) {
            System.out.println("Ingrese el mensaje a enviar: ");
            String mensaje = scanner.nextLine();
            String salida = nombre + " dice " + mensaje;
            envia_mensaje_multicast(salida.getBytes("Windows-1252"),"230.0.0.0",50000); 

        }
    }
    
    static void envia_mensaje_multicast(byte[] buffer, String ip, int puerto) throws IOException 
    {
        DatagramSocket socket = new DatagramSocket();
        socket.send(new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ip), puerto));
        socket.close();
    }
    
    static byte[] recibe_mensaje_multicast(MulticastSocket socket, int longitud_mensaje) throws IOException 
    {
        byte[] buffer = new byte[longitud_mensaje];
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
        socket.receive(paquete);
        return paquete.getData();
    }
}
