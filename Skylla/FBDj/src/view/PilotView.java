package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class PilotView {
    JPanel     pilotListPanel;
    JPanel     pilotDetailsPanel;
    JPanel     pilotActionsPanel;
    JList      pilotList;
    JTextArea  pilotDetailsTA;
    JTextField pilotBanTF;
    JTextField sendTextTF;
    JButton    sendTextPlayerButton;
    JButton    sendTextAllButton;

    String[]   testList;

    public JPanel pilotList() {
        // Pilot list panel
        pilotListPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pilotListPanel.setBackground(Color.white);
        TitledBorder pilotListPanelTitle = BorderFactory.createTitledBorder("Pilot List");
        pilotListPanel.setBorder(pilotListPanelTitle);

        // Pilot list List
        testList = new String[] { "Pilot1", "Pilot2", "Pilot3", "Pilot4" }; // temp
        // for
        // testing
        pilotList = new JList(testList);
        pilotList.addListSelectionListener(new PilotListSelectListener());

        // Build pilot list panel
        pilotListPanel.add(pilotList);

        return pilotListPanel;
    }

    public JPanel pilotDetails() {
        // Detail panel
        pilotDetailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pilotDetailsPanel.setBackground(Color.white);
        TitledBorder pilotDetailsPanelTitle = BorderFactory.createTitledBorder("Pilot Details");
        pilotDetailsPanel.setBorder(pilotDetailsPanelTitle);

        // Pilot details TextArea
        pilotDetailsTA = new JTextArea("Pilot details go here.");

        // Build detail panel
        pilotDetailsPanel.add(pilotDetailsTA);

        return pilotDetailsPanel;
    }

    public JPanel pilotActions() {
        // Action panel
        pilotActionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        pilotActionsPanel.setBackground(Color.white);
        TitledBorder pilotActionsPanelTitle = BorderFactory.createTitledBorder("Controls");
        pilotActionsPanel.setBorder(pilotActionsPanelTitle);

        // Kick panel
        JPanel pilotKickPanel = new JPanel();
        pilotKickPanel.setBackground(Color.white);

        // Kick button
        JButton pilotKickButton = new JButton("Kick");
        pilotKickButton.addActionListener(new KickListener());

        // Build kick panel
        pilotKickPanel.add(pilotKickButton);

        // Ban panel
        JPanel pilotBanPanel = new JPanel();
        pilotBanPanel.setBackground(Color.white);

        // Ban button
        JButton pilotBanButton = new JButton("Ban");
        pilotBanButton.addActionListener(new BanListener());
        // Ban mins TextField
        pilotBanTF = new JTextField("0", 3);
        JLabel pilotBanTFLabel = new JLabel("mins");

        // Build ban panel
        pilotBanPanel.add(pilotBanTF);
        pilotBanPanel.add(pilotBanTFLabel);
        pilotBanPanel.add(pilotBanButton);

        // SendText panel
        JPanel sendTextPanel = new JPanel();
        sendTextPanel.setBackground(Color.white);

        // SendText TextField
        sendTextTF = new JTextField("", 10);
        JLabel sendTextLabel = new JLabel("Send Text");
        // SendTextToSelectedPlayer button
        sendTextPlayerButton = new JButton("To Selected Player");
        sendTextPlayerButton.addActionListener(new SendTextListener());
        // SendTextToAll button
        sendTextAllButton = new JButton("To All");
        sendTextAllButton.addActionListener(new SendTextListener());

        // Build sendtext panel
        sendTextPanel.add(sendTextTF);
        sendTextPanel.add(sendTextLabel);
        sendTextPanel.add(sendTextPlayerButton);
        sendTextPanel.add(sendTextAllButton);

        // Build action panel
        pilotActionsPanel.add(pilotKickPanel);
        pilotActionsPanel.add(pilotBanPanel);
        pilotActionsPanel.add(sendTextPanel);

        return pilotActionsPanel;
    }

    public JPanel createPanel() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Build main panel
        mainPanel.add(BorderLayout.CENTER, this.pilotList());
        mainPanel.add(BorderLayout.EAST, this.pilotDetails());
        mainPanel.add(BorderLayout.SOUTH, this.pilotActions());

        return mainPanel;
    }

    // Observer
    public void notifyObserver() {
        // Update pilot list
    }

    // Action listeners
    class PilotListSelectListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            // Only get 1 event - will get 2 otherwise
            if (e.getValueIsAdjusting()) {
                // Update pilot details
                System.out.println("PilotView.PilotListSelectListener.valueChanged! Selected: " + testList[pilotList.getSelectedIndex()]);
                pilotDetailsTA.setText("Selected: " + testList[pilotList.getSelectedIndex()]);
            }
        }
    }

    class KickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Make sure a valid pilot is selected
            if (pilotList.getSelectedIndex() > -1 && pilotList.getSelectedIndex() < testList.length) {
                // Kick pilot
                System.out.println("PilotView.KickListener.actionPerformed! Kick: " + testList[pilotList.getSelectedIndex()]);
            } else {
                System.out.println("PilotView.KickListener.actionPerformed! Kick: Invalid Pilot Selected: " + pilotList.getSelectedIndex());
            }
        }
    }

    class BanListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Make sure a valid pilot is selected
            if (pilotList.getSelectedIndex() > -1 && pilotList.getSelectedIndex() < testList.length) {
                // Ban pilot
                System.out.println("PilotView.BanListener.actionPerformed! Ban: " + testList[pilotList.getSelectedIndex()] + " for " + pilotBanTF.getText() + " mins");
            } else {
                System.out.println("PilotView.BanListener.actionPerformed! Ban: Invalid Pilot Selected: " + pilotList.getSelectedIndex());
            }
        }
    }

    class SendTextListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Make sure we have some text to send
            if (sendTextTF.getText().length() > 0) {
                // Send text to player
                if (e.getSource().equals(sendTextPlayerButton)) {
                    // Make sure a valid pilot is selected
                    if (pilotList.getSelectedIndex() > -1 && pilotList.getSelectedIndex() < testList.length) {
                        System.out.println("PilotView.SendTextListener.actionPerformed! Send Text to Player: " + testList[pilotList.getSelectedIndex()] + ": " + sendTextTF.getText());
                    } else {
                        System.out.println("PilotView.SendTextListener.actionPerformed! Send Text to Player: Invalid Pilot Selected: " + pilotList.getSelectedIndex());
                    }
                }
                // Send text to all
                else if (e.getSource().equals(sendTextAllButton)) {
                    System.out.println("PilotView.SendTextListener.actionPerformed! Send Text to All: " + sendTextTF.getText());
                }
            } else {
                System.out.println("PilotView.SendTextListener.actionPerformed! No Text to Send!");
            }
        }
    }

    // Test main
    public static void main(String[] args) {
        PilotView pilotView = new PilotView();

        JFrame mainFrame = new JFrame();
        mainFrame.add(pilotView.createPanel());

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(700, 500);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }
}