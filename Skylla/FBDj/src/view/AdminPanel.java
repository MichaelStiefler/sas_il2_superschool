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

import mainController.AdminController;
import mainController.MainController;
import model.Admin;

public class AdminPanel extends JPanel {
    /**
     * 
     */
    private static final long  serialVersionUID = 1378456002210108442L;
    private JScrollPane        adminListSP      = new JScrollPane();
    private JTable             adminListTable   = new JTable();
    private AdminTableModel    adminTableModel;
    private JButton            addButton        = new JButton();
    private JButton            removeButton     = new JButton();
    private JButton            saveButton       = new JButton();
    private JButton            cancelButton     = new JButton();
    private ListSelectionModel adminSelection;
    private static boolean     dataChanged      = false;

    public AdminPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class AdminTableModel extends DefaultTableModel {

        /**
         * 
         */
        private static final long serialVersionUID = -4006238455898318289L;
        private static final int  ROW              = 0;

        public AdminTableModel(String[] columnNames) {
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
        adminListSP.setBorder(BorderFactory.createTitledBorder("Adminstrator List"));

        addButton.setText("Add");
        addButton.setToolTipText("Add New Admin");
        addButton.setBounds(470, 460, 75, 25);
        addButton.addActionListener(new AddListener());

        removeButton.setText("Remove");
        removeButton.setToolTipText("Remove Selected Admin");
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

        add(adminListTable, null);
        add(adminListSP, null);
        add(addButton, null);
        add(removeButton, null);
        add(saveButton, null);
        add(cancelButton, null);

        String[] columnHeader = { "Name", "Host", "Password", "Kick", "Ban-A", "Ban-R", "M-Time", "M-Restart", "M-Load", "Console", "Ping", "Stop", "Restart" };
        adminTableModel = new AdminTableModel(columnHeader);
        adminTableModel.addTableModelListener(new AdminTableUpdate());

        adminListTable = new javax.swing.JTable(adminTableModel);
        adminListTable.setRowSelectionAllowed(true);
        adminListTable.setOpaque(true);
        adminListTable.setBackground(Color.cyan);

        // Setup Tool Tips for Column Headers
        JTableHeader header = adminListTable.getTableHeader();
        ColumnHeaderToolTips tips = new ColumnHeaderToolTips();
        tips.setToolTip(adminListTable.getColumnModel().getColumn(1), "Allow Admin to Auto Login");
        tips.setToolTip(adminListTable.getColumnModel().getColumn(2), "Password");
        tips.setToolTip(adminListTable.getColumnModel().getColumn(3), "Allow Admin to Kick Players");
        tips.setToolTip(adminListTable.getColumnModel().getColumn(4), "Allow Admin to Ban Players");
        tips.setToolTip(adminListTable.getColumnModel().getColumn(5), "Allow Admin to Remove Bans");
        tips.setToolTip(adminListTable.getColumnModel().getColumn(6), "Allow Admin to Adjust Mission Time");
        tips.setToolTip(adminListTable.getColumnModel().getColumn(7), "Allow Admin to Restart Current Mission");
        tips.setToolTip(adminListTable.getColumnModel().getColumn(8), "Allow Admin to Load a Temporary Mission");
        tips.setToolTip(adminListTable.getColumnModel().getColumn(9), "Allow Admin to Send a Console Command");
        tips.setToolTip(adminListTable.getColumnModel().getColumn(10), "Allow Admin to Enable/Disable PingKick");
        tips.setToolTip(adminListTable.getColumnModel().getColumn(11), "Allow Admin to Stop FBDj");
        tips.setToolTip(adminListTable.getColumnModel().getColumn(12), "Allow Admin to Restart FBDj");
        header.addMouseMotionListener(tips);

        adminSelection = adminListTable.getSelectionModel();
        adminSelection.clearSelection();

        // This Listener picks up the row the user selects
        adminListTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // Is left mouse click
                if (SwingUtilities.isLeftMouseButton(e)) {
                    // get the coordinates of the mouse click
                    Point p = e.getPoint();

                    // get the row index that contains that coordinate
                    int rowNumber = adminListTable.rowAtPoint(p);

                    // set the selected interval of rows. Using the "rowNumber"
                    // variable for the beginning and end selects only that one row.
                    adminSelection.setSelectionInterval(rowNumber, rowNumber);
                }
            }
        });

        TableCellEditor editor = new DefaultCellEditor(new JPasswordField());
        TableColumn column = adminListTable.getColumnModel().getColumn(adminTableModel.findColumn("Password"));
        column.setCellRenderer(new PasswordTableCellRenderer());
        column.setCellEditor(editor);

