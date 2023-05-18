package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import mainController.MainController;
import model.DifficultySettings;
import model.GUICommand;
import model.MissionCycle;
import model.MissionCycleEntry;
import model.MissionFile;
import model.QueueObj;
import model.UserMissionCycles;
import viewController.MissionCycleController;
import viewController.ViewConfigurationController;

public class MissionCyclePanel extends JPanel {

    /**
     * 
     */
    private static final long        serialVersionUID      = -7482640429422298004L;

    private static boolean           dataChanged           = false;

    private static UserMissionCycles userMissionCycles;
    private String                   missionFileName;
    private String                   missionDirectory;
    private javax.swing.JTable       missionFilesTable;
    private MissionCycleTableModel   missionFilesTableModel;
    private javax.swing.JTable       missionCycleEntryTable;
    private MissionCycleTableModel   missionCycleEntryModel;
    private ListSelectionModel       missionCycleEntrySelection;
    private javax.swing.JScrollPane  missionCycleEntryScrollPane;
    private javax.swing.JButton      missionCycleEntryRemoveButton;
    private javax.swing.JButton      missionCycleEntryUpButton;
    private javax.swing.JButton      missionCycleEntryDownButton;
    private javax.swing.JScrollPane  missionFilesScrollPane;
    private javax.swing.JButton      missionFilesAddButton;
    private javax.swing.JButton      missionFilesRemoveButton;
    private javax.swing.JButton      missionFilesRightButton;
    private javax.swing.JButton      writeMissionFilesButton;
    private javax.swing.JButton      writeMissionCyclesButton;
    private ListSelectionModel       missionFilesSelection;
    private javax.swing.JFileChooser missionFileChooser;
    private javax.swing.JScrollPane  missionCycleScrollPane;
    @SuppressWarnings("rawtypes")
    private static DefaultListModel  missionCycleListModel;
    @SuppressWarnings("rawtypes")
    private javax.swing.JList        missionCycleList;
    private javax.swing.JButton      missionCycleAddButton;
    private javax.swing.JButton      missionCycleRemoveButton;
    private String                   selectedMissionCycle;
    private javax.swing.JScrollPane  difficultyScrollPane;
    @SuppressWarnings("rawtypes")
    private javax.swing.JList        difficultyList;
    @SuppressWarnings("rawtypes")
    private DefaultListModel         difficultyListModel;
    private String                   selectedDifficulty;
    private javax.swing.JButton      difficultyAddButton;
    private javax.swing.JButton      difficultyRemoveButton;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox    difficultyComboBox;
    private javax.swing.JScrollPane  difficultySettingsScrollPane;
    private MissionCycleTableModel   difficultySettingsTableModel;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox    availableMissionComboBox;
    @SuppressWarnings("rawtypes")
    private javax.swing.JList        availableMissionList;
    @SuppressWarnings("rawtypes")
    private DefaultListModel         availableMissionListModel;

    private javax.swing.JTable       difficultySettingsTable;

    private javax.swing.JButton      resetMissionCycleButton;
    private javax.swing.JButton      runTempMissionButton;
    private javax.swing.JButton      saveButton;
    private javax.swing.JButton      cancelButton;
    private File                     missionStartDirectory = null;

    public MissionCyclePanel() {
        initComponents();
    }

    public boolean isDataChanged() {
        return dataChanged;
    }

    class MissionCycleTableModel extends DefaultTableModel {

        /**
         * 
         */
        private static final long serialVersionUID = -4321921329317546835L;
        private static final int  ROW              = 0;
        private String            tableName;

        public MissionCycleTableModel(String tableName, String[] columnNames) {
            super(columnNames, ROW);
            this.tableName = tableName;
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            // Only allow editing in the last cell
            // Unless the table is MissionCycleEntry where they can edit the last 2 cells

            if (this.tableName.equals("MISSIONFILES")) {
                if (col < this.getColumnCount() - 3)
                    return false;
                else
                    return true;
            } else if (this.tableName.equals("DIFFICULTYSETTINGS")) {
                if (col < this.getColumnCount() - 1)
                    return false;
                else
                    return true;
            } else
                return false;

        }

        public String getTableName() {
            return tableName;
        }

    }  // End class MissionStatusPanelTableModel

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void initComponents() {
        resetMissionCycleButton = new javax.swing.JButton();
        runTempMissionButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        missionFilesScrollPane = new javax.swing.JScrollPane();
        missionCycleEntryScrollPane = new javax.swing.JScrollPane();
        missionFilesAddButton = new javax.swing.JButton();
        missionFilesRemoveButton = new javax.swing.JButton();
        missionFilesRightButton = new javax.swing.JButton();
        missionCycleEntryRemoveButton = new javax.swing.JButton();
        missionCycleEntryUpButton = new javax.swing.JButton();
        missionCycleEntryDownButton = new javax.swing.JButton();
        missionCycleScrollPane = new javax.swing.JScrollPane();
        missionCycleAddButton = new javax.swing.JButton();
        missionCycleRemoveButton = new javax.swing.JButton();
        difficultyScrollPane = new javax.swing.JScrollPane();
        difficultyComboBox = new javax.swing.JComboBox();
        difficultyAddButton = new javax.swing.JButton();
        difficultyRemoveButton = new javax.swing.JButton();
        difficultySettingsScrollPane = new javax.swing.JScrollPane();
        writeMissionCyclesButton = new javax.swing.JButton();
        writeMissionFilesButton = new javax.swing.JButton();
        availableMissionComboBox = new javax.swing.JComboBox();

        userMissionCycles = MissionCycleController.loadMissionCycleList();

        MissionCycleController.verifyCurrentMissionFiles();
        MissionCycleController.addNewMissions(userMissionCycles);

        setMaximumSize(new java.awt.Dimension(790, 500));
        setMinimumSize(new java.awt.Dimension(790, 500));
        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(790, 500));
        setLayout(null);

