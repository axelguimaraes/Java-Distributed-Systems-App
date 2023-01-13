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

public class TrainLinesListPopupWindow extends JFrame{
    private JPanel panel;
    private JList<String> trainLinesList;
    private JButton selectButton;
    private JButton cancelButton;
    private JScrollPane trainLinesScrollPanel;
    private ArrayList<LocalNode> localNodes;
    private final Client client;
    private final Passenger passenger;

    public TrainLinesListPopupWindow(ArrayList<LocalNode> localNodes, Client client, Passenger passenger) {
        this.passenger = passenger;
        this.client = client;
        this.localNodes = localNodes;
        configFrame();
        configTabTrainLinesToAdd();
    }

    private void configFrame() {
        setContentPane(panel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        setPreferredSize(new Dimension(300, 500));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void configTabTrainLinesToAdd() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < this.localNodes.size(); i++) {
            for (int j = 0; j < this.localNodes.get(i).getTrainLines().size(); j++) {
                list.add(this.localNodes.get(i).getTrainLines().get(j).toString());
            }
        }
        loadJList(trainLinesScrollPanel, trainLinesList, list);

        selectButton.addActionListener(e -> {
            if (trainLinesList.isSelectionEmpty()) {
                showMessageDialog(new JFrame(), "Choose a train line", "", ERROR_MESSAGE);
                return;
            }

            String selectedTrainLine = trainLinesList.getSelectedValue();
            AssociateTrainLineHelper associateTrainLineHelper = new AssociateTrainLineHelper(this.passenger.getUsername(), selectedTrainLine);

            Request<AssociateTrainLineHelper> request = new Request<>(RequestType.ASSOCIATE_TRAIN_LINE, associateTrainLineHelper);
            this.client.sendMessage(new Gson().toJson(request));
            dispose();
        });
    }

    private void loadJList(JScrollPane scrollPane, JList<String> list, ArrayList<String> data) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addAll(data);
        list.setModel(listModel);
        scrollPane.setViewportView(list);
    }
}

