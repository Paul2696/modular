package com.modular.persistence.model;

import javax.persistence.*;

@Entity
@Table(name = "mydb.question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idQuestion;
    private String question;
    private String visualAnswer;
    private String auditivoAnswer;
    private String kinestesicoAnswer;

    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getVisualAnswer() {
        return visualAnswer;
    }

    public void setVisualAnswer(String visualAnswer) {
        this.visualAnswer = visualAnswer;
    }

    public String getAuditivoAnswer() {
        return auditivoAnswer;
    }

    public void setAuditivoAnswer(String auditivoAnswer) {
        this.auditivoAnswer = auditivoAnswer;
    }

    public String getKinestesicoAnswer() {
        return kinestesicoAnswer;
    }

    public void setKinestesicoAnswer(String kinestesicoAnswer) {
        this.kinestesicoAnswer = kinestesicoAnswer;
    }
}
