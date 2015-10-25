import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by root on 24/10/15.
 */
public class ServerP2P extends Thread{

    private ArrayList<TCPConnection> clients;
    private boolean running;

    private int SERVERPORT;
    private String path = "/home/yarvis";

    public ServerP2P(String port){

        clients = new ArrayList<TCPConnection>();
        running = true;
        SERVERPORT = (new Integer(port)).intValue();
        path  += "/"+SERVERPORT;
    }

    public void run(){

        ServerSocket serverSocket=null;
        try{
            serverSocket  = new ServerSocket(SERVERPORT);
            System.out.println("Servidor iniciado");

            while(running){
                Socket client = serverSocket.accept();
                System.out.println("Peer conectado ...");

                TCPConnection download = new TCPConnection(client,SERVERPORT, path);

                clients.add(download);
                //System.out.println("Peer descargando conectado ...");
                download.start();

            }
            serverSocket.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                if (serverSocket != null) serverSocket.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public void setStop(){

        for(TCPConnection client : clients){
            client.setStop();
        }
    }

    /*public void reRun(){
        for(TCPConnection client:clients){
            client.start();
        }
    }*/

}
