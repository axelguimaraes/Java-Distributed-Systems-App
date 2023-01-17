package edu.estg.userInterface.GUI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edu.estg.userInterface.Client;
import edu.estg.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.swing.JOptionPane.*;

public class InitialFrame extends JFrame {
    private JPanel panel;
    private JTabbedPane tabbedPane;
    private JTextField usernameLoginTextField;
    private JPasswordField passwordLoginTextField;
    private JButton loginButton;
    private JTextField nameRegisterTextField;
    private JRadioButton localNodeRegisterRadioButton;
    private JRadioButton passengerRegisterRadioButton;
    private JTextField usernameRegisterTextField;
    private JPasswordField passwordRegisterTextField;
    private JButton registerButton;
    private JRadioButton localNodeLoginRadioButton;
    private JRadioButton passengerLoginRadioButton;
    private JList<String> trainList;
    private JPanel listPanel;
    private JScrollPane listScrollPanel;

    private final Gson jsonHelper;
    private final Client client;
    private LocalNodeMenuFrame localNodeMenuFrame;
    private PassengerMenuFrame passengerMenuFrame;
    private ArrayList<LocalNode> currentLocalNodes;
    private ArrayList<String> linesToAdd;

    public InitialFrame(Client client) throws IOException {
        this.jsonHelper = new GsonBuilder().serializeNulls().create();
        this.client = client;
        this.currentLocalNodes = new ArrayList<>();
        Request<ArrayList<LocalNode>> request = new Request<>(RequestType.GET_CURRENT_LOCAL_NODES);
        this.client.sendMessage(this.jsonHelper.toJson(request));
        this.linesToAdd = new ArrayList<>();

    }

    private void configButtons() {
        this.linesToAdd.addAll(linesToAdd());
        loadJList(this.listScrollPanel, this.trainList, this.linesToAdd);
        // Login group
        ButtonGroup loginButtonGroup = new ButtonGroup();
        loginButtonGroup.add(localNodeLoginRadioButton);
        loginButtonGroup.add(passengerLoginRadioButton);
        localNodeLoginRadioButton.setSelected(true);

        // Register group
        ButtonGroup registerButtonGroup = new ButtonGroup();
        registerButtonGroup.add(localNodeRegisterRadioButton);
        registerButtonGroup.add(passengerRegisterRadioButton);
        localNodeRegisterRadioButton.setSelected(true);

        AtomicBoolean isLoginLocalNode = new AtomicBoolean(true);
        localNodeLoginRadioButton.addActionListener(e -> {
            isLoginLocalNode.set(true);
        });

        passengerLoginRadioButton.addActionListener(e -> {
            isLoginLocalNode.set(false);
        });

        AtomicBoolean isRegisterLocalNode = new AtomicBoolean(true);
        localNodeRegisterRadioButton.addActionListener(e -> {
            isRegisterLocalNode.set(true);
            this.trainList.clearSelection();
            this.trainList.disable();
        });

        passengerRegisterRadioButton.addActionListener(e -> {
            isRegisterLocalNode.set(false);
            this.trainList.enable();
        });


        loginButton.addActionListener(e -> {
            Login login = new Login(usernameLoginTextField.getText(), String.valueOf(passwordLoginTextField.getPassword()));

            // if is local node login
            if (isLoginLocalNode.get()) {
                Request<Login> request = new Request<>(RequestType.LOCAL_NODE_LOGIN, login);
                this.client.sendMessage(new Gson().toJson(request));

            // if is passenger login
            } else if (!isLoginLocalNode.get()) {
                Request<Login> request = new Request<>(RequestType.PASSENGER_LOGIN, login);
                this.client.sendMessage(new Gson().toJson(request));
            }
        });

        registerButton.addActionListener(e -> {

            // if is local node register
            if (isRegisterLocalNode.get()) {
                LocalNode localNode = new LocalNode(nameRegisterTextField.getText(), usernameRegisterTextField.getText(), String.valueOf(passwordRegisterTextField.getPassword()));
                LocalNodeRegister localNodeRegister = new LocalNodeRegister(localNode);
                Request<LocalNodeRegister> request = new Request<>(RequestType.LOCAL_NODE_REGISTER, localNodeRegister);
                this.client.sendMessage(new Gson().toJson(request));

            // if is passenger register
            } else if (!isRegisterLocalNode.get()) {
                ArrayList<TrainLine> linesAdded = new ArrayList<>();
                for (int i = 0; i < trainList.getSelectedValuesList().size(); i++) {
                    linesAdded.add(getTrainLineFromString(trainList.getSelectedValuesList().get(i)));
                }

                Passenger passenger = new Passenger(nameRegisterTextField.getText(), usernameRegisterTextField.getText(), String.valueOf(passwordRegisterTextField.getPassword()), linesAdded);

                PassengerRegister passengerRegister = new PassengerRegister(passenger);
                Request<PassengerRegister> request = new Request<>(RequestType.PASSENGER_REGISTER, passengerRegister);
                this.client.sendMessage(new Gson().toJson(request));
            }
        });
    }

