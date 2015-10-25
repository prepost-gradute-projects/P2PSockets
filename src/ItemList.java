import java.util.Observable;

/**
 * Created by root on 24/10/15.
 */
public class ItemList extends Observable {

    String ip, file;

    public ItemList(String ip, String file){
        this.ip =  ip;
        this.file = file;
    }

    public String getIp(){ return ip;}

    public String getFile() {return file;}

    private void stateChanged(){
        setChanged();
        notifyObservers();
    }

}
