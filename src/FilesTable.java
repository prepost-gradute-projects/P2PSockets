import java.io.Serializable;

/**
 * Created by root on 24/10/15.
 */
public class FilesTable implements Serializable {

    private PeerFileTable[] peerFileTables;
    private boolean[] states;
    public int len = 100, cur;

    public FilesTable() {

        peerFileTables = new PeerFileTable[len];
        states = new boolean[len];
        cur = 0;
    }

    public synchronized void add(PeerFileTable table) {
        table.setId(cur);
        peerFileTables[cur] = table;
        states[cur] = true;
        cur++;
    }

    public void show() {
        //System.out.println("Tabla de archivos");
        //Vector<String> vstr = new Vector<String>();
        if (GUITable.pft.getId() == -1) {
            GUITable.pft.setId(cur-1);
        }

        GUITable.peerModel.clearTable();


        for (int i = 0; i < cur; i++) {
            if (states[i]) {
                String[] tmp = peerFileTables[i].show();
                String port = peerFileTables[i].getPort();
                for (int j = 0; j < tmp.length; j++) {
                    //vstr.add(tmp[j]);
                    GUITable.peerModel.addItem(new ItemList(port, tmp[j]));
                }
            }
        }

        //resume downloads waiting in queue
        GUITable.resumeAll();
        //return vstr;
    }

    public synchronized void remove(int id) {
        states[id] = false;
    }

}