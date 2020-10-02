package com.example.questionandanswer;

public class MyQuestion {

    String Question;
    String Time;
    String Type;
    String imageurl;
    String username;
    String token;
    String answersize;


    public MyQuestion(String question, String time, String type, String imageurl, String username,String token,String answersize) {

        this.Question = question;
        this.Time = time;
        this.Type = type;
        this.imageurl = imageurl;
        this.username = username;
        this.token=token;
        this.answersize=answersize;

    }
public MyQuestion(){

}

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAnswersize() {
        return answersize;
    }

    public void setAnswersize(String answersize) {
        this.answersize = answersize;
    }

}
