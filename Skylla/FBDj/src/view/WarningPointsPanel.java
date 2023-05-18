package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import mainController.MainController;
import model.WarningPoints;
import model.WarningPointsItem;
import utility.DoubleEditor;
import utility.EachRowEditor;
import utility.IntegerEditor;
import utility.SortedHashMap;
import viewController.WarningPointsViewController;

public class WarningPointsPanel extends JPanel {
    /**
     * 
     */
    private static final long      serialVersionUID      = 8925559727630133260L;
    private JScrollPane            warningPointListSP    = new JScrollPane();
    private JTable                 warningPointListTable = new JTable();
    private WarningPointTableModel warningPointTableModel;
    private JScrollPane            warningLevelListSP    = new JScrollPane();
    private JTable                 warningLevelListTable = new JTable();
    private WarningPointTableModel warningLevelTableModel;

    @SuppressWarnings("unused")
    private JButton                addButton             = new JButton();
    @SuppressWarnings("unused")
    private JButton                removeButton          = new JButton();
    private JButton                saveButton            = new JButton();
    private JButton                cancelButton          = new JButton();
    @SuppressWarnings("unused")
    private JButton                refreshButton         = new JButton();
    private ListSelectionModel     selection;
    private static boolean         dataChanged           = false;
    private static WarningPoints   currentWP             = WarningPointsViewController.getWarningPoints();
    private static WarningPoints   workingWP             = new WarningPoints();

