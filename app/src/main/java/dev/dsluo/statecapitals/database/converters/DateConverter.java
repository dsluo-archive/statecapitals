package dev.dsluo.statecapitals.database.converters;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Helper to allow storage of {@link Date} objects in the database as timestamps.
 *
 * @author David Luo
 */
public class DateConverter {

    /**
     * Convert from a timestamp to a {@link Date} object.
     *
     * @param value The timestamp.
     * @return A {@link Date} object.
     */
    @TypeConverter
    public static Date timestampToDate(Long value) {
        return value == null ? null : new Date(value);
    }

    /**
     * Convert from a {@link Date} object to a timestamp.
     *
     * @param date A {@link Date} object.
     * @return The timestamp.
     */
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