//		adminListTable.setCellEditor(editor);

        adminListTable.setName("adminListTable"); // NOI18N
        adminListSP.setViewportView(adminListTable);
        adminListSP.setOpaque(true);
        adminListSP.setBounds(20, 20, 755, 380);

        add(adminListSP);
        adminSelection.clearSelection();

        // Load Admins at Startup
        loadAdmins(MainController.getAdmins());

    }

    class PasswordTableCellRenderer extends JPasswordField implements TableCellRenderer {
        /**
         * 
         */
        private static final long serialVersionUID = -6103113546985355081L;
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
    public void loadAdmins(HashMap<String, Admin> admins) {
        Iterator<String> it = admins.keySet().iterator();
        while (it.hasNext()) {
            try {
                // Key is actually name but this is for clarity
                String key = it.next();
                Admin adminRecord = admins.get(key);
                Object[] adminRow = { adminRecord.getName(), adminRecord.getAdminHost(), adminRecord.getPassword(), adminRecord.isKick(), adminRecord.isBanAdd(), adminRecord.isBanRemove(), adminRecord.isMissionExtend(), adminRecord.isMissionRestart(),
                        adminRecord.isLoadMission(), adminRecord.isConsole(), adminRecord.isPingKick(), adminRecord.isFbdjStop(), adminRecord.isFbdjRestart() };
                adminTableModel.addRow(adminRow);

            } catch (Exception ex) {}
        }

    }

    // Update the Admin model with new data.
    public void updateAdmin(String adminName, int tableRow) {

        @SuppressWarnings("unused")
        String[] columnHeader = { "Name", "Host", "Password", "Kick", "Ban-A", "Ban-R", "M-Time", "M-Restart", "M-Load", "Console", "Ping", "Stop", "Restart" };
        Admin adminRecord = MainController.getAdmins().get(adminName);
        adminRecord.setAdminHost((String) adminTableModel.getValueAt(tableRow, adminTableModel.findColumn("Host")));
        adminRecord.setPassword((String) adminTableModel.getValueAt(tableRow, adminTableModel.findColumn("Password")));
        adminRecord.setKick((Boolean) adminTableModel.getValueAt(tableRow, adminTableModel.findColumn("Kick")));
        adminRecord.setBanAdd((Boolean) adminTableModel.getValueAt(tableRow, adminTableModel.findColumn("Ban-A")));
        adminRecord.setBanRemove((Boolean) adminTableModel.getValueAt(tableRow, adminTableModel.findColumn("Ban-R")));
        adminRecord.setMissionExtend((Boolean) adminTableModel.getValueAt(tableRow, adminTableModel.findColumn("M-Time")));
        adminRecord.setMissionRestart((Boolean) adminTableModel.getValueAt(tableRow, adminTableModel.findColumn("M-Restart")));
        adminRecord.setLoadMission((Boolean) adminTableModel.getValueAt(tableRow, adminTableModel.findColumn("M-Load")));
        adminRecord.setConsole((Boolean) adminTableModel.getValueAt(tableRow, adminTableModel.findColumn("Console")));
        adminRecord.setPingKick((Boolean) adminTableModel.getValueAt(tableRow, adminTableModel.findColumn("Ping")));
        adminRecord.setFbdjStop((Boolean) adminTableModel.getValueAt(tableRow, adminTableModel.findColumn("Stop")));
        adminRecord.setFbdjRestart((Boolean) adminTableModel.getValueAt(tableRow, adminTableModel.findColumn("Restart")));
    }

    // Listens for Changes to Table then updates the corresponding Admin Record in MainController.ADMINS
    class AdminTableUpdate implements TableModelListener {
        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE) {
                dataChanged = true;
                updateAdmin(adminTableModel.getValueAt(e.getFirstRow(), 0).toString(), e.getFirstRow());
            }
        }
    }

    class AddListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String adminName = (String) JOptionPane.showInputDialog(null, "Enter the Administrator Name:\n", "Administrator Name", JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (adminName != null && adminName.length() > 0) {
                String adminPwd = (String) JOptionPane.showInputDialog(null, "Enter the Administrator Password:\n", "Administrator Password", JOptionPane.PLAIN_MESSAGE, null, null, null);

                if (adminPwd != null && adminPwd.length() > 0) {
                    AdminController.adminAdd(adminName, adminPwd);
                    Admin adminRecord = MainController.getAdmins().get(adminName);
                    Object[] adminRow = { adminRecord.getName(), adminRecord.getAdminHost(), adminRecord.getPassword(), adminRecord.isKick(), adminRecord.isBanAdd(), adminRecord.isBanRemove(), adminRecord.isMissionExtend(),
                            adminRecord.isMissionRestart(), adminRecord.isLoadMission(), adminRecord.isConsole(), adminRecord.isPingKick(), adminRecord.isFbdjStop(), adminRecord.isFbdjRestart() };
                    adminTableModel.addRow(adminRow);
                }
            }
        }
    } // End AddListener

    class RemoveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Make Sure an Admin is selected
            if (adminSelection.getMinSelectionIndex() != -1) {
                String adminToRemove = (String) adminListTable.getValueAt(adminSelection.getMinSelectionIndex(), adminTableModel.findColumn("Name"));

                try {
                    // Confirm Deletion
                    int n = JOptionPane.showConfirmDialog(null, "Remove ( " + adminToRemove + " ) from Admin List ?", "Remove Admin", JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.YES_OPTION) {
                        AdminController.adminRemove(adminToRemove);
                        adminTableModel.removeRow(adminSelection.getMinSelectionIndex());
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
                    AdminController.adminsWrite();
                    dataChanged = false;
                    JOptionPane.showMessageDialog(null, "Administrator Data Saved", "Message", JOptionPane.INFORMATION_MESSAGE);
                    adminSelection.clearSelection();
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
                    int rowCount = adminTableModel.getRowCount();
                    for (int i = 0; i < rowCount; i++) {
                        adminTableModel.removeRow(0);
                    }
                    MainController.setAdmins(AdminController.adminsLoad());
                    loadAdmins(MainController.getAdmins());
                }
            } else {
                adminSelection.clearSelection();
            }
        }
    }  // End CancelListener
}