    public WarningPointsPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class WarningPointTableModel extends DefaultTableModel {

        /**
         * 
         */
        private static final long serialVersionUID = 6527452640711565799L;
        private static final int  ROW              = 0;

        public WarningPointTableModel(String[] columnNames) {
            super(columnNames, ROW);
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            if (col < (this.getColumnCount() - 1))
                return false;
            else
                return true;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void jbInit() throws Exception {

        setMaximumSize(new java.awt.Dimension(790, 500));
        setMinimumSize(new java.awt.Dimension(790, 500));
        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(790, 500));
        setLayout(null);

        String[] columnHeader = { "key", "Setting", "Value" };

        warningPointListSP.setBounds(85, 35, 300, 250);
        warningPointListSP.setBorder(BorderFactory.createTitledBorder("Warning Points"));
        warningLevelListSP.setBounds(410, 35, 300, 250);
        warningLevelListSP.setBorder(BorderFactory.createTitledBorder("Warning Levels"));

        warningPointTableModel = new WarningPointTableModel(columnHeader);
        warningPointTableModel.addTableModelListener(new WarningPointsTableUpdate());
        warningPointListTable = new javax.swing.JTable(warningPointTableModel);
        warningPointListTable.setRowHeight(20);
        warningPointListTable.setOpaque(true);
        warningPointListTable.setBackground(Color.cyan);
        warningPointListTable.setName("warningPointTable");
        warningPointListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        warningPointListTable.removeColumn(warningPointListTable.getColumn("key"));
        warningPointListSP.getViewport().add(warningPointListTable);
        warningPointListSP.setOpaque(true);

        warningLevelTableModel = new WarningPointTableModel(columnHeader);
        warningLevelTableModel.addTableModelListener(new WarningLevelTableUpdate());
        warningLevelListTable = new javax.swing.JTable(warningLevelTableModel);
        warningLevelListTable.setRowHeight(20);
        warningLevelListTable.setOpaque(true);
        warningLevelListTable.setBackground(Color.cyan);
        warningLevelListTable.setName("warningLevelTable"); // NOI18N
        warningLevelListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        warningLevelListTable.removeColumn(warningLevelListTable.getColumn("key"));
        warningLevelListSP.getViewport().add(warningLevelListTable);
        warningLevelListSP.setOpaque(true);

        saveButton.setText("Save");
        saveButton.setToolTipText("Save Changes");
        saveButton.setBounds(630, 460, 75, 25);
        saveButton.addActionListener(new SaveListener());

        cancelButton.setText("Cancel");
        cancelButton.setToolTipText("Cancel All Changes");
        cancelButton.setBounds(710, 460, 75, 25);
        cancelButton.addActionListener(new CancelListener());

        add(warningPointListSP, null);
        add(warningLevelListSP, null);
        add(saveButton, null);
        add(cancelButton, null);

        workingWP.setWarningPoints(new HashMap(currentWP.getWarningPoints()));
        workingWP.setWarningLevels(new HashMap(currentWP.getWarningLevels()));
        populateWarningPointSettings(workingWP);

        // clearWPTableModel();
    }

    class WarningPointsTableUpdate implements TableModelListener {

        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE) {
                try {

                    @SuppressWarnings("unused")
                    int row = e.getFirstRow();
                    String name = (String) warningPointTableModel.getValueAt(e.getFirstRow(), warningPointTableModel.findColumn("key"));
                    Object value = warningPointTableModel.getValueAt(e.getFirstRow(), warningPointTableModel.findColumn("Value"));
                    WarningPointsViewController.updateItem(workingWP, workingWP.getWarningPoints(), name, value);
                    dataChanged = true;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Invalid Configuration Setting Error:" + ex, "Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    class WarningLevelTableUpdate implements TableModelListener {

        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE) {
                try {

                    @SuppressWarnings("unused")
                    int row = e.getFirstRow();
                    String name = (String) warningLevelTableModel.getValueAt(e.getFirstRow(), warningLevelTableModel.findColumn("key"));
                    Object value = warningLevelTableModel.getValueAt(e.getFirstRow(), warningLevelTableModel.findColumn("Value"));
                    WarningPointsViewController.updateItem(workingWP, workingWP.getWarningLevels(), name, value);
                    dataChanged = true;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Invalid Configuration Setting Error:" + ex, "Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (dataChanged) {
                if (WarningPointsViewController.writeWarningPoints(workingWP)) {
                    MainController.writeDebugLogFile(1, "Warning Points Saved");
                    JOptionPane.showMessageDialog(null, "Warning Points Saved", "Message", JOptionPane.INFORMATION_MESSAGE);
                    dataChanged = false;
                    currentWP.setWarningLevels(workingWP.getWarningLevels());
                    currentWP.setWarningPoints(workingWP.getWarningPoints());
                } else {
                    JOptionPane.showMessageDialog(null, "Error Saving Warning Points ", "Message", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "No Changes to Save", "Message", JOptionPane.INFORMATION_MESSAGE);
            }

        }
    } // End SaveListener

    // Handle Cancel Button Clicked
    class CancelListener implements ActionListener {
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public void actionPerformed(ActionEvent e) {
            if (dataChanged) {
                // Ban Data has changed, confirm cancel
                int n = JOptionPane.showConfirmDialog(null, "Cancel Changes ?", "Cancel", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    // Yes cancel changes. Remove all rows from table and reload from file.
                    dataChanged = false;
                    clearWPTableModel();
                    workingWP.setWarningPoints(new HashMap(currentWP.getWarningPoints()));
                    workingWP.setWarningLevels(new HashMap(currentWP.getWarningLevels()));
                    populateWarningPointSettings(workingWP);
                }
            } else {
                selection.clearSelection();
            }
        }
    }  // End CancelListener

    public static boolean isDataChanged() {
        return dataChanged;
    }

    public static void setDataChanged(boolean dataChanged) {
        WarningPointsPanel.dataChanged = dataChanged;
    }

    private void setConfigEditor(WarningPointTableModel tableModel, EachRowEditor rowEditor, WarningPointsItem item, int rowId) {

        try {
            String label = item.getLabel();
            switch (item.getItemType()) {
                case INTEGER:
                    int ivalue = (Integer) item.getValue();
                    rowEditor.setEditorAt(rowId, new IntegerEditor((Integer) item.getLowLimit(), (Integer) item.getHighLimit()));
                    tableModel.addRow(new Object[] { item.getName(), label, ivalue });
                    break;
                case BOOLEAN:
                    Boolean bvalue = (Boolean) item.getValue();
                    rowEditor.setEditorAt(rowId, new DefaultCellEditor(new JCheckBox()));
                    tableModel.addRow(new Object[] { item.getName(), label, bvalue });
                    break;
                case STRING:
                    String svalue = (String) item.getValue();
                    tableModel.addRow(new Object[] { item.getName(), label, svalue });
                    break;
                case DOUBLE:
                    Double dvalue = (Double) item.getValue();
                    rowEditor.setEditorAt(rowId, new DoubleEditor(Double.valueOf(item.getLowLimit().toString()), Double.valueOf(item.getHighLimit().toString())));
                    tableModel.addRow(new Object[] { item.getName(), label, dvalue });
                    break;
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "ConfigurationPanel.setConfigEditor - Error Unhandled Exception: " + ex);
        }
    }

    private void populateWarningPointSettings(WarningPoints wp) {
        int rowId = 0;
        HashMap<String, WarningPointsItem> sortedMap;
        ArrayList<String> sortedKeys;

        try {

            rowId = 0;
            sortedMap = wp.getWarningPoints();
            sortedKeys = SortedHashMap.getSortedStringKeys(sortedMap);
            EachRowEditor wpRowEditor = new EachRowEditor(warningPointListTable);
            warningPointListTable.getColumn("Value").setCellEditor(wpRowEditor);
            for (String key : sortedKeys) {
                setConfigEditor(warningPointTableModel, wpRowEditor, wp.getWarningPoints().get(key), rowId);
                rowId++;
            }

            rowId = 0;
            HashMap<String, WarningPointsItem> sortedHashMap = wp.getWarningLevels();
            sortedKeys = SortedHashMap.getSortedStringKeys(sortedHashMap);
            EachRowEditor wlRowEditor = new EachRowEditor(warningLevelListTable);
            warningLevelListTable.getColumn("Value").setCellEditor(wlRowEditor);
            for (String key : sortedKeys) {
                setConfigEditor(warningLevelTableModel, wlRowEditor, wp.getWarningLevels().get(key), rowId);
                rowId++;
            }

        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "WarmingPointsPanel.populateWarningPointSettings - Error Unhandled Exception for Config (" + wp + "): " + ex);
        }
    }

    private void clearWPTableModel() {
        int rowCount = 0;
        rowCount = warningPointTableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            warningPointTableModel.removeRow(0);
        }

        rowCount = warningLevelTableModel.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            warningLevelTableModel.removeRow(0);
        }

    }  // End clearConfigTableModel

}
