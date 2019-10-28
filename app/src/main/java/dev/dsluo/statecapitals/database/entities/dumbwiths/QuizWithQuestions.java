package dev.dsluo.statecapitals.database.entities.dumbwiths;

import java.util.List;

import dev.dsluo.statecapitals.database.entities.Question;
import dev.dsluo.statecapitals.database.entities.Quiz;

public class QuizWithQuestions {
    private Quiz quiz;
    private List<Question> questions;

    public QuizWithQuestions(Quiz quiz, List<Question> questions) {
        this.quiz = quiz;
        this.questions = questions;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
