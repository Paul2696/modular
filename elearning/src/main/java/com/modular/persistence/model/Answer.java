package com.modular.persistence.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAnswer;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idQuestion")
    private Question question;
    private String answer;
    private int learningType;

    public int getIdAnswer() {
        return idAnswer;
    }

    public void setIdAnswer(int idAnswer) {
        this.idAnswer = idAnswer;
    }


    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getLearningType() {
        return learningType;
    }

    public void setLearningType(int learningType) {
        this.learningType = learningType;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer1 = (Answer) o;
        return idAnswer == answer1.idAnswer &&
                learningType == answer1.learningType &&
                Objects.equals(question, answer1.question) &&
                Objects.equals(answer, answer1.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAnswer, question, answer, learningType);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "idAnswer=" + idAnswer +
                ", question=" + question +
                ", answer='" + answer + '\'' +
                ", learningType=" + learningType +
                '}';
    }
}
