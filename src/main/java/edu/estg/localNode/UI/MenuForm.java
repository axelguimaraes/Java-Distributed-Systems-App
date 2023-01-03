package edu.estg.localNode.UI;

import com.google.gson.Gson;
import edu.estg.localNode.LocalServer;
import edu.estg.models.LocalNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class MenuForm extends JFrame{
    private JPanel mainPanel;
    private JPanel toolsPanel;
    private JPanel passengerPanel;
    private JLabel nameLabel;
    private JLabel notificationLabel;
    private JTextField notificationTextField;
    private JButton sendButton;
    private JList passengersList;
    private JLabel passengersLabel;
    private Gson jsonHelper;
    private LocalNode currentLocalNode;
    private LocalServer localServer;

    private StartFrame startFrame;

    public MenuForm(StartFrame startFrame, LocalServer localServer, LocalNode loggedLocalNode) {
        setContentPane(mainPanel);

        this.jsonHelper = new Gson();
        this.startFrame = startFrame;
        this.localServer = localServer;
        this.currentLocalNode = loggedLocalNode;
    }

    public void configFrame() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        setPreferredSize(new Dimension(900, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }
}
