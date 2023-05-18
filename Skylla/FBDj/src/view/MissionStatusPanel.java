package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import mainController.MainController;
import mainController.MissionStatusPanelController;
import model.Aerodrome;
import model.GUICommand;
import model.MissionTargetObjective;
import model.QueueObj;

public class MissionStatusPanel extends JPanel {

    /**
	 * 
	 */
    private static final long               serialVersionUID   = 1L;
    private static ArrayList<GUICommand>    missionGUICommands = new ArrayList<GUICommand>();
    private static boolean                  dataChanged        = false;

    private javax.swing.JButton             cancelButton;
    private javax.swing.JTable              loadoutRestrictionTable;
    private MissionStatusPanelTableModel    loadoutRestrictionTableModel;
    private javax.swing.JScrollPane         loadoutRestrictionsScrollPane;
    private javax.swing.JTable              missionCountObjectiveTable;
    private MissionStatusPanelTableModel    countObjectiveTableModel;
    private javax.swing.JScrollPane         missionCountObjectivesSPane;
    private javax.swing.JTextField          missionObjectiveTypeTF;
    private javax.swing.JLabel              missionStatusMinutesLabel;
    private javax.swing.JLabel              missionStatusMissionNameLabel;
    private javax.swing.JLabel              missionStatusStartTimeLabel;
    private javax.swing.JLabel              missionStatusStartTimeValueLabel;
    private javax.swing.JLabel              missionStatusTimeLeftLabel;
    private javax.swing.JFormattedTextField missionStatusTimeLeftTF;
    private javax.swing.JScrollPane         missionTargetObjectiveSPane;
    private javax.swing.JTable              missionTargetObjectiveTable;
    private MissionStatusPanelTableModel    targetObjectiveTableModel;
    private javax.swing.JLabel              objectiveTypeLabel;
    private javax.swing.JScrollPane         planeLimitScrollPane;
    private javax.swing.JTable              planeLimitTable;
    private MissionStatusPanelTableModel    planeLimitTableModel;
    private javax.swing.JButton             refreshButton;
    private javax.swing.JButton             saveButton;
    private javax.swing.JLabel              missionStatusMessageLabel;
    private javax.swing.JLabel              countObjectiveLabel;
    private javax.swing.JLabel              targetObjectiveLabel;
    private javax.swing.JFormattedTextField redCountObjectivesNeededTF;
    private javax.swing.JFormattedTextField blueCountObjectivesNeededTF;
    private javax.swing.JFormattedTextField redTargetObjectivesNeededTF;
    private javax.swing.JFormattedTextField blueTargetObjectivesNeededTF;

    public MissionStatusPanel() {
        initComponents();
    }

    public boolean isDataChanged() {
        return dataChanged;
    }

    class MissionStatusPanelTableModel extends DefaultTableModel {
        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;
        private static final int  ROW              = 0;
        private String            tableName;

        public MissionStatusPanelTableModel(String tableName, String[] columnNames) {
            super(columnNames, ROW);
            this.tableName = tableName;
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public Class getColumnClass(int columnIndex) {
            if (this.getRowCount() > 0)
                return this.getValueAt(0, columnIndex).getClass();
            else
                return Object.class;
        }

        public boolean isCellEditable(int row, int col) {
            // Only allow editing in the last cell
            // no matter if the user moved the cell to another position
            if (tableName.equals("PLANELIMIT")) {
                if (col == planeLimitTableModel.findColumn("In Use Limit") || col == planeLimitTableModel.findColumn("Total Limit"))
                    return true;
                else
                    return false;
            } else {
                if (col < (this.getColumnCount() - 1))
                    return false;
                else
                    return true;
            }
        }

        public String getTableName() {
            return tableName;
        }
    }  // End class MissionStatusPanelTableModel

