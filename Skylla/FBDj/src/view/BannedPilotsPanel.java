package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import mainController.MainController;
import model.PilotBan;
import utility.IPAddress;
import utility.Time;
import viewController.BannedPilotsController;

public class BannedPilotsPanel extends JPanel {
    /**
         * 
         */
    private static final long  serialVersionUID = 175963861194781779L;
    private JScrollPane        banListSP        = new JScrollPane();
    private JTable             banListTable     = new JTable();
    private BanTableModel      banTableModel;
    private JButton            addButton        = new JButton();
    private JButton            removeButton     = new JButton();
    private JButton            saveButton       = new JButton();
    private JButton            cancelButton     = new JButton();
    private JButton            refreshButton    = new JButton();
    private ListSelectionModel banSelection;
    private static boolean     dataChanged      = false;

    public BannedPilotsPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class BanTableModel extends DefaultTableModel {

        /**
             * 
             */
        private static final long serialVersionUID = -7762552067072427241L;
        private static final int  ROW              = 0;

        public BanTableModel(String[] columnNames) {
            super(columnNames, ROW);
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            if (col == 2)
                return false;
            else
                return true;
        }
    }

    private void jbInit() throws Exception {

        setMaximumSize(new java.awt.Dimension(790, 500));
        setMinimumSize(new java.awt.Dimension(790, 500));
        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(790, 500));
        setLayout(null);
        banListSP.setBorder(BorderFactory.createTitledBorder("Banned Pilot List"));

        refreshButton.setText("Refresh");
        refreshButton.setToolTipText("Add New Ban");
        refreshButton.setBounds(390, 460, 75, 25);
        refreshButton.addActionListener(new RefreshListener());

        addButton.setText("Add");
        addButton.setToolTipText("Add New Ban");
        addButton.setBounds(470, 460, 75, 25);
        addButton.addActionListener(new AddListener());

        removeButton.setText("Remove");
        removeButton.setToolTipText("Remove Selected Ban");
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

        add(banListTable, null);
        add(banListSP, null);
        add(refreshButton, null);
        add(addButton, null);
        add(removeButton, null);
        add(saveButton, null);
        add(cancelButton, null);

        String[] columnHeader = { "Name", "IPAddress", "Start Date", "Reason", "Duration" };
        banTableModel = new BanTableModel(columnHeader);
        banTableModel.addTableModelListener(new BanTableUpdate());
        banListTable = new javax.swing.JTable(banTableModel);
        banListTable.setRowSelectionAllowed(true);
        banListTable.setAutoCreateRowSorter(true);
        banListTable.setOpaque(true);
        banListTable.setBackground(Color.cyan);
        banListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Setup Tool Tips for Column Headers
        JTableHeader header = banListTable.getTableHeader();
        ColumnHeaderToolTips tips = new ColumnHeaderToolTips();
        tips.setToolTip(banListTable.getColumnModel().getColumn(0), "Name of Banned Pilot");
        tips.setToolTip(banListTable.getColumnModel().getColumn(1), "IP Address of Banned Pilot or IP Range(For range use format xxx.xxx.)");
        tips.setToolTip(banListTable.getColumnModel().getColumn(2), "Date/Time Ban Took effect");
        tips.setToolTip(banListTable.getColumnModel().getColumn(3), "Why this pilot was banned");
        tips.setToolTip(banListTable.getColumnModel().getColumn(4), "Duration of Ban in Hours.  0 = Permanent Ban");
        header.addMouseMotionListener(tips);

        banSelection = banListTable.getSelectionModel();
        banSelection.clearSelection();

        banListTable.setName("banListTable"); // NOI18N
        banListSP.setViewportView(banListTable);
        banListSP.setOpaque(true);
        banListSP.setBounds(20, 20, 755, 380);

        add(banListSP);
        banSelection.clearSelection();

