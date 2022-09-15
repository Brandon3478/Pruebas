import java.io.IOException;
import java.io.BufferedReader; 
import java.io.InputStreamReader; 
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.DatagramSocket;

public class ChatMulticast {
    
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
                    System.out.println(new String(buffer,"CP850"));
    
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
        BufferedReader b = new BufferedReader(new InputStreamReader(System.in, "CP850"));        
        // En un ciclo infinito se leerá cada mensaje del teclado y se enviará el mensaje al
        // grupo 230.0.0.0 a través del puerto 50000.
        while(true) {
            String msg = b.readLine();
	    System.out.println("Ingrese el mensaje a enviar: ");
            String salida = nombre + " dice " + msg;
            envia_mensaje_multicast(salida.getBytes(),"230.0.0.0",50000); 

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