    private void initComponents() {

        missionStatusMissionNameLabel = new javax.swing.JLabel();
        missionStatusStartTimeLabel = new javax.swing.JLabel();
        missionStatusStartTimeValueLabel = new javax.swing.JLabel();
        missionStatusTimeLeftLabel = new javax.swing.JLabel();
        missionStatusTimeLeftTF = new javax.swing.JFormattedTextField();
        missionStatusMinutesLabel = new javax.swing.JLabel();
        objectiveTypeLabel = new javax.swing.JLabel();
        missionObjectiveTypeTF = new javax.swing.JTextField();
        refreshButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        loadoutRestrictionsScrollPane = new javax.swing.JScrollPane();
        planeLimitScrollPane = new javax.swing.JScrollPane();
        missionCountObjectivesSPane = new javax.swing.JScrollPane();
        missionTargetObjectiveSPane = new javax.swing.JScrollPane();
        missionStatusMessageLabel = new javax.swing.JLabel();
        redCountObjectivesNeededTF = new JFormattedTextField();
        redTargetObjectivesNeededTF = new JFormattedTextField();
        blueCountObjectivesNeededTF = new JFormattedTextField();
        blueTargetObjectivesNeededTF = new JFormattedTextField();
        countObjectiveLabel = new javax.swing.JLabel("Count Objectives Needed");
        targetObjectiveLabel = new javax.swing.JLabel("Target Objectives Needed");

        setMaximumSize(new java.awt.Dimension(790, 500));
        setMinimumSize(new java.awt.Dimension(790, 500));
        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(790, 500));
        setLayout(null);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(view.MainWindowApp2.class).getContext().getResourceMap(MissionStatusPanel.class);
        missionStatusMissionNameLabel.setFont(resourceMap.getFont("missionStatusMissionNameLabel.font")); // NOI18N
        missionStatusMissionNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        missionStatusMissionNameLabel.setText(resourceMap.getString("missionStatusMissionNameLabel.text")); // NOI18N
        missionStatusMissionNameLabel.setFocusable(false);
        missionStatusMissionNameLabel.setName("missionStatusMissionNameLabel"); // NOI18N
        missionStatusMissionNameLabel.setBounds(300, 10, 180, 17);
        add(missionStatusMissionNameLabel);

        missionStatusStartTimeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        missionStatusStartTimeLabel.setText(resourceMap.getString("missionStatusStartTimeLabel.text")); // NOI18N
        missionStatusStartTimeLabel.setName("missionStatusStartTimeLabel"); // NOI18N
        missionStatusStartTimeLabel.setBounds(30, 10, 75, 14);
        add(missionStatusStartTimeLabel);

        missionStatusStartTimeValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        missionStatusStartTimeValueLabel.setText(resourceMap.getString("missionStatusStartTimeValueLabel.text")); // NOI18N
        missionStatusStartTimeValueLabel.setName("missionStatusStartTimeValueLabel"); // NOI18N
        missionStatusStartTimeValueLabel.setBounds(105, 10, 140, 14);
        add(missionStatusStartTimeValueLabel);

        missionStatusTimeLeftLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        missionStatusTimeLeftLabel.setText(resourceMap.getString("missionStatusTimeLeftLabel.text")); // NOI18N
        missionStatusTimeLeftLabel.setName("missionStatusTimeLeftLabel"); // NOI18N
        missionStatusTimeLeftLabel.setBounds(640, 10, 45, 14);

        add(missionStatusTimeLeftLabel);

        missionStatusTimeLeftTF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        missionStatusTimeLeftTF.setText(resourceMap.getString("missionStatusTimeLeftTF.text")); // NOI18N
        missionStatusTimeLeftTF.setName("missionStatusTimeLeftTF"); // NOI18N
        missionStatusTimeLeftTF.setBounds(690, 7, 34, 20);
        missionStatusTimeLeftTF.addActionListener(new DataChangeListener());
        add(missionStatusTimeLeftTF);

        missionStatusMinutesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        missionStatusMinutesLabel.setText(resourceMap.getString("missionStatusMinutesLabel.text")); // NOI18N
        missionStatusMinutesLabel.setName("missionStatusMinutesLabel"); // NOI18N
        missionStatusMinutesLabel.setBounds(730, 10, 37, 14);
        add(missionStatusMinutesLabel);

        countObjectiveLabel.setBounds(30, 37, 150, 14);
        javax.swing.JLabel redCountLabel = new javax.swing.JLabel("Red:");
        redCountLabel.setBounds(35, 55, 50, 14);
        javax.swing.JLabel blueCountLabel = new javax.swing.JLabel("Blue:");
        blueCountLabel.setBounds(150, 55, 50, 14);
        redCountObjectivesNeededTF.setValue(new Integer(0));
        redCountObjectivesNeededTF.setToolTipText("Enter the number of Count Objectives Red needs to win the mission");
        redCountObjectivesNeededTF.setBounds(65, 54, 34, 17);
        redCountObjectivesNeededTF.addActionListener(new DataChangeListener());

