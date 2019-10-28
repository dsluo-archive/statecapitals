package dev.dsluo.statecapitals.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import dev.dsluo.statecapitals.R;
import dev.dsluo.statecapitals.database.Repository;
import dev.dsluo.statecapitals.database.entities.Answer;
import dev.dsluo.statecapitals.database.entities.City;
import dev.dsluo.statecapitals.database.entities.Question;
import dev.dsluo.statecapitals.database.entities.State;

public class QuestionFragment extends Fragment {

    private static final String QUIZ_ID = "dev.dsluo.statecapitals.QuestionFragment.QUIZ_ID";
    private static final String QUESTION_INDEX = "dev.dsluo.statecapitals.QuestionFragment.QUESTION_INDEX";

    private long quizId = -1;
    private int questionIndex = -1;

    private Question question;
    private State state;
    private List<Answer> answers;
    private List<City> cities;

    private View view;
    private RadioGroup questionGroup;
    private TextView questionNumber;
    private TextView questionText;

    // Required empty public constructor
    public QuestionFragment() {
    }

    public static QuestionFragment newInstance(long quizId, int questionIndex) {
        Bundle args = new Bundle();
        args.putLong(QUIZ_ID, quizId);
        args.putInt(QUESTION_INDEX, questionIndex);

        QuestionFragment fragment = new QuestionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.quizId = args.getLong(QUIZ_ID);
            this.questionIndex = args.getInt(QUESTION_INDEX);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_question, container, false);
        questionNumber = view.findViewById(R.id.question_number);
        questionText = view.findViewById(R.id.question_text);
        questionGroup = view.findViewById(R.id.question_group);

        new GetQuestionTask(this).execute();

        return view;
    }

    private static class GetQuestionTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<QuestionFragment> fragmentReference;

        public GetQuestionTask(QuestionFragment fragment) {
            this.fragmentReference = new WeakReference<>(fragment);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            QuestionFragment fragment = fragmentReference.get();
            if (fragment == null || fragment.isRemoving())
                return null;
            Repository repo = Repository.newInstance(fragment.getContext());

            Question question = repo.getQuestionForQuiz(fragment.quizId, fragment.questionIndex);
            State state = repo.getState(question.stateId);
            List<Answer> answers = repo.getAnswersForQuestion(question.id);

            List<City> cities = new ArrayList<>();
            for (Answer answer : answers)
                cities.add(repo.getCity(answer.cityId));

            fragment.question = question;
            fragment.state = state;
            fragment.answers = answers;
            fragment.cities = cities;

            return null;
        }

        @Override
        protected void onPostExecute(Void question) {
            super.onPostExecute(question);
            QuestionFragment fragment = fragmentReference.get();
            if (fragment == null || fragment.isRemoving())
                return;
            fragment.questionNumber.setText(
                    String.format(
                            fragment.getString(R.string.question_number),
                            fragment.questionIndex + 1
                    )
            );

            fragment.questionText.setText(
                    String.format(
                            fragment.getString(R.string.question_text),
                            fragment.state.name
                    )
            );

            List<String> choices = new ArrayList<>();
            for (City city : fragment.cities)
                choices.add(city.name);

            for (int i = 0; i < choices.size(); i++) {
                RadioButton button = new RadioButton(fragment.getContext());
                fragment.questionGroup.addView(button);

                button.setText(choices.get(i));
            }
        }
    }
}
