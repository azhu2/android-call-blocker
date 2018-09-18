package com.alexazhu.callblocker.blockednumber;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import com.google.i18n.phonenumbers.AsYouTypeFormatter;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.regex.Pattern;

@Entity(tableName = "blockednumbers")
public class BlockedNumber {
    @ColumnInfo(name = "type") @NonNull
    private final BlockedNumberType type;
    @PrimaryKey @NonNull
    private final Pattern regex;
    @ColumnInfo(name = "country_code") @NonNull
    private final String countryCode;
    @ColumnInfo(name = "phone_number") @NonNull
    private final String phoneNumber;

    @Ignore
    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    public BlockedNumber(@NonNull final BlockedNumberType type, @NonNull final Pattern regex, @NonNull final String countryCode, @NonNull final String phoneNumber) {
        this.type = type;
        this.regex = regex;
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
    }

    public BlockedNumber(@NonNull final BlockedNumberType type, @NonNull final String countryCode, @NonNull final String number) {
        this.type = type;
        this.countryCode = countryCode.replace("+", "");
        this.phoneNumber = number;
        switch (type) {
            case EXACT_MATCH:
                if (!Pattern.matches("\\d+", number)) {
                    throw new IllegalArgumentException(String.format("Blocked number must be a string of digits only: %s", number));
                }
                this.regex = Pattern.compile("^" + getInternationalNumber() + "$");
                break;
            case REGEX_MATCH:
                if (!Pattern.matches("\\d+", number)) {
                    throw new IllegalArgumentException(String.format("Blocked number prefix must be a string of digits only: %s", number));
                }
                this.regex = Pattern.compile("^" + getInternationalNumber()+ "\\d+$");
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

    @NonNull
    public String getCountryCode() { return countryCode; }

    @NonNull
    public String getPhoneNumber() { return phoneNumber; }

    private String getInternationalNumber() {
        return String.format("\\+%s%s", countryCode, phoneNumber);
    }

    public String toFormattedString() {
        AsYouTypeFormatter formatter = phoneNumberUtil.getAsYouTypeFormatter(phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode)));
        String formattedNumber = "";
        for (char digit : phoneNumber.toCharArray()) {
            formattedNumber = formatter.inputDigit(digit);
        }
        return formattedNumber;
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
