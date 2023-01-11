package edu.estg.userInterface.GUI;

import com.google.gson.Gson;
import edu.estg.userInterface.Client;
import edu.estg.utils.LocalNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LocalNodeMenuFrame extends JFrame {
    private JPanel mainPanel;
    private JLabel nameLabel;
    private JPanel passengersPanel;
    private JList passengersList;
    private JPanel namePanel;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private final Gson jsonHelper;
    private final LocalNode localNode;
    private final Client client;
    private final InitialFrame initialFrame;

    public LocalNodeMenuFrame(InitialFrame initialFrame, Client client, LocalNode localNode) {
        setContentPane(mainPanel);

        this.jsonHelper = new Gson();
        this.initialFrame = initialFrame;
        this.client = client;
        this.localNode = localNode;
        this.nameLabel.setText(this.localNode.getName());
    }

    public void configFrame() {
        addWindowListener(new WindowAdapter() {
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
