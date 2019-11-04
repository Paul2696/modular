package com.modular.persistence.model;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Question.class)
public class Question_ {
    public static volatile SingularAttribute<Question, Integer> idQuestion;
    public static volatile SingularAttribute<Question, String> question;
    public static volatile SetAttribute<Question, Answer> answers;

}
