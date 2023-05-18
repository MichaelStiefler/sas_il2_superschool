package view;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import mainController.IL2DataLoadController;
import mainController.MainController;
import model.Aerodrome;
import model.GUICommand;
import model.Mission;
import model.MissionCountObjective;
import model.MissionParameters;
import model.MissionTargetObjective;
import model.PlaneLoadoutRestriction;
import model.Weapon;
import viewController.MissionBuilderController;
import viewController.PlaneLoadoutsController;

public class MissionBuilderPanel extends JPanel {

    /**
         * 
         */
    private static final long                       serialVersionUID      = -3475103783117226246L;
    private static ArrayList<GUICommand>            missionGUICommands    = new ArrayList<GUICommand>();
    private static boolean                          dataChanged           = false;

    private String                                  missionFileName;
    private String                                  missionDirectory;
    private javax.swing.JButton                     cancelButton;
    private javax.swing.JTable                      loadoutRestrictionTable;
    private MissionBuilderTableModel                loadoutRestrictionTableModel;
    private javax.swing.JScrollPane                 loadoutRestrictionsScrollPane;
    private javax.swing.JTable                      missionCountObjectiveTable;
    private MissionBuilderTableModel                countObjectiveTableModel;
    private javax.swing.JScrollPane                 missionCountObjectivesSPane;
    private javax.swing.JLabel                      missionMinutesLabel;
    private javax.swing.JLabel                      missionMissionNameLabel;
    private javax.swing.JLabel                      missionTimeLeftLabel;
    private javax.swing.JFormattedTextField         missionTimeLeftTF;
    private javax.swing.JLabel                      countObjectiveLabel;
    private javax.swing.JLabel                      targetObjectiveLabel;
    private javax.swing.JFormattedTextField         redCountObjectivesNeededTF;
    private javax.swing.JFormattedTextField         blueCountObjectivesNeededTF;
    private javax.swing.JFormattedTextField         redTargetObjectivesNeededTF;
    private javax.swing.JFormattedTextField         blueTargetObjectivesNeededTF;
    private javax.swing.JScrollPane                 missionTargetObjectiveSPane;
    private javax.swing.JTable                      missionTargetObjectiveTable;
    private MissionBuilderTableModel                targetObjectiveTableModel;
    private javax.swing.JLabel                      objectiveTypeLabel;
    private javax.swing.JScrollPane                 planeLimitScrollPane;
    private javax.swing.JTable                      planeLimitTable;
    private MissionBuilderTableModel                planeLimitTableModel;
    private javax.swing.JButton                     newButton;
    private javax.swing.JButton                     editButton;
    private javax.swing.JButton                     saveButton;
    private javax.swing.JLabel                      missionStatusMessageLabel;
    private javax.swing.JFileChooser                missionFileChooser;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox                   missionObjectiveComboBox;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox                   armyComboBox;
    private static MissionParameters                missionDetails;
    private javax.swing.JButton                     planeLimitAddButton;
    private javax.swing.JButton                     planeLimitRemoveButton;
    private javax.swing.JButton                     loadoutRestrictionAddButton;
    private javax.swing.JButton                     loadoutRestrictionRemoveButton;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox                   weaponLoadoutComboBox;
    @SuppressWarnings({ "unused", "rawtypes" })
    private javax.swing.ComboBoxModel               weaponLoadoutListModel;
    private javax.swing.tree.DefaultMutableTreeNode planeTreeRoot;
    private javax.swing.tree.DefaultMutableTreeNode redPlaneTreeRoot;
    private javax.swing.tree.DefaultMutableTreeNode bluePlaneTreeRoot;
    private javax.swing.JTree                       planeTree;
    private javax.swing.tree.DefaultTreeModel       planeTreeModel;
    private javax.swing.JScrollPane                 planeTreeScrollPane;
    private String                                  selectedAerodrome     = null;
    private String                                  selectedPlane         = null;
    private String                                  selectedArmy          = null;
    private File                                    missionStartDirectory = null;

    public MissionBuilderPanel() {
        initComponents();
    }

    public boolean isDataChanged() {
        return dataChanged;
    }

    class MissionBuilderTableModel extends DefaultTableModel {

        /**
             * 
             */
        private static final long serialVersionUID = -4365765854384843190L;
        private static final int  ROW              = 0;
        private String            tableName;

