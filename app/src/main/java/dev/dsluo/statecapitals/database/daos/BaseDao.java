package dev.dsluo.statecapitals.database.daos;

import androidx.room.Insert;
import androidx.room.Update;

import java.util.List;

/**
 * A base class for other DAOs implemented in this package.
 *
 * @author David Luo
 */
public abstract class BaseDao<T> {
    /**
     * Insert a {@link T} into the database.
     *
     * @param t A {@link T}.
     * @return The ID of the {@link T} inserted.
     */
    @Insert
    public abstract long insert(T t);

    /**
     * Insert many {@link T}s into the database.
     *
     * @param ts A {@link List} of {@link T}s.
     * @return The IDs of the {@link T}s inserted.
     */
    @Insert
    public abstract List<Long> insertAll(List<T> ts);

    /**
     * Update a {@link T}.
     *
     * @param t A {@link T}.
     * @return The number of {@link T}s updated.
     */
    @Update
    public abstract int update(T t);

    /**
     * Update many {@link T}s.
     *
     * @param ts A {@link List} of {@link T}s.
     * @return The number of {@link T}s updated.
     */
    @Update
    public abstract int updateAll(List<T> ts);

    public final String thing = "asdf";

    /**
     * Get a {@link T} by its ID.
     *
     * @param id The ID of the {@link T} to retrieve.
     * @return A {@link T}.
     */
    public abstract T get(long id);

    /**
     * Get all {@link T}s.
     *
     * @return A {@link List} of all {@link T}s.
     */
    public abstract List<T> getAll();
}