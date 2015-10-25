import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by root on 24/10/15.
 */
public class TCPConnection extends Thread{

    private Socket socket;
    private boolean running;

    private OutputStream ostream;
    private ObjectOutputStream oostream;

    private InputStream istream;
    private ObjectInputStream oistream;
    private  ServerP2p p2p;


    public TCPConnection(Socket socket, ServerP2p p2p){

        this.socket = socket;
        running=false;
        this.p2p = p2p;
    }

    public void sendTable(FilesTable ft){
        try {
            if(oostream!=null) {
                oostream.reset();
                oostream.writeObject(ft);
                oostream.flush();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void run(){

        running = true;

        try{

            ostream = socket.getOutputStream();
            oostream = new ObjectOutputStream(ostream);

            istream = socket.getInputStream();
            oistream = new ObjectInputStream(istream);

            // receive table with files
            PeerFileTable tmp = (PeerFileTable) oistream.readObject();
            p2p.filesTable.add(tmp);
            p2p.sendTableClients();
            System.out.println("Peer envia lista de archivos");

            // next call is to warning closing connection
            tmp = (PeerFileTable) oistream.readObject();
            //System.out.println(tmp.getId());
            p2p.filesTable.remove(tmp.getId());
            oostream.close();
            oistream.close();
            oostream = null;
            p2p.sendTableClients();
            System.out.println("Peer desconectado");

        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            try{
                socket.close();
            }catch (Exception e){ e.printStackTrace();}
        }

    }

}
