import java.io.Serializable;

/**
 * Created by root on 24/10/15.
 */
public class PeerFileTable implements Serializable {

    private String[] files;
    private int len =100,id;
    private String ip,port;

    public PeerFileTable(String[] files, String ip, String port){
        this.files = files;
        this.ip = ip;
        this.port = port;
    }

    public String[] show(){
        /*for(int i=0;i<files.length;i++)
            System.out.println(port+" - "+files[i]);
        */
        return files;
    }

    public String getIp(){ return ip;}

    public String getPort(){ return port;}

    public void setId(int id) { this.id = id;}

    public int getId() {return id;}

}
