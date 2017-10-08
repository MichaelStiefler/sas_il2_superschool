package view;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JToggleButton;

import mainController.MainController;
import mainController.StopMainController;

import org.jdesktop.application.Action;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The application's main frame.
 */
public class MainWindowView2 extends FrameView {

    public static MissionCyclePanel       missionCyclePanel   = new MissionCyclePanel();
    public static MissionStatusPanel      missionStatusPanel  = new MissionStatusPanel();
    public static ConfigurationPanel      configPanel         = new ConfigurationPanel();
    public static PilotsPanel             pilotsPanel         = new PilotsPanel();
    public static AdminPanel              adminsPanel         = new AdminPanel();
    public static MissionBuilderPanel     missionBuilderPanel = new MissionBuilderPanel();
    public static BannedPilotsPanel       bannedPilotsPanel   = new BannedPilotsPanel();
    public static ReservedNamePanel       reservedNamesPanel  = new ReservedNamePanel();
    public static WarningPointsPanel      warningPointsPanel  = new WarningPointsPanel();

    private javax.swing.JPanel            jPanel1;
    private javax.swing.JTabbedPane       jTabbedPane1;
    private javax.swing.JPanel            mainPanel;
    private javax.swing.JMenuBar          menuBar;
    private javax.swing.ButtonGroup       serverButtonGroup;
    private javax.swing.JToggleButton     serverConnectToggleButton;
    private javax.swing.JLabel            serverConnectionLabel;
    private javax.swing.JToggleButton     serverDisconnectToggleButton;
    private javax.swing.ButtonGroup       statsButtonGroup;
    private javax.swing.JToggleButton     statsOffToggleButton;
    private javax.swing.JToggleButton     statsOnToggleButton;
    private javax.swing.JLabel            statsServerLabel;
    private static javax.swing.JTextField statusMessageTF;
    private javax.swing.JPanel            statusPanel;

    private JDialog                       aboutBox;

    public MainWindowView2(SingleFrameApplication app) {
        super(app);
        initComponents();
    }

