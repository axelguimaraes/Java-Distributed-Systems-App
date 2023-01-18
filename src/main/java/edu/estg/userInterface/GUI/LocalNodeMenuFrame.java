package edu.estg.userInterface.GUI;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
    private JList<String> linesList;
    private JPanel namePanel;
    private JButton exitButton;
    private JButton sendMessageToPassengersButton;
    private JButton addTrainLineButton;
    private JScrollPane jScrollPane;
    private JList<String> listPassengerMessages;
    private JScrollPane scrollPassengerMessages;
    private final Gson jsonHelper;
    private final LocalNode localNode;
    private ArrayList<String> trainLines;
    private final Client client;
    private final InitialFrame initialFrame;
    private ArrayList<MessageFromPassenger> messageFromPassengers;

    public LocalNodeMenuFrame(InitialFrame initialFrame, Client client, LocalNode localNode) {
        setContentPane(mainPanel);

        this.jsonHelper = new Gson();
        this.initialFrame = initialFrame;
        this.client = client;
        this.localNode = localNode;
        this.messageFromPassengers = new ArrayList<>();
        this.nameLabel.setText("Local node: " + this.localNode.getName());
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
        configPassengerMessagesList();
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
                showMessageDialog(new JFrame(), "Can't leave empty fields!", "", ERROR_MESSAGE);
                return;
            }
            AddTrainLinesToLocalNodeHelper addTrainLinesToLocalNodeHelper = new AddTrainLinesToLocalNodeHelper(this.localNode.getName(), beginning, end);
            Request<AddTrainLinesToLocalNodeHelper> request = new Request<>(RequestType.ADD_TRAIN_LINE, addTrainLinesToLocalNodeHelper);
            this.client.sendMessage(this.jsonHelper.toJson(request));
        });

        sendMessageToPassengersButton.addActionListener(e -> {
            String message = JOptionPane.showInputDialog("Message:");
            if (linesList.isSelectionEmpty()) {
                showMessageDialog(new JFrame(), "Please select the affected line!","", ERROR_MESSAGE);
                return;
            }

            ArrayList<TrainLine> trainLinesSelected = new ArrayList<>();
            for (int i = 0; i < linesList.getSelectedValuesList().size(); i++) {
                trainLinesSelected.add(MessageFromPassenger.getTrainLineFromString(linesList.getSelectedValuesList().get(i)));
            }

            MessageToPassenger messageToPassenger = new MessageToPassenger(this.localNode, trainLinesSelected, message);

            Request<MessageToPassenger> request = new Request<>(RequestType.PASSENGER_MESSAGE_FROM_NODE, messageToPassenger);
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
            case FEEDBACK_ADD_TRAIN_LINE:
                AddTrainLinesToLocalNodeHelper line = this.jsonHelper.<Response<AddTrainLinesToLocalNodeHelper>>fromJson(message, new TypeToken<Response<AddTrainLinesToLocalNodeHelper>>() {
                }.getType()).getData();
                this.trainLines.add(line.getLine());

                loadJList(jScrollPane, this.linesList, this.trainLines);
                break;

            case PASSENGER_MESSAGE_TO_NODE:
                MessageFromPassenger messageFromPassenger = this.jsonHelper.<Response<MessageFromPassenger>>fromJson(message, new TypeToken<Response<MessageFromPassenger>>() {
                }.getType()).getData();

                if (!(messageFromPassenger.getLocalNode().equals(this.localNode))) {
                    break;
                }

                messageFromPassengers.add(messageFromPassenger);
                configPassengerMessagesList();
        }
    }

    private void configPassengerMessagesList() {
        ArrayList<String> messages = new ArrayList<>();
        for (int i = 0; i < this.messageFromPassengers.size(); i++) {
            messages.add(this.messageFromPassengers.get(i).toString());
        }

        loadJList(scrollPassengerMessages, listPassengerMessages, messages);
    }
}
