package dev.dsluo.statecapitals.database.entities;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Answer model.
 *
 * @author David Luo
 */
@Entity(foreignKeys = {
        @ForeignKey(entity = Question.class,
                parentColumns = "id",
                childColumns = "questionId",
                deferred = true),
        @ForeignKey(entity = City.class,
                parentColumns = "id",
                childColumns = "cityId")
},
        indices = {
                @Index(value = "questionId"),
                @Index(value = "cityId")
        })
public class Answer {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public long questionId;
    public long cityId;

    /**
     * Required empty constructor for Room.
     */
    public Answer() {
    }

    /**
     * Constructor for creating with deferred question foreign key.
     */
    @Ignore
    public Answer(long cityId) {
        this.cityId = cityId;
    }

    /**
     * toString for debugging.
     */
    @SuppressLint("DefaultLocale")
    @Ignore
    @NonNull
    @Override
    public String toString() {
        return String.format("Answer(id=%d, questionId=%d, cityId=%d)",
                this.id,
                this.questionId,
                this.cityId);
    }
}
