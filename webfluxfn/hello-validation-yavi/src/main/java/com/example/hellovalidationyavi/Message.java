package com.example.hellovalidationyavi;

import am.ik.yavi.core.Validator;

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

    public static Validator<Message> validator = Validator.builder(Message.class)
        .constraint(Message::getText, "text", c -> c.notBlank().lessThanOrEqual(8))
        .build();
}
