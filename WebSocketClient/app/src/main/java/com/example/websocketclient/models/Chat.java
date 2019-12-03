package com.example.websocketclient.models;

import org.w3c.dom.Comment;

public class Chat {

    public String id;
    public String author;
    public String text;

    public Comment(){//Default constructor

    }

    public Comment(String uid, String author, String text){

        this.text = text;

    }


}
