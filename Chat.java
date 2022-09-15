import java.net.MulticastSocket;

import java.net.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;  

public class Chat 
{
    static final String IP = "230.0.0.0";
    static final int puerto = 50000;
    static final int tam = 256;

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

    static class Worker extends Thread 
    {
        MulticastSocket socket;

        Worker() throws IOException 
        {
            InetAddress IP_grupo = InetAddress.getByName(IP);
            socket = new MulticastSocket(puerto);
            socket.joinGroup(IP_grupo);
        }

        public void run() 
        {
            try 
            {
                while (true) 
                {
                    byte[] buffer = recibe_mensaje_multicast(socket, tam);

                    System.out.println(new String(buffer, "Cp850").trim());
                }
            } catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException 
    {
        new Worker().start();
        String nombre = args[0];
        Scanner scanner = new Scanner(System.in,"Cp850");

        while (true) 
        {
            System.out.println("Ingrese el mensaje a enviar:");
            String mensaje = nombre + " dice " + scanner.nextLine();
            envia_mensaje_multicast(mensaje.getBytes("Cp850"), IP, puerto);
        }
    }
}