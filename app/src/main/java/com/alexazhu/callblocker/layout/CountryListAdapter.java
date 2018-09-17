package com.alexazhu.callblocker.layout;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alexazhu.callblocker.R;
import com.alexazhu.callblocker.util.AsyncExecutorUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CountryListAdapter extends BaseAdapter {
    private final Activity activity;
    private final PhoneNumberUtil phoneNumberUtil;
    private final Locale deviceLocale;
    private List<Country> countryCodes;

    public CountryListAdapter(final Activity activity, final AddNumberDialogFragment dialog) {
        super();

        this.activity = activity;
        this.deviceLocale = activity.getResources().getConfiguration().getLocales().get(0);
        this.phoneNumberUtil = PhoneNumberUtil.getInstance();
        AsyncExecutorUtil.getInstance().getExecutor().execute(() -> {
            this.countryCodes = PhoneNumberUtil.getInstance().getSupportedCallingCodes().stream()
                    .map(Country::new)
                    .sorted()
                    .collect(Collectors.toList());
            int defaultPosition = IntStream.range(0, countryCodes.size())
                    .filter(index -> countryCodes.get(index).regionCode.equals(deviceLocale.getCountry()))
                    .findFirst()
                    .orElse(0);
            activity.runOnUiThread(() -> {
                this.notifyDataSetChanged();
                dialog.selectCountry(defaultPosition);
            });
        });
    }

    @Override
    public int getCount() {
        return countryCodes != null ? countryCodes.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return countryCodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View itemView = inflater.inflate(R.layout.item_country, null);

        TextView regionCode = itemView.findViewById(R.id.country_name);
        regionCode.setText(countryCodes.get(position).countryName);
        TextView flag = itemView.findViewById(R.id.country_flag);
        flag.setText(countryCodes.get(position).flagCode);

        return itemView;
    }

    public class Country implements Comparable<Country>{
        private final Integer countryCode;
        private final String regionCode;
        private final String countryName;
        private final String flagCode;

        public Country(Integer countryCode) {
            this.countryCode = countryCode;

            this.regionCode = phoneNumberUtil.getRegionCodeForCountryCode(countryCode);
            String deviceLanguage = deviceLocale.getLanguage();
            Locale countryLocale = new Locale(deviceLanguage, regionCode);

            String name = countryLocale.getDisplayCountry();
            if (name.equals("World")) {
                // Handle odd cases
                this.countryName = "\u200bOther " + this.countryCode;
                this.flagCode = "";
            } else {
                this.countryName = name;

                // See https://stackoverflow.com/a/35849652
                int firstLetter = Character.codePointAt(regionCode, 0) - 0x41 + 0x1F1E6;
                int secondLetter = Character.codePointAt(regionCode, 1) - 0x41 + 0x1F1E6;
                this.flagCode = new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
            }
        }

        @Override
        public int compareTo(@NonNull Country o) {
            return countryName.compareTo(o.countryName);
        }

        public Integer getCountryCode() {
            return countryCode;
        }
    }
}
