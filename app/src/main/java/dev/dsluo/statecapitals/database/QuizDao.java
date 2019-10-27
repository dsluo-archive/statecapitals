package dev.dsluo.statecapitals.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.Arrays;
import java.util.List;

@Dao
abstract class QuizDao {
    @Insert
    abstract long insert(Quiz quiz);

    @Insert
    abstract long[] insert(Question... question);

    @Update
    public abstract void update(Quiz quiz);

    @Transaction
    public QuizWithQuestions newQuiz(List<State> states) {
        Quiz quiz = new Quiz();
        quiz.id = (int) insert(quiz);

        Question[] questions = new Question[states.size()];

        for (int i = 0; i < states.size(); i++)
            questions[i] = new Question(quiz.id, states.get(i).id);

        long[] ids = insert(questions);
        QuestionWithState[] questionWithStates = new QuestionWithState[questions.length];

        for (int i = 0; i < questions.length; i++) {
            questions[i].id = (int) ids[i];
            questionWithStates[i] = new QuestionWithState(questions[i], states.get(i));
        }


        return new QuizWithQuestions(quiz, Arrays.asList(questionWithStates));
    }

    @Transaction
    @Query("SELECT * FROM quizzes ORDER BY id DESC LIMIT 1")
    public abstract QuizWithQuestions getLatest();

    @Transaction
    @Query("SELECT * FROM quizzes")
    public abstract QuizWithQuestions getQuestions();
}
