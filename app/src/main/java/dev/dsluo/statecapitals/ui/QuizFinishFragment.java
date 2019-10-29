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
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;

import dev.dsluo.statecapitals.R;
import dev.dsluo.statecapitals.database.Repository;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * Use the {@link QuizFinishFragment#newInstance} factory method to
 * create an instance of this fragment.
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

    public interface OnQuizFinishListener {
        void onQuizFinished();
    }

    public QuizFinishFragment() {
        // Required empty public constructor
    }

    public static QuizFinishFragment newInstance(long quizId, boolean finished) {
        QuizFinishFragment fragment = new QuizFinishFragment();
        Bundle args = new Bundle();
        args.putLong(QUIZ_ID, quizId);
        args.putBoolean(FINISHED, finished);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            quizId = getArguments().getLong(QUIZ_ID);
            finished = getArguments().getBoolean(FINISHED);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            finished = savedInstanceState.getBoolean(FINISHED);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnQuizFinishListener)
            onQuizFinishListener = (OnQuizFinishListener) context;
        else
            throw new RuntimeException(context.toString()
                    + " must implement OnQuizFinishListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onQuizFinishListener = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

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

    private static class FinishQuizTask extends AsyncTask<Void, Void, Integer> {

        private WeakReference<QuizFinishFragment> fragmentReference;

        public FinishQuizTask(QuizFinishFragment fragment) {
            this.fragmentReference = new WeakReference<>(fragment);
        }

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
