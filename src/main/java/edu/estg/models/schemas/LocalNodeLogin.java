package edu.estg.models.schemas;

import edu.estg.models.LocalNode;

public class LocalNodeLogin {
    private LocalNode localNode;

    public LocalNodeLogin(LocalNode localNode) {
        this.localNode = localNode;
    }

    public LocalNode getLocalNode() {
        return localNode;
    }

    public void setLocalNode(LocalNode localNode) {
        this.localNode = localNode;
    }
}
