package com.example.hellodbr2dbcdatabaseclienth2;

import java.io.Serializable;

public class Message implements Serializable {

    private String text;

    Message() {
    }

    public Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
