package kr.koinichi.ms2boss;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Created by KOINICHI on 2015/07/15.
 */
public class NotiFlagSettingActivity extends PreferenceActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new NotiFlagFragment()).commit();
    }

    public static class NotiFlagFragment extends PreferenceFragment {

        Preference noti_pref;
        Preference delay_pref;
        SharedPreferences sp;
        Context c;

        private void updatePreferenceStrings() {
            String delay = sp.getString("pref_noti_delay", "15");

            EditTextPreference delaysp = (EditTextPreference) findPreference("pref_noti_delay");
            delaysp.setSummary(delay + c.getString(R.string.minutes_before));
            delaysp.setEnabled(sp.getBoolean("pref_noti_flag", false));
        }

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.notiflag);

            noti_pref = findPreference("pref_noti_flag");
            delay_pref = findPreference("pref_noti_delay");
            sp = getPreferenceScreen().getSharedPreferences();
            c = BossTimer.getContext();

            delay_pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference pref, Object newValue) {
                    pref.setSummary((String) newValue + BossTimer.getContext().getString(R.string.minutes_before));
                    return true;
                }
            });

            noti_pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference pref, Object newValue) {
                    delay_pref.setEnabled((Boolean) newValue);
                    return true;
                }
            });

        }

        @Override
        public void onResume() {
            updatePreferenceStrings();
            super.onResume();
        }

        @Override
        public void onPause() {
            BossTimer.noti_before = Integer.parseInt(sp.getString("pref_noti_delay", "15"));
            super.onPause();
        }
    }
}
