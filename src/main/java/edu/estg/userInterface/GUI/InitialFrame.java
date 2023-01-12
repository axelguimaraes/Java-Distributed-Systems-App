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
import java.util.concurrent.atomic.AtomicBoolean;

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

    private final Gson jsonHelper;
    private final Client client;
    private LocalNodeMenuFrame menuFrame;

    public InitialFrame(Client client) throws IOException {
        this.jsonHelper = new GsonBuilder().serializeNulls().create();
        this.client = client;
        configButtons();
    }

    private void configButtons() {
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
        });

        passengerRegisterRadioButton.addActionListener(e -> {
            isRegisterLocalNode.set(false);
        });



        loginButton.addActionListener(e -> {
            Login login = new Login(usernameLoginTextField.getText(), String.valueOf(passwordLoginTextField.getPassword()));

            if (isLoginLocalNode.get()) {
                Request<Login> request = new Request<>(RequestType.LOCAL_NODE_LOGIN, login);
                this.client.sendMessage(new Gson().toJson(request));

            } else if (!isLoginLocalNode.get()) {
                Request<Login> request = new Request<>(RequestType.PASSENGER_LOGIN, login);
                this.client.sendMessage(new Gson().toJson(request));
            }
        });

        registerButton.addActionListener(e -> {
            if (isRegisterLocalNode.get()) {
                LocalNode localNode = new LocalNode(nameRegisterTextField.getText(), usernameRegisterTextField.getText(), String.valueOf(passwordRegisterTextField.getPassword()));
                LocalNodeRegister localNodeRegister = new LocalNodeRegister(localNode);
                Request<LocalNodeRegister> request = new Request<>(RequestType.LOCAL_NODE_REGISTER, localNodeRegister);
                this.client.sendMessage(new Gson().toJson(request));

            } else if (!isRegisterLocalNode.get()) {
                Passenger passenger = new Passenger(nameRegisterTextField.getText(), usernameRegisterTextField.getText(), String.valueOf(passwordRegisterTextField.getPassword()));
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

                this.menuFrame = new LocalNodeMenuFrame(this, this.client, localNodeLogin.getLocalNode());
                this.menuFrame.configFrame();
                cleanVariables();
                this.setVisible(false);
                break;

            case PASSENGER_LOGIN:
                PassengerLogin passengerLogin = this.jsonHelper.<Response<PassengerLogin>>fromJson(message, new TypeToken<Response<PassengerLogin>>() {
                }.getType()).getData();

                // TODO: Menu frame
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
            default:
                this.menuFrame.processMessage(message, response.type);
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
}
