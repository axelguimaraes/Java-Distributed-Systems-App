package edu.estg.localNode.UI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.estg.localNode.LocalServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class StartForm extends JFrame{

    private final Gson jsonHelper;
    private final LocalServer localServer;
    private JTabbedPane tabbedPane1;
    private JTextField textField1;
    private JButton loginButton;
    private JButton registerButton;
    private JTextField textField3;
    private JPanel namePanel;
    private JPanel usernamePanel;
    private JTextField textField4;
    private JPanel passwordPanel;
    private JTextField textField5;
    private JPasswordField passwordField;
    private JPanel mainPanel;

    public StartForm(LocalServer localServer) throws IOException {
        this.jsonHelper = new GsonBuilder().serializeNulls().create();
        this.localServer = localServer;
    }

    public void configFrame() {
        setContentPane(mainPanel); // TODO: Null
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
}
