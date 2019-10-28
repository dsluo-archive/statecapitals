package dev.dsluo.statecapitals.database.daos;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import dev.dsluo.statecapitals.database.entities.Answer;

/**
 * DAO for {@link Answer}s.
 *
 * @author David Luo
 */
@Dao
public abstract class AnswerDao extends BaseDao<Answer> {
    /**
     * Get an {@link Answer} by its ID.
     *
     * @param id The ID of the {@link Answer} to retrieve.
     * @return An {@link Answer}.
     */
    @Query("SELECT * FROM Answer WHERE id = :id")
    public abstract Answer get(long id);

    /**
     * Get all {@link Answer}s.
     *
     * @return A {@link List} of all {@link Answer}s.
     */
    @Query("SELECT * FROM Answer")
    public abstract List<Answer> getAll();

    @Query("SELECT * FROM Answer WHERE questionId = :questionId")
    public abstract List<Answer> getAnswersForQuestion(int questionId);
}
