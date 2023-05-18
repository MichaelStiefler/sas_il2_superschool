package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import mainController.MainController;
import model.Configuration;
import model.ConfigurationItem;
import utility.DirectoryRead;
import utility.DoubleEditor;
import utility.EachRowEditor;
import utility.FileRead;
import utility.FileWrite;
import utility.IntegerEditor;
import utility.SortedHashMap;
import viewController.MissionCycleController;
import viewController.ViewConfigurationController;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ConfigurationPanel extends JPanel {

    /**
     * 
     */
    private static final long       serialVersionUID   = 4063162464803544175L;
    private boolean                 dataChanged        = false;
    private boolean                 dynamicDataChanged = false;
    private JScrollPane             pingSettingsSP     = new JScrollPane();
    private JScrollPane             iconSettingsSP     = new JScrollPane();
    private JScrollPane             dynamicSettingsSP  = new JScrollPane();
    private JScrollPane             staticSettingsSP   = new JScrollPane();
    private JLabel                  title              = new JLabel();
    private JTextField              configNameTF       = new JTextField();
    private JComboBox               selectConfigFileComboBox;
    private static JComboBox        missionCycleComboBox;
    private static JComboBox        chatReciepientsComboBox;
    private JButton                 loadConfigButton   = new JButton();
    private JButton                 saveConfigButton   = new JButton();
    private JButton                 cancelButton       = new JButton();
    private JButton                 reloadButton       = new JButton();
    private ConfigurationTableModel pingTableModel;
    private ConfigurationTableModel iconTableModel;
    private ConfigurationTableModel dynamicTableModel;
    private ConfigurationTableModel staticTableModel;
    private JTable                  pingTable;
    private JTable                  iconTable;
    private JTable                  dynamicTable;
    private JTable                  staticTable;
    private JLabel                  statusMessageLabel = new JLabel();
    private static Configuration    newConfig          = ViewConfigurationController.getConfiguration();
    private static String           configDirectory    = ViewConfigurationController.getConfigDirectory();

    public ConfigurationPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ConfigurationTableModel extends DefaultTableModel {
        /**
         * 
         */
        private static final long serialVersionUID = -5616887741540705124L;

        private static final int  ROW              = 0;

        Class                     types[]          = new Class[] { String.class, String.class, String.class };

        public ConfigurationTableModel(String[] columnNames) {
            super(columnNames, ROW);
        }

        public Class getColumnClass(int c) {
            return types[c];
        }

        public boolean isCellEditable(int row, int col) {
            // Note that the data/cell address is constant,
            // no matter where the cell appears onscreen.
            if (col < (this.getColumnCount() - 1))
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

        title.setText("Configuration: ");
        title.setBounds(345, 10, 75, 20);
        configNameTF.setText("");
        configNameTF.setName("configNameTF"); // NOI18N
        configNameTF.setToolTipText("Enter a Name for the Configuration");
        configNameTF.setBounds(425, 10, 100, 20);
        configNameTF.addActionListener(new NameListener());
        add(configNameTF);
        pingSettingsSP.setBounds(85, 35, 300, 130);
        pingSettingsSP.setBorder(BorderFactory.createTitledBorder("Ping Kick"));
        iconSettingsSP.setBounds(410, 35, 300, 130);
        iconSettingsSP.setBorder(BorderFactory.createTitledBorder("Icons"));
        dynamicSettingsSP.setBounds(15, 180, 370, 240);
        dynamicSettingsSP.setBorder(BorderFactory.createTitledBorder("Dynamic Settings"));
        staticSettingsSP.setBounds(410, 180, 370, 240);
        staticSettingsSP.setBorder(BorderFactory.createTitledBorder("Static Settings"));
        ArrayList<String> dirArray = DirectoryRead.getDirectories();
        String[] directories = new String[dirArray.size()];
        directories = dirArray.toArray(directories);

        selectConfigFileComboBox = new JComboBox(directories);
        selectConfigFileComboBox.setBounds(295, 430, 100, 22);
        selectConfigFileComboBox.setToolTipText("Select the Configuration to Load");

        loadConfigButton.setText("Load Selected");
        loadConfigButton.setBounds(407, 430, 125, 25);
        loadConfigButton.setToolTipText("Load the Selected Configuration");
        loadConfigButton.addActionListener(new LoadConfigurationListener());

        saveConfigButton.setText("Save");
        saveConfigButton.setBounds(592, 430, 73, 25);
        saveConfigButton.setToolTipText("Save Configuration changes");
        saveConfigButton.addActionListener(new SaveConfigurationListener());

        cancelButton.setText("Cancel");
        cancelButton.setBounds(677, 430, 73, 25);
        cancelButton.setToolTipText("Cancel any changes to configuration");
        cancelButton.addActionListener(new CancelConfigurationListener());

        reloadButton.setText("Reload Files");
        reloadButton.setBounds(612, 470, 125, 25);
        reloadButton.setToolTipText("Reload Data Files");
        reloadButton.addActionListener(new ReloadListener());

        statusMessageLabel.setText("Loaded Startup Configuration ( " + newConfig.getConfigName() + " )");
        statusMessageLabel.setBounds(295, 470, 200, 20);
        statusMessageLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        String[] columnHeader = { "key", "Setting", "Value" };
        pingTableModel = new ConfigurationTableModel(columnHeader);

        pingTableModel.addTableModelListener(new PingTableUpdate());
        pingTable = new javax.swing.JTable(pingTableModel);
        pingTable.setRowHeight(20);
        pingTable.setOpaque(true);
        pingTable.setBackground(Color.cyan);
        pingTable.setName("pingTable"); // NOI18N
        pingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pingTable.removeColumn(pingTable.getColumn("key"));
        pingSettingsSP.getViewport().add(pingTable);
        pingSettingsSP.setOpaque(true);
//
        iconTableModel = new ConfigurationTableModel(columnHeader);
        iconTableModel.addTableModelListener(new IconTableUpdate());
        iconTable = new javax.swing.JTable(iconTableModel);
        iconTable.setRowHeight(20);
        iconTable.setOpaque(true);
        iconTable.setBackground(Color.cyan);
        iconTable.setName("iconTable"); // NOI18N
        iconTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        iconTable.removeColumn(iconTable.getColumn("key"));
        iconSettingsSP.getViewport().add(iconTable);
        iconSettingsSP.setOpaque(true);

        dynamicTableModel = new ConfigurationTableModel(columnHeader);
        dynamicTableModel.addTableModelListener(new DynamicTableUpdate());
        dynamicTable = new javax.swing.JTable(dynamicTableModel);
        dynamicTable.setRowHeight(20);
        dynamicTable.setOpaque(true);
        dynamicTable.setBackground(Color.cyan);
        dynamicTable.setName("dynamicTable"); // NOI18N
        dynamicTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dynamicTable.removeColumn(dynamicTable.getColumn("key"));
        dynamicSettingsSP.getViewport().add(dynamicTable);
        dynamicSettingsSP.setOpaque(true);

        staticTableModel = new ConfigurationTableModel(columnHeader);
        staticTableModel.addTableModelListener(new StaticTableUpdate());
        staticTableModel.addRow(new Object[] { " ", " ", " " });
        staticTableModel.removeRow(0);
        staticTable = new javax.swing.JTable(staticTableModel);
        staticTable.setRowHeight(20);
        staticTable.setOpaque(true);
        staticTable.setBackground(Color.cyan);
        staticTable.setName("staticTable"); // NOI18N
        staticTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        staticTable.removeColumn(staticTable.getColumn("key"));
        staticSettingsSP.getViewport().add(staticTable);
        staticSettingsSP.setOpaque(true);

        add(selectConfigFileComboBox, null);
        add(loadConfigButton, null);
        add(cancelButton, null);
        add(reloadButton, null);
        add(saveConfigButton, null);
        add(pingSettingsSP, null);
        add(iconSettingsSP, null);
        add(dynamicSettingsSP, null);
        add(staticSettingsSP, null);
        add(statusMessageLabel, null);
        add(title, null);

        Object[] missionCycleList = MissionCycleController.getMissionCycleList();
        missionCycleComboBox = new JComboBox(missionCycleList);
        Object[] chatReciepientList = ConfigurationItem.ChatReciepients.values();
        chatReciepientsComboBox = new JComboBox(chatReciepientList);

        populateConfigurationSettings(newConfig);

    }  // End jbInit

    class NameListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            {
                dataChanged = true;
                ViewConfigurationController.updateConfigName(newConfig, configNameTF.getText());
            }
        }
    }

    class PingTableUpdate implements TableModelListener {

        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE) {
                try {

                    @SuppressWarnings("unused")
                    int row = e.getFirstRow();
                    String name = (String) pingTableModel.getValueAt(e.getFirstRow(), pingTableModel.findColumn("key"));
                    Object value = pingTableModel.getValueAt(e.getFirstRow(), pingTableModel.findColumn("Value"));
                    ViewConfigurationController.updateConfigurationItem(newConfig, newConfig.getPingSettings(), name, value);
                    dataChanged = true;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Invalid Configuration Setting Error:" + ex, "Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    class IconTableUpdate implements TableModelListener {

        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE) {
                try {

                    @SuppressWarnings("unused")
                    int row = e.getFirstRow();
                    String name = (String) iconTableModel.getValueAt(e.getFirstRow(), iconTableModel.findColumn("key"));
                    Object value = iconTableModel.getValueAt(e.getFirstRow(), iconTableModel.findColumn("Value"));
                    ViewConfigurationController.updateConfigurationItem(newConfig, newConfig.getIconSettings(), name, value);
                    dataChanged = true;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Invalid Configuration Setting Error:" + ex, "Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    class DynamicTableUpdate implements TableModelListener {

        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE) {
                try {

                    @SuppressWarnings("unused")
                    int row = e.getFirstRow();
                    String name = (String) dynamicTableModel.getValueAt(e.getFirstRow(), dynamicTableModel.findColumn("key"));
                    Object value = dynamicTableModel.getValueAt(e.getFirstRow(), dynamicTableModel.findColumn("Value"));
                    ViewConfigurationController.updateConfigurationItem(newConfig, newConfig.getDynamicVariables(), name, value);
                    dataChanged = true;
                    dynamicDataChanged = true;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Invalid Configuration Setting Error:" + ex, "Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    class StaticTableUpdate implements TableModelListener {

        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE) {
                try {

                    @SuppressWarnings("unused")
                    int row = e.getFirstRow();
                    String name = (String) staticTableModel.getValueAt(e.getFirstRow(), staticTableModel.findColumn("key"));
                    Object value = staticTableModel.getValueAt(e.getFirstRow(), staticTableModel.findColumn("Value"));
                    ViewConfigurationController.updateConfigurationItem(newConfig, newConfig.getStaticVariables(), name, value);
                    dataChanged = true;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Invalid Configuration Setting Error:" + ex, "Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    class SaveConfigurationListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (dataChanged) {
                configDirectory = MainController.FBDJROOTDIRECTORY + "/config/" + newConfig.getConfigName() + "/";
                FileWrite.writeFileSerialized(configDirectory, MainController.CONFIGFILENAME, newConfig);
                selectConfigFileComboBox.removeItem(newConfig.getConfigName());
                selectConfigFileComboBox.addItem(newConfig.getConfigName());
                selectConfigFileComboBox.setSelectedItem(newConfig.getConfigName());
                configNameTF.setText(newConfig.getConfigName());
                statusMessageLabel.setText("Saved Configuration ( " + newConfig.getConfigName() + " )");
                MainController.writeDebugLogFile(1, "Configuration Saved ( " + newConfig.getConfigName() + " )");
                JOptionPane.showMessageDialog(null, "Configuration ( " + newConfig.getConfigName() + " ) Saved", "Message", JOptionPane.INFORMATION_MESSAGE);
                ViewConfigurationController.setCurrentConfiguration(newConfig.getConfigName());
                if (dynamicDataChanged && newConfig.getConfigName().equals(MainController.CONFIG.getConfigName()) && MainController.CONNECTED) {
                    int n = JOptionPane.showConfirmDialog(null, "Apply Changes to Server Now ?", "Message", JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.YES_OPTION) {
                        ViewConfigurationController.applyDynamicChanges(newConfig);
                    }
                }
                dataChanged = false;
                dynamicDataChanged = false;
            }
        }
    }

    class CancelConfigurationListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (dataChanged) {
                int n = JOptionPane.showConfirmDialog(null, "Discard Changes to Configuration ?", "Message", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) { // Yes they want to edit it
                    newConfig = null;
                    clearConfigTableModel();
                }
            }
        }
    }

    class ReloadListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int n = JOptionPane.showConfirmDialog(null, "Reload Data Files ?", "Message", JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                ViewConfigurationController.reloadDataFiles();
            }
        }
    }

    class LoadConfigurationListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Now see if Configuration already exists
            newConfig = null;
            configDirectory = MainController.FBDJROOTDIRECTORY + "/config/" + selectConfigFileComboBox.getSelectedItem() + "/";
            if ((newConfig = (Configuration) FileRead.getFileSerialized(configDirectory + MainController.CONFIGFILENAME)) == null) {
                newConfig = new Configuration(selectConfigFileComboBox.getSelectedItem().toString());
                clearConfigTableModel();
                populateConfigurationSettings(newConfig);
                // }
            } else {
                clearConfigTableModel();
                populateConfigurationSettings(newConfig);
            }
            ViewConfigurationController.setCurrentConfiguration(newConfig.getConfigName());
            statusMessageLabel.setText("Loaded Configuration ( " + newConfig.getConfigName() + " )");

        }
    }

    // Populate the Table with the configuration data from configuration object

    private void setConfigEditor(ConfigurationTableModel tableModel, EachRowEditor rowEditor, ConfigurationItem item, int rowId) {

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
                case CHATRECIEPIENTS:
                    Object cvalue = item.getValue();
                    rowEditor.setEditorAt(rowId, new DefaultCellEditor(chatReciepientsComboBox));
                    tableModel.addRow(new Object[] { item.getName(), label, cvalue });
                    break;
                case MISSIONCYCLE:
                    Object mvalue = item.getValue();
                    rowEditor.setEditorAt(rowId, new DefaultCellEditor(missionCycleComboBox));
                    tableModel.addRow(new Object[] { item.getName(), label, mvalue });
                    break;
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "ConfigurationPanel.setConfigEditor - Error Unhandled Exception: " + ex);
        }
    }

    private void populateConfigurationSettings(Configuration config) {
        int rowId = 0;
        Iterator<String> item;
        HashMap<String, ConfigurationItem> sortedMap;
        ArrayList<String> sortedKeys;

        try {
            sortedMap = config.getPingSettings();
            EachRowEditor pingRowEditor = new EachRowEditor(pingTable);
            pingTable.getColumn("Value").setCellEditor(pingRowEditor);
            item = sortedMap.keySet().iterator();
            while (item.hasNext()) {
                String key = item.next();
                setConfigEditor(pingTableModel, pingRowEditor, config.getPingSettings().get(key), rowId);
                rowId++;
            }

            rowId = 0;
            sortedMap = config.getIconSettings();
            EachRowEditor iconRowEditor = new EachRowEditor(iconTable);
            iconTable.getColumn("Value").setCellEditor(iconRowEditor);
            item = sortedMap.keySet().iterator();
            while (item.hasNext()) {
                String key = item.next();
                setConfigEditor(iconTableModel, iconRowEditor, config.getIconSettings().get(key), rowId);
                rowId++;
            }

            rowId = 0;
            sortedMap = config.getDynamicVariables();
            sortedKeys = SortedHashMap.getSortedStringKeys(sortedMap);
            EachRowEditor dynamicRowEditor = new EachRowEditor(dynamicTable);
            dynamicTable.getColumn("Value").setCellEditor(dynamicRowEditor);
            for (String key : sortedKeys) {
                setConfigEditor(dynamicTableModel, dynamicRowEditor, config.getDynamicVariables().get(key), rowId);
                rowId++;
            }

            rowId = 0;
            HashMap<String, ConfigurationItem> sortedHashMap = config.getStaticVariables();
            sortedKeys = SortedHashMap.getSortedStringKeys(sortedHashMap);
            EachRowEditor staticRowEditor = new EachRowEditor(staticTable);
            staticTable.getColumn("Value").setCellEditor(staticRowEditor);
            for (String key : sortedKeys) {
                setConfigEditor(staticTableModel, staticRowEditor, config.getStaticVariables().get(key), rowId);
                rowId++;
            }
            configNameTF.setText(newConfig.getConfigName());
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "ConfigurationPanel.populateConfigurationSettings - Error Unhandled Exception for Config (" + config + "): " + ex);
        }
    }

    // Clear out the Configuration Table Data
    private void clearConfigTableModel() {
        int rowCount = 0;
        rowCount = pingTableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            pingTableModel.removeRow(0);
        }

        rowCount = iconTableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            iconTableModel.removeRow(0);
        }

        rowCount = dynamicTableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            dynamicTableModel.removeRow(0);
        }

        rowCount = staticTableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            staticTableModel.removeRow(0);
        }

        title.setText("Configuration Settings");

    }  // End clearConfigTableModel

    public static JComboBox getMissionCycleComboBox() {
        return missionCycleComboBox;
    }

}
