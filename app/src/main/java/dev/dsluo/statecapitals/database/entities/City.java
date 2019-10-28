package dev.dsluo.statecapitals.database.entities;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

/**
 * City Model
 *
 * @author David Luo
 */
@Entity(foreignKeys = {
        @ForeignKey(entity = State.class,
                parentColumns = "id",
                childColumns = "stateId",
                onDelete = ForeignKey.CASCADE,
                deferred = true)
},
        indices = {
                @Index(value = "stateId")
        })
public class City {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public long stateId;

    /**
     * Constructor for creating new cities.
     */
    @Ignore
    public City(String name) {
        this.name = name;
    }

    /**
     * Required constructor for Room.
     */
    public City() {
    }

    /**
     * toString for debugging.
     */
    @SuppressLint("DefaultLocale")
    @Ignore
    @NonNull
    @Override
    public String toString() {
        return String.format("City(name=%s, stateId=%d)",
                this.name,
                this.stateId);
    }

    /**
     * equals() for removal from {@link List}s
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!(obj instanceof City))
            return false;
        City other = (City) obj;
        return this.id == other.id &&
                this.name.equals(other.name) &&
                this.stateId == other.stateId;
    }
}