        blueCountObjectivesNeededTF.setValue(new Integer(0));
        blueCountObjectivesNeededTF.setToolTipText("Enter the number of Count Objectives Blue needs to win the mission");
        blueCountObjectivesNeededTF.setBounds(180, 54, 34, 17);
        blueCountObjectivesNeededTF.addActionListener(new DataChangeListener());
        add(redCountLabel);
        add(blueCountLabel);
        add(redCountObjectivesNeededTF);
        add(blueCountObjectivesNeededTF);

        add(countObjectiveLabel);
        targetObjectiveLabel.setBounds(410, 40, 150, 14);
        javax.swing.JLabel redTargetLabel = new javax.swing.JLabel("Red:");
        redTargetLabel.setBounds(420, 55, 50, 14);
        javax.swing.JLabel blueTargetLabel = new javax.swing.JLabel("Blue:");
        blueTargetLabel.setBounds(537, 55, 50, 14);
        add(redTargetLabel);
        add(blueTargetLabel);
        redTargetObjectivesNeededTF.setValue(new Integer(0));
        redTargetObjectivesNeededTF.setToolTipText("Enter the number of Count Objectives Red needs to win the mission");
        redTargetObjectivesNeededTF.setBounds(446, 54, 34, 17);
        redTargetObjectivesNeededTF.addActionListener(new DataChangeListener());

        blueTargetObjectivesNeededTF.setValue(new Integer(0));
        blueTargetObjectivesNeededTF.setToolTipText("Enter the number of Count Objectives Blue needs to win the mission");
        blueTargetObjectivesNeededTF.setBounds(565, 54, 34, 17);
        blueTargetObjectivesNeededTF.addActionListener(new DataChangeListener());
        add(redTargetObjectivesNeededTF);
        add(blueTargetObjectivesNeededTF);
        add(targetObjectiveLabel);

        objectiveTypeLabel.setText(resourceMap.getString("objectiveTypeLabel.text")); // NOI18N
        objectiveTypeLabel.setName("objectiveTypeLabel"); // NOI18N
        objectiveTypeLabel.setBounds(615, 40, 77, 14);
        add(objectiveTypeLabel);

        missionObjectiveTypeTF.setEditable(false);
        missionObjectiveTypeTF.setText(resourceMap.getString("missionObjectiveTypeTF.text")); // NOI18N
        missionObjectiveTypeTF.setName("missionObjectiveTypeTF"); // NOI18N
        missionObjectiveTypeTF.setBounds(695, 37, 100, 20);
        add(missionObjectiveTypeTF);

        refreshButton.setText(resourceMap.getString("refreshButton.text")); // NOI18N
        refreshButton.setName("refreshButton"); // NOI18N
        refreshButton.addActionListener(new RefreshButtonListener());
        add(refreshButton);
        refreshButton.setBounds(550, 460, 75, 25);

        saveButton.setText(resourceMap.getString("saveButton.text")); // NOI18N
        saveButton.setName("saveButton"); // NOI18N
        saveButton.setOpaque(true);
        saveButton.addActionListener(new SaveButtonListener());
        add(saveButton);
        saveButton.setBounds(630, 460, 75, 25);

        cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new CancelButtonListener());
        add(cancelButton);
        cancelButton.setBounds(710, 460, 75, 25);

        loadoutRestrictionsScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("loadoutRestrictionsScrollPane.border.title"))); // NOI18N
        loadoutRestrictionsScrollPane.setName("loadoutRestrictionsScrollPane"); // NOI18N

        String[] columnHeader = { "Army", "Plane", "Restricted Weapon" };
        loadoutRestrictionTableModel = new MissionStatusPanelTableModel("LOADOUTRESTRICTION", columnHeader);
        loadoutRestrictionTable = new javax.swing.JTable(loadoutRestrictionTableModel);
        loadoutRestrictionTable.setOpaque(true);
        loadoutRestrictionTable.setBackground(Color.cyan);
        loadoutRestrictionTable.setName("loadoutRestrictionTable"); // NOI18N
        loadoutRestrictionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loadoutRestrictionsScrollPane.setViewportView(loadoutRestrictionTable);
        loadoutRestrictionsScrollPane.setOpaque(true);
        setUpArmyColumn(loadoutRestrictionTable.getColumnModel().getColumn(0));

        add(loadoutRestrictionsScrollPane);
        loadoutRestrictionsScrollPane.setBounds(5, 305, 392, 140);

        planeLimitScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("planeLimitScrollPane.border.title"))); // NOI18N
        planeLimitScrollPane.setName("planeLimitScrollPane"); // NOI18N

        columnHeader = null;
        columnHeader = new String[] { "Aerodrome", "Plane", "# Lost", "# in Use", "In Use Limit", "Total Limit", "planeLimitId" };
        planeLimitTableModel = new MissionStatusPanelTableModel("PLANELIMIT", columnHeader);
        planeLimitTableModel.addTableModelListener(new PlaneLimitTableUpdate());
        planeLimitTable = new javax.swing.JTable(planeLimitTableModel);
        planeLimitTable.setOpaque(true);
        planeLimitTable.setBackground(Color.cyan);
        planeLimitTable.setName("planeLimitTable"); // NOI18N
        planeLimitTable.removeColumn(planeLimitTable.getColumn("planeLimitId"));
        planeLimitTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        planeLimitScrollPane.setViewportView(planeLimitTable);
        planeLimitScrollPane.setOpaque(true);
        setUpArmyColumn(planeLimitTable.getColumnModel().getColumn(0));

        add(planeLimitScrollPane);
        planeLimitScrollPane.setBounds(402, 305, 392, 140);

        missionCountObjectivesSPane.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("missionCountObjectivesSPane.border.title"))); // NOI18N
        missionCountObjectivesSPane.setName("missionCountObjectivesSPane"); // NOI18N

        columnHeader = null;
        columnHeader = new String[] { "Army", "Objective", "# Start", "# Dest", "# To Win" };
        countObjectiveTableModel = new MissionStatusPanelTableModel("COUNTOBJECTIVE", columnHeader);
        countObjectiveTableModel.addTableModelListener(new CountTableUpdate());
        missionCountObjectiveTable = new javax.swing.JTable(countObjectiveTableModel);
        missionCountObjectiveTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        missionCountObjectiveTable.setName("missionCountObjectiveTable"); // NOI18N
        missionCountObjectiveTable.setOpaque(true);
        missionCountObjectiveTable.setBackground(Color.cyan);
        missionCountObjectiveTable.setAutoCreateRowSorter(true);
        missionCountObjectiveTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        missionCountObjectivesSPane.setViewportView(missionCountObjectiveTable);
        missionCountObjectivesSPane.setOpaque(true);
        setUpArmyColumn(missionCountObjectiveTable.getColumnModel().getColumn(0));

        add(missionCountObjectivesSPane);
        missionCountObjectivesSPane.setBounds(5, 70, 392, 225);

        missionTargetObjectiveSPane.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("missionTargetObjectiveSPane.border.title"))); // NOI18N
        missionTargetObjectiveSPane.setName("missionTargetObjectiveSPane"); // NOI18N

        columnHeader = new String[] { "Army", "MapGrid", "Description", "# Start", "# Dest", "# To Win", "targetObjectiveId" };
        targetObjectiveTableModel = new MissionStatusPanelTableModel("TARGETOBJECTIVE", columnHeader);
        targetObjectiveTableModel.addTableModelListener(new TargetTableUpdate());

        missionTargetObjectiveTable = new javax.swing.JTable(targetObjectiveTableModel);
        missionTargetObjectiveTable.setName("missionTargetObjectiveTable"); // NOI18N
        missionTargetObjectiveTable.setOpaque(true);
        missionTargetObjectiveTable.setBackground(Color.cyan);
        missionTargetObjectiveTable.removeColumn(missionTargetObjectiveTable.getColumn("targetObjectiveId"));
        missionTargetObjectiveTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        missionTargetObjectiveSPane.setViewportView(missionTargetObjectiveTable);
        missionTargetObjectiveSPane.setOpaque(true);
        setUpArmyColumn(missionTargetObjectiveTable.getColumnModel().getColumn(0));

        add(missionTargetObjectiveSPane);
        missionTargetObjectiveSPane.setBounds(402, 70, 392, 225);

        missionStatusMessageLabel.setBounds(100, 460, 400, 20);
        add(missionStatusMessageLabel);

    }// </editor-fold>//GEN-END:initComponents

    class DataChangeListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            GUICommand newGUICommand = null;
            if (e.getSource().equals(missionStatusTimeLeftTF)) {
                newGUICommand = new GUICommand(GUICommand.GUICommandType.ADJTIMELEFT);
                newGUICommand.setValue(missionStatusTimeLeftTF.getValue());
            } else if (e.getSource().equals(redCountObjectivesNeededTF)) {
                newGUICommand = new GUICommand(GUICommand.GUICommandType.ADJREDCNTOBJNEEDED);
                newGUICommand.setValue(redCountObjectivesNeededTF.getValue());
            } else if (e.getSource().equals(blueCountObjectivesNeededTF)) {
                newGUICommand = new GUICommand(GUICommand.GUICommandType.ADJBLUECNTOBJNEEDED);
                newGUICommand.setValue(blueCountObjectivesNeededTF.getValue());
            } else if (e.getSource().equals(redTargetObjectivesNeededTF)) {
                newGUICommand = new GUICommand(GUICommand.GUICommandType.ADJREDTGTOBJNEEDED);
                newGUICommand.setValue(redTargetObjectivesNeededTF.getValue());
            } else if (e.getSource().equals(blueTargetObjectivesNeededTF)) {
                newGUICommand = new GUICommand(GUICommand.GUICommandType.ADJBLUETGTOBJNEEDED);
                newGUICommand.setValue(blueTargetObjectivesNeededTF.getValue());
            }
            if (newGUICommand != null) {
                dataChanged = true;
                missionGUICommands.add(newGUICommand);
                missionStatusMessageLabel.setText("Data has Changed, Click Save to commit");
            }
        }
    }

