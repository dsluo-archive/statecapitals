package dev.dsluo.statecapitals.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class QuizWithQuestions {
    @Embedded
    public Quiz quiz;

    @Relation(
            parentColumn = "id",
            entity = Question.class,
            entityColumn = "quiz_id"
    )
    public List<QuestionWithState> questions;

    QuizWithQuestions(Quiz quiz, List<QuestionWithState> questions) {
        this.quiz = quiz;
        this.questions = questions;
    }
}
