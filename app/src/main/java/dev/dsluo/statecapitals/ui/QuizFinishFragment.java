package dev.dsluo.statecapitals.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import dev.dsluo.statecapitals.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuizFinishFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuizFinishFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizFinishFragment extends Fragment {
    private static final String QUIZ_ID = "QUIZ_ID";

    // TODO: Rename and change types of parameters
    private long quizId;

    public QuizFinishFragment() {
        // Required empty public constructor
    }

    public static QuizFinishFragment newInstance(long quizId) {
        QuizFinishFragment fragment = new QuizFinishFragment();
        Bundle args = new Bundle();
        args.putLong(QUIZ_ID, quizId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            quizId = getArguments().getLong(QUIZ_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz_finish, container, false);
    }
}
