package dev.dsluo.statecapitals.database;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.dsluo.statecapitals.database.daos.AnswerDao;
import dev.dsluo.statecapitals.database.daos.CityDao;
import dev.dsluo.statecapitals.database.daos.QuestionDao;
import dev.dsluo.statecapitals.database.daos.QuizDao;
import dev.dsluo.statecapitals.database.daos.StateDao;
import dev.dsluo.statecapitals.database.entities.Answer;
import dev.dsluo.statecapitals.database.entities.City;
import dev.dsluo.statecapitals.database.entities.Question;
import dev.dsluo.statecapitals.database.entities.Quiz;
import dev.dsluo.statecapitals.database.entities.State;
import dev.dsluo.statecapitals.database.entities.withs.QuizWithQuestions;

/**
 * Repository for the database. All other parts of the app should interact with the database through
 * this class.
 *
 * @author David Luo
 */
public class Repository {

    private static final int DEFAULT_QUESTION_COUNT = 6;

    private AppDatabase db;

    private QuizDao quizDao;
    private QuestionDao questionDao;
    private AnswerDao answerDao;

    private StateDao stateDao;
    private CityDao cityDao;

    /**
     * Get a {@link Repository}.
     *
     * @param context The application context.
     * @return A repository instance.
     */
    public static Repository newInstance(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        return new Repository(db);
    }

    /**
     * Delete the database, then get a new {@link Repository}. This is mostly used for testing.
     *
     * @param context The application context.
     * @return A clean repository instance.
     */

    public static Repository newCleanInstance(Context context) {
        AppDatabase db = AppDatabase.getCleanInstance(context);
        return new Repository(db);
    }

    /**
     * Constructor.
     *
     * @param db An instance of {@link AppDatabase}.
     */
    public Repository(AppDatabase db) {
        this.db = db;

        quizDao = db.quizDao();
        questionDao = db.questionDao();
        answerDao = db.answerDao();

        stateDao = db.stateDao();
        cityDao = db.cityDao();
    }


    /**
     * Create a new {@link Quiz}.
     *
     * @param questionCount How many questions should be in the quiz.
     * @return A new quiz.
     */
    public QuizWithQuestions newQuiz(int questionCount) {
        Quiz quiz = new Quiz();

        db.runInTransaction(() -> {
            // Get the states to ask about
            List<State> states = stateDao.getRandoms(questionCount);
            List<Question> questions = new ArrayList<>();
            for (State state : states) {
                City capital = cityDao.getCapitalForState(state.id);

                // Capital is always first element
                List<City> cities = cityDao.getCitiesForState(state.id);
                cities.remove(capital);
                cities.add(0, capital);

                // Correct answer always first element
                Answer correctAnswer = new Answer(capital.id);
                List<Answer> answers = new ArrayList<>();
                answers.add(correctAnswer);
                for (int i = 1; i < cities.size(); i++) {
                    City city = cities.get(i);
                    answers.add(new Answer(city.id));
                }

                // Insert the answers in a random order.
                Collections.shuffle(answers);

                // Insert the answers and update IDs.
                List<Long> answerIds = answerDao.insertAll(answers);
                for (int i = 0; i < answers.size(); i++)
                    answers.get(i).id = answerIds.get(i);

                // Insert question and update its ID.
                Question question = new Question(state.id, correctAnswer.id);
                questions.add(question);
                question.id = questionDao.insert(question);

                // Update answer's questionId to satisfy deferred foreign key.
                for (Answer answer : answers)
                    answer.questionId = question.id;
                answerDao.updateAll(answers);
            }

            quiz.id = quizDao.insert(quiz);
            for (Question question : questions)
                question.quizId = quiz.id;
            questionDao.updateAll(questions);
        });
        return quizDao.getWithQuestions(quiz.id);
    }

    /**
     * Retrieve the last in-progress quiz or create a new quiz, using the default question count
     * ({@value DEFAULT_QUESTION_COUNT} questions).
     *
     * @return A new {@link QuizWithQuestions}.
     */
    public QuizWithQuestions getNewOrLastQuiz() {
        return getNewOrLastQuiz(DEFAULT_QUESTION_COUNT);

    }

    /**
     * Retrieve the last in-progress quiz or create a new quiz.
     *
     * @param questionCount The number of questions in the quiz.
     * @return A new {@link QuizWithQuestions}.
     */
    public QuizWithQuestions getNewOrLastQuiz(int questionCount) {
        QuizWithQuestions quiz = quizDao.getLast();
        if (quiz == null || quiz.quiz.completed != null)
            quiz = this.newQuiz(questionCount);

        return quiz;
    }

    public List<Question> getQuestionsForQuiz(long quizId) {
        return questionDao.getQuestionsForQuiz(quizId);
    }

    public Question getQuestionForQuiz(long quizId, int questionIndex) {
        return questionDao.getQuestionForQuiz(quizId, questionIndex);
    }

}
