package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import mainController.MainController;
import mainController.PilotPanelController;
import model.GUICommand;
import model.QueueObj;
import utility.UnicodeFormatter;

public class PilotsPanel extends JPanel {

    /**
     * 
     */
    private static final long  serialVersionUID = 2683601114055509840L;
    private JScrollPane        pilotListSP      = new JScrollPane();
    private JTable             pilotListTable   = new JTable();
    private PilotTableModel    pilotTableModel;
    private JButton            refreshButton    = new JButton();
    private JButton            kickButton       = new JButton();
    private JButton            banButton        = new JButton();
    private JButton            chatButton       = new JButton();
    private ListSelectionModel pilotSelection;

    public PilotsPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class PilotTableModel extends DefaultTableModel {

        /**
         * 
         */
        private static final long serialVersionUID = 7953568911763504167L;
        private static final int  ROW              = 0;

        public PilotTableModel(String[] columnNames) {
            super(columnNames, ROW);
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public Class getColumnClass(int c) {
            try {
                return getValueAt(0, c).getClass();
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "PilotPanel.DefaultTableModel.getColumnClass Exception: " + ex);

            }
            return getValueAt(0, c).getClass();

        }

        public boolean isCellEditable(int row, int col) {
            // Note that the data/cell address is constant,
            // no matter where the cell appears onscreen.
            return false;
        }
    }

    public void setUpArmyColumn(TableColumn col) {
        col.setCellRenderer(new ArmyRenderer());
    }

    private void jbInit() throws Exception {

        setMaximumSize(new java.awt.Dimension(790, 500));
        setMinimumSize(new java.awt.Dimension(790, 500));
        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(790, 500));
        setLayout(null);
        pilotListSP.setBorder(BorderFactory.createTitledBorder("Pilot List"));

        refreshButton.setText("Refresh");
        refreshButton.setToolTipText("Refresh Pilot List");
        refreshButton.setBounds(450, 460, 75, 25);
        refreshButton.addActionListener(new RefreshListener());

        kickButton.setText("Kick");
        kickButton.setToolTipText("Kick Selected Pilot");
        kickButton.setBounds(530, 460, 75, 25);
        kickButton.addActionListener(new KickListener());

        banButton.setText("Ban");
        banButton.setToolTipText("Ban Selected Pilot");
        banButton.setBounds(615, 460, 75, 25);
        banButton.addActionListener(new BanListener());

        chatButton.setText("Send Chat");
        chatButton.setToolTipText("Send Chat to Selected Pilot or All");
        chatButton.setBounds(700, 460, 85, 25);
        chatButton.addActionListener(new SendTextListener());

        add(chatButton, null);
        add(pilotListTable, null);
        add(pilotListSP, null);
        add(banButton, null);
        add(kickButton, null);
        add(refreshButton, null);

        String[] columnHeader = { "Army", "Plane", "Markings", "Status", "Name", "IP Address", "Country", "Ping", "Admin" };
        pilotTableModel = new PilotTableModel(columnHeader);

        pilotListTable = new javax.swing.JTable(pilotTableModel);
        pilotListTable.setRowSelectionAllowed(true);
        pilotListTable.setOpaque(true);
        pilotListTable.setBackground(Color.cyan);
        pilotListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pilotSelection = pilotListTable.getSelectionModel();

        pilotListTable.setName("pilotListTable"); // NOI18N
        pilotListSP.setViewportView(pilotListTable);
        pilotListSP.setOpaque(true);
        setUpArmyColumn(pilotListTable.getColumn("Army"));