        // Load Admins at Startup
        loadBannedPilots(MainController.getBannedPilots());

    }

    // Load all Banned Pilots from HashMap into the Table
    public void loadBannedPilots(HashMap<String, PilotBan> bannedPilots) {
        Iterator<String> it = bannedPilots.keySet().iterator();
        while (it.hasNext()) {
            try {
                // Key is actually name but this is for clarity
                String key = it.next();
                PilotBan banRecord = bannedPilots.get(key);
                Object[] banRow = { banRecord.getName(), banRecord.getIpAddress(), Time.getTimeCustom(banRecord.getTimestamp(), "yyyy-MM-dd   HH:mm"), banRecord.getBanner(), new Double(banRecord.getDuration()) / 3600000.0 };
                banTableModel.addRow(banRow);

            } catch (Exception ex) {}
        }

    }

    class RefreshListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            clearForm();
            loadBannedPilots(MainController.getBannedPilots());
        }
    } // End Refresh Listener

    // Listens for Changes to Table then updates the corresponding Admin Record in MainController.ADMINS
    class BanTableUpdate implements TableModelListener {
        boolean ipOK = true;

        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE) {
                if (e.getColumn() == banTableModel.findColumn("IPAddress")) {
                    String ipAddress = (String) banTableModel.getValueAt(e.getFirstRow(), e.getColumn());
//						if (!BannedPilotsController.validateIPAddress(ipAddress) && !BannedPilotsController.validateSubnet(ipAddress))
                    if (!IPAddress.validateIPAddress(ipAddress)) {
                        JOptionPane.showMessageDialog(null, "IP Address Format Not Valid\n Valid format xxx.xxx.xxx.xxx\n Where xxx > 0 and < 255\n Use * for a WildCard", "Error", JOptionPane.ERROR_MESSAGE);
                        ipOK = false;
                    } else {
                        ipOK = true;
                    }
                }
                if (ipOK) {
                    dataChanged = true;
                    Long banDuration = Math.round((Double) banTableModel.getValueAt(e.getFirstRow(), banTableModel.findColumn("Duration")) * 3600000);
                    String name = (String) banTableModel.getValueAt(e.getFirstRow(), banTableModel.findColumn("Name"));
                    String reason = (String) banTableModel.getValueAt(e.getFirstRow(), banTableModel.findColumn("Reason"));
                    String ipAddress = (String) banTableModel.getValueAt(e.getFirstRow(), banTableModel.findColumn("IPAddress"));
                    if ((name != null && name.length() > 0) || (ipAddress != null && ipAddress.length() > 0) && reason != null && reason.length() > 0 && banDuration > -1) {

                        BannedPilotsController.updateBannedPilot(ipAddress, name, reason, banDuration);
                    }
//						MainController.writeDebugLogFile(1, "Ban Data changed on " + name);
                }
            }
        }
    }

    class AddListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object[] banRow = { "", "", Time.getTimeCustom(Time.getTime(), "yyyy-MM-dd   HH:mm"), "", new Double(0.0) };
            banTableModel.addRow(banRow);
            dataChanged = true;
        }
    } // End AddListener

    class RemoveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Make Sure a Banned Pilot is selected
            if (banListTable.getSelectedRow() != -1) {
                String name = (String) banListTable.getValueAt(banListTable.getSelectedRow(), banTableModel.findColumn("Name"));
                String ipAddress = (String) banListTable.getValueAt(banListTable.getSelectedRow(), banTableModel.findColumn("IPAddress"));

                try {
                    // Confirm Deletion
                    int n = JOptionPane.showConfirmDialog(null, "Remove Name( " + name + " ) IP( " + ipAddress + " ) from Banned List ?", "Remove Admin", JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.YES_OPTION) {
                        BannedPilotsController.removePilotBan(name, ipAddress);
                        banTableModel.removeRow(banSelection.getMinSelectionIndex());
                    }
                } catch (NumberFormatException ex) {}
            } else {
                JOptionPane.showMessageDialog(null, "No Banned Pilot Selected", "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }  // End RemoveListener

    class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (dataChanged) {
                try {
                    BannedPilotsController.saveBannedPilots();
                    dataChanged = false;
                    JOptionPane.showMessageDialog(null, "Banned Pilot List Saved", "Message", JOptionPane.INFORMATION_MESSAGE);
                    banSelection.clearSelection();
                } catch (Exception ex) {}
            }
        }
    } // End SaveListener

    // Handle Cancel Button Clicked
    class CancelListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (dataChanged) {
                // Ban Data has changed, confirm cancel
                int n = JOptionPane.showConfirmDialog(null, "Cancel Changes ?", "Cancel", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    // Yes cancel changes. Remove all rows from table and reload from file.
                    dataChanged = false;
                    int rowCount = banTableModel.getRowCount();
                    for (int i = 0; i < rowCount; i++) {
                        banTableModel.removeRow(0);
                    }
                    BannedPilotsController.cancelBannedPilotChanges();
                    loadBannedPilots(MainController.getBannedPilots());
                }
            } else {
                banSelection.clearSelection();
            }
        }
    }  // End CancelListener

    public static boolean isDataChanged() {
        return dataChanged;
    }

    public static void setDataChanged(boolean dataChanged) {
        BannedPilotsPanel.dataChanged = dataChanged;
    }

    public void clearForm() {
        int rowCount = banTableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            banTableModel.removeRow(0);
        }
    }
}
