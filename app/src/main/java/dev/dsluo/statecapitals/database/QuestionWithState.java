package dev.dsluo.statecapitals.database;

import androidx.room.Embedded;
import androidx.room.Relation;

public class QuestionWithState {
    @Embedded
    public Question question;

    @Relation(
            parentColumn = "state_id",
            entity = State.class,
            entityColumn = "id"
    )
    public State state;

    QuestionWithState(Question question, State state) {
        this.question = question;
        this.state = state;
    }
}
