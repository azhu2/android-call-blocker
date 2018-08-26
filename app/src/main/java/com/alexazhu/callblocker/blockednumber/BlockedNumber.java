package com.alexazhu.callblocker.blockednumber;

import java.util.regex.Pattern;

public class BlockedNumber {
    private final BlockedNumberType type;
    private final Pattern regex;

    public BlockedNumber(final BlockedNumberType type, final String regex) {
        this.type = type;
        this.regex = Pattern.compile(regex);
    }

    public BlockedNumber(final BlockedNumberType type, final Pattern regex) {
        this.type = type;
        this.regex = regex;
    }

    public BlockedNumberType getType() {
        return type;
    }

    public String getPattern() {
        return regex.pattern();
        // TODO Strip \d*$ from end of regex
    }
}
