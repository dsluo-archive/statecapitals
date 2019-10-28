package dev.dsluo.statecapitals.database.daos;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import dev.dsluo.statecapitals.database.entities.Question;

/**
 * DAO for {@link Question}s.
 *
 * @author David Luo
 */
@Dao
public abstract class QuestionDao extends BaseDao<Question> {

    /**
     * Get a {@link Question} by its ID.
     *
     * @param id The ID of the {@link Question} to retrieve.
     * @return A {@link Question}.
     */
    @Query("SELECT * FROM Question WHERE id = :id")
    public abstract Question get(long id);

    /**
     * Get all {@link Question}s.
     *
     * @return A {@link List} of all {@link Question}s.
     */
    @Query("SELECT * FROM Question")
    public abstract List<Question> getAll();

    @Query("SELECT * FROM Question " +
            "WHERE quizId = :quizId ")
    public abstract List<Question> getQuestionsForQuiz(long quizId);

    @Query("SELECT * FROM Question " +
            "WHERE quizId = :quizId " +
            "ORDER BY id ASC " +
            "LIMIT 1 OFFSET :questionIndex")
    public abstract Question getQuestionForQuiz(long quizId, int questionIndex);

}
