import java.io.*;
import java.net.Socket;

/**
 * Created by root on 24/10/15.
 */
public class TCPConnection extends Thread{

    private static final int MAX_SIZE = 32;

    private Socket download;
    private int port;
    private String path;

    private OutputStream ostream;
    private ObjectOutputStream oostream;

    private InputStream istream;
    private ObjectInputStream oistream;

    private long downloaded;
    private String filename;

    public TCPConnection(Socket download, int port, String path){
        this.download = download;
        this.port = port;
        this.path = path;
        downloaded=0;
    }


    public void setStop(){
        try{
            oostream.close();
            oistream.close();
            download.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void run(){

        try {
            ostream = download.getOutputStream();
            oostream = new ObjectOutputStream(ostream);

            istream = download.getInputStream();
            oistream = new ObjectInputStream(istream);

            //System.out.println("Comunicacion establecida con cliente");


            //receive current downloaded size
            downloaded = ((Long)oistream.readObject()).longValue();
            //System.out.println("Recibiendo ultima posicion de cliente...");

            //receive nameFile
            filename = (String) oistream.readObject();
            path = path + "/"+filename;
            //System.out.println("Recibiendo nombre de archivo...");

            //calculate file size
            DataInputStream in = new DataInputStream(new FileInputStream(path));
            File f = new File(path);
            long size = f.length();

            //send size of file
            oostream.reset();
            oostream.writeObject(new Long(size));
            oostream.flush();
            //System.out.println("Enviando tamano de archivo...");

            //go to the last position
            in.skipBytes((int)downloaded);

            byte[] buffer = new byte[MAX_SIZE];
            //System.out.println("Iniciando envio de archivo..."+size);
            try{
                int len = 0;
                while((len=in.read(buffer))!=-1){
                    //send file
                    ostream.write(buffer,0,len);
                    //ostream.flush();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            if(oistream!=null) {
                try {
                    oistream.close();
                }catch (Exception e) { e.printStackTrace();}
            }
            if(istream!=null) {
                try {
                    istream.close();
                }catch (Exception e) { e.printStackTrace();}
            }
            if(oostream!=null) {
                try {
                    oostream.close();
                }catch (Exception e) { e.printStackTrace();}
            }
            if(ostream!=null) {
                try {
                    ostream.close();
                }catch (Exception e) { e.printStackTrace();}
            }
            try {
                download.close();
            }catch (Exception ex) {ex.printStackTrace();}
        }


    }

}
