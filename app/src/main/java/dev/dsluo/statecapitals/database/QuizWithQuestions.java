package dev.dsluo.statecapitals.database;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class QuizWithQuestions {
    @Embedded
    Quiz quiz;

    @Relation(
            parentColumn = "id",
            entity = State.class,
            entityColumn = "id",
            associateBy = @Junction(
                    value = QuizQuestions.class,
                    parentColumn = "quiz_id",
                    entityColumn = "state_id"
            )
    )
    List<State> states;
}
