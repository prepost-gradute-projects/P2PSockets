import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Observable;

/**
 * Created by jeslev on 18/10/15.
 */
public class Download extends Observable implements Runnable {


    // Set max size of download buffer
    private static final int MAX_BUFFER_SIZE = 32;

    //States of downloads
    public static final String[] states = {"Descargando", "Pausado","Terminado","Cancelado","Error"};

    //Set int values for states
    public static final int DOWNLOADING = 0;
    public static final int PAUSED = 1;
    public static final int COMPLETE = 2;
    public static final int CANCELLED = 3;
    public static final int ERROR = 4;

    private String port, fileName;
    private long size;
    private long downloaded;
    private int status;

    //Variables for socket connection
    private String SERVERIP ="127.0.0.1";
    private String SERVERPORT;

    private OutputStream ostream;
    private ObjectOutputStream oostream;
    private InputStream istream;
    private ObjectInputStream oistream;

    private InetAddress serverAddr;
    private Socket socket;

    //variables for storing file
    private String path = "/home/yarvis";
    private String myport;

    public Download(String port, String fileName,String myport){
        this.SERVERPORT = port;
        this.fileName = fileName;
        size = -1;
        downloaded = 0;
        status= DOWNLOADING;

        this.myport = myport;
        this.path += "/"+myport+"/"+fileName;

        download();

    }


    //start or resume current download
    private void download(){
        Thread thread = new Thread(this);
        thread.start();
    }


    public void run(){
        System.out.println("Peer cliente inicia descarga");
        RandomAccessFile file = null;
        oistream = null;

        try{
            //Connect and set portion to download
            //System.out.println(SERVERIP + " - "+new Integer(SERVERPORT).intValue());
            serverAddr = InetAddress.getByName(SERVERIP);
            socket = new Socket(serverAddr,new Integer(SERVERPORT));
            //System.out.println("Cliente conectando a peer ...");
           try{
               ostream = socket.getOutputStream();
               oostream = new ObjectOutputStream(ostream);

               istream = socket.getInputStream();
               oistream = new ObjectInputStream(istream);

           } catch (Exception ex){
               System.out.println(ex.toString());
               error();
           }
            //System.out.println("Inicializando configuracion de descarga...");
            //send current download size
            oostream.reset();
            oostream.writeObject(new Long(downloaded));
            oostream.flush();
            //System.out.println("Enviando ultima posicion...");

            //send filename
            oostream.reset();
            oostream.writeObject(fileName);
            oostream.flush();
            //System.out.println("Enviando nombre de archivo...");

            //receive size of the file
            long contentLength = ((Long) oistream.readObject()).longValue();
            if(contentLength<1) error();
            //System.out.println("Recibienod tamano de archivo...");

            //set size
            if(size==-1){
                size = contentLength;
                stateChanged();
            }


            //open file and go to the end to append new content
            file = new RandomAccessFile(path,"rw");
            //System.out.println(fileName);
            file.seek(downloaded);


            while(status==DOWNLOADING){
                byte buffer[];

                if(size-downloaded > MAX_BUFFER_SIZE) buffer = new byte[MAX_BUFFER_SIZE];
                else buffer = new byte[(int)(size-downloaded)];

                int read = istream.read(buffer);
                //System.out.println("Bytes Recibidos: "+read);
                if(read<1) break;
                //write to file
                file.write(buffer, 0,read);
                downloaded+=read;
                stateChanged();

            }
            //System.out.println("Descarga terminada ..."+downloaded);
            //now is complete
            if(status==DOWNLOADING && downloaded==size){
                status=COMPLETE;
                stateChanged();
            }else {
                status=PAUSED;
                stateChanged();
            }

        }catch(Exception e) { error();  }
        finally{
            //close file if was open
            if(file!=null){
                try{
                    file.close();
                }catch(Exception e) { e.printStackTrace();}
            }

            //close input connection if was open
            if(oistream!=null){
                try{
                    oistream.close();
                }catch (Exception e) { e.printStackTrace();}
            }
        }
    }

    //set notify observer
    private void stateChanged(){
        setChanged();
        notifyObservers();
    }


    public void pause(){
        status=PAUSED;
        stateChanged();
    }

    public void resume(){
        status = DOWNLOADING;
        stateChanged();
        download();
    }

    public void cancel(){
        status = CANCELLED;
        stateChanged();
    }

    public void error(){
        status = ERROR;
        stateChanged();
    }

    public String getUrl(){
        return fileName;
    }

    public int getSize(){
        return (int)size;
    }

    public float getProgress(){
        return ((float)downloaded/size)*100;
    }

    public int getStatus(){
        return status;
    }

}
