package com.example.questionandanswer;

public class MyQuestion {
    String Question;
    String Time;
    String Type;
    String imageurl;
    String username;

    public MyQuestion(String question, String time, String type, String imageurl, String username) {
        this.Question = question;
        this.Time = time;
        this.Type = type;
        this.imageurl = imageurl;
        this.username = username;
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
}
