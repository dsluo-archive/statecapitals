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
import java.util.Collections;
import java.util.List;

import dev.dsluo.statecapitals.R;
import dev.dsluo.statecapitals.database.QuestionWithState;
import dev.dsluo.statecapitals.database.QuizWithQuestions;
import dev.dsluo.statecapitals.database.Repository;

public class QuestionFragment extends Fragment {

    private static final String QUIZ_ID = "dev.dsluo.statecapitals.QuestionFragment.QUIZ_ID";
    private static final String QUESTION_INDEX = "dev.dsluo.statecapitals.QuestionFragment.QUESTION_INDEX";

    private int quizId = -1;
    private int questionIndex = -1;

    private View view;
    private RadioGroup questionGroup;
    private TextView questionNumber;
    private TextView questionText;

    // Required empty public constructor
    public QuestionFragment() {
    }

    public static QuestionFragment newInstance(int quizId, int questionIndex) {
        Bundle args = new Bundle();
        args.putInt(QUIZ_ID, quizId);
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
            this.quizId = args.getInt(QUIZ_ID);
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

    private static class GetQuestionTask extends AsyncTask<Void, Void, QuestionWithState> {

        private WeakReference<QuestionFragment> fragmentReference;

        public GetQuestionTask(QuestionFragment fragment) {
            this.fragmentReference = new WeakReference<>(fragment);
        }

        @Override
        protected QuestionWithState doInBackground(Void... voids) {
            QuestionFragment fragment = fragmentReference.get();
            if (fragment == null || fragment.isRemoving())
                return null;
            Repository repo = new Repository(fragment.getContext());

            QuizWithQuestions quiz = repo.getQuizQuestions(fragment.quizId);
            // Quiz *should* be nonnull
            assert quiz != null;

            return quiz.questions.get(fragment.questionIndex);
        }

        @Override
        protected void onPostExecute(QuestionWithState question) {
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
                            question.state.name
                    )
            );

            List<String> choices = new ArrayList<>();
            choices.add(question.state.capital);
            choices.add(question.state.city2);
            choices.add(question.state.city3);

            Collections.shuffle(choices);

            for (int i = 0; i < 3; i++) {
                RadioButton button = (RadioButton) fragment.questionGroup.getChildAt(i);
                button.setText(choices.get(i));
                button.setOnClickListener((answer) -> {
                    if (answer.toString().equals(question.state.capital));
                });
            }
        }
    }
}
