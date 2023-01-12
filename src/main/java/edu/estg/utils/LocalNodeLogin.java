package edu.estg.utils;

import java.util.ArrayList;

public class LocalNodeLogin {
    private LocalNode localNode;
    private ArrayList<String> listIpsToJoin;

    public LocalNodeLogin(LocalNode localNode, ArrayList<String> listIpsToJoin) {
        this.localNode = localNode;
        this.listIpsToJoin = listIpsToJoin;
    }

    public LocalNode getLocalNode() {
        return this.localNode;
    }

    public ArrayList<String> getListIpsToJoin() {
        return listIpsToJoin;
    }
}
