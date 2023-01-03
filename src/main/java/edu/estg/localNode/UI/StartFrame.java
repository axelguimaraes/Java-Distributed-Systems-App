package edu.estg.localNode.UI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edu.estg.localNode.LocalServer;
import edu.estg.models.LocalNode;
import edu.estg.models.schemas.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class StartFrame extends JFrame {
    private JTabbedPane tabbedPane1;
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JButton loginButton;
    private JTextField registerNameField;
    private JTextField registerUsernameField;
    private JPasswordField registerPasswordField;
    private JButton registerButton;
    private JPanel mainPanel;
    private final LocalServer localServer;
    private MenuForm menuForm;
    private Gson jsonHelper;

    public StartFrame(LocalServer localServer) throws IOException {
        this.jsonHelper = new GsonBuilder().serializeNulls().create();
        this.localServer = localServer;
        configButtons();

        //this.localServer.sendMessage(this.jsonHelper.toJson(new Request<>(RequestType.REGISTER)));
    }

    private void configButtons() {
        loginButton.addActionListener(e -> {
            Login login = new Login(loginUsernameField.getText(), String.valueOf(loginPasswordField.getPassword()));
            Request<Login> request = new Request<>(RequestType.LOGIN, login);
            this.localServer.sendMessage(new Gson().toJson(request));
        });

        registerButton.addActionListener(e -> {
            LocalNode localNode = new LocalNode(registerNameField.getText(), registerUsernameField.getText(), String.valueOf(registerPasswordField.getPassword()));
            LocalNodeRegister localNodeRegister = new LocalNodeRegister(localNode);

            Request<LocalNodeRegister> request = new Request<>(RequestType.REGISTER, localNodeRegister);
            this.localServer.sendMessage(new Gson().toJson(request));
        });
    }

    private void clearVariables() {
        registerNameField.setText("");
        registerUsernameField.setText("");
        registerPasswordField.setText("");

        loginUsernameField.setText("");
        loginPasswordField.setText("");
    }

    public void configFrame() {
        setContentPane(mainPanel);
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

    public void setPreferredSize(Dimension dimension) {
        super.setPreferredSize(dimension);
    }

    public synchronized void processResponse(String message) {
        Response<Object> response = jsonHelper.<Response<Object>>fromJson(message, Response.class);

        if (response.getStatus() != ResponseStatus.OK) {
            showMessageDialog(new JDialog(), response.getMessage(), "", ERROR_MESSAGE);
            return;
        }

        switch (response.getType()) {
            case LOGIN:
                LocalNodeLogin localNodeLogin = this.jsonHelper.<Response<LocalNodeLogin>>fromJson(message, new TypeToken<Response<LocalNodeLogin>>() {
                }.getType()).getData();

                this.menuForm = new MenuForm(this, this.localServer, localNodeLogin.getLocalNode());
                this.menuForm.configFrame();

                clearVariables();
                this.setVisible(false);
                break;
            default:
                //this.menuForm.processMessage(message, response.getType());
        }
    }
}
