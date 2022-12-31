package edu.estg.localNode;


import edu.estg.localNode.UI.StartForm;

import java.io.IOException;
import java.nio.channels.NotYetConnectedException;

public class LocalServer {
    private StartForm startForm;

    public LocalServer() {
        try {
            this.startForm = new StartForm(this);
            this.startForm.configFrame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            LocalServer localServer = new LocalServer();
        } catch (NotYetConnectedException e) {
            e.printStackTrace();
        }
    }
}
