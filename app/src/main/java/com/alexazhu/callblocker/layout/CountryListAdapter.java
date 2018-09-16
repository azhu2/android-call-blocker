package com.alexazhu.callblocker.layout;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alexazhu.callblocker.R;
import com.alexazhu.callblocker.activity.ConfigurationActivity;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CountryListAdapter extends BaseAdapter {
    private final ConfigurationActivity context;
    private final PhoneNumberUtil phoneNumberUtil;
    private final List<Country> countryCodes;
    private final Locale deviceLocale;
    private final int defaultPosition;

    public CountryListAdapter(final ConfigurationActivity context) {
        super();

        this.context = context;
        this.deviceLocale = context.getResources().getConfiguration().getLocales().get(0);
        this.phoneNumberUtil = PhoneNumberUtil.getInstance();
        this.countryCodes = PhoneNumberUtil.getInstance().getSupportedCallingCodes().stream()
                .map(countryCode -> new Country(countryCode))
                .sorted()
                .collect(Collectors.toList());
        this.defaultPosition = IntStream.range(0, countryCodes.size())
                .filter(index -> countryCodes.get(index).regionCode.equals(deviceLocale.getCountry()))
                .findFirst()
                .orElse(0);
    }

    @Override
    public int getCount() {
        return countryCodes.size();
    }

    @Override
    public Object getItem(int position) {
        return countryCodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public int getDefaultPosition() {
        return defaultPosition;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View itemView = inflater.inflate(R.layout.item_country, null);

        TextView regionCode = itemView.findViewById(R.id.country_name);
        regionCode.setText(countryCodes.get(position).countryName);
        TextView countryCode = itemView.findViewById(R.id.country_code);
        countryCode.setText(countryCodes.get(position).countryCode.toString());
        TextView flag = itemView.findViewById(R.id.country_flag);
        flag.setText(countryCodes.get(position).flagCode);

        return itemView;
    }

    private class Country implements Comparable<Country>{
        private final Integer countryCode;
        private final String regionCode;
        private final String countryName;
        private final String flagCode;

        public Country(Integer countryCode) {
            this.countryCode = countryCode;

            this.regionCode = phoneNumberUtil.getRegionCodeForCountryCode(countryCode);
            String deviceLanguage = deviceLocale.getLanguage();
            Locale countryLocale = new Locale(deviceLanguage, regionCode);

            this.countryName = countryLocale.getDisplayCountry();

            int firstLetter = Character.codePointAt(regionCode, 0) - 0x41 + 0x1F1E6;
            int secondLetter = Character.codePointAt(regionCode, 1) - 0x41 + 0x1F1E6;
            this.flagCode = new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
        }

        @Override
        public int compareTo(@NonNull Country o) {
            return countryName.compareTo(o.countryName);
        }
    }
}
