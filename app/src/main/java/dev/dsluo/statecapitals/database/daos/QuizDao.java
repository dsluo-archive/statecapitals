package dev.dsluo.statecapitals.database.daos;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import dev.dsluo.statecapitals.database.entities.Quiz;

/**
 * DAO for {@link Quiz}s.
 *
 * @author David Luo
 */
@Dao
public abstract class QuizDao extends BaseDao<Quiz> {

    /**
     * Get a {@link Quiz} by its ID.
     *
     * @param id The ID of the {@link Quiz} to retrieve.
     * @return A {@link Quiz}.
     */
    @Query("SELECT * FROM Quiz WHERE id = :id")
    public abstract Quiz get(long id);

    /**
     * Get all {@link Quiz}s.
     *
     * @return A {@link List} of all {@link Quiz}s.
     */
    @Query("SELECT * FROM Quiz")
    public abstract List<Quiz> getAll();

    @Transaction
    @Query("SELECT * FROM Quiz WHERE id = :id")
    public abstract Quiz getWithQuestions(long id);

    @Transaction
    @Query("SELECT * FROM Quiz ORDER BY id DESC LIMIT 1")
    public abstract Quiz getLast();
}
