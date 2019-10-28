package dev.dsluo.statecapitals.database.daos;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import dev.dsluo.statecapitals.database.entities.State;

/**
 * DAO for {@link State}s.
 *
 * @author David Luo
 */
@Dao
public abstract class StateDao extends BaseDao<State> {

    /**
     * Get a {@link State} by its ID.
     *
     * @param id The ID of the {@link State} to retrieve.
     * @return A {@link State}.
     */
    @Query("SELECT * FROM State WHERE id = :id")
    public abstract State get(long id);

    /**
     * Get all {@link State}s.
     *
     * @return A {@link List} of all {@link State}s.
     */
    @Query("SELECT * FROM State")
    public abstract List<State> getAll();

    // only 50 states, so whatever
    @Query("SELECT * FROM State ORDER BY RANDOM() LIMIT :count")
    public abstract List<State> getRandoms(int count);

}
