package com.modular.persistence.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "questionsTest")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idQuestion;
    private String question;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "idQuestion")
    private Set<Answer> answers;

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

    @Override
    public String toString() {
        return "Question{" +
                "idQuestion=" + idQuestion +
                ", question='" + question + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question1 = (Question) o;
        return idQuestion == question1.idQuestion &&
                Objects.equals(question, question1.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idQuestion, question);
    }
}
