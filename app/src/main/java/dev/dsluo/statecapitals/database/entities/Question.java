package dev.dsluo.statecapitals.database.entities;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Question model.
 *
 * @author David Luo
 */
@Entity(foreignKeys = {
        @ForeignKey(entity = Quiz.class,
                parentColumns = "id",
                childColumns = "quizId",
                deferred = true),
        @ForeignKey(entity = State.class,
                parentColumns = "id",
                childColumns = "stateId"),
        @ForeignKey(entity = Answer.class,
                parentColumns = "id",
                childColumns = "correctAnswerId"),
        @ForeignKey(entity = Answer.class,
                parentColumns = "id",
                childColumns = "selectedAnswerId"),
},
        indices = {
                @Index(value = {"quizId"}),
                @Index(value = {"stateId"}),
                @Index(value = {"correctAnswerId"}),
                @Index(value = {"selectedAnswerId"}),
        })
public class Question {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long quizId;
    public long stateId;
    public long correctAnswerId;
    @Nullable
    public Long selectedAnswerId;

    /**
     * Required empty constructor for Room.
     */
    public Question() {
    }

    /**
     * Constructor for building new actual questions.
     */
    @Ignore
    public Question(long stateId, long correctAnswerId) {
        this.stateId = stateId;
        this.correctAnswerId = correctAnswerId;
    }
}
