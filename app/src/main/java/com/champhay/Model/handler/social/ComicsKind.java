package com.champhay.Model.handler.social;

/**
 * Created by lucius on 1/11/2017.
 */

public class ComicsKind {
    public int id;
    public String kind;

    public ComicsKind(int id, String kind) {
        this.id = id;
        this.kind = kind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
