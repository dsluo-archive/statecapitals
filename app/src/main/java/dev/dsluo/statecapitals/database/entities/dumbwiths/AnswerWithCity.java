package dev.dsluo.statecapitals.database.entities.dumbwiths;

import dev.dsluo.statecapitals.database.entities.Answer;
import dev.dsluo.statecapitals.database.entities.City;

public class AnswerWithCity {
    private Answer answer;
    private City city;

    public AnswerWithCity(Answer answer, City city) {
        this.answer = answer;
        this.city = city;
    }

    public Answer getAnswer() {
        return answer;
    }

    public City getCity() {
        return city;
    }
}