        public MissionBuilderTableModel(String tableName, String[] columnNames) {
            super(columnNames, ROW);
            this.tableName = tableName;
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {

            if (tableName.equals("LOADOUTRESTRICTION")) {   // No Editing of this table
                return false;
            } else if (tableName.equals("COUNTOBJECTIVE")) {   // Allow editing of # To Win
                if (col < (this.getColumnCount() - 1))
                    return false;
            } else if (tableName.equals("TARGETOBJECTIVE")) {  // Allow editing of Army and # To Win
                if (col == 1 || col == 2 || col == 3 || col == 5)
                    return false;
            } else if (tableName.equals("PLANELIMIT")) {  // Allow editing of # In Use and # Total
                if (col == 0 || col == 1 || col == 2)
                    return false;
            }

            return true;
        }

        public String getTableName() {
            return tableName;
        }

    }  // End class MissionStatusPanelTableModel

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void initComponents() {

        missionMissionNameLabel = new javax.swing.JLabel();
        missionTimeLeftLabel = new javax.swing.JLabel();
        missionTimeLeftTF = new JFormattedTextField();
        countObjectiveLabel = new javax.swing.JLabel("Count Objectives Needed");
        targetObjectiveLabel = new javax.swing.JLabel("Target Objectives Needed");
        redCountObjectivesNeededTF = new JFormattedTextField();
        redTargetObjectivesNeededTF = new JFormattedTextField();
        blueCountObjectivesNeededTF = new JFormattedTextField();
        blueTargetObjectivesNeededTF = new JFormattedTextField();
        missionMinutesLabel = new javax.swing.JLabel();
        objectiveTypeLabel = new javax.swing.JLabel();
        missionObjectiveComboBox = new javax.swing.JComboBox();
        armyComboBox = new javax.swing.JComboBox();
        editButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        newButton = new javax.swing.JButton();
        loadoutRestrictionsScrollPane = new javax.swing.JScrollPane();
        planeLimitScrollPane = new javax.swing.JScrollPane();
        missionCountObjectivesSPane = new javax.swing.JScrollPane();
        missionTargetObjectiveSPane = new javax.swing.JScrollPane();
        missionStatusMessageLabel = new javax.swing.JLabel();
        planeLimitAddButton = new javax.swing.JButton();
        planeLimitRemoveButton = new javax.swing.JButton();
        loadoutRestrictionAddButton = new javax.swing.JButton();
        loadoutRestrictionRemoveButton = new javax.swing.JButton();
//            weaponLoadoutListModel = new javax.swing.ComboBoxModel();
        weaponLoadoutComboBox = new javax.swing.JComboBox();
        planeTreeScrollPane = new javax.swing.JScrollPane();

        setMaximumSize(new java.awt.Dimension(790, 500));
        setMinimumSize(new java.awt.Dimension(790, 500));
        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(790, 500));
        setLayout(null);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(view.MainWindowApp2.class).getContext().getResourceMap(MissionStatusPanel.class);
        missionMissionNameLabel.setFont(resourceMap.getFont("missionStatusMissionNameLabel.font")); // NOI18N
        missionMissionNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        missionMissionNameLabel.setText(">Mission<");
        missionMissionNameLabel.setFocusable(false);
        missionMissionNameLabel.setName("missionStatusMissionNameLabel"); // NOI18N
        missionMissionNameLabel.setBounds(300, 10, 180, 17);
        add(missionMissionNameLabel);

        missionTimeLeftLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        missionTimeLeftLabel.setText("TimeLimit");
        missionTimeLeftLabel.setName("missionStatusTimeLeftLabel"); // NOI18N

        missionTimeLeftLabel.setBounds(640, 10, 45, 14);
        add(missionTimeLeftLabel);

        missionTimeLeftTF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        missionTimeLeftTF.setValue(new Long(0));
        missionTimeLeftTF.setName("missionStatusTimeLeftTF"); // NOI18N
        missionTimeLeftTF.setToolTipText("Enter the Time in Minutes for Mission to Run Without a Winner");
        missionTimeLeftTF.setBounds(690, 7, 34, 20);
        missionTimeLeftTF.addActionListener(new DataChangeListener());
        add(missionTimeLeftTF);

        missionMinutesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        missionMinutesLabel.setText("Minutes");
        missionMinutesLabel.setName("missionStatusMinutesLabel"); // NOI18N
        missionMinutesLabel.setBounds(730, 10, 37, 14);
        add(missionMinutesLabel);

        countObjectiveLabel.setBounds(100, 37, 150, 14);
        javax.swing.JLabel redCountLabel = new javax.swing.JLabel("Red:");
        redCountLabel.setBounds(80, 55, 50, 14);
        javax.swing.JLabel blueCountLabel = new javax.swing.JLabel("Blue:");
        blueCountLabel.setBounds(200, 55, 50, 14);
        redCountObjectivesNeededTF.setValue(new Integer(0));
        redCountObjectivesNeededTF.setToolTipText("Enter the number of Count Objectives Red needs to win the mission");
        redCountObjectivesNeededTF.setBounds(106, 54, 34, 17);
        redCountObjectivesNeededTF.addActionListener(new DataChangeListener());

        blueCountObjectivesNeededTF.setValue(new Integer(0));
        blueCountObjectivesNeededTF.setToolTipText("Enter the number of Count Objectives Blue needs to win the mission");
        blueCountObjectivesNeededTF.setBounds(225, 54, 34, 17);
        blueCountObjectivesNeededTF.addActionListener(new DataChangeListener());
        add(redCountLabel);
        add(blueCountLabel);
        add(redCountObjectivesNeededTF);
        add(blueCountObjectivesNeededTF);

        add(countObjectiveLabel);
        targetObjectiveLabel.setBounds(400, 40, 150, 14);
        javax.swing.JLabel redTargetLabel = new javax.swing.JLabel("Red:");
        redTargetLabel.setBounds(380, 55, 50, 14);
        javax.swing.JLabel blueTargetLabel = new javax.swing.JLabel("Blue:");
        blueTargetLabel.setBounds(500, 55, 50, 14);
        add(redTargetLabel);
        add(blueTargetLabel);
        redTargetObjectivesNeededTF.setValue(new Integer(0));
        redTargetObjectivesNeededTF.setToolTipText("Enter the number of Count Objectives Red needs to win the mission");
        redTargetObjectivesNeededTF.setBounds(406, 54, 34, 17);
        blueTargetObjectivesNeededTF.setValue(new Integer(0));
        blueTargetObjectivesNeededTF.setToolTipText("Enter the number of Count Objectives Blue needs to win the mission");
        blueTargetObjectivesNeededTF.setBounds(525, 54, 34, 17);
        add(redTargetObjectivesNeededTF);
        add(blueTargetObjectivesNeededTF);
        add(targetObjectiveLabel);

        objectiveTypeLabel.setText(resourceMap.getString("objectiveTypeLabel.text")); // NOI18N
        objectiveTypeLabel.setName("objectiveTypeLabel"); // NOI18N
        objectiveTypeLabel.setBounds(565, 40, 77, 14);
        add(objectiveTypeLabel);

        missionObjectiveComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Count", "Target Area", "Both", "Either" }));
        missionObjectiveComboBox.setName("missionObjectiveComboBox"); // NOI18N
        missionObjectiveComboBox.setSelectedItem("Count");
        missionObjectiveComboBox.setBounds(645, 37, 140, 20);
        missionObjectiveComboBox.addActionListener(new DataChangeListener());
//			missionObjectiveComboBox.addActionListener( new ActionListener() {
//		        public void actionPerformed(ActionEvent e) { objectiveTypeChanged(e); }
//		    });