        // ************** Mission File Section ******************************
        String[] columnHeader = { "Name", "Directory", "Difficulty Setup", "Red Won", "Blue Won" };
        missionFilesTableModel = new MissionCycleTableModel("MISSIONFILES", columnHeader);
        missionFilesTableModel.addTableModelListener(new MissionFilesTableUpdate());
        missionFilesTable = new javax.swing.JTable(missionFilesTableModel);
        missionFilesTable.setOpaque(true);
        missionFilesTable.setBackground(Color.cyan);
        missionFilesTable.setName("missionFilesTable"); // NOI18N
        missionFilesTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        missionFilesTable.getColumn("Difficulty Setup").setCellEditor(new DefaultCellEditor(difficultyComboBox));
        missionFilesTable.getColumn("Red Won").setCellEditor(new DefaultCellEditor(availableMissionComboBox));
        missionFilesTable.getColumn("Blue Won").setCellEditor(new DefaultCellEditor(availableMissionComboBox));
        missionFilesSelection = missionFilesTable.getSelectionModel();
        missionFilesSelection.clearSelection();

        // Setup Tool Tips for Column Headers
        JTableHeader header = missionFilesTable.getTableHeader();
        ColumnHeaderToolTips tips = new ColumnHeaderToolTips();
        tips.setToolTip(missionFilesTable.getColumnModel().getColumn(0), "Mission/File Name");
        tips.setToolTip(missionFilesTable.getColumnModel().getColumn(1), "Directory under IL2/Missions where mission resides");
        tips.setToolTip(missionFilesTable.getColumnModel().getColumn(2), "Difficulty to set for mission");
        tips.setToolTip(missionFilesTable.getColumnModel().getColumn(3), "Mission to run if Red Wins");
        tips.setToolTip(missionFilesTable.getColumnModel().getColumn(4), "Mission to run if Blue Wins");
        header.addMouseMotionListener(tips);

        missionFilesScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Available Mission Files")); // NOI18N
        missionFilesScrollPane.setName("missionFilesScrollPane"); // NOI18N
        missionFilesScrollPane.setViewportView(missionFilesTable);
        missionFilesScrollPane.setOpaque(true);
        add(missionFilesScrollPane);
        missionFilesScrollPane.setBounds(10, 10, 340, 440);

        missionFilesAddButton.setIcon(new javax.swing.ImageIcon("Images/add.png"));
        missionFilesAddButton.setName("addButton"); // NOI18N
        missionFilesAddButton.setToolTipText("Add a new Mission to Available Mission Files");
        missionFilesAddButton.addActionListener(new AddMissionFileButtonListener());
        add(missionFilesAddButton);
        missionFilesAddButton.setBounds(355, 20, 25, 25);

        missionFilesRemoveButton.setIcon(new javax.swing.ImageIcon("Images/delete.png"));
        missionFilesRemoveButton.setName("RemoveButton"); // NOI18N
        missionFilesRemoveButton.setToolTipText("Remove Selected Mission from the Available Mission Files");
        missionFilesRemoveButton.addActionListener(new RemoveMissionFileButtonListener());
        add(missionFilesRemoveButton);
        missionFilesRemoveButton.setBounds(355, 50, 25, 25);

        missionFilesRightButton.setIcon(new javax.swing.ImageIcon("Images/right.png"));
        missionFilesRightButton.setName("RightButton"); // NOI18N
        missionFilesRightButton.setActionCommand("Right");
        missionFilesRightButton.setToolTipText("Move Selected Missions into the Mission Cycle");
        missionFilesRightButton.addActionListener(new MissionFilesMoveListener());
        add(missionFilesRightButton);
        missionFilesRightButton.setBounds(355, 195, 25, 25);

        availableMissionListModel = new DefaultListModel();
        availableMissionList = new javax.swing.JList(availableMissionListModel);
        availableMissionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        availableMissionList.addListSelectionListener( new DifficultyListListener() );
        availableMissionList.setModel(availableMissionComboBox.getModel());

        // ****************** Mission Cycle Section *****************************
        missionCycleListModel = new DefaultListModel();
        missionCycleListModel.addListDataListener(new MissionCycleListModelListener());
        missionCycleList = new javax.swing.JList(missionCycleListModel);
        missionCycleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        missionCycleList.setSelectedIndex(0);
        missionCycleList.addListSelectionListener(new MissionCycleListListener());

        missionCycleScrollPane.setViewportView(missionCycleList);
        missionCycleScrollPane.setOpaque(true);
        missionCycleScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Mission Cycle List")); // NOI18N
        missionCycleScrollPane.setName("missionCycleScrollPane"); // NOI18N
        add(missionCycleScrollPane);
        missionCycleScrollPane.setBounds(395, 10, 150, 75);

        missionCycleAddButton.setIcon(new javax.swing.ImageIcon("Images/add.png"));
        missionCycleAddButton.setName("missionCycleAddButton"); // NOI18N
        missionCycleAddButton.setToolTipText("Add a new Mission Cycle");
        missionCycleAddButton.addActionListener(new AddMissionCycleButtonListener());
        add(missionCycleAddButton);
        missionCycleAddButton.setBounds(550, 20, 25, 25);

        missionCycleRemoveButton.setIcon(new javax.swing.ImageIcon("Images/delete.png"));
        missionCycleRemoveButton.setName("missionCycleRemoveButton"); // NOI18N
        missionCycleRemoveButton.setToolTipText("Remove Selected Mission Cycle");
        missionCycleRemoveButton.addActionListener(new RemoveMissionCycleListener());
        add(missionCycleRemoveButton);
        missionCycleRemoveButton.setBounds(550, 50, 25, 25);

