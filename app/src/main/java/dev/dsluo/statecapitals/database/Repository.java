package dev.dsluo.statecapitals.database;

import android.content.Context;

import java.util.List;

public class Repository {

    public static final int DEFAULT_QUESTION_COUNT = 6;

    private AppDatabase db;
    private QuizDao quizDao;
    private StateDao stateDao;

    public Repository(Context context) {
        db = AppDatabase.getInstance(context);
        quizDao = db.quizDao();
        stateDao = db.stateDao();
    }

    public QuizWithQuestions getLastQuiz() {
        return quizDao.getLatest();
    }

    public QuizWithQuestions newQuiz() {
        return newQuiz(DEFAULT_QUESTION_COUNT);
    }

    public QuizWithQuestions newQuiz(int questionCount) {
        List<State> states = stateDao.getRandoms(questionCount);
        return quizDao.newQuiz(states);
    }

    public QuizWithQuestions getQuizQuestions(int quizId) {
        return quizDao.getQuestions();
    }

}
