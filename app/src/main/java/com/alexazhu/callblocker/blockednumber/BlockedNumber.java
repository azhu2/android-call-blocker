package com.alexazhu.callblocker.blockednumber;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import java.util.regex.Pattern;

@Entity(tableName = "blockednumbers")
public class BlockedNumber {
    @ColumnInfo(name = "type")
    private final BlockedNumberType type;
    @PrimaryKey
    @NonNull
    private final Pattern regex;

    public BlockedNumber(@NonNull final BlockedNumberType type, @NonNull final Pattern regex) {
        this.type = type;
        this.regex = regex;
    }

    public BlockedNumber(@NonNull final BlockedNumberType type, @NonNull final String number) {
        this.type = type;
        switch (type) {
            case EXACT_MATCH:
                if (!Pattern.matches("\\d+", number)) {
                    throw new IllegalArgumentException(String.format("Blocked number must be a string of digits only: %s", number));
                }
                this.regex = Pattern.compile("^" + number + "$");
                break;
            case REGEX_MATCH:
                if (!Pattern.matches("\\d+", number)) {
                    throw new IllegalArgumentException(String.format("Blocked number prefix must be a string of digits only: %s", number));
                }
                this.regex = Pattern.compile("^" + number + "\\d+$");
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown blocked number type: %s", type));
        }
    }

    @NonNull
    public BlockedNumberType getType() {
        return type;
    }

    @NonNull
    public Pattern getRegex() {
        return regex;
    }

    public String toFormattedString() {
        String pattern = regex.pattern()
                .replace("^", "")
                .replace("\\d+", "")
                .replace("$", "");

        if (pattern.length() > 6) {
            return String.format("(%s) %s-%s",
                    pattern.substring(0, 3),
                    pattern.substring(3, 6),
                    pattern.substring(6));
        } else if (pattern.length() >= 3) {
            return String.format("(%s) %s",
                    pattern.substring(0, 3),
                    pattern.substring(3));
        }
        return pattern;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null || !(other instanceof BlockedNumber)) {
            return false;
        }

        final BlockedNumber otherObj = (BlockedNumber) other;

        return this.type == otherObj.type && this.regex.pattern().equals(otherObj.regex.pattern());
    }

    @Override
    public String toString() {
        return String.format("%s|%s", type, regex);
    }

    @TypeConverter
    public static String typeToString(final BlockedNumberType type) {
        return type.name();
    }

    @TypeConverter
    public static BlockedNumberType typeFromString(final String type) {
        return BlockedNumberType.valueOf(type);
    }

    @TypeConverter
    public static String patternToString(final Pattern regex) {
        return regex.pattern();
    }

    @TypeConverter
    public static Pattern patternFromString(final String regex) {
        return Pattern.compile(regex);
    }
}
