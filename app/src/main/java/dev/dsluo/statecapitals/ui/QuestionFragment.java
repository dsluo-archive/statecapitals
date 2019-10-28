package dev.dsluo.statecapitals.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import dev.dsluo.statecapitals.database.entities.dumbwiths.AnswerWithCity;
import dev.dsluo.statecapitals.database.entities.dumbwiths.QuestionWithStateAndAnswers;

public class QuestionFragment extends Fragment {

    private static final String QUIZ_ID = "dev.dsluo.statecapitals.QuestionFragment.QUIZ_ID";
    private static final String QUESTION_INDEX = "dev.dsluo.statecapitals.QuestionFragment.QUESTION_INDEX";
    private static final String SELECTED_ANSWER = "dev.dsluo.statecapitals.QuestionFragment.SELECTED_ANSWER";

    private long quizId = -1;
    private int questionIndex = -1;
    private int selected = -1;

    private Question question;
    private State state;
    private List<Answer> answers;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null)
            selected = savedInstanceState.getInt(SELECTED_ANSWER);

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_question, container, false);
        questionNumber = view.findViewById(R.id.question_number);
        questionText = view.findViewById(R.id.question_text);
        questionGroup = view.findViewById(R.id.question_group);

        new GetQuestionTask(this).execute();

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ANSWER, selected);
    }

    private static class GetQuestionTask extends AsyncTask<Void, Void, QuestionWithStateAndAnswers> {

        private WeakReference<QuestionFragment> fragmentReference;

        public GetQuestionTask(QuestionFragment fragment) {
            this.fragmentReference = new WeakReference<>(fragment);
        }

        @Override
        protected QuestionWithStateAndAnswers doInBackground(Void... voids) {
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

            List<AnswerWithCity> composite = new ArrayList<>();
            for (int i = 0; i < answers.size(); i++)
                composite.add(new AnswerWithCity(answers.get(i), cities.get(i)));

            return new QuestionWithStateAndAnswers(question, state, composite);
        }

        @Override
        protected void onPostExecute(QuestionWithStateAndAnswers question) {
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
                            question.getState().name
                    )
            );

            List<String> choices = new ArrayList<>();
            for (AnswerWithCity answer : question.getAnswers())
                choices.add(answer.getCity().name);

            List<Long> answerIds = new ArrayList<>();
            for (AnswerWithCity answer : question.getAnswers())
                answerIds.add(answer.getAnswer().id);
            fragment.selected = answerIds.indexOf(question.getQuestion().selectedAnswerId);

            for (int i = 0; i < choices.size(); i++) {
                RadioButton button = new RadioButton(fragment.getContext());
                fragment.questionGroup.addView(button);

                if (fragment.selected == i)
                    button.setChecked(true);

                button.setText(choices.get(i));
                final int selectedIndex = i;
                button.setOnClickListener(view -> {
                    fragment.selected = selectedIndex;

                    AsyncTask.execute(() -> {
                        Repository repo = Repository.newInstance(fragment.getContext());
                        Question q = question.getQuestion();
                        q.selectedAnswerId = question.getAnswers().get(selectedIndex).getAnswer().id;

                        repo.updateQuestion(q);
                    });
                });
            }

        }
    }
}
