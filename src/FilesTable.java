import java.io.Serializable;

/**
 * Created by root on 24/10/15.
 */
public class FilesTable implements Serializable {

    private PeerFileTable[] peerFileTables;
    private boolean[] states;
    public int len = 100,cur;

    public FilesTable(){

        peerFileTables = new PeerFileTable[len];
        states = new boolean[len];
        cur = 0;
    }

    public synchronized void add(PeerFileTable table){
        table.setId(cur);
        peerFileTables[cur] = table;
        states[cur] = true;
        cur++;
    }

    public void show(){
        System.out.println("Tabla de archivos");
        for(int i=0;i<cur;i++){
            if(states[i]) peerFileTables[i].show();
        }

    }

    public synchronized void remove(int id){
        states[id] = false;
    }
}