        // ******************** Mission Cycle Entry Section **********************

        String[] entriesColumnHeader = { "Mission Name" };
        missionCycleEntryModel = new MissionCycleTableModel("MISSIONCYCLEENTRIES", entriesColumnHeader);
        missionCycleEntryModel.addTableModelListener(new MissionCycleEntryTableUpdate());
        missionCycleEntryTable = new javax.swing.JTable(missionCycleEntryModel);
        missionCycleEntryTable.setOpaque(true);
        missionCycleEntryTable.setBackground(Color.cyan);
        missionCycleEntryTable.setName("missionCycleEntryTable"); // NOI18N
        missionCycleEntryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        missionCycleEntrySelection = missionCycleEntryTable.getSelectionModel();
        missionCycleEntrySelection.clearSelection();

        missionCycleEntryScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Mission Cycle Missions")); // NOI18N
        missionCycleEntryScrollPane.setName("missionCycleEntryScrollPane"); // NOI18N
        missionCycleEntryScrollPane.setViewportView(missionCycleEntryTable);
        missionCycleEntryScrollPane.setOpaque(true);
        add(missionCycleEntryScrollPane);
        missionCycleEntryScrollPane.setBounds(395, 95, 150, 350);

        missionCycleEntryRemoveButton.setIcon(new javax.swing.ImageIcon("Images/delete.png"));
        missionCycleEntryRemoveButton.setName("RemoveButton"); // NOI18N
        missionCycleEntryRemoveButton.setToolTipText("Remove Selected Mission from the Mission Cycle");
        missionCycleEntryRemoveButton.addActionListener(new RemoveMissionCycleEntryButtonListener());
        add(missionCycleEntryRemoveButton);
        missionCycleEntryRemoveButton.setBounds(550, 125, 25, 25);

        missionCycleEntryUpButton.setIcon(new javax.swing.ImageIcon("Images/up.png"));
        missionCycleEntryUpButton.setName("UpButton"); // NOI18N
        missionCycleEntryUpButton.setActionCommand("Up");
        missionCycleEntryUpButton.setToolTipText("Move Selected Mission up in the Mission Cycle Order");
        missionCycleEntryUpButton.addActionListener(new MissionCycleEntryMoveListener());
        add(missionCycleEntryUpButton);
        missionCycleEntryUpButton.setBounds(550, 155, 25, 25);

        missionCycleEntryDownButton.setIcon(new javax.swing.ImageIcon("Images/down.png"));
        missionCycleEntryDownButton.setName("DownButton"); // NOI18N
        missionCycleEntryDownButton.setActionCommand("Down");
        missionCycleEntryDownButton.setToolTipText("Move Selected Mission Down in the Mission Cycle Order");
        missionCycleEntryDownButton.addActionListener(new MissionCycleEntryMoveListener());
        add(missionCycleEntryDownButton);
        missionCycleEntryDownButton.setBounds(550, 185, 25, 25);

        difficultyListModel = new DefaultListModel();
        difficultyList = new javax.swing.JList(difficultyListModel);
        difficultyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        difficultyList.addListSelectionListener(new DifficultyListListener());
        difficultyList.setModel(difficultyComboBox.getModel());

        difficultyScrollPane.setViewportView(difficultyList);
        difficultyScrollPane.setOpaque(true);
        difficultyScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Difficulty Setup")); // NOI18N
        difficultyScrollPane.setName("difficultyScrollPane"); // NOI18N
        add(difficultyScrollPane);
        difficultyScrollPane.setBounds(590, 10, 175, 75);

        difficultyAddButton.setIcon(new javax.swing.ImageIcon("Images/add.png"));
        difficultyAddButton.setName("difficultyAddButton"); // NOI18N
        difficultyAddButton.setToolTipText("Add a new Difficulty Setup");
        difficultyAddButton.addActionListener(new AddDifficultyButtonListener());
        add(difficultyAddButton);
        difficultyAddButton.setBounds(770, 20, 25, 25);

        difficultyRemoveButton.setIcon(new javax.swing.ImageIcon("Images/delete.png"));
        difficultyRemoveButton.setName("difficultyRemoveButton"); // NOI18N
        difficultyRemoveButton.setToolTipText("Remove Selected Difficulty Setup");
        difficultyRemoveButton.addActionListener(new RemoveDifficultyListener());
        add(difficultyRemoveButton);
        difficultyRemoveButton.setBounds(770, 50, 25, 25);

        columnHeader = new String[] { "Setting", "On/Off" };
        difficultySettingsTableModel = new MissionCycleTableModel("DIFFICULTYSETTINGS", columnHeader);
        difficultySettingsTableModel.addTableModelListener(new DifficultySettingsTableUpdate());
        difficultySettingsTable = new javax.swing.JTable(difficultySettingsTableModel);
        difficultySettingsTable.setOpaque(true);
        difficultySettingsTable.setBackground(Color.cyan);
        difficultySettingsTable.setName("difficultySettingsTable"); // NOI18N
        difficultySettingsTable.setAutoCreateRowSorter(true);

        difficultySettingsScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Difficulty Settings")); // NOI18N
        difficultySettingsScrollPane.setName("difficultySettingsScrollPane"); // NOI18N
        difficultySettingsScrollPane.setViewportView(difficultySettingsTable);
        difficultySettingsScrollPane.setOpaque(true);
        difficultySettingsScrollPane.setBounds(590, 95, 205, 355);
        add(difficultySettingsScrollPane);

        writeMissionFilesButton.setText("Write Mission Files");
        writeMissionFilesButton.setName("writeMissionFilesButton"); // NOI18N
        writeMissionFilesButton.setToolTipText("Write Mission Files to Database");
        writeMissionFilesButton.addActionListener(new WriteMissionFilesButtonListener());
        add(writeMissionFilesButton);
        writeMissionFilesButton.setBounds(65, 465, 150, 25);

