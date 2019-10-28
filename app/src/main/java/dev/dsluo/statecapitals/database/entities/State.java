package dev.dsluo.statecapitals.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * State model.
 *
 * @author David Luo
 */
@Entity(foreignKeys = {
        @ForeignKey(entity = City.class,
                parentColumns = "id",
                childColumns = "capitalId",
                onDelete = ForeignKey.RESTRICT)
},
        indices = {
                @Index(value = "capitalId")
        })
public class State {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public long capitalId;
    public int statehood;
    public int capitalSince;
    public int sizeRank;

    /**
     * Constructor.
     */
    public State(String name, long capitalId, int statehood, int capitalSince, int sizeRank) {
        this.name = name;
        this.capitalId = capitalId;
        this.statehood = statehood;
        this.capitalSince = capitalSince;
        this.sizeRank = sizeRank;
    }

    /**
     * toString for debugging.
     */
    @Ignore
    @NonNull
    @Override
    public String toString() {
        return String.format("State(name=%s)", this.name);
    }
}