//	private void timeLeftChanged(ActionEvent e)
//	{
//		dataChanged = true;
//		GUICommand newGUICommand = new GUICommand(GUICommand.GUICommandType.ADJTIMELEFT);
//		newGUICommand.setValue(Integer.valueOf(missionStatusTimeLeftTF.getText().toString()));
//		missionGUICommands.add(newGUICommand);
//		missionStatusMessageLabel.setText("TimeLeft has Changed, Click Save to commit");
//	}

    class RefreshButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            MissionStatusPanelController.initializeData(null);
        }
    }

    class SaveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            for (GUICommand guiCommand : missionGUICommands) {
                try {
                    // Add data to the main thread queue
                    MainController.doQueue(new QueueObj(QueueObj.Source.GUI, guiCommand));
                } catch (Exception ex) {}
            }
            missionGUICommands.clear();
            dataChanged = false;
            missionStatusMessageLabel.setText(" ");
            planeLimitTable.clearSelection();
            loadoutRestrictionTable.clearSelection();
            missionCountObjectiveTable.clearSelection();
            missionTargetObjectiveTable.clearSelection();
        }
    }// GEN-LAST:event_saveButtonMouseClicked

    class CancelButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (missionGUICommands.size() > 0) {
                // GUI Commands are waiting to be sent so Confirm Cancel
                int n = JOptionPane.showConfirmDialog(null, "Are You Sure ?", "Cancel Updates", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) { // Yes they want to edit it
                    missionGUICommands.clear();
                    dataChanged = false;
                    missionStatusMessageLabel.setText(" ");
                    planeLimitTable.clearSelection();
                    loadoutRestrictionTable.clearSelection();
                    missionCountObjectiveTable.clearSelection();
                    missionTargetObjectiveTable.clearSelection();
                }
            }
        }
    }// GEN-LAST:event_saveButtonMouseClicked

    class CountTableUpdate implements TableModelListener {
        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE) {
                int army;
                int newObjectiveValue = (Integer) countObjectiveTableModel.getValueAt(e.getFirstRow(), e.getColumn());
                String name = (String) countObjectiveTableModel.getValueAt(e.getFirstRow(), 1);
                int startValue = (Integer) countObjectiveTableModel.getValueAt(e.getFirstRow(), 2);
                // Make sure # to destroy is not greater than the number they started with. Does not count
                // for PLANE or PILOT objectives since there is no start # for those
                if (newObjectiveValue <= startValue || name.equals("PILOT") || name.equals("PLANE")) {
                    String armyText = (String) countObjectiveTableModel.getValueAt(e.getFirstRow(), 0);

                    if (armyText.equals("Red"))
                        army = MainController.REDARMY;
                    else
                        army = MainController.BLUEARMY;

                    dataChanged = true;
                    GUICommand newGUICommand = new GUICommand(GUICommand.GUICommandType.ADJCOUNTOBJECTIVE);
                    newGUICommand.setValue(newObjectiveValue);
                    newGUICommand.setArmy(army);
                    newGUICommand.setName(name);
                    missionGUICommands.add(newGUICommand);
                    missionStatusMessageLabel.setText("Count Objective Changed, Click Save to commit");
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot Set # to Win(" + newObjectiveValue + ") more than # Start(" + startValue + ")", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    class TargetTableUpdate implements TableModelListener {
        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE) {
                int army;
                String armyText = (String) targetObjectiveTableModel.getValueAt(e.getFirstRow(), 0);
                int newObjectiveValue = (Integer) targetObjectiveTableModel.getValueAt(e.getFirstRow(), targetObjectiveTableModel.findColumn("# To Win"));
                int startValue = (Integer) targetObjectiveTableModel.getValueAt(e.getFirstRow(), targetObjectiveTableModel.findColumn("# Start"));
                MissionTargetObjective targetObjective = (MissionTargetObjective) targetObjectiveTableModel.getValueAt(e.getFirstRow(), targetObjectiveTableModel.findColumn("targetObjectiveId"));
                // Make sure # to destroy is not greater than the number they started with.
                if (newObjectiveValue <= startValue) {
                    if (armyText.equals("Red"))
                        army = MainController.REDARMY;
                    else
                        army = MainController.BLUEARMY;

                    dataChanged = true;
                    GUICommand newGUICommand = new GUICommand(GUICommand.GUICommandType.ADJTARGETOBJECTIVE);
                    newGUICommand.setValue(newObjectiveValue);
                    newGUICommand.setArmy(army);
                    newGUICommand.setChangeObject(targetObjective);
                    missionGUICommands.add(newGUICommand);
                    missionStatusMessageLabel.setText("Target Objective Changed, Click Save to commit");
//					System.out.println("New Value: "+newObjectiveValue);
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot Set # to Win(" + newObjectiveValue + ") more than # Start(" + startValue + ")", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        }
    }

    class PlaneLimitTableUpdate implements TableModelListener {
        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE) {
                Aerodrome aerodrome = (Aerodrome) planeLimitTableModel.getValueAt(e.getFirstRow(), planeLimitTableModel.findColumn("planeLimitId"));
                int newLimit = (Integer) planeLimitTableModel.getValueAt(e.getFirstRow(), e.getColumn());
                String columnChanged = planeLimitTableModel.getColumnName(e.getColumn());
                String plane = (String) planeLimitTableModel.getValueAt(e.getFirstRow(), planeLimitTableModel.findColumn("Plane"));
                int inUseLimit = (Integer) planeLimitTableModel.getValueAt(e.getFirstRow(), planeLimitTableModel.findColumn("In Use Limit"));
                int totalLimit = (Integer) planeLimitTableModel.getValueAt(e.getFirstRow(), planeLimitTableModel.findColumn("Total Limit"));
                if (totalLimit > 0 && inUseLimit <= totalLimit) {
                    dataChanged = true;
                    GUICommand newGUICommand = new GUICommand(GUICommand.GUICommandType.ADJPLANELIMIT);
                    newGUICommand.setValue(newLimit);
                    newGUICommand.setName(plane);
                    newGUICommand.setIpAddress(columnChanged);
                    newGUICommand.setChangeObject(aerodrome);
                    missionGUICommands.add(newGUICommand);

                    missionStatusMessageLabel.setText("Plane Limit Changed, Click Save to commit");
//					System.out.println("New "+ columnChanged + " Value: "+newLimit);
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot Set In Use Limit(" + inUseLimit + ") more than Total Limit(" + totalLimit + ")", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public void setUpArmyColumn(TableColumn col) {
        col.setCellRenderer(new ArmyRenderer());
    }

    public javax.swing.JLabel getMissionStatusMissionNameLabel() {
        return missionStatusMissionNameLabel;
    }

    public void setMissionStatusMissionNameLabel(String value) {
        this.missionStatusMissionNameLabel.setText(value);
    }

    public javax.swing.JLabel getMissionStatusStartTimeValueLabel() {
        return missionStatusStartTimeValueLabel;
    }

    public void setMissionStatusStartTimeValueLabel(String value) {
        this.missionStatusStartTimeValueLabel.setText(value);
    }

    public javax.swing.JTextField getMissionStatusTimeLeftTF() {
        return missionStatusTimeLeftTF;
    }

    public void setMissionStatusTimeLeftTF(String value) {
        this.missionStatusTimeLeftTF.setText(value);
    }

    public void setMissionObjectiveTypeTF(String value) {
        this.missionObjectiveTypeTF.setText(value);
    }

    public void setRedCountObjectivesNeededTF(Integer value) {
        this.redCountObjectivesNeededTF.setValue(value);
    }

    public void setBlueCountObjectivesNeededTF(Integer value) {
        this.blueCountObjectivesNeededTF.setValue(value);
    }

    public void setRedTargetObjectivesNeededTF(Integer value) {
        this.redTargetObjectivesNeededTF.setValue(value);
    }

    public void setBlueTargetObjectivesNeededTF(Integer value) {
        this.blueTargetObjectivesNeededTF.setValue(value);
    }

    public void addObjectiveTableRow(String tableName, int army, String objective, String targetDesc, int startCount, int lostCount, int winCount) {
        String armyString;
        if (army == MainController.REDARMY) {
            armyString = "Red";
        } else {
            armyString = "Blue";
        }
        if (tableName.equals("COUNT")) {
            countObjectiveTableModel.addRow(new Object[] { armyString, objective, startCount, lostCount, winCount });
        } else if (tableName.equals("TARGET")) {
            targetObjectiveTableModel.addRow(new Object[] { armyString, objective, targetDesc, startCount, lostCount, winCount });
        }
    }

    public void addLoadoutRestrictionRow(int army, String plane, String restrictedWeapon) {
        String armyString;
        if (army == MainController.REDARMY) {
            armyString = "Red";
        } else {
            armyString = "Blue";
        }
        loadoutRestrictionTableModel.addRow(new Object[] { armyString, plane, restrictedWeapon });
    }

    public void addPlaneLimitRow(String aerodrome, String plane, int planesLostCount, int planesInUseCount, int inUseLimit, int totalLimit, Aerodrome planeLimitId) {
        planeLimitTableModel.addRow(new Object[] { (String) aerodrome, (String) plane, (Integer) planesLostCount, (Integer) planesInUseCount, (Integer) inUseLimit, (Integer) totalLimit, (Aerodrome) planeLimitId });
    }

    public void updateObjectiveTableRow(String objectiveType, int row, int lostCount) {
        try {

            if (objectiveType.equals("COUNT")) {
                countObjectiveTableModel.setValueAt(lostCount, row, 3);
            } else {
                targetObjectiveTableModel.setValueAt(lostCount, row, 3);
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionStatusPanel.updateObjectiveTableRow - Error Updating: " + objectiveType + " Row " + row + " Col ");
        }
    }

    public void updatePlaneLimtRow(int row, int planesLostCount, int planesInUseCount) {
        planeLimitTableModel.setValueAt(planesLostCount, row, planeLimitTableModel.findColumn("# Lost"));
        planeLimitTableModel.setValueAt(planesInUseCount, row, planeLimitTableModel.findColumn("# In Use"));
    }

    public void removeObjectiveTableData() {
        missionStatusMissionNameLabel.setText("No Mission Running");
        missionStatusTimeLeftTF.setValue(0);
        redCountObjectivesNeededTF.setValue(0);
        blueCountObjectivesNeededTF.setValue(0);
        redTargetObjectivesNeededTF.setValue(0);
        blueTargetObjectivesNeededTF.setValue(0);
        missionObjectiveTypeTF.setText(" ");
        missionStatusStartTimeValueLabel.setText(" ");

        // Clear out the Count Objective Table
        int rowCount = countObjectiveTableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            countObjectiveTableModel.removeRow(0);
        }
        // Clear out the Target Objective Table
        rowCount = targetObjectiveTableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            targetObjectiveTableModel.removeRow(0);
        }
        // Clear out the Loadout Restriction Table
        rowCount = loadoutRestrictionTableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            loadoutRestrictionTableModel.removeRow(0);
        }

        // Clear out the Plane Limit Table
        rowCount = planeLimitTableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            planeLimitTableModel.removeRow(0);
        }

        // Since we have removed the data from this screen we need to clear out any change
        // commands and set dataChanged to false
        missionGUICommands.clear();
        dataChanged = false;
        missionStatusMessageLabel.setText(" ");
    }

    public static void setDataChanged(boolean dataChanged) {
        MissionStatusPanel.dataChanged = dataChanged;
    }

    public void displayErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error Message", JOptionPane.ERROR_MESSAGE);
    }

}