        writeMissionCyclesButton.setText("Write Mission Cycles");
        writeMissionCyclesButton.setName("writeMissionCycleButton"); // NOI18N
        writeMissionCyclesButton.setToolTipText("Write Mission Cycle Names to Database");
        writeMissionCyclesButton.addActionListener(new WriteMissionCyclesButtonListener());
        add(writeMissionCyclesButton);
        writeMissionCyclesButton.setBounds(225, 465, 150, 25);
        resetMissionCycleButton.setText("Reset");
        resetMissionCycleButton.setName("resetMissionCycleButton"); // NOI18N
        resetMissionCycleButton.setToolTipText("Reset the Currently Running Mission Cycle");
        resetMissionCycleButton.addActionListener(new ResetMissionCycleButtonListener());
        add(resetMissionCycleButton);
        resetMissionCycleButton.setBounds(385, 465, 75, 25);

        runTempMissionButton.setText("Run Temp Mission");
        runTempMissionButton.setName("runTempMissionButton"); // NOI18N
        runTempMissionButton.setToolTipText("Run A Single Mission");
        runTempMissionButton.addActionListener(new RunTempMissionButtonListener());
        add(runTempMissionButton);
        runTempMissionButton.setBounds(465, 465, 150, 25);

        saveButton.setText("Save");
        saveButton.setName("saveButton"); // NOI18N
        saveButton.setToolTipText("Save Changes");
        saveButton.addActionListener(new SaveButtonListener());
        add(saveButton);
        saveButton.setBounds(620, 465, 75, 25);

        cancelButton.setText("Cancel");
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.setToolTipText("Cancel Changes");
        cancelButton.addActionListener(new CancelButtonListener());
        add(cancelButton);
        cancelButton.setBounds(700, 465, 75, 25);

