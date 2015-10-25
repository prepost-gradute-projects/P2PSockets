import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by root on 24/10/15.
 */
public class ClientP2P extends Thread{

    private boolean running;
    private String SERVERIP;
    public static final int SERVERPORT=4444;

    private OutputStream ostream;
    private ObjectOutputStream oostream;
    private InputStream istream;
    private ObjectInputStream oistream;

    InetAddress serverAddr;
    Socket socket;

    public ClientP2P(String ip){
        SERVERIP = ip;
        running=false;

        try{

            serverAddr = InetAddress.getByName(SERVERIP);
            socket = new Socket(serverAddr,SERVERPORT);

            try{
                ostream = socket.getOutputStream();
                oostream = new ObjectOutputStream(ostream);

                istream = socket.getInputStream();
                oistream = new ObjectInputStream(istream);

            }catch (Exception ex){
                ex.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void run(){

        running = true;

        try {
            try {

                while (running) {
                    FilesTable ft = (FilesTable) oistream.readObject();
                    ft.show();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                socket.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void sendTable(PeerFileTable pt){
        try {
            oostream.reset();
            oostream.writeObject(pt);
            oostream.flush();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public void setStop(){
        sendTable(GUITable.pft);
        try {
            while (running) {
                running = false;
                Thread.sleep(1000);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
