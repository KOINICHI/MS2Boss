package kr.koinichi.ms2boss;

import android.app.Activity;
import android.content.Context;
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
public class SettingActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }


    public static class MyPreferenceFragment extends PreferenceFragment {

        public void updatePreferenceStrings() {
            SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
            EditTextPreference editTextPref = (EditTextPreference) findPreference("pref_noti_delay");
            String delay = sp.getString("pref_noti_delay", "15");
            Context c = BossTimer.getContext();

            editTextPref.setSummary(delay + c.getString(R.string.minutes_before));
        }

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            final Preference delay_pref = findPreference("pref_noti_delay");
            delay_pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference pref, Object newValue) {
                    SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
                    pref.setSummary((String) newValue + BossTimer.getContext().getString(R.string.minutes_before));
                    return true;
                }
            });

            Preference noti_pref = findPreference("pref_noti_flag");
            noti_pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference pref, Object newValue) {
                    delay_pref.setEnabled((Boolean)newValue);
                    return true;
                }
            });

        }

        @Override
        public void onResume() {
            super.onResume();
            updatePreferenceStrings();
        }

        @Override
        public void onPause() {
            SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
            BossTimer.noti_before = Integer.parseInt(sp.getString("pref_noti_delay", "15"));
            super.onPause();
        }

    }
}
