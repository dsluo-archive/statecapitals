package dev.dsluo.statecapitals.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;

import dev.dsluo.statecapitals.R;
import dev.dsluo.statecapitals.database.Repository;

/**
 * fragment for the finishing quiz screen.
 *
 * @author David Luo
 */
public class QuizFinishFragment extends Fragment {
    private static final String QUIZ_ID = "QUIZ_ID";
    private static final String FINISHED = "FINISHED";

    // TODO: Rename and change types of parameters
    private long quizId = -1;
    private boolean finished = false;

    private Button finishButton;
    private Group scoreGroup;
    private TextView score;

    private OnQuizFinishListener onQuizFinishListener;

    /**
     * tells the quiz activity that the quiz has been finished
     */
    public interface OnQuizFinishListener {
        void onQuizFinished();
    }

    /**
     * required empty public constructor
     */
    public QuizFinishFragment() {
    }

    /**
     * get a new instance of this fragment
     *
     * @param quizId   the id of the quiz for this quiz
     * @param finished if to render the layout as finished
     * @return an instance of {@link QuizFinishFragment}
     */
    public static QuizFinishFragment newInstance(long quizId, boolean finished) {
        QuizFinishFragment fragment = new QuizFinishFragment();
        Bundle args = new Bundle();
        args.putLong(QUIZ_ID, quizId);
        args.putBoolean(FINISHED, finished);

        fragment.setArguments(args);
        return fragment;
    }

    /**
     * restore instance variables from arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            quizId = getArguments().getLong(QUIZ_ID);
            finished = getArguments().getBoolean(FINISHED);
        }
    }

    /**
     * gets the {@link OnQuizFinishListener}
     *
     * @param context a context that must implement {@link OnQuizFinishListener}
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnQuizFinishListener)
            onQuizFinishListener = (OnQuizFinishListener) context;
        else
            throw new RuntimeException(context.toString()
                    + " must implement OnQuizFinishListener");
    }

    /**
     * removes the {@link OnQuizFinishListener}
     */
    @Override
    public void onDetach() {
        super.onDetach();
        onQuizFinishListener = null;
    }


    /**
     * Inflate the views and setup the {@link android.view.View.OnClickListener} for the button,
     * or start finishing if instantiated as such.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz_finish, container, false);

        finishButton = view.findViewById(R.id.finish);
        scoreGroup = view.findViewById(R.id.score_group);
        score = view.findViewById(R.id.score);

        if (finished)
            new FinishQuizTask(this).execute();
        else
            finishButton.setOnClickListener(button -> new FinishQuizTask(this).execute());
        return view;
    }

    /**
     * Async task to finish the quiz.
     */
    private static class FinishQuizTask extends AsyncTask<Void, Void, Integer> {

        private WeakReference<QuizFinishFragment> fragmentReference;

        /**
         * constructor
         *
         * @param fragment the fragment
         */
        public FinishQuizTask(QuizFinishFragment fragment) {
            this.fragmentReference = new WeakReference<>(fragment);
        }

        /**
         * get the score of the quiz if already finished, or finish the quiz otherwise
         *
         * @param voids nothing
         * @return the score of the quiz
         */
        @Override
        protected Integer doInBackground(Void... voids) {
            QuizFinishFragment fragment = fragmentReference.get();
            if (fragment == null || fragment.isRemoving())
                return null;

            Repository repo = Repository.newInstance(fragment.getContext());

            if (fragment.finished)
                return repo.getScore(fragment.quizId);
            else
                return repo.finishQuiz(fragment.quizId);
        }

        /**
         * update the ui with the score.
         *
         * @param score the quiz score
         */
        @Override
        protected void onPostExecute(Integer score) {
            super.onPostExecute(score);
            QuizFinishFragment fragment = fragmentReference.get();
            if (fragment == null || fragment.isRemoving())
                return;

            fragment.scoreGroup.setVisibility(View.VISIBLE);
            fragment.finishButton.setEnabled(false);
            fragment.score.setText(String.format(
                    fragment.getResources().getText(R.string.score).toString(),
                    score
            ));

            fragment.finished = true;

            Bundle args = fragment.getArguments();
            assert args != null;
            args.putBoolean(FINISHED, true);
            fragment.setArguments(args);
            fragment.onQuizFinishListener.onQuizFinished();
        }
    }
}
