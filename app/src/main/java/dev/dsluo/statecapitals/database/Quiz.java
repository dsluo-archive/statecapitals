package dev.dsluo.statecapitals.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "quizzes")
public class Quiz {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public Date completed;
}
