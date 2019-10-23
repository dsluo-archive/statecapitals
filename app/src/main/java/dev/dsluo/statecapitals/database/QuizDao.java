package dev.dsluo.statecapitals.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface QuizDao {
    @Insert
    void insert(Quiz quiz);

    @Query("SELECT * FROM quizzes ORDER BY id DESC LIMIT 1")
    Quiz getLast();

    @Query("SELECT * FROM quiz_questions WHERE quiz_id = :quizId")
    QuizQuestions getQuestions(int quizId);
}
