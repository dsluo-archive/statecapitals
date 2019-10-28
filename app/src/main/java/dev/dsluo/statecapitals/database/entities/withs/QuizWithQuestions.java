package dev.dsluo.statecapitals.database.entities.withs;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import dev.dsluo.statecapitals.database.entities.Question;
import dev.dsluo.statecapitals.database.entities.Quiz;

public class QuizWithQuestions {
    @Embedded
    public Quiz quiz;

    @Relation(entityColumn = "quizId",
            parentColumn = "id")
    public List<Question> questions;
}
