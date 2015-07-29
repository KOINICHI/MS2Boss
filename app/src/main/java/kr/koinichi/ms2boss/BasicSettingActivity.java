package kr.koinichi.ms2boss;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by KOINICHI on 2015/07/12.
 */
public class BasicSettingActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new BasicSettingFragment()).commit();
    }

    public static class BasicSettingFragment extends PreferenceFragment {

        SharedPreferences sp;
        ListPreference sort_by;
        ListPreference time_format;
        Preference show_flag;
        Preference noti_flag;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            sp = getPreferenceManager().getSharedPreferences();

            sort_by = (ListPreference) findPreference("pref_sort_by");
            time_format = (ListPreference) findPreference("pref_time_format");
            show_flag = (Preference) findPreference("pref_show_setting");
            noti_flag = (Preference) findPreference("pref_noti_setting");

            show_flag.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent(getActivity(), ShowFlagSettingActivity.class);
                    startActivity(i);
                    return true;
                }
            });

            noti_flag.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent(getActivity(), NotiFlagSettingActivity.class);
                    startActivity(i);
                    return true;
                }
            });
        }
    }





}
