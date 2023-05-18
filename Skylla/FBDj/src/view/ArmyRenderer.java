package view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ArmyRenderer extends DefaultTableCellRenderer {
    /**
     * 
     */
    private static final long serialVersionUID = -1345038480711636194L;

    public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        String army;
        setValue(value);
        army = (String) value.toString();
        if (army.contains("Red"))
            setBackground(Color.red);
        else if (army.contains("Blue"))
            setBackground(Color.blue);
        else
            setBackground(Color.white);
        return this;
    }
}
