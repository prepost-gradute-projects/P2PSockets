
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by jeslev on 18/10/15.
 */
public class PeerTable extends AbstractTableModel implements Observer{

    private static final String[] columns = {"IP","Archivo"};
    private static final Class[] columnsClass = {String.class, String.class};

    private ArrayList<ItemList> peerList = new ArrayList<ItemList>();


    public void addItem(ItemList item){
        item.addObserver(this);
        peerList.add(item);

        fireTableRowsInserted(getRowCount()-1, getRowCount()-1);
    }

    public ItemList getitem(int index){
        return peerList.get(index);
    }

    public void clearItem(int index){
        peerList.remove(index);
        fireTableRowsDeleted(index,index);
    }

    public void clearTable(){
        peerList.clear();
        fireTableRowsDeleted(0,getRowCount());
    }


    //handle col event
    public Object getValueAt(int row, int col){
        ItemList item = peerList.get(row);

        switch (col){
            case 0:
                return item.getIp();
            case 1:
                return item.getFile();
        }

        return "";
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
        return peerList.size();
    }

    public void update(Observable o, Object arg){
        int index = peerList.indexOf(arg);
        fireTableCellUpdated(index,index);
    }

}