    private void cleanVariables() {
        // LOGIN
        usernameLoginTextField.setText("");
        passwordLoginTextField.setText("");

        // REGISTER
        nameRegisterTextField.setText("");
        usernameRegisterTextField.setText("");
        passwordRegisterTextField.setText("");
    }

    public void configFrame() {
        setContentPane(panel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        setPreferredSize(new Dimension(600, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        this.trainList.getSelectionModel().addSelectionInterval(0, this.linesToAdd.size());
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
    }

    public synchronized void processResponse(String message) {
        Response<Object> response = jsonHelper.<Response<Object>>fromJson(message, Response.class);

        if (response.status != ResponseStatus.OK) {
            showMessageDialog(new JDialog(), response.message, "", ERROR_MESSAGE);
            return;
        }

        switch (response.type) {
            case LOCAL_NODE_LOGIN:
                LocalNodeLogin localNodeLogin = this.jsonHelper.<Response<LocalNodeLogin>>fromJson(message, new TypeToken<Response<LocalNodeLogin>>() {
                }.getType()).getData();

                try {
                    this.client.joinGroups(localNodeLogin.getListIpsToJoin());
                } catch (IOException e) {
                    dialogShow("Unexpected error joining groups!", ERROR_MESSAGE, 1000);
                }

                this.localNodeMenuFrame = new LocalNodeMenuFrame(this, this.client, localNodeLogin.getLocalNode());
                this.localNodeMenuFrame.configFrame();
                cleanVariables();
                this.setVisible(false);
                break;

            case PASSENGER_LOGIN:
                PassengerLogin passengerLogin = this.jsonHelper.<Response<PassengerLogin>>fromJson(message, new TypeToken<Response<PassengerLogin>>() {
                }.getType()).getData();

                try {
                    this.client.joinGroups(passengerLogin.getListIpsToJoin());
                } catch (IOException e) {
                    dialogShow("Unexpected error joining groups!", ERROR_MESSAGE, 1000);
                }

                this.passengerMenuFrame = new PassengerMenuFrame(this, this.client, passengerLogin.getPassenger());
                this.passengerMenuFrame.configFrame();
                cleanVariables();
                this.setVisible(false);
                break;

            case LOCAL_NODE_REGISTER:
            case PASSENGER_REGISTER:
                showMessageDialog(new JDialog(), response.message, "", PLAIN_MESSAGE);
                tabbedPane.setSelectedIndex(0);
                cleanVariables();
                break;
            case MULTICAST_MESSAGE:
                System.out.println("BROADCAST_MESSAGE");
                showMessageDialog(null, response.message, "Broadcast message", WARNING_MESSAGE);
                break;

            case GET_CURRENT_LOCAL_NODES:
                this.currentLocalNodes = this.jsonHelper.<Response<ArrayList<LocalNode>>>fromJson(message, new TypeToken<Response<ArrayList<LocalNode>>>() {
                }.getType()).getData();
                configButtons();
                break;

            // PASSENGER PROCESS RESPONSE
            case ASSOCIATE_TRAIN_LINE:
            case PASSENGER_MESSAGE_FEEDBACK:
                this.passengerMenuFrame.processMessage(message, response.type);
                break;

            // LOCAL NODE PROCESS RESPONSE
            case FEEDBACK_ADD_TRAIN_LINE:
                this.localNodeMenuFrame.processMessage(message, response.type);
                break;
        }
    }

    private void dialogShow(String message, int typeMessage, int time) {
        final JLabel label = new JLabel(message);
        new Timer(0, e -> {
            ((Timer) e.getSource()).stop();
            Window win = SwingUtilities.getWindowAncestor(label);
            if (win != null)
                win.dispose();
        }) {{
            setInitialDelay(time);
        }}.start();
        showMessageDialog(null, label, "", typeMessage);
    }

    private ArrayList<String> linesToAdd() {
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < this.currentLocalNodes.size(); i++) {
            for (int j = 0; j < this.currentLocalNodes.get(i).getTrainLines().size(); j++) {
                list.add(this.currentLocalNodes.get(i).getTrainLines().get(j).toString());
            }
        }
        return list;
    }

    private void loadJList(JScrollPane scrollPane, JList<String> list, ArrayList<String> data) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addAll(data);
        list.setModel(listModel);
        scrollPane.setViewportView(list);
    }

    public static TrainLine getTrainLineFromString(String string) {
        TrainLine trainLine = null;

        String newString = string.replaceAll("\\s-\\s", " ");
        String rxFirst = "(\\S+)";
        String rxLast = "\\s(.*)";

        Pattern pFirst = Pattern.compile(rxFirst);
        Pattern pLast = Pattern.compile(rxLast);

        Matcher matcherFirst = pFirst.matcher(newString);
        Matcher matcherLast = pLast.matcher(newString);

        if (matcherFirst.find() && matcherLast.find()) {
            trainLine = new TrainLine(matcherFirst.group(0).toLowerCase(), matcherLast.group(0).replaceAll("\\s", "").toLowerCase());
        }

        return trainLine;
    }
}
