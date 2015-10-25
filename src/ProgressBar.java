
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by jeslev on 18/10/15.
 */
public class ProgressBar extends JProgressBar implements TableCellRenderer {

    public ProgressBar(int min, int max){
        super(min,max);
    }

    public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected, boolean hasFocus, int row, int col){
        setValue((int) ((Float)value).floatValue());
        return this;
    }


}
