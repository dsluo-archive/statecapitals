package dev.dsluo.statecapitals.database.entities.dumbwiths;

import java.util.List;

import dev.dsluo.statecapitals.database.entities.Question;
import dev.dsluo.statecapitals.database.entities.State;

public class QuestionWithStateAndAnswers {
    private Question question;
    private State state;
    private List<AnswerWithCity> answers;

    public QuestionWithStateAndAnswers(Question question, State state, List<AnswerWithCity> answers) {
        this.question = question;
        this.state = state;
        this.answers = answers;
    }

    public Question getQuestion() {
        return question;
    }

    public State getState() {
        return state;
    }

    public List<AnswerWithCity> getAnswers() {
        return answers;
    }
}
