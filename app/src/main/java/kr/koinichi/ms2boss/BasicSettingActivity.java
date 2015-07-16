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
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);


            Preference show_flag = (Preference) findPreference("pref_show_setting");
            show_flag.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent(getActivity(), ShowFlagSettingActivity.class);
                    startActivity(i);
                    return true;
                }
            });


            Preference noti_flag = (Preference) findPreference("pref_noti_setting");
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