        add(missionObjectiveComboBox);

        armyComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Red", "Blue" }));
        armyComboBox.setName("armyComboBox");

        newButton.setText("New"); // NOI18N
        newButton.setName("newButton"); // NOI18N
        newButton.setToolTipText("Create a new FBDj Mission Parameters File");
        newButton.addActionListener(new NewButtonListener());
        add(newButton);
        newButton.setBounds(460, 460, 75, 25);

        editButton.setText("Edit"); // NOI18N
        editButton.setName("editButton"); // NOI18N
        editButton.setToolTipText("Edit an existing FBDj Mission Parameters File");
        editButton.addActionListener(new EditButtonListener());
        add(editButton);
        editButton.setBounds(545, 460, 75, 25);

        saveButton.setText("Save"); // NOI18N
        saveButton.setName("saveButton"); // NOI18N
        saveButton.setToolTipText("Save Changes");
        saveButton.setOpaque(true);
        saveButton.addActionListener(new SaveButtonListener());
        add(saveButton);
        saveButton.setBounds(630, 460, 75, 25);

        cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.setToolTipText("Cancel Changes and Clear Form");
        cancelButton.addActionListener(new CancelButtonListener());
        add(cancelButton);
        cancelButton.setBounds(715, 460, 75, 25);

        loadoutRestrictionsScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("loadoutRestrictionsScrollPane.border.title"))); // NOI18N
        loadoutRestrictionsScrollPane.setName("loadoutRestrictionsScrollPane"); // NOI18N

        String[] columnHeader = { "Army", "Plane", "# Restrictions" };
        loadoutRestrictionTableModel = new MissionBuilderTableModel("LOADOUTRESTRICTION", columnHeader);
        loadoutRestrictionTableModel.addTableModelListener(new LoadoutRestrictionTableUpdate());

        loadoutRestrictionTable = new javax.swing.JTable(loadoutRestrictionTableModel);

        loadoutRestrictionTable.setOpaque(true);
        loadoutRestrictionTable.setBackground(Color.cyan);
        loadoutRestrictionTable.setName("loadoutRestrictionTable"); // NOI18N
        loadoutRestrictionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loadoutRestrictionTable.setAutoCreateRowSorter(true);
        loadoutRestrictionTable.getColumn("Army").setCellEditor(new DefaultCellEditor(armyComboBox));

        ListSelectionModel loadoutLSM = loadoutRestrictionTable.getSelectionModel();
        loadoutLSM.addListSelectionListener(new LoadoutTableSelectionListener());

        // This Listener picks up the row the user selects
        loadoutRestrictionTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // Is left mouse click
                if (SwingUtilities.isRightMouseButton(e)) {
                    // get the coordinates of the mouse click
                    Point p = e.getPoint();

                    // get the row index that contains that coordinate
                    int rowNumber = loadoutRestrictionTable.rowAtPoint(p);
                    String armyDesc = (String) loadoutRestrictionTableModel.getValueAt(rowNumber, loadoutRestrictionTableModel.findColumn("Army"));
                    String plane = (String) loadoutRestrictionTableModel.getValueAt(rowNumber, loadoutRestrictionTableModel.findColumn("Plane"));
                    int army = 0;
                    if (armyDesc.equals("Red")) {
                        army = MainController.REDARMY;
                    } else {
                        army = MainController.BLUEARMY;
                    }
                    PlaneLoadoutsController.displayLoadouts(army, plane);
                    loadLoadoutRestrictions();
                    dataChanged = true;
                }
            }
        });
        loadoutRestrictionsScrollPane.setViewportView(loadoutRestrictionTable);
        loadoutRestrictionsScrollPane.setOpaque(true);
        setUpArmyColumn(loadoutRestrictionTable.getColumnModel().getColumn(0));

        add(loadoutRestrictionsScrollPane);
        loadoutRestrictionsScrollPane.setBounds(5, 300, 300, 150);

        loadoutRestrictionAddButton.setIcon(new javax.swing.ImageIcon("Images/add.png")); // NOI18N
        loadoutRestrictionAddButton.setToolTipText("Add New Restricted Loadout");
        loadoutRestrictionAddButton.addActionListener(new AddLoadoutRestrictionButtonListener());
        add(loadoutRestrictionAddButton);
        loadoutRestrictionAddButton.setBounds(10, 455, 25, 25);

        loadoutRestrictionRemoveButton.setIcon(new javax.swing.ImageIcon("Images/delete.png")); // NOI18N
        loadoutRestrictionRemoveButton.setToolTipText("Add New Restricted Loadout");
        loadoutRestrictionRemoveButton.addActionListener(new RemoveLoadoutRestrictionButtonListener());
        add(loadoutRestrictionRemoveButton);
        loadoutRestrictionRemoveButton.setBounds(40, 455, 25, 25);

        planeLimitScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("planeLimitScrollPane.border.title"))); // NOI18N
        planeLimitScrollPane.setName("planeLimitScrollPane"); // NOI18N

        columnHeader = null;
        columnHeader = new String[] { "Army", "Aerodrome", "Plane", "# In Use", "# Total", "aerodromeId" };
        planeLimitTableModel = new MissionBuilderTableModel("PLANELIMIT", columnHeader);
        planeLimitTableModel.addTableModelListener(new PlaneLimitTableUpdate());

        planeLimitTable = new javax.swing.JTable(planeLimitTableModel);
        planeLimitTable.setOpaque(true);
        planeLimitTable.setBackground(Color.cyan);
        planeLimitTable.setName("planeLimitTable"); // NOI18N
        planeLimitTable.removeColumn(planeLimitTable.getColumn("aerodromeId"));
        planeLimitTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setUpArmyColumn(planeLimitTable.getColumnModel().getColumn(0));

        planeLimitScrollPane.setViewportView(planeLimitTable);
        planeLimitScrollPane.setOpaque(true);
        setUpArmyColumn(planeLimitTable.getColumnModel().getColumn(0));
        add(planeLimitScrollPane);
        planeLimitScrollPane.setBounds(309, 300, 300, 150);

        planeLimitRemoveButton.setIcon(new javax.swing.ImageIcon("Images/delete.png")); // NOI18N
        planeLimitRemoveButton.setToolTipText("Remove Selected Plane Limit");
        planeLimitRemoveButton.addActionListener(new RemovePlaneLimitButtonListener());
        add(planeLimitRemoveButton);
        planeLimitRemoveButton.setBounds(345, 455, 25, 25);

        planeLimitAddButton.setIcon(new javax.swing.ImageIcon("Images/add.png")); // NOI18N
        planeLimitAddButton.setToolTipText("Add New Plane Limit");
        planeLimitAddButton.addActionListener(new AddPlaneLimitButtonListener());
        add(planeLimitAddButton);
        planeLimitAddButton.setBounds(315, 455, 25, 25);

        missionCountObjectivesSPane.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("missionCountObjectivesSPane.border.title"))); // NOI18N
        missionCountObjectivesSPane.setName("missionCountObjectivesSPane"); // NOI18N

        columnHeader = null;
        columnHeader = new String[] { "Army", "Objective", "# Start", "# To Win" };
        countObjectiveTableModel = new MissionBuilderTableModel("COUNTOBJECTIVE", columnHeader);
        countObjectiveTableModel.addTableModelListener(new CountTableUpdate());
        missionCountObjectiveTable = new javax.swing.JTable(countObjectiveTableModel);
        missionCountObjectiveTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        missionCountObjectiveTable.setName("missionCountObjectiveTable"); // NOI18N
        missionCountObjectiveTable.setOpaque(true);
        missionCountObjectiveTable.setBackground(Color.cyan);
        missionCountObjectiveTable.setAutoCreateRowSorter(true);
        setUpArmyColumn(missionCountObjectiveTable.getColumnModel().getColumn(0));

        missionCountObjectivesSPane.setViewportView(missionCountObjectiveTable);
        missionCountObjectivesSPane.setOpaque(true);
        add(missionCountObjectivesSPane);
        missionCountObjectivesSPane.setBounds(5, 70, 300, 225);
        missionTargetObjectiveSPane.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("missionTargetObjectiveSPane.border.title"))); // NOI18N
        missionTargetObjectiveSPane.setName("missionTargetObjectiveSPane"); // NOI18N

        columnHeader = new String[] { "Army", "MapGrid", "Description", "# Start", "# To Win", "ObjectiveId" };
        targetObjectiveTableModel = new MissionBuilderTableModel("TARGETOBJECTIVE", columnHeader);
        targetObjectiveTableModel.addTableModelListener(new TargetTableUpdate());

        missionTargetObjectiveTable = new javax.swing.JTable(targetObjectiveTableModel);
        missionTargetObjectiveTable.setName("missionTargetObjectiveTable"); // NOI18N
        missionTargetObjectiveTable.setOpaque(true);
        missionTargetObjectiveTable.setBackground(Color.cyan);
        missionTargetObjectiveTable.setAutoCreateRowSorter(true);
        missionTargetObjectiveTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        missionTargetObjectiveTable.removeColumn(missionTargetObjectiveTable.getColumn("ObjectiveId"));
        missionTargetObjectiveSPane.setViewportView(missionTargetObjectiveTable);
        missionTargetObjectiveSPane.setOpaque(true);
        setUpArmyColumn(missionTargetObjectiveTable.getColumnModel().getColumn(0));
        missionTargetObjectiveTable.getColumn("Army").setCellEditor(new DefaultCellEditor(armyComboBox));

        add(missionTargetObjectiveSPane);
        missionTargetObjectiveSPane.setBounds(309, 70, 300, 225);

        missionStatusMessageLabel.setBounds(100, 460, 400, 20);
        add(missionStatusMessageLabel);

        planeTreeRoot = new DefaultMutableTreeNode("Planes");
        planeTreeModel = new DefaultTreeModel(planeTreeRoot);
        redPlaneTreeRoot = new DefaultMutableTreeNode("Red");
        bluePlaneTreeRoot = new DefaultMutableTreeNode("Blue");
        planeTreeRoot.add(redPlaneTreeRoot);
        planeTreeRoot.add(bluePlaneTreeRoot);
        planeTree = new JTree(planeTreeModel);
        TreeSelectionModel tsm = planeTree.getSelectionModel();
        tsm.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        planeTree.addTreeSelectionListener(new PlaneTreeSelectionListener());
        planeTree.setRootVisible(false);
        planeTreeScrollPane.setViewportView(planeTree);

        planeTreeScrollPane.setOpaque(true);
        planeTreeScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Available Planes"));
        planeTreeScrollPane.setBounds(615, 70, 178, 380);
        add(planeTreeScrollPane);

    }  // End InitComponents

    class DataChangeListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (missionDetails != null) {
                dataChanged = true;
                if (e.getSource().equals(missionTimeLeftTF)) {
                    missionDetails.setTimeLimit((Long) missionTimeLeftTF.getValue());
                } else if (e.getSource().equals(redCountObjectivesNeededTF)) {
                    missionDetails.setRedCountObjectivesNeeded((Integer) redCountObjectivesNeededTF.getValue());
                } else if (e.getSource().equals(blueCountObjectivesNeededTF)) {
                    missionDetails.setBlueCountObjectivesNeeded((Integer) blueCountObjectivesNeededTF.getValue());
                } else if (e.getSource().equals(redTargetObjectivesNeededTF)) {
                    missionDetails.setRedTargetObjectivesNeeded((Integer) redTargetObjectivesNeededTF.getValue());
                } else if (e.getSource().equals(blueTargetObjectivesNeededTF)) {
                    missionDetails.setBlueTargetObjectivesNeeded((Integer) blueTargetObjectivesNeededTF.getValue());
                } else if (e.getSource().equals(missionObjectiveComboBox)) {
                    if (missionObjectiveComboBox.getSelectedItem().equals("Target Area")) {
                        missionDetails.setObjectiveType(Mission.MissionObjectiveType.TARGET);
                    } else if (missionObjectiveComboBox.getSelectedItem().equals("Count")) {
                        missionDetails.setObjectiveType(Mission.MissionObjectiveType.COUNT);
                    } else if (missionObjectiveComboBox.getSelectedItem().equals("Both")) {
                        missionDetails.setObjectiveType(Mission.MissionObjectiveType.BOTH);
                    } else {
                        missionDetails.setObjectiveType(Mission.MissionObjectiveType.EITHER);
                    }
                } else {
                    dataChanged = false;
                }
            }
        }
    }

    class EditButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (dataChanged) {
                // Mission Parameter data has changed so confirm cancel
                int n = JOptionPane.showConfirmDialog(null, "Discard Changes ?",

                "Discard Changes", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) { // Yes they want to edit it
                    dataChanged = false;
                }
            }
            if (!dataChanged) {
                clearForm();
                if (missionFileChooser == null) {
                    missionFileChooser = new javax.swing.JFileChooser();
                }

                ArrayList<String> fileExtensions = new ArrayList<String>(1);
                fileExtensions.add("FBDj");
                fileExtensions.add("fbdj");
                missionFileChooser.addChoosableFileFilter(new MissionFileFilter(fileExtensions));
                missionFileChooser.setAcceptAllFileFilterUsed(false);
                // Set the start directory to IL2 Server Missions directory
                if (missionStartDirectory == null) {
                    missionStartDirectory = new File(MainController.getMissionDirectory());
                    missionFileChooser.setCurrentDirectory(missionStartDirectory);
                }

                int returnVal = missionFileChooser.showDialog(null, "Open");

                // Process the results.
                if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {

                    File file = missionFileChooser.getSelectedFile();
                    String filePath = file.getPath();
                    missionFileName = file.getName();
                    int directoryIndex = filePath.lastIndexOf(File.separatorChar);
                    missionDirectory = filePath.substring(0, directoryIndex + 1);
                    int extensionIndex = missionFileName.lastIndexOf(".");
                    missionFileName = missionFileName.substring(0, extensionIndex);
                    missionMissionNameLabel.setText(missionFileName);
                    File missionFile = new File(missionDirectory + missionFileName + ".mis");
                    if (missionFile.canRead()) {
                        missionDetails = MissionBuilderController.loadMission(missionDirectory + missionFileName + ".mis").getMissionParameters();

//							missionDetails = new MissionParameters();
//							MissionBuilderController.loadMission(missionDetails, missionDirectory+missionFileName+".mis");

                        MissionParameters missionParameters = MissionBuilderController.readMission(missionDirectory + missionFileName + ".FBDj");
                        missionDetails = MissionBuilderController.mergeMissionDetails(missionDetails, missionParameters);
                        if (missionDetails != null) {
                            loadGUI();
                            dataChanged = false;
                        } else {
                            JOptionPane.showMessageDialog(null, "Mission Details Not Read", "Message", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Mission file ( " + missionDirectory + missionFileName + ".FBDj ) not Readable", "Message", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    class NewButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (dataChanged) {
                // Mission Parameter data has changed so confirm cancel
                int n = JOptionPane.showConfirmDialog(null, "Discard Changes ?", "Discard Changes", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) { // Yes they want to edit it
                    dataChanged = false;
                }
            }
            if (!dataChanged) {
                clearForm();
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

                int returnVal = missionFileChooser.showDialog(null, "Open");

                // Process the results.
                if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                    File file = missionFileChooser.getSelectedFile();
                    String filePath = file.getPath();
                    missionFileName = file.getName();
                    int directoryIndex = filePath.lastIndexOf(File.separatorChar);
                    missionDirectory = filePath.substring(0, directoryIndex + 1);
                    int extensionIndex = missionFileName.lastIndexOf(".");
                    missionFileName = missionFileName.substring(0, extensionIndex);
                    missionMissionNameLabel.setText(missionFileName);
                    File missionFile = new File(missionDirectory + missionFileName + ".mis");
                    if (missionFile.canRead()) {
                        missionDetails = MissionBuilderController.loadMission(missionDirectory + missionFileName + ".mis").getMissionParameters();
                        loadGUI();
                        dataChanged = false;
                    }
                } else {
                    System.out.println("Opening cancelled by user.");
                }
            }
        }
    }

    class SaveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            saveData();
            if (missionDetails != null && missionDirectory != null && missionFileName != null) {
                MissionBuilderController.missionParametersWrite(missionDetails, missionDirectory, missionFileName + ".FBDj");
                dataChanged = false;
                JOptionPane.showMessageDialog(null, "Mission Parameters File ( " + missionDirectory + missionFileName + ".FBDj ) Saved", "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    class CancelButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (dataChanged) {
                // Mission Parameter data has changed so confirm cancel
                int n = JOptionPane.showConfirmDialog(null, "Discard Changes ?", "Cancel Updates", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) { // Yes they want to edit it
                    clearForm();
                    dataChanged = false;
                }
            }
        }
    }

    class AddPlaneLimitButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (missionDetails != null) {
                if (selectedPlane != null) {
                    int aerodromeId = Integer.valueOf(selectedAerodrome.split("-")[0]);
                    Object[] planeLimitRow = { selectedArmy, selectedAerodrome, selectedPlane, 0, 0, missionDetails.getAerodromes().get(aerodromeId) };
                    planeLimitTableModel.addRow(planeLimitRow);
                    dataChanged = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot Add, No Plane Selected", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    class RemovePlaneLimitButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Make Sure a Plane Limit is selected
            if (planeLimitTable.getSelectedRow() > -1) {
                String aerodromeString = (String) planeLimitTableModel.getValueAt(planeLimitTable.getSelectedRow(), planeLimitTableModel.findColumn("Aerodrome"));
                int aerodromeId = Integer.valueOf(aerodromeString.split("-")[0]);
                Aerodrome aerodrome = missionDetails.getAerodromes().get(aerodromeId);

                String plane = (String) planeLimitTable.getValueAt(planeLimitTable.getSelectedRow(), planeLimitTableModel.findColumn("Plane"));
                try {
                    // Confirm Deletion
                    int n = JOptionPane.showConfirmDialog(null, "Remove Plane Limit for ( " + plane + " ) ?", "Remove Plane", JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.YES_OPTION) {
                        MissionBuilderController.updatePlaneLimit(aerodrome, plane, 0, 0);
                        planeLimitTableModel.removeRow(planeLimitTable.getSelectedRow());
                        dataChanged = true;
                    }
                } catch (Exception ex) {}
            } else {
                JOptionPane.showMessageDialog(null, "No Plane Limit Selected", "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    class LoadoutTableSelectionListener implements ListSelectionListener {
        @SuppressWarnings("unchecked")
        public void valueChanged(ListSelectionEvent le) {
            weaponLoadoutComboBox.removeAllItems();
            try {
                String plane = (String) loadoutRestrictionTableModel.getValueAt(loadoutRestrictionTable.getSelectedRow(), loadoutRestrictionTableModel.findColumn("Plane"));
                ArrayList<Weapon> weapons = MissionBuilderController.getPlaneWeapons(plane);
                for (Weapon weapon : weapons) {
                    weaponLoadoutComboBox.addItem(weapon.getWeaponDescription());
                }
            } catch (Exception ex) {
//					System.out.println("error in loadouttableselectionlistener: " + ex);
            }
        }
    }

    class AddLoadoutRestrictionButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            int army = 0;
            if (missionDetails != null) {
                if (selectedPlane != null) {
                    if (selectedArmy.equals("Red")) {
                        army = MainController.REDARMY;
                    } else {
                        army = MainController.BLUEARMY;
                    }
                    dataChanged = true;
                    PlaneLoadoutsController.displayLoadouts(army, selectedPlane);
                    loadLoadoutRestrictions();
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot Add, No Plane Selected", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        }  // End addLoadoutRestrictionMouseButtonClicked
    }

    class RemoveLoadoutRestrictionButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int army = 0;
            // Make Sure a Plane Limit is selected
            if (loadoutRestrictionTable.getSelectedRow() > -1) {
                String armyString = (String) loadoutRestrictionTable.getValueAt(loadoutRestrictionTable.getSelectedRow(), loadoutRestrictionTableModel.findColumn("Army"));
                String plane = (String) loadoutRestrictionTable.getValueAt(loadoutRestrictionTable.getSelectedRow(), loadoutRestrictionTableModel.findColumn("Plane"));
                if (armyString.equals("Red"))
                    army = MainController.REDARMY;
                else
                    army = MainController.BLUEARMY;

                try {
                    // Confirm Deletion
                    int n = JOptionPane.showConfirmDialog(null, "Remove Restricted Loadouts\n" + "for ( " + armyString + " / " + plane + " ) ?", "Remove Loadout Restriction", JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.YES_OPTION) {
                        loadoutRestrictionTableModel.removeRow(loadoutRestrictionTable.getSelectedRow());
                        MissionBuilderController.removeLoadoutRestriction(army, plane);
                        dataChanged = true;
                    }
                } catch (Exception ex) {
                    System.out.println("Remove error: " + ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "No Restricted Plane Loadout Selected", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    class LoadoutRestrictionTableUpdate implements TableModelListener {
        public void tableChanged(TableModelEvent e) {
//			    int army = 0;
//			    String restrictedWeapon = null;
//			    PlaneLoadoutRestriction planeLoadoutId = null;
//				if ( e.getType() == TableModelEvent.UPDATE)
//				{
//					if (loadoutRestrictionTableModel.getValueAt(e.getFirstRow(), 0).toString().equals("Red"))
//					{
//						army = MainController.REDARMY;
//					}
//					else
//					{
//						army = MainController.BLUEARMY;
//					}
//					String plane = (String)loadoutRestrictionTableModel.getValueAt(e.getFirstRow(), loadoutRestrictionTableModel.findColumn("Plane"));
//					String weaponDescription = (String)loadoutRestrictionTableModel.getValueAt(e.getFirstRow(),loadoutRestrictionTableModel.findColumn("Restricted Weapon"));
//                    planeLoadoutId = (PlaneLoadoutRestriction)loadoutRestrictionTableModel.getValueAt(e.getFirstRow(),loadoutRestrictionTableModel.findColumn("planeLoadoutId"));
//					ArrayList<Weapon> weapons = MissionBuilderController.getPlaneWeapons(plane);
//					for (Weapon weapon : weapons)
//					{
//						if (weapon.getWeaponDescription().equals(weaponDescription))
//						{
//							restrictedWeapon = weapon.getWeapon();
//						}
//					}
//					MissionBuilderController.updateLoadoutRestriction(restrictedWeapon, planeLoadoutId);
//					dataChanged = true;
//				}
        }
    }

    class PlaneLimitTableUpdate implements TableModelListener {
        public void tableChanged(TableModelEvent e) {
            @SuppressWarnings("unused")
            int army = 0;
            if (e.getType() == TableModelEvent.UPDATE) {

                String aerodrome = planeLimitTableModel.getValueAt(e.getFirstRow(), 1).toString();
                int aerodromeId = Integer.valueOf(aerodrome.split("-")[0]);
                Aerodrome planeLimit = missionDetails.getAerodromes().get(aerodromeId);
                army = planeLimit.getArmy();
                String plane = (String) planeLimitTableModel.getValueAt(e.getFirstRow(), planeLimitTableModel.findColumn("Plane"));
                int inUseLimit = (Integer) planeLimitTableModel.getValueAt(e.getFirstRow(), planeLimitTableModel.findColumn("# In Use"));
                int totalLimit = (Integer) planeLimitTableModel.getValueAt(e.getFirstRow(), planeLimitTableModel.findColumn("# Total"));
                if (planeLimit.getPlanes().containsKey(plane)) {
                    MissionBuilderController.updatePlaneLimit(planeLimit, plane, inUseLimit, totalLimit);
                    dataChanged = true;
                }
            }
        }
    }

    class CountTableUpdate implements TableModelListener {

        public void tableChanged(TableModelEvent e) {

            int army = 0;

            if (e.getType() == TableModelEvent.UPDATE) {
                if (countObjectiveTableModel.getValueAt(e.getFirstRow(), 0) == "Red") {
                    army = MainController.REDARMY;
                } else {
                    army = MainController.BLUEARMY;
                }

                missionDetails.getCountObjective(army, IL2DataLoadController.il2StaticObjectGetType(countObjectiveTableModel.getValueAt(e.getFirstRow(), 1).toString())).setNumberToDestroy((Integer) countObjectiveTableModel.getValueAt(e.getFirstRow(), 3));
                dataChanged = true;
            }
        }
    }

    class TargetTableUpdate implements TableModelListener {

        public void tableChanged(TableModelEvent e) {
            int army = 0;
            if (e.getType() == TableModelEvent.UPDATE) {
                if (targetObjectiveTableModel.getValueAt(e.getFirstRow(), 0) == "Red") {
                    army = MainController.REDARMY;
                } else if (targetObjectiveTableModel.getValueAt(e.getFirstRow(), 0) == "Blue") {
                    army = MainController.BLUEARMY;
                } else {
                    army = 0;
                }
                MissionTargetObjective targetObjective = (MissionTargetObjective) targetObjectiveTableModel.getValueAt(e.getFirstRow(), 5);
                targetObjective.setArmy(army);
                targetObjective.setNumberToDestroy((Integer) targetObjectiveTableModel.getValueAt(e.getFirstRow(), 4));
                dataChanged = true;
            }
        }
    }

    class PlaneTreeSelectionListener implements TreeSelectionListener {
        public void valueChanged(TreeSelectionEvent tse) {
            TreePath tp = tse.getPath();
            if (planeTree.getModel().isLeaf(tp.getLastPathComponent()) && tp.getLastPathComponent().toString() != "Red" && tp.getLastPathComponent().toString() != "Blue") {
                String selectedNode = tp.toString();
                selectedArmy = selectedNode.split(", ")[1];
                selectedPlane = tp.getLastPathComponent().toString();
                selectedAerodrome = selectedNode.split(", ")[2];
            }
        }
    }

    public void setUpArmyColumn(TableColumn col) {
        col.setCellRenderer(new ArmyRenderer());
    }

    public javax.swing.JLabel getMissionStatusMissionNameLabel() {
        return missionMissionNameLabel;
    }

    public void setMissionStatusMissionNameLabel(String value) {
        this.missionMissionNameLabel.setText(value);
    }

    public void setMissionStatusTimeLeftTF(Integer value) {
        this.missionTimeLeftTF.setValue(value);
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

//		public void updatePlaneLimtRow(int row, int planesLostCount, int planesInUseCount)
//		{
//			planeLimitTableModel.setValueAt(planesLostCount, row, 2);
//			planeLimitTableModel.setValueAt(planesInUseCount, row, 3);
//		}

    public void removeObjectiveTableData() {
        missionMissionNameLabel.setText("No Mission");
        missionTimeLeftTF.setText("0");

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

    public void loadLoadoutRestrictions() {
        String armyString;

        // Clear out the Loadout Restriction Table
        int rowCount = loadoutRestrictionTableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            loadoutRestrictionTableModel.removeRow(0);
        }

        boolean newPlane = false;
        String planeName = "";
        int weaponsCount = 0;
        for (PlaneLoadoutRestriction loadoutRestriction : missionDetails.getPlaneLoadoutRestrictions()) {
            if (planeName.equals(loadoutRestriction.getPlane())) {
                weaponsCount++;
                newPlane = false;
            } else {
                planeName = loadoutRestriction.getPlane();
                newPlane = true;

            }
            if (newPlane) {
                if (loadoutRestrictionTableModel.getRowCount() > 0) {
                    loadoutRestrictionTableModel.setValueAt(weaponsCount + " Restrictions", loadoutRestrictionTableModel.getRowCount() - 1, loadoutRestrictionTableModel.findColumn("# Restrictions"));
                }
                if (loadoutRestriction.getArmy() == MainController.REDARMY) {
                    armyString = "Red";
                } else {
                    armyString = "Blue";
                }
                Object[] restrictedLoadout = new Object[] { armyString, loadoutRestriction.getPlane(), weaponsCount + " Restrictions" };
                loadoutRestrictionTableModel.addRow(restrictedLoadout);
                weaponsCount = 1;
            }
        }
        if (loadoutRestrictionTableModel.getRowCount() > 0) {
            loadoutRestrictionTableModel.setValueAt(weaponsCount + " Restrictions", loadoutRestrictionTableModel.getRowCount() - 1, loadoutRestrictionTableModel.findColumn("# Restrictions"));
        }
    }

    public void loadGUI() {
        String armyString;

        missionTimeLeftTF.setText(Long.toString(missionDetails.getTimeLimit()));
        missionTimeLeftTF.setValue(missionDetails.getTimeLimit() / 60000);
        redCountObjectivesNeededTF.setValue(missionDetails.getRedCountObjectivesNeeded());
        blueCountObjectivesNeededTF.setValue(missionDetails.getBlueCountObjectivesNeeded());
        redTargetObjectivesNeededTF.setValue(missionDetails.getRedTargetObjectivesNeeded());
        blueTargetObjectivesNeededTF.setValue(missionDetails.getBlueTargetObjectivesNeeded());

        if (missionDetails.getObjectiveType() == Mission.MissionObjectiveType.TARGET) {
            missionObjectiveComboBox.setSelectedItem("Target Area");
        } else if (missionDetails.getObjectiveType() == Mission.MissionObjectiveType.COUNT) {
            missionObjectiveComboBox.setSelectedItem("Count");
        } else if (missionDetails.getObjectiveType() == Mission.MissionObjectiveType.BOTH) {
            missionObjectiveComboBox.setSelectedItem("Both");
        } else {
            missionObjectiveComboBox.setSelectedItem("Either");
        }

        for (MissionCountObjective countObjective : missionDetails.getCountObjectives()) {
            if (countObjective.getArmy() == MainController.REDARMY) {
                armyString = "Red";
            } else {
                armyString = "Blue";
            }
            Object[] objective = { armyString, countObjective.getObjectType().toString(), countObjective.getMissionStartCount(), countObjective.getNumberToDestroy() };
            countObjectiveTableModel.addRow(objective);
        }
        for (MissionTargetObjective targetObjective : missionDetails.getTargetObjectives()) {
            if (targetObjective.getArmy() == MainController.REDARMY) {
                armyString = "Red";
            } else if (targetObjective.getArmy() == MainController.BLUEARMY) {
                armyString = "Blue";
            } else {
                armyString = "None";
            }
            Object[] objective = new Object[] { armyString, targetObjective.getMapGridLocation(), targetObjective.getTargetDesc(), targetObjective.getTotalTargets(), targetObjective.getNumberToDestroy(), targetObjective };
            targetObjectiveTableModel.addRow(objective);
        }

        loadLoadoutRestrictions();

        DefaultMutableTreeNode armyRoot;

        for (int i = 0; i < missionDetails.getAerodromes().size(); i++) {
            if (missionDetails.getAerodromes().get(i).getArmy() == MainController.REDARMY) {
                armyString = "Red";
                armyRoot = redPlaneTreeRoot;
            } else {
                armyString = "Blue";
                armyRoot = bluePlaneTreeRoot;
            }
            String aerodrome = i + "-" + missionDetails.getAerodromes().get(i).getAerodromeMapGrid();
            DefaultMutableTreeNode aerodromeTreeNode = new DefaultMutableTreeNode(aerodrome);
            armyRoot.add(aerodromeTreeNode);

            Iterator<String> it = missionDetails.getAerodromes().get(i).getPlanes().keySet().iterator();
            while (it.hasNext()) {
                // Key is actually name but this is for clarity
                String key = it.next();
                aerodromeTreeNode.add(new DefaultMutableTreeNode(key));
                if (missionDetails.getAerodromes().get(i).getPlanes().get(key).getPlanesInUseLimit() > 0 || missionDetails.getAerodromes().get(i).getPlanes().get(key).getPlaneTotalLimit() > 0) {
                    Object[] rowData = new Object[] { armyString, aerodrome, key, missionDetails.getAerodromes().get(i).getPlanes().get(key).getPlanesInUseLimit(), missionDetails.getAerodromes().get(i).getPlanes().get(key).getPlaneTotalLimit(),
                            missionDetails.getAerodromes().get(i) };
                    planeLimitTableModel.addRow(rowData);
                }
            }
        }
        planeTreeModel.reload();
        planeTreeModel.nodeStructureChanged(redPlaneTreeRoot);
        planeTreeModel.nodeStructureChanged(bluePlaneTreeRoot);
    }  // End load GUI

    private void clearForm() {
        missionDetails = null;
        missionMissionNameLabel.setText("No Mission");
        missionTimeLeftTF.setValue(0);
        redCountObjectivesNeededTF.setValue(0);
        blueCountObjectivesNeededTF.setValue(0);
        redTargetObjectivesNeededTF.setValue(0);
        blueTargetObjectivesNeededTF.setValue(0);
        missionObjectiveComboBox.setSelectedItem(null);
        redPlaneTreeRoot.removeAllChildren();
        bluePlaneTreeRoot.removeAllChildren();
        planeTreeModel.reload();
        planeTreeModel.nodeStructureChanged(redPlaneTreeRoot);
        planeTreeModel.nodeStructureChanged(bluePlaneTreeRoot);

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
        dataChanged = false;
        missionStatusMessageLabel.setText(" ");
    }

    private void saveData() {
        if (missionDetails != null) {
            missionDetails.setTimeLimit((Long) missionTimeLeftTF.getValue() * 60000);
            missionDetails.setRedCountObjectivesNeeded((Integer) redCountObjectivesNeededTF.getValue());
            missionDetails.setBlueCountObjectivesNeeded((Integer) blueCountObjectivesNeededTF.getValue());
            missionDetails.setRedTargetObjectivesNeeded((Integer) redTargetObjectivesNeededTF.getValue());
            missionDetails.setBlueTargetObjectivesNeeded((Integer) blueTargetObjectivesNeededTF.getValue());
            if (missionObjectiveComboBox.getSelectedItem().equals("Target Area")) {
                missionDetails.setObjectiveType(Mission.MissionObjectiveType.TARGET);
            } else if (missionObjectiveComboBox.getSelectedItem().equals("Count")) {
                missionDetails.setObjectiveType(Mission.MissionObjectiveType.COUNT);
            } else if (missionObjectiveComboBox.getSelectedItem().equals("Both")) {
                missionDetails.setObjectiveType(Mission.MissionObjectiveType.BOTH);
            } else {
                missionDetails.setObjectiveType(Mission.MissionObjectiveType.EITHER);
            }
        }
    }

    public static MissionParameters getMissionDetails() {
        return missionDetails;
    }

    public static void setMissionDetails(MissionParameters missionDetails) {
        MissionBuilderPanel.missionDetails = missionDetails;
    }

}
