package dev.dsluo.statecapitals.database.daos;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import dev.dsluo.statecapitals.database.entities.City;

/**
 * DAO for {@link City}s.
 *
 * @author David Luo
 */
@Dao
public abstract class CityDao extends BaseDao<City> {
    /**
     * Get a {@link City} by its ID.
     *
     * @param id The ID of the {@link City} to retrieve.
     * @return A {@link City}.
     */
    @Query("SELECT * FROM City WHERE id = :id")
    public abstract City get(long id);

    /**
     * Get all {@link City}s.
     *
     * @return A {@link List} of all {@link City}s.
     */
    @Query("SELECT * FROM City")
    public abstract List<City> getAll();

    @Query("SELECT * FROM City WHERE stateId = :stateId")
    public abstract List<City> getCitiesForState(long stateId);

    @Query("SELECT c.* FROM City c " +
            "INNER JOIN State s on c.id = s.capitalId " +
            "WHERE stateId = :stateId")
    public abstract City getCapitalForState(long stateId);
}
