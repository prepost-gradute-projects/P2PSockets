
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by jeslev on 18/10/15.
 */
public class DownloadTable extends AbstractTableModel implements Observer {

    private static final String[] columns = {"Archivo","Tamano","Progreso","Estado"};

    private static final Class[] columnsClass = {String.class, String.class, JProgressBar.class, String.class};

    private ArrayList<Download> downloadList = new ArrayList<Download>();

    public void addDownload(Download download){
        download.addObserver(this);
        downloadList.add(download);

        fireTableRowsInserted(getRowCount()-1, getRowCount()-1);
    }

    public Download getDownload(int index){
        return downloadList.get(index);
    }

    public void clearDownload(int index){
        downloadList.remove(index);
        fireTableRowsDeleted(index,index);
    }

    //handle col event
    public Object getValueAt(int row, int col){
        Download download = downloadList.get(row);

        switch (col){
            case 0:
                return download.getUrl();
            case 1:
                int size = download.getSize();
                return (size==-1)?"No Definido": Integer.toString(size);
            case 2:
                return new Float(download.getProgress());
            case 3:
                return Download.states[download.getStatus()];
        }

        return "";
    }

    public void update(Observable o, Object arg){
        int index = downloadList.indexOf(o);

        fireTableRowsUpdated(index,index);
    }



    public int getColumnCount(){
        return columns.length;
    }

    public String getColumnName(int index){
        return columns[index];
    }

    public Class<?> getColumnClass(int index){
        return columnsClass[index];
    }

    public int getRowCount(){
        return downloadList.size();
    }
}