    @Override
    public void setFrame(JFrame arg0) {
        super.setFrame(arg0);
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = MainWindowApp2.getApplication().getMainFrame();
            aboutBox = new MainWindowAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        MainWindowApp2.getApplication().show(aboutBox);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        statusPanel = new javax.swing.JPanel();
        serverConnectionLabel = new javax.swing.JLabel();
        serverConnectToggleButton = new javax.swing.JToggleButton();
        serverDisconnectToggleButton = new javax.swing.JToggleButton();
        statsServerLabel = new javax.swing.JLabel();
        statsOnToggleButton = new javax.swing.JToggleButton();
        statsOffToggleButton = new javax.swing.JToggleButton();
        statusMessageTF = new javax.swing.JTextField();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        serverButtonGroup = new javax.swing.ButtonGroup();
        statsButtonGroup = new javax.swing.ButtonGroup();

        serverButtonGroup.add(serverConnectToggleButton);
        serverButtonGroup.add(serverDisconnectToggleButton);
        statsButtonGroup.add(statsOnToggleButton);
        statsButtonGroup.add(statsOffToggleButton);

        mainPanel.setMaximumSize(new java.awt.Dimension(800, 600));
        mainPanel.setMinimumSize(new java.awt.Dimension(800, 600));
        mainPanel.setName("mainPanel"); // NOI18N

        jTabbedPane1.setMaximumSize(new java.awt.Dimension(790, 527));
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(790, 527));
        jTabbedPane1.setName("jTabbedPane1"); // NOI18N
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(790, 527));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(view.MainWindowApp2.class).getContext().getResourceMap(MainWindowView2.class);

        String frameTitle = "FBDj 2.0 FAC Edition - " + MainController.CONFIG.getConfigName();
        this.getFrame().setTitle(frameTitle);
        ImageIcon icon = new ImageIcon("Images/FBDjIcon.png");
        Image image = icon.getImage();
        this.getFrame().setIconImage(image);
        missionStatusPanel.setName("MissionStatusPanel"); // NOI18N
        jTabbedPane1.addTab(resourceMap.getString("MissionStatusPanel.TabConstraints.tabTitle"), missionStatusPanel); // NOI18N

        pilotsPanel.setName("pilotsPanel"); // NOI18N
        jTabbedPane1.addTab(resourceMap.getString("pilotsPanel.TabConstraints.tabTitle"), pilotsPanel); // NOI18N

        adminsPanel.setName("AdminPanel"); // NOI18N
        jTabbedPane1.addTab(resourceMap.getString("AdminsPanel.TabConstraints.tabTitle"), adminsPanel); // NOI18N

        reservedNamesPanel.setName("ReservedNamesPanel"); // NOI18N
        jTabbedPane1.addTab(resourceMap.getString("reservedNamesPanel.TabConstraints.tabTitle"), reservedNamesPanel); // NOI18N

        bannedPilotsPanel.setName("bannedPilotsPanel");
        jTabbedPane1.addTab("Ban List", bannedPilotsPanel);

        configPanel.setName("ConfigPanel"); // NOI18N
        jTabbedPane1.addTab(resourceMap.getString("ConfigPanel.TabConstraints.tabTitle"), configPanel); // NOI18N

        missionCyclePanel.setName("missionCyclePanel"); // NOI18N
        jTabbedPane1.addTab("MissionCycle", missionCyclePanel);

        missionBuilderPanel.setName("MissionBuilderPanel");
        jTabbedPane1.addTab("Mission Builder", missionBuilderPanel);

        warningPointsPanel.setName("WarningPointsPanel");
        jTabbedPane1.addTab("Warning Points", warningPointsPanel);

        statusPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        statusPanel.setMaximumSize(new java.awt.Dimension(790, 30));
        statusPanel.setMinimumSize(new java.awt.Dimension(790, 30));
        statusPanel.setName("statusPanel"); // NOI18N
        statusPanel.setPreferredSize(new java.awt.Dimension(790, 30));

        serverConnectionLabel.setFont(resourceMap.getFont("serverConnectionLabel.font")); // NOI18N
        serverConnectionLabel.setText(resourceMap.getString("serverConnectionLabel.text")); // NOI18N
        serverConnectionLabel.setName("serverConnectionLabel"); // NOI18N

        serverConnectToggleButton.setText(resourceMap.getString("serverConnectToggleButton.text")); // NOI18N
        serverConnectToggleButton.setToolTipText(resourceMap.getString("serverConnectToggleButton.toolTipText")); // NOI18N
        serverConnectToggleButton.setName("serverConnectToggleButton"); // NOI18N
        serverConnectToggleButton.addActionListener(new ServerConnectionListener());

        serverDisconnectToggleButton.setText(resourceMap.getString("serverDisconnectToggleButton.text")); // NOI18N
        serverDisconnectToggleButton.setToolTipText(resourceMap.getString("serverDisconnectToggleButton.toolTipText")); // NOI18N
        serverDisconnectToggleButton.setName("serverDisconnectToggleButton"); // NOI18N
        serverDisconnectToggleButton.addActionListener(new ServerConnectionListener());

        statsServerLabel.setFont(resourceMap.getFont("statsServerLabel.font")); // NOI18N
        statsServerLabel.setText(resourceMap.getString("statsServerLabel.text")); // NOI18N
        statsServerLabel.setName("statsServerLabel"); // NOI18N

        statsOnToggleButton.setText(resourceMap.getString("statsOnToggleButton.text")); // NOI18N
        statsOnToggleButton.setToolTipText(resourceMap.getString("statsOnToggleButton.toolTipText")); // NOI18N
        statsOnToggleButton.setName("statsOnToggleButton"); // NOI18N
        statsOnToggleButton.addActionListener(new StatsConnectionListener());

        statsOffToggleButton.setText(resourceMap.getString("statsOffToggleButton.text")); // NOI18N
        statsOffToggleButton.setToolTipText(resourceMap.getString("statsOffToggleButton.toolTipText")); // NOI18N
        statsOffToggleButton.setName("statsOffToggleButton"); // NOI18N
        statsOffToggleButton.addActionListener(new StatsConnectionListener());

        statusMessageTF.setEditable(false);
        statusMessageTF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        statusMessageTF.setText(resourceMap.getString("statusMessageTF.text")); // NOI18N
        statusMessageTF.setBorder(null);
        statusMessageTF.setFocusable(false);
        statusMessageTF.setName("statusMessageTF"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                statusPanelLayout.createSequentialGroup().addContainerGap().addComponent(serverConnectionLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(serverConnectToggleButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(serverDisconnectToggleButton).addGap(18, 18, 18)
                        .addComponent(statusMessageTF, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(statsServerLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(statsOnToggleButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(statsOffToggleButton).addGap(22, 22, 22)));
        statusPanelLayout.setVerticalGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(serverConnectionLabel).addComponent(serverConnectToggleButton).addComponent(serverDisconnectToggleButton).addComponent(statsOnToggleButton)
                        .addComponent(statsOffToggleButton).addComponent(statsServerLabel).addComponent(statusMessageTF, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)));

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                mainPanelLayout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(statusPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 807, Short.MAX_VALUE)).addGap(22, 22, 22)));
        mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                javax.swing.GroupLayout.Alignment.TRAILING,
                mainPanelLayout.createSequentialGroup().addContainerGap().addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 527, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(view.MainWindowApp2.class).getContext().getActionMap(MainWindowView2.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        jPanel1.setName("jPanel1"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
        if (MainController.CONFIG.isRecordStats()) {
            statsOnToggleButton.setSelected(true);
            MainController.statsConnect();
        }
        if (MainController.CONFIG.isAutoStart()) {
            serverConnectToggleButton.setSelected(true);
            new Thread(new MainController()).start();
            statusMessageTF.setText("IL2 Connection Started");
        }

    }// </editor-fold>//GEN-END:initComponents

    class ServerConnectionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JToggleButton sourceButton = (JToggleButton) e.getSource();
            if (e.getActionCommand().equals("Connect") && sourceButton.isSelected()) {
                new Thread(new MainController()).start();
            } else if (e.getActionCommand().equals("Disconnect") && sourceButton.isSelected()) {
                new Thread(new StopMainController()).start();
            }
        }
    }

    class StatsConnectionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JToggleButton sourceButton = (JToggleButton) e.getSource();
            if (e.getActionCommand().equals("On") && sourceButton.isSelected()) {
                MainController.statsConnect();
            } else if (e.getActionCommand().equals("Off") && sourceButton.isSelected()) {
                MainController.statsDisconnect();
            }
        }
    }

    public static void setStatusMessageTF(String value) {
        statusMessageTF.setText(value);
    }

}