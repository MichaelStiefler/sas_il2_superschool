package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import mainController.MainController;
import mainController.ReservedNameController;
import model.ReservedName;

public class ReservedNamePanel extends JPanel {
    /**
         * 
         */
    private static final long      serialVersionUID      = 5803466033977965095L;
    private JScrollPane            reservedNameListSP    = new JScrollPane();
    private JTable                 reservedNameListTable = new JTable();
    private ReservedNameTableModel reservedNameTableModel;
    private JButton                addButton             = new JButton();
    private JButton                removeButton          = new JButton();
    private JButton                saveButton            = new JButton();
    private JButton                cancelButton          = new JButton();
    private ListSelectionModel     reservedNameSelection;
    private static boolean         dataChanged           = false;

    public ReservedNamePanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ReservedNameTableModel extends DefaultTableModel {

        /**
             * 
             */
        private static final long serialVersionUID = 4752063778786372438L;
        private static final int  ROW              = 0;

        public ReservedNameTableModel(String[] columnNames) {
            super(columnNames, ROW);
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            if (col > 0)
                return true;
            else
                return false;
        }
    }

    private void jbInit() throws Exception {

        setMaximumSize(new java.awt.Dimension(790, 500));
        setMinimumSize(new java.awt.Dimension(790, 500));
        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(790, 500));
        setLayout(null);
        reservedNameListSP.setBorder(BorderFactory.createTitledBorder("Reserved Name List"));

        addButton.setText("Add");
        addButton.setToolTipText("Add New Name");
        addButton.setBounds(470, 460, 75, 25);
        addButton.addActionListener(new AddListener());

        removeButton.setText("Remove");
        removeButton.setToolTipText("Remove Selected Name");
        removeButton.setBounds(550, 460, 75, 25);
        removeButton.addActionListener(new RemoveListener());

        saveButton.setText("Save");
        saveButton.setToolTipText("Save Changes");
        saveButton.setBounds(630, 460, 75, 25);
        saveButton.addActionListener(new SaveListener());

        cancelButton.setText("Cancel");
        cancelButton.setToolTipText("Cancel All Changes");
        cancelButton.setBounds(710, 460, 75, 25);
        cancelButton.addActionListener(new CancelListener());

        add(reservedNameListTable, null);
        add(reservedNameListSP, null);
        add(addButton, null);
        add(removeButton, null);
        add(saveButton, null);
        add(cancelButton, null);

        String[] columnHeader = { "Name", "Password" };
        reservedNameTableModel = new ReservedNameTableModel(columnHeader);
        reservedNameTableModel.addTableModelListener(new ReservedNameTableUpdate());

        reservedNameListTable = new javax.swing.JTable(reservedNameTableModel);
        reservedNameListTable.setRowSelectionAllowed(true);
        reservedNameListTable.setOpaque(true);
        reservedNameListTable.setBackground(Color.cyan);

        // Setup Tool Tips for Column Headers
        JTableHeader header = reservedNameListTable.getTableHeader();
        ColumnHeaderToolTips tips = new ColumnHeaderToolTips();
        tips.setToolTip(reservedNameListTable.getColumnModel().getColumn(1), "Password");
        header.addMouseMotionListener(tips);

        reservedNameSelection = reservedNameListTable.getSelectionModel();
        reservedNameSelection.clearSelection();