        // Load Model Data into Form
        loadGUI();

    }  // End InitComponents

    class MissionFilesTableUpdate implements TableModelListener {

        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE) {
                dataChanged = true;
                String missionName = (String) missionFilesTableModel.getValueAt(e.getFirstRow(), missionFilesTableModel.findColumn("Name"));
                @SuppressWarnings("unused")
                String missionCycle = (String) missionCycleList.getSelectedValue();
                String missionDirectory = (String) missionFilesTableModel.getValueAt(e.getFirstRow(), missionFilesTableModel.findColumn("Directory"));
                String difficulty = (String) missionFilesTableModel.getValueAt(e.getFirstRow(), missionFilesTableModel.findColumn("Difficulty Setup"));
                String redWonMission = (String) missionFilesTableModel.getValueAt(e.getFirstRow(), missionFilesTableModel.findColumn("Red Won"));
                String blueWonMission = (String) missionFilesTableModel.getValueAt(e.getFirstRow(), missionFilesTableModel.findColumn("Blue Won"));

                MissionCycleController.updateMissionFile(userMissionCycles, missionDirectory, missionName, difficulty, redWonMission, blueWonMission);
            }
        }
    }

    class MissionCycleEntryTableUpdate implements TableModelListener {

        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE) {
                // Nothing to do here now
            }
        }
    }

    class DifficultySettingsTableUpdate implements TableModelListener {

        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE) {
                dataChanged = true;
                String difficultySetup = (String) difficultyList.getSelectedValue();
                String setting = (String) difficultySettingsTableModel.getValueAt(e.getFirstRow(), difficultySettingsTableModel.findColumn("Setting"));
                boolean settingValue = (Boolean) difficultySettingsTableModel.getValueAt(e.getFirstRow(), difficultySettingsTableModel.findColumn("On/Off"));
                MissionCycleController.updateDifficultySettings(difficultySetup, setting, settingValue);
            }
        }
    }

    class MissionCycleListModelListener implements ListDataListener {
        public void intervalAdded(ListDataEvent e) {
//			System.out.println("New element added to MissionCycleList");
            ViewConfigurationController.updateMissionCycleList(missionCycleListModel);
        }

        public void intervalRemoved(ListDataEvent e) {
            ViewConfigurationController.updateMissionCycleList(missionCycleListModel);
//			System.out.println("Element removed from MissionCycleList");
        }

        public void contentsChanged(ListDataEvent e) {
//			System.out.println("Element Changed in MissionCycleList");
            ViewConfigurationController.updateMissionCycleList(missionCycleListModel);
        }
    }

    class MissionCycleListListener implements ListSelectionListener {
        // This method is required by ListSelectionListener.
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting() == false) {

                if (missionCycleList.getSelectedIndex() == -1) {
                    // No selection, disable fire button.
                    missionCycleRemoveButton.setEnabled(false);

                } else {
                    // Selection, enable the fire button.
                    selectedMissionCycle = (String) missionCycleList.getSelectedValue();
                    missionCycleRemoveButton.setEnabled(true);
                    loadMissionCycleEntries();
                }
            }
        }
    }

    class DifficultyListListener implements ListSelectionListener {
        // This method is required by ListSelectionListener.
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting() == false) {

                if (difficultyList.getSelectedIndex() == -1) {
                    // No selection, disable fire button.
                    difficultyRemoveButton.setEnabled(false);

                } else {
                    // Selection, enable the fire button.
                    selectedDifficulty = (String) difficultyList.getSelectedValue();
                    difficultyRemoveButton.setEnabled(true);
                    loadDifficultySettingsTableData();
                }
            }
        }
    }

    class AddMissionFileButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int index = missionCycleList.getSelectedIndex();
            // No mission Cycles are selected
            if (index == -1) {
                JOptionPane.showMessageDialog(null, "Select a Mission Cycle First", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {   // A Mission Cycle is selected, select the Mission file
                @SuppressWarnings("unused")
                String missionCycle = (String) missionCycleList.getSelectedValue();
                if (missionFileChooser == null) {
                    missionFileChooser = new javax.swing.JFileChooser();
                }
                ArrayList<String> fileExtensions = new ArrayList<String>(1);
                fileExtensions.add("mis");
                missionFileChooser.addChoosableFileFilter(new MissionFileFilter(fileExtensions));
                missionFileChooser.setAcceptAllFileFilterUsed(false);
                // Set the start directory to IL2 Server Missions directory
                if (missionStartDirectory == null) {
                    missionStartDirectory = new File(MainController.getMissionDirectory());
                    missionFileChooser.setCurrentDirectory(missionStartDirectory);
                }

                // Show the Open File Dialog
                int returnVal = missionFileChooser.showDialog(null, "Open");

                // Process the results.
                if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                    File file = missionFileChooser.getSelectedFile();
                    String filePath = file.getAbsolutePath();

                    String missionFileName = file.getName();
                    int directoryIndex = filePath.lastIndexOf(File.separatorChar);
                    String missionDirectory = filePath.substring(0, directoryIndex + 1);
                    missionDirectory = MissionCycleController.validateMissionPath(missionDirectory);

                    int extensionIndex = missionFileName.lastIndexOf(".");
                    missionFileName = missionFileName.substring(0, extensionIndex);
                    String IL2serverMissionPath = MissionCycleController.getIL2serverMissionPath();
                    File missionFile = new File(IL2serverMissionPath + missionDirectory + missionFileName + ".mis");
                    if (missionFile.canRead()) {
//						Object[] rowData = { missionFileName, missionDirectory, "Default", "None", "None" };
//						missionFilesTableModel.addRow(rowData);
                        MissionFile tempMissionFile = new MissionFile(missionFileName);
                        tempMissionFile.setDirectory(missionDirectory);
                        userMissionCycles.getMissionFiles().put(missionFileName, tempMissionFile);
                        loadMissionFilesTableData();
                        populateAvailableMissionFilesList();
                        dataChanged = true;
                    } else {
                        JOptionPane.showMessageDialog(null, "Mission file ( " + missionDirectory + missionFileName + ".mis ) not Readable", "Message", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    class ResetMissionCycleButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String currentMissionCycle = MainController.CONFIG.getMissionCycle();
            String newMissionCycle = currentMissionCycle;
            int selectedMissionFile = missionCycleEntryTable.getSelectedRow();

            if (MainController.CONNECTED) {
                String selectedMissionCycle = (String) missionCycleList.getSelectedValue();
                if (selectedMissionCycle != null) {
                    if (!selectedMissionCycle.equals(currentMissionCycle)) {
                        // Mission Parameter data has changed so confirm cancel
                        int n = JOptionPane.showConfirmDialog(null, "Reset current Mission Cycle (" + currentMissionCycle + ") \n" + " to Selected Mission Cycle (" + selectedMissionCycle + ") ?", "Reset Mission Cycle", JOptionPane.YES_NO_OPTION);
                        if (n == JOptionPane.YES_OPTION) { // Yes they want to change MissionCycle
                            newMissionCycle = selectedMissionCycle;
                            if (selectedMissionFile == -1) {
                                selectedMissionFile = 0;
                            }
                        } else {   // No they do not want to change the mission Cycle, but they want to reset current one
                                 // from the beginning.
                            selectedMissionFile = 0;
                        }
                    }
                    String missionFileName = (String) missionCycleEntryModel.getValueAt(selectedMissionFile, missionCycleEntryModel.findColumn("Mission Name"));
                    Object[] options = { "Reset Now", "Wait", "Cancel" };
                    int i = JOptionPane.showOptionDialog(null, "Reset Mission Cycle Starting with ( " + missionFileName + " ) \n" + "Now or Wait until Current Mission finishes ?", "Reset Mission Cycle", JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null,     // do not use a custom Icon
                            options,  // the titles of buttons
                            options[0]); // default button title
                    if (i == JOptionPane.YES_OPTION) {
                        MissionCycleController.resetMissionCycle(newMissionCycle, true, selectedMissionFile);
                    } else if (i == JOptionPane.NO_OPTION) {
                        MissionCycleController.resetMissionCycle(newMissionCycle, false, selectedMissionFile);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "You must Select a Mission Cycle to Reset", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }

            else {
                JOptionPane.showMessageDialog(null, "FBDj Not Currently Connected", "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    class RunTempMissionButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (MainController.CONNECTED) {
                if (missionFileChooser == null) {
                    missionFileChooser = new javax.swing.JFileChooser();
                }
                ArrayList<String> fileExtensions = new ArrayList<String>(1);
                fileExtensions.add("mis");
                missionFileChooser.addChoosableFileFilter(new MissionFileFilter(fileExtensions));
                missionFileChooser.setAcceptAllFileFilterUsed(false);
                if (missionStartDirectory == null) {
                    missionStartDirectory = new File(MainController.getMissionDirectory());
                    missionFileChooser.setCurrentDirectory(missionStartDirectory);
                }

                int returnVal = missionFileChooser.showDialog(null, "Open");
                // Process the results.
                if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                    File file = missionFileChooser.getSelectedFile();
                    String filePath = file.getAbsolutePath();

                    missionFileName = file.getName();
                    int directoryIndex = filePath.lastIndexOf("\\");
                    missionDirectory = filePath.substring(0, directoryIndex + 1);
                    missionDirectory = MissionCycleController.validateMissionPath(missionDirectory);
                    int extensionIndex = missionFileName.lastIndexOf(".");
                    missionFileName = missionFileName.substring(0, extensionIndex);
                    String IL2serverMissionPath = MissionCycleController.getIL2serverMissionPath();
                    File missionFile = new File(IL2serverMissionPath + missionDirectory + missionFileName + ".mis");
                    if (missionFile.canRead()) {
                        MissionFile tempMissionFile = new MissionFile(missionFileName);
                        tempMissionFile.setDirectory(missionDirectory);

                        ArrayList<Object> possibilities = new ArrayList<Object>();
                        for (int i = 0; i < difficultyComboBox.getModel().getSize(); i++) {
                            possibilities.add(difficultyComboBox.getModel().getElementAt(i));
                        }
                        Object[] comboBoxList = possibilities.toArray();
                        String s = (String) JOptionPane.showInputDialog(null, "Select the Difficulty Setup:", "Difficulty Selection", JOptionPane.PLAIN_MESSAGE, null, comboBoxList, null);

                        if ((s != null) && (s.length() > 0)) {
                            tempMissionFile.setDifficulty(s);

                            Object[] options = { "Run Now", "Wait", "Cancel" };
                            int n = JOptionPane.showOptionDialog(null, "Run Mission(" + missionFileName + ") Now or Wait until Current Mission finishes ?", "Run Mission", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,     // do not use
                                                                                                                                                                                                                                                 // a custom
                                                                                                                                                                                                                                                 // Icon
                                    options,  // the titles of buttons
                                    options[0]); // default button title
                            if (n == JOptionPane.YES_OPTION) {
                                GUICommand newGUICommand = new GUICommand(GUICommand.GUICommandType.RUNTEMPMISSION);
                                newGUICommand.setValue(tempMissionFile);
                                newGUICommand.setChangeObject(new Boolean(true));
                                // Add data to the main thread queue
                                MainController.doQueue(new QueueObj(QueueObj.Source.GUI, newGUICommand));
                            } else if (n == JOptionPane.NO_OPTION) {
                                GUICommand newGUICommand = new GUICommand(GUICommand.GUICommandType.RUNTEMPMISSION);
                                newGUICommand.setValue(tempMissionFile);
                                newGUICommand.setChangeObject(new Boolean(false));
                                // Add data to the main thread queue
                                MainController.doQueue(new QueueObj(QueueObj.Source.GUI, newGUICommand));
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Mission file ( " + missionDirectory + missionFileName + ".mis ) not Readable", "Message", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "FBDj Not Currently Connected", "Message", JOptionPane.INFORMATION_MESSAGE);
            }

        }
    }

    class SaveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (dataChanged) {
                MissionCycleController.saveMissionCycles(userMissionCycles);
                dataChanged = false;
                JOptionPane.showMessageDialog(null, "Mission Cycle Saved", "Message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No Changes to Save", "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    class CancelButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (dataChanged) {
                // Mission Parameter data has changed so confirm cancel
                int n = JOptionPane.showConfirmDialog(null, "Are You Sure ?", "Cancel Updates", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) { // Yes they want to edit it
                    clearForm();
                    dataChanged = false;
                }
            } else {
                JOptionPane.showMessageDialog(null, "No Changes to Cancel", "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    class WriteMissionFilesButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            boolean writeOK = false;
            try {
                writeOK = MissionCycleController.writeMissionFiles();
                if (writeOK) {
                    JOptionPane.showMessageDialog(null, "Mission File Names wrote to the Database", "Message", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to write Mission File Names to the Database", "Message", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MissionCyclePanel.WriteMissionFilesButtonListener - Error unhandled exception writing Mission Cycles: " + ex);
            }
        }
    }

    class WriteMissionCyclesButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            boolean writeOK = false;
            try {
                writeOK = MissionCycleController.writeMissionCycles();
                if (writeOK) {
                    JOptionPane.showMessageDialog(null, "Mission Cycle Names wrote to the Database", "Message", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to write Mission Cycle Names to the Database", "Message", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MissionCyclePanel.WriteMissioncyclesButtonListener - Error unhandled exception writing Mission Cycles: " + ex);
            }
        }
    }

    class AddMissionCycleButtonListener implements ActionListener {
        @SuppressWarnings("unchecked")
        public void actionPerformed(ActionEvent e) {

            String addError;
            String missionCycle = (String) JOptionPane.showInputDialog(null, "Enter Mission Cycle Name:", "Add Mission Cycle", JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (missionCycle != null && missionCycle.length() > 0) {
                if ((addError = MissionCycleController.addMissionCycle(missionCycle)) != null) {
                    JOptionPane.showMessageDialog(null, "Error Adding Mission Cycle: " + addError, "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    missionCycleListModel.addElement(missionCycle);
                    dataChanged = true;
                }
            }
        }
    }

    class RemoveMissionCycleListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // This method can be called only if
            // there's a valid selection
            // so go ahead and remove whatever's selected.
            int index = missionCycleList.getSelectedIndex();
            String missionCycleToRemove = (String) missionCycleList.getSelectedValue();
            try {
                // Confirm Deletion
                int n = JOptionPane.showConfirmDialog(null, "Remove Mission Cycle( " + missionCycleToRemove + " ) ?", "Remove Mission Cycle", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    missionCycleListModel.remove(index);
                    MissionCycleController.removeMissionCycle(missionCycleToRemove);
                    removeMissionCycleEntries();
                    dataChanged = true;

                    int size = missionCycleListModel.getSize();

                    if (size == 0) { // Nobody's left, disable firing.
                        missionCycleRemoveButton.setEnabled(false);
                    } else { // Select an index.
                        if (index == missionCycleListModel.getSize()) {
                            // removed item in last position
                            index--;
                        }
                        missionCycleList.setSelectedIndex(index);
                        missionCycleList.ensureIndexIsVisible(index);
                    }
                }
            } catch (Exception ex) {}
        }
    }

    class AddDifficultyButtonListener implements ActionListener {
        @SuppressWarnings("unchecked")
        public void actionPerformed(ActionEvent e) {
            String addError;
            String difficulty = (String) JOptionPane.showInputDialog(null, "Enter New Difficulty Setup Name:", "Add Difficulty Setup", JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (difficulty != null && difficulty.length() > 0) {
                if ((addError = MissionCycleController.addDifficultySetup(difficulty)) != null) {
                    JOptionPane.showMessageDialog(null, "Error Adding Difficulty Setup: " + addError, "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    difficultyComboBox.addItem(difficulty);
                    // Need to figure out how to set the selected row in Difficulty Setup list to the new one
                    // Added. I haven't been able to figure it out since the list model is combo box model
                    loadDifficultySettingsTableData();
                    dataChanged = true;
                }
            }
        }
    }

    class RemoveDifficultyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // This method can be called only if
            // there's a valid selection
            // so go ahead and remove whatever's selected.
            int index = difficultyList.getSelectedIndex();
            String difficultySetupToRemove = (String) difficultyList.getSelectedValue();
            try {
                // Confirm Deletion
                int n = JOptionPane.showConfirmDialog(null, "Remove Difficulty Setup( " + difficultySetupToRemove + " ) ?", "Remove Mission", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    difficultyComboBox.removeItemAt(index);
                    MissionCycleController.removeDifficultySetup(difficultySetupToRemove);
                    dataChanged = true;
                    int size = difficultyListModel.getSize();

                    if (size == 0) { // Nobody's left, disable firing.
                        difficultyRemoveButton.setEnabled(false);
                        removeDifficultySettingsTableData();

                    } else { // Select an index.
                        if (index == difficultyListModel.getSize()) {
                            // removed item in last position
                            index--;
                        }

                        difficultyList.setSelectedIndex(index);
                        difficultyList.ensureIndexIsVisible(index);
                        loadDifficultySettingsTableData();
                    }
                }
            } catch (Exception ex) {}
        }
    }

    class RemoveMissionFileButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            if (missionFilesSelection.getMinSelectionIndex() > -1) {
                String missionToRemove = missionFilesTable.getValueAt(missionFilesSelection.getMinSelectionIndex(), 0).toString();
                try {
                    // Confirm Deletion
                    int n = JOptionPane.showConfirmDialog(null, "Remove Mission( " + missionToRemove + " ) ?", "Remove Mission", JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.YES_OPTION) {
                        missionFilesTableModel.removeRow(missionFilesSelection.getMinSelectionIndex());
                        MissionCycleController.removeMissionFile(selectedMissionCycle, missionToRemove);
                        populateAvailableMissionFilesList();
                        dataChanged = true;
                    }
                } catch (Exception ex) {}
            } else {
                JOptionPane.showMessageDialog(null, "No Mission Selected", "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    class RemoveMissionCycleEntryButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Make Sure a Plane Limit is selected
            if (missionCycleEntrySelection.getMinSelectionIndex() > -1) {
                String missionToRemove = missionCycleEntryTable.getValueAt(missionCycleEntrySelection.getMinSelectionIndex(), missionCycleEntryModel.findColumn("Mission Name")).toString();
                try {
                    // Confirm Deletion
                    int n = JOptionPane.showConfirmDialog(null, "Remove Mission( " + missionToRemove + " ) ?", "Remove Mission", JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.YES_OPTION) {
                        missionCycleEntryModel.removeRow(missionCycleEntrySelection.getMinSelectionIndex());
                        MissionCycleController.removeMissionCycleEntry(missionToRemove, selectedMissionCycle);
                        dataChanged = true;
                    }
                } catch (Exception ex) {}
            } else {
                JOptionPane.showMessageDialog(null, "No Mission Selected", "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    class MissionFilesMoveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int index = missionCycleList.getSelectedIndex();
            if (index == -1) {
                // No mission Cycles are selected
                JOptionPane.showMessageDialog(null, "Select a Mission Cycle First", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {   // A Mission Cycle is selected, select the Mission file
                String missionCycle = (String) missionCycleList.getSelectedValue();
                // If user Pressed the Right button move the selected rows to Mission Cycle
                if (e.getActionCommand().equals("Right")) {
                    int[] selectedRows = missionFilesTable.getSelectedRows();
                    if (selectedRows.length > 0) {
                        for (int i : selectedRows) {
                            String missionName = (String) missionFilesTableModel.getValueAt(i, missionFilesTableModel.findColumn("Name"));
                            Object[] rowData = { missionName, "None", "None" };
                            missionCycleEntryModel.addRow(rowData);
                            MissionCycleController.addMissionToCycle(missionName, missionCycle);
                            dataChanged = true;
                        }
                    }
                    missionFilesSelection.clearSelection();
                }
            }
        }
    }

    class MissionCycleEntryMoveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Make sure a Mission File Row has been selected
            if (missionCycleEntryTable.getSelectedRow() != -1) {
                int rowToMove = missionCycleEntryTable.getSelectedRow();
                // If user Pressed the Up button move the row up 1
                if (e.getActionCommand().equals("Up")) {
                    if (rowToMove == 0)
                        return;
                    missionCycleEntryModel.moveRow(rowToMove, rowToMove, rowToMove - 1);
                    missionCycleEntryTable.setRowSelectionInterval(rowToMove - 1, rowToMove - 1);
                }// Otherwise move it down 1
                else {
                    if (rowToMove == missionCycleEntryTable.getRowCount() - 1)
                        return;
                    missionCycleEntryModel.moveRow(rowToMove, rowToMove, rowToMove + 1);
                    missionCycleEntryTable.setRowSelectionInterval(rowToMove + 1, rowToMove + 1);
                }
                // Create an arraylist of the new Mission File order
                // There may be an easier way to do this, but I'm still new to this
                ArrayList<MissionCycleEntry> missionCycleEntryList = new ArrayList<MissionCycleEntry>();
                for (int i = 0; i < missionCycleEntryModel.getRowCount(); i++) {
                    String tempName = (String) missionCycleEntryModel.getValueAt(i, missionCycleEntryModel.findColumn("Mission Name"));
                    MissionCycleEntry newMissionCycleEntry = new MissionCycleEntry(tempName);
                    missionCycleEntryList.add(newMissionCycleEntry);
                }
                // Update the Model with new Mission File Order
                MissionCycleController.reOrderMissionFileList((String) missionCycleList.getSelectedValue(), missionCycleEntryList);
                dataChanged = true;
            } else {
                JOptionPane.showMessageDialog(null, "No Mission File Selected", "Warning", JOptionPane.WARNING_MESSAGE);
            }

        }
    }

    public void removeMissionFilesTableData() {
        int rowCount = missionFilesTableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            missionFilesTableModel.removeRow(0);
        }
    }

    public void loadMissionFilesTableData() {
        removeMissionFilesTableData();

        ArrayList<String> missionNames = new ArrayList<String>();
        missionNames.addAll(userMissionCycles.getMissionFiles().keySet());
        // and sorting it (in reverse order)
        Collections.sort(missionNames);

        for (String missionName : missionNames) {
            MissionFile missionFile = userMissionCycles.getMissionFiles().get(missionName);

            Object[] rowData = { missionFile.getMissionName(), missionFile.getDirectory(), missionFile.getDifficulty(), missionFile.getRedWonMission(), missionFile.getBlueWonMission() };
            missionFilesTableModel.addRow(rowData);

        }
//			Iterator<String> it = userMissionCycles.getMissionFiles().keySet().iterator();
//			while (it.hasNext())
//			{		
//	           String key = it.next();
//			   MissionFile missionFile = userMissionCycles.getMissionFiles().get(key);
//			
//				Object[] rowData = { missionFile.getMissionName(), missionFile.getDirectory(), missionFile.getDifficulty(), missionFile.getRedWonMission(), missionFile.getBlueWonMission() };
//				missionFilesTableModel.addRow(rowData);
//			}
    }

    @SuppressWarnings("unchecked")
    public void populateAvailableMissionFilesList() {
        availableMissionComboBox.removeAllItems();
        availableMissionListModel.removeAllElements();
        availableMissionListModel.addElement("None");
        availableMissionComboBox.addItem("None");
        for (int i = 0; i < missionFilesTableModel.getRowCount(); i++) {
            String missionName = (String) missionFilesTableModel.getValueAt(i, missionFilesTableModel.findColumn("Name"));
            availableMissionListModel.addElement(missionName);
            availableMissionComboBox.addItem(missionName);
        }
        availableMissionComboBox.setSelectedItem("None");

    }

    public void removeDifficultySettingsTableData() {
        int rowCount = difficultySettingsTableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            difficultySettingsTableModel.removeRow(0);
        }
    }

    public void loadDifficultySettingsTableData() {
        removeDifficultySettingsTableData();
        String difficulty = selectedDifficulty;
        DifficultySettings difficultySettings = userMissionCycles.getDifficultySettings().get(difficulty);
        if (difficultySettings != null) {
            Iterator<String> it = difficultySettings.getSettings().keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                Object[] rowData = { key, difficultySettings.getSettings().get(key).booleanValue() };
                difficultySettingsTableModel.addRow(rowData);
            }
        }
    }

    public void removeMissionCycleEntries() {
        int rowCount = missionCycleEntryModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            missionCycleEntryModel.removeRow(0);
        }
    }

    public void loadMissionCycleEntries() {
        removeMissionCycleEntries();
        String selectedMissionCycleName = (String) missionCycleList.getSelectedValue();
        MissionCycle missionCycle = null;
        ArrayList<MissionCycleEntry> missionEntries = null;
        missionCycle = userMissionCycles.getMissionCycleList().get(selectedMissionCycleName);
        if (missionCycle != null) {
            missionEntries = missionCycle.getMissionFiles();
            for (MissionCycleEntry missionEntry : missionEntries) {
                String missionName = missionEntry.getMissionName();
                Object[] rowData = { missionName };
                missionCycleEntryModel.addRow(rowData);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void loadGUI() {
        // Mission Files
        loadMissionFilesTableData();

        // Mission Cycles
        Iterator<String> it = userMissionCycles.getMissionCycleList().keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            missionCycleListModel.addElement(key);
        }
        missionCycleList.setSelectedIndex(0);

        // Mission Cycle Entries
        loadMissionCycleEntries();

        // Difficulty Settings
        it = userMissionCycles.getDifficultySettings().keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            difficultyComboBox.addItem(key);
        }
        difficultyList.setSelectedIndex(0);
        loadDifficultySettingsTableData();

        populateAvailableMissionFilesList();

    }  // End load GUI

    private void clearForm() {
        difficultyComboBox.setSelectedItem(null);
        removeMissionFilesTableData();
        dataChanged = false;
    }

    public static UserMissionCycles getUserMissionCycles() {
        return userMissionCycles;
    }

    public static void setUserMissionCycles(UserMissionCycles userMissionCycles) {
        MissionCyclePanel.userMissionCycles = userMissionCycles;
    }

    @SuppressWarnings("rawtypes")
    public static DefaultListModel getMissionCycleListModel() {
        return missionCycleListModel;
    }

    @SuppressWarnings("rawtypes")
    public void setMissionCycleListModel(DefaultListModel missionCycleListModel) {
        MissionCyclePanel.missionCycleListModel = missionCycleListModel;
    }

}
