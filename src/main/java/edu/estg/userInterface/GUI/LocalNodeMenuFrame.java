package edu.estg.userInterface.GUI;

import com.google.gson.Gson;
import edu.estg.userInterface.Client;
import edu.estg.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class LocalNodeMenuFrame extends JFrame {
    private JPanel mainPanel;
    private JLabel nameLabel;
    private JPanel passengersPanel;
    private JList linesList;
    private JPanel namePanel;
    private JButton exitButton;
    private JButton button2;
    private JButton addTrainLineButton;
    private JList passengersList;
    private JScrollPane jScrollPane;
    private final Gson jsonHelper;
    private final LocalNode localNode;
    private ArrayList<String> trainLines;
    private final Client client;
    private final InitialFrame initialFrame;

    public LocalNodeMenuFrame(InitialFrame initialFrame, Client client, LocalNode localNode) {
        setContentPane(mainPanel);

        this.jsonHelper = new Gson();
        this.initialFrame = initialFrame;
        this.client = client;
        this.localNode = localNode;
        this.nameLabel.setText(this.localNode.getName());
        this.trainLines = this.localNode.getTrainLinesStringList();
        loadJList(jScrollPane, linesList, this.trainLines);
        configButtons();
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

    public void configButtons() {
        exitButton.addActionListener(e -> {
            exitButton.setEnabled(false);
            this.initialFrame.setVisible(true);
            this.dispose();
        });

        addTrainLineButton.addActionListener(e -> {
            String beginning = JOptionPane.showInputDialog("Beginning:");
            String end = JOptionPane.showInputDialog("End:");

            if (beginning.equals("") || end.equals("")) {
                showMessageDialog(new JFrame(), "Can't leave empty fields!","", ERROR_MESSAGE);
                return;
            }
            AddTrainLinesHelper addTrainLinesHelper = new AddTrainLinesHelper(this.localNode.getName(), beginning, end);
            Request<AddTrainLinesHelper> request = new Request<>(RequestType.ADD_TRAIN_LINE, addTrainLinesHelper);
            this.client.sendMessage(this.jsonHelper.toJson(request));
        });
    }

    private void loadJList(JScrollPane scrollPane, JList<String> list, ArrayList<String> data) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addAll(data);
        list.setModel(listModel);
        scrollPane.setViewportView(list);
    }

    public void processMessage(String message, RequestType type) {
        switch (type) {
            case FEEDBACK_ADD_TRAIN_LINE: // TODO: List not updating
                loadJList(jScrollPane, this.linesList, this.trainLines);
                break;
        }
    }
}