        // This Listener picks up the row the user selects
        reservedNameListTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // Is left mouse click
                if (SwingUtilities.isLeftMouseButton(e)) {
                    // get the coordinates of the mouse click
                    Point p = e.getPoint();

                    // get the row index that contains that coordinate
                    int rowNumber = reservedNameListTable.rowAtPoint(p);

                    // set the selected interval of rows. Using the "rowNumber"
                    // variable for the beginning and end selects only that one row.
                    reservedNameSelection.setSelectionInterval(rowNumber, rowNumber);
                }
            }
        });

        TableCellEditor editor = new DefaultCellEditor(new JPasswordField());
        TableColumn column = reservedNameListTable.getColumnModel().getColumn(1);
        column.setCellRenderer(new PasswordTableCellRenderer());
        column.setCellEditor(editor);

        reservedNameListTable.setName("reservedNameListTable"); // NOI18N
        reservedNameListSP.setViewportView(reservedNameListTable);
        reservedNameListSP.setOpaque(true);
        reservedNameListSP.setBounds(20, 20, 755, 380);

        add(reservedNameListSP);
        reservedNameSelection.clearSelection();

        // Load Reserved Names at Startup
        loadReservedNames(MainController.getReservedNames());

    }

    class PasswordTableCellRenderer extends JPasswordField implements TableCellRenderer {
        /**
             * 
             */
        private static final long serialVersionUID = -6210276980955852919L;
        // Used to pad the cell when it doesn't have focus
        protected Border          normalBorder     = new EmptyBorder(1, 1, 1, 1);

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }

            if (hasFocus) {
                setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            } else {
                setBorder(normalBorder);
            }

            setText((String) value);
            return this;
        }
    }

    // Load all Admins from HashMap into the Table
    public void loadReservedNames(HashMap<String, ReservedName> reservedNames) {
        Iterator<String> it = reservedNames.keySet().iterator();
        while (it.hasNext()) {
            try {
                // Key is actually name but this is for clarity
                String key = it.next();
                ReservedName reservedNameRecord = reservedNames.get(key);
                Object[] adminRow = { reservedNameRecord.getName(), reservedNameRecord.getPassword() };
                reservedNameTableModel.addRow(adminRow);

            } catch (Exception ex) {}
        }

    }

    // Update the Admin model with new data.
    public void updateName(String reservedName, int tableRow) {
        ReservedName reservedNameRecord = MainController.getReservedNames().get(reservedName);
        reservedNameRecord.setPassword((String) reservedNameTableModel.getValueAt(tableRow, 1));
    }

    // Listens for Changes to Table then updates the corresponding Admin Record in MainController.ADMINS
    class ReservedNameTableUpdate implements TableModelListener {
        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE) {
                dataChanged = true;
                updateName(reservedNameTableModel.getValueAt(e.getFirstRow(), 0).toString(), e.getFirstRow());
            }
        }
    }

    class AddListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String reservedName = (String) JOptionPane.showInputDialog(null, "Enter the Reserved Name:\n", "Reserved Name", JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (reservedName != null && reservedName.length() > 0) {
                String reservedPwd = (String) JOptionPane.showInputDialog(null, "Enter the Password:\n", "Password", JOptionPane.PLAIN_MESSAGE, null, null, null);

                if (reservedPwd != null && reservedPwd.length() > 0) {
                    ReservedNameController.reservedNameAdd(reservedName, reservedPwd);
                    ReservedName reservedNameRecord = MainController.getReservedNames().get(reservedName);
                    Object[] nameRow = { reservedNameRecord.getName(), reservedNameRecord.getPassword() };
                    reservedNameTableModel.addRow(nameRow);
                }
            }
        }
    } // End AddListener

    class RemoveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Make Sure a Record is selected
            if (reservedNameSelection.getMinSelectionIndex() != -1) {
                String nameToRemove = reservedNameListTable.getValueAt(reservedNameSelection.getMinSelectionIndex(), 0).toString();

                try {
                    // Confirm Deletion
                    int n = JOptionPane.showConfirmDialog(null, "Remove ( " + nameToRemove + " ) from Reserved Name List ?", "Remove Name", JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.YES_OPTION) {
                        ReservedNameController.reservedNameRemove(nameToRemove);
                        reservedNameTableModel.removeRow(reservedNameSelection.getMinSelectionIndex());
                    }
                } catch (NumberFormatException ex) {}
            } else {
                JOptionPane.showMessageDialog(null, "No Admin Selected", "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }  // End RemoveListener

    class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (dataChanged) {
                try {
                    ReservedNameController.reservedNamesWrite();
                    dataChanged = false;
                    JOptionPane.showMessageDialog(null, "Administrator Data Saved", "Message", JOptionPane.INFORMATION_MESSAGE);
                    reservedNameSelection.clearSelection();
                } catch (Exception ex) {}
            }
        }
    } // End SaveListener

    // Handle Cancel Button Clicked
    class CancelListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (dataChanged) {
                // Admin Data has changed, confirm cancel
                int n = JOptionPane.showConfirmDialog(null, "Cancel Changes ?", "Cancel", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    // Yes cancel changes. Remove all rows from table and reload from file.
                    dataChanged = false;
                    int rowCount = reservedNameTableModel.getRowCount();
                    for (int i = 0; i < rowCount; i++) {
                        reservedNameTableModel.removeRow(0);
                    }
                    MainController.setReservedNames(ReservedNameController.reservedNamesLoad());
                    loadReservedNames(MainController.getReservedNames());
                }
            } else {
                reservedNameSelection.clearSelection();
            }
        }
    }  // End CancelListener
}
