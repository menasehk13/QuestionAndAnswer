package com.example.questionandanswer;

public class AnswerDetail {
    String Answer;
    String username;
    String Time;
    String Imageurl;

    public AnswerDetail(String answer, String username, String time, String imageurl) {
        Answer = answer;
        this.username = username;
        Time = time;
        Imageurl = imageurl;
    }
public AnswerDetail(){

}

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getImageurl() {
        return Imageurl;
    }

    public void setImageurl(String imageurl) {
        Imageurl = imageurl;
    }
}