        add(pilotListSP);
        pilotListSP.setBounds(20, 20, 755, 380);
        pilotSelection.clearSelection();

    }

    class RefreshListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            PilotPanelController.refreshPilotPanel();
        }
    }

    class KickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int selectedRow = pilotListTable.getSelectedRow();
            // Make sure a valid pilot is selected
            if (selectedRow != -1) {
                // Confirm Pilot Kick
                String pilotToKick = (String) pilotListTable.getValueAt(selectedRow, pilotTableModel.findColumn("Name"));
                int n = JOptionPane.showConfirmDialog(null, "Kick Pilot ( " + pilotToKick + " ) ?", "Pilot Kick", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    // Yep there outa here
                    GUICommand newGUICommand = new GUICommand(GUICommand.GUICommandType.KICKPILOT);
                    newGUICommand.setName(pilotToKick);
                    MainController.doQueue(new QueueObj(QueueObj.Source.GUI, newGUICommand));
                    pilotTableModel.removeRow(selectedRow);
                    pilotSelection.clearSelection();
                }
            } else {
                JOptionPane.showMessageDialog(null, "No Pilot Selected", "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }  // End KickListener

    class BanListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int selectedRow = pilotListTable.getSelectedRow();
            // Make sure a valid pilot is selected
            if (selectedRow != -1) {
                // Ban pilot
                String pilotToBan = (String) pilotListTable.getValueAt(selectedRow, pilotTableModel.findColumn("Name"));
                String banDuration = (String) JOptionPane.showInputDialog(null, "Enter the Ban length in Hours (0 = Permanent Ban):\n", "Ban Pilot", JOptionPane.PLAIN_MESSAGE, null, null, null);
                try {
                    if ((banDuration != null) && (Double.valueOf(banDuration) >= 0)) {   // Got a good Ban Duration so Ban Pilot
                        GUICommand newGUICommand = new GUICommand(GUICommand.GUICommandType.ADDBAN);
                        newGUICommand.setName(pilotToBan);
                        newGUICommand.setIpAddress((String) pilotListTable.getValueAt(selectedRow, pilotTableModel.findColumn("IP Address")));
                        newGUICommand.setDuration(Math.round((Double.valueOf(banDuration) * 3600000)));
                        MainController.doQueue(new QueueObj(QueueObj.Source.GUI, newGUICommand));
                        pilotTableModel.removeRow(selectedRow);
                        pilotSelection.clearSelection();
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid Ban Duration:" + ex, "Message", JOptionPane.ERROR_MESSAGE);

                    // say number exception
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Invalid Ban Duration:" + ex, "Message", JOptionPane.ERROR_MESSAGE);
                    // say other exception
                }
            } else {
                JOptionPane.showMessageDialog(null, "No Pilot Selected", "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }   // End BanPilotListener

    class SendTextListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object[] options;
            int selectedRow = pilotListTable.getSelectedRow();
            // Make sure a valid pilot is selected
            if (selectedRow != -1) {
                // Get Selected Pilot if any
                @SuppressWarnings("unused")
                String pilotSelected = pilotListTable.getValueAt(selectedRow, pilotTableModel.findColumn("Name")).toString();
                options = new Object[] { "Selected Pilot", "ALL", "Cancel" };
            } else {
                options = new Object[] { "ALL", "Cancel" };
            }

            // Get Chat message to send
            String chatMessage = (String) JOptionPane.showInputDialog(null, "Enter Chat Message and Select Receipent", "Chat Message", JOptionPane.PLAIN_MESSAGE, null, null, null);

            if (chatMessage != null && chatMessage.length() > 0) {
                if (selectedRow != -1) {
                    // Ask to send to Pilot or All
                    String pilotSelected = pilotListTable.getValueAt(selectedRow, pilotTableModel.findColumn("Name")).toString();
                    options = new Object[] { "Selected Pilot", "ALL", "Cancel" };
                    int n = JOptionPane.showOptionDialog(null, "Send Chat Message to Selected Pilot, All, or Cancel ?", "Chat Message", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,     // do not use a custom Icon
                            options,  // the titles of buttons
                            options[0]); // default button title

                    if (n == JOptionPane.OK_OPTION) {   // Send to Selected Pilot
                        GUICommand newGUICommand = new GUICommand(GUICommand.GUICommandType.CHAT);
                        pilotSelected = UnicodeFormatter.convertUnicodeToString(pilotSelected);
                        newGUICommand.setValue("chat " + chatMessage + " TO \"" + pilotSelected + "\"");
                        MainController.doQueue(new QueueObj(QueueObj.Source.GUI, newGUICommand));
                    } else if (n == JOptionPane.NO_OPTION) {   // Send to All
                        GUICommand newGUICommand = new GUICommand(GUICommand.GUICommandType.CHAT);
                        newGUICommand.setValue("chat " + chatMessage + " TO ALL");
                        MainController.doQueue(new QueueObj(QueueObj.Source.GUI, newGUICommand));
                    }
                    pilotSelection.clearSelection();

                } else {   // No Pilot Selected so ask to send to All
                    int n = JOptionPane.showOptionDialog(null, "Send Chat Message to All ?", "Chat Message", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,     // do not use a custom Icon
                            null,  // the titles of buttons
                            null); // default button title

                    if (n == JOptionPane.YES_OPTION) {
                        GUICommand newGUICommand = new GUICommand(GUICommand.GUICommandType.CHAT);
                        newGUICommand.setValue("chat " + chatMessage + " TO ALL");
                        MainController.doQueue(new QueueObj(QueueObj.Source.GUI, newGUICommand));
                    }
                }
            }
        }
    }    // End SendTextListener

    public void addPilot(int army, String name, boolean isAdmin, String ipAddress, String state, String country, int ping, String plane, String markings) {
        String armyText;
        if (army == MainController.REDARMY)
            armyText = "Red";
        else if (army == MainController.BLUEARMY)
            armyText = "Blue";
        else
            armyText = "None";
        if (name == null) {
            name = "Unknown";
        }
        if (ipAddress == null) {
            ipAddress = "Unknown";
        }
        if (state == null) {
            state = "Unknown";
        }
        if (country == null) {
            country = "Unknown";
        }
        if (plane == null) {
            plane = "Unknown";
        }
        if (markings == null) {
            markings = "Unknown";
        }
        Object[] tempRow = { armyText, plane, markings, state, name, ipAddress, country, ping, isAdmin };
        pilotTableModel.addRow(tempRow);
    }

    public void updatePilot(String name, boolean isAdmin, int army, String plane, String markings, String state) {
        int pilotCount;
        int pilotRow = -1;
        String armyText;
        try {
            if (army == MainController.REDARMY)
                armyText = "Red";
            else if (army == MainController.BLUEARMY)
                armyText = "Blue";
            else
                armyText = "None";
            pilotCount = pilotTableModel.getRowCount();
            for (int i = 0; i < pilotCount; i++) {
                int pilotNameColumn = pilotTableModel.findColumn("Name");
                if (pilotNameColumn != -1) {
                    if (pilotTableModel.getValueAt(i, pilotNameColumn).equals(name)) {
                        pilotRow = i;
                        break;
                    }
                }
            }
            if (pilotRow >= 0) {
                pilotTableModel.setValueAt(isAdmin, pilotRow, pilotTableModel.findColumn("Admin"));
                pilotTableModel.setValueAt(armyText, pilotRow, pilotTableModel.findColumn("Army"));
                pilotTableModel.setValueAt(plane, pilotRow, pilotTableModel.findColumn("Plane"));
                pilotTableModel.setValueAt(markings, pilotRow, pilotTableModel.findColumn("Markings"));
                pilotTableModel.setValueAt(state, pilotRow, pilotTableModel.findColumn("Status"));
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "PilotsPanel.updatePilot - Error: " + ex);
        }
    }  // End updatePilot

    public void removePilot(String name) {
        int pilotCount;
        int pilotRow = -1;
        try {
            pilotCount = pilotTableModel.getRowCount();
            for (int i = 0; i < pilotCount; i++) {
                if (pilotTableModel.getValueAt(i, pilotTableModel.findColumn("Name")).equals(name)) {
                    pilotRow = i;
                    break;
                }
            }
            if (pilotRow >= 0) {
                pilotTableModel.removeRow(pilotRow);
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "PilotsPanel.removePilot - Error: " + ex);
        }
    }  // End removePilot

    public void clearPilotPanel() {
        int pilotCount = pilotTableModel.getRowCount();
        for (int i = 0; i < pilotCount; i++) {
            pilotTableModel.removeRow(0);
        }
    }
}
