package edu.estg.models.schemas;

import edu.estg.models.LocalNode;

public class LocalNodeRegister {
    private LocalNode localNode;

    public LocalNodeRegister(LocalNode localNode) {
        this.localNode = localNode;
    }

    public LocalNode getLocalNode() {
        return localNode;
    }

    public void setLocalNode(LocalNode localNode) {
        this.localNode = localNode;
    }
}
