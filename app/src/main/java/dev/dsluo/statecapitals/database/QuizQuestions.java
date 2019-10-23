package dev.dsluo.statecapitals.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "quiz_questions",
        foreignKeys = {
                @ForeignKey(entity = Quiz.class,
                        parentColumns = "id",
                        childColumns = "quiz_id"),
                @ForeignKey(entity = State.class,
                        parentColumns = "id",
                        childColumns = "state_id")
        })
public class QuizQuestions {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "quiz_id")
    public int quizId;

    @ColumnInfo(name = "state_id")
    public int stateId;

    // 0 = capital, 1 = city2, 2 = city3, -1 = unanswered
    @ColumnInfo(defaultValue = "-1")
    public int answer;
}
