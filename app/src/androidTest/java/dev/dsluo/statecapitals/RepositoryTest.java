package dev.dsluo.statecapitals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import dev.dsluo.statecapitals.database.Repository;
import dev.dsluo.statecapitals.database.entities.withs.QuizWithQuestions;

@RunWith(AndroidJUnit4.class)
public class RepositoryTest {
    private Repository repo;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        repo = Repository.newCleanInstance(context);
    }


    @Test
    public void testNewQuiz() {
        QuizWithQuestions quiz = repo.newQuiz(10);
    }
}
