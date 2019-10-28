package dev.dsluo.statecapitals.ui;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.lang.ref.WeakReference;
import java.util.List;

import dev.dsluo.statecapitals.R;
import dev.dsluo.statecapitals.database.Repository;
import dev.dsluo.statecapitals.database.entities.Question;
import dev.dsluo.statecapitals.database.entities.Quiz;
import dev.dsluo.statecapitals.database.entities.dumbwiths.QuizWithQuestions;

public class QuizActivity extends AppCompatActivity {

    private static final String QUIZ_ID = "dev.dsluo.statecapitals.QuizActivity.QUIZ_ID";
    private static final String QUESTION_INDEX = "dev.dsluo.statecapitals.QuizActivity.QUESTION_INDEX";

    private int quizId = -1;
    private int questionIndex = -1;

    private QuizWithQuestions quiz;

    private ViewPager pager;
    private QuestionPageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            this.quizId = savedInstanceState.getInt(QUIZ_ID);
            this.questionIndex = savedInstanceState.getInt(QUESTION_INDEX);
        }

        pager = findViewById(R.id.question_pager);
        adapter = new QuestionPageAdapter(getSupportFragmentManager(), this);
        pager.setAdapter(adapter);

        if (this.quiz == null || this.quizId == -1)
            new GetQuizTask(this).execute();
    }

    /**
     * GetQuizTask.
     *
     * @see <a href="https://stackoverflow.com/a/46166223">Uses this StackOverflow answer.</a>
     */
    private static class GetQuizTask extends AsyncTask<Void, Void, QuizWithQuestions> {

        private WeakReference<QuizActivity> activityReference;

        GetQuizTask(QuizActivity context) {
            this.activityReference = new WeakReference<>(context);
        }

        @Override
        protected QuizWithQuestions doInBackground(Void... voids) {
            QuizActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing())
                return null;

            Repository repo = Repository.newInstance(activity);
            Quiz quiz = repo.getNewOrLastQuiz();
            List<Question> questions = repo.getQuestionsForQuiz(quiz.id);

            return new QuizWithQuestions(quiz, questions);
        }

        @Override
        protected void onPostExecute(QuizWithQuestions result) {
            super.onPostExecute(result);
            QuizActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing())
                return;

            activity.quiz = result;
            activity.adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(QUIZ_ID, questionIndex);
        outState.putInt(QUESTION_INDEX, questionIndex);
    }

    public static class QuestionPageAdapter extends FragmentPagerAdapter {

        private QuizActivity activity;

        public QuestionPageAdapter(@NonNull FragmentManager fm, QuizActivity activity) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

            this.activity = activity;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            QuizWithQuestions quiz = activity.quiz;
            if (quiz == null)
                throw new RuntimeException("This shouldn't happen");
            if (position == getCount() - 1)
                return QuizFinishFragment.newInstance(quiz.getQuiz().id);
            return QuestionFragment.newInstance(quiz.getQuiz().id, position);
        }

        @Override
        public int getCount() {
            QuizWithQuestions quiz = activity.quiz;
            return quiz != null ? 1 + activity.quiz.getQuestions().size() : 0;
        }
    }
}
