import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by root on 24/10/15.
 */
public class ServerP2p {

    private ArrayList<TCPConnection> clients;
    private boolean running;

    public static final int SERVERPORT = 4444;
    public static FilesTable filesTable;

    public ServerP2p(){

        clients = new ArrayList<TCPConnection>();
        running= false;
        filesTable = new FilesTable();
    }

    public void sendTableClients(){
        for(TCPConnection client : clients){
            client.sendTable(filesTable);
        }
    }

    public void run(){
        running=true;

        try{
            ServerSocket serverSocket = new ServerSocket(SERVERPORT);

            while(running){
                Socket client = serverSocket.accept();
                TCPConnection connection = new TCPConnection(client,this);

                clients.add(connection);
                System.out.println("Peer conectado ...");
                connection.start();

            }

        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

}
