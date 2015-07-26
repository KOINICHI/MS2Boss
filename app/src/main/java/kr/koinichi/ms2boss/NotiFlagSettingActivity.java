package kr.koinichi.ms2boss;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;

import java.util.ArrayList;

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

        PreferenceCategory list_pref;
        Preference noti_pref;
        Preference delay_pref;
        SharedPreferences sp;
        Context c;

        private void updatePreferenceStrings() {
            String delay = sp.getString("pref_noti_delay", "10");

            EditTextPreference delaysp = (EditTextPreference) findPreference("pref_noti_delay");
            delaysp.setSummary(delay + c.getString(R.string.minutes_before));
            delaysp.setEnabled(sp.getBoolean("pref_noti_flag", false));
        }


        private Preference createBossPreference(final Boss boss)
        {
            SwitchPreference pref = new SwitchPreference(c);

            pref.setKey("noti_flag_" + boss.name);
            pref.setTitle(boss.name);
            pref.setEnabled(true);
            pref.setDefaultValue(boss.noti_flag);
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    boss.noti_flag = !boss.noti_flag;
                    return true;
                }
            });

            return pref;
        }

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.notiflag);

            list_pref = (PreferenceCategory) findPreference("pref_noti_flag_boss_list");
            noti_pref = findPreference("pref_noti_flag");
            delay_pref = findPreference("pref_noti_delay");
            sp = getPreferenceScreen().getSharedPreferences();
            c = BossTimer.getContext();

            boolean noti_flag = sp.getBoolean("pref_noti_flag", false);


            ArrayList<Boss> bosses = BossTimer.bosses;
            for (int i=0; i<bosses.size(); i++) {
                list_pref.addPreference(createBossPreference(bosses.get(i)));
            }

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
                    BossTimer.noti_flag = (Boolean) newValue;
                    delay_pref.setEnabled((Boolean) newValue);
                    list_pref.setEnabled((Boolean) newValue);
                    return true;
                }
            });

            delay_pref.setEnabled(noti_flag);
            list_pref.setEnabled(noti_flag);
        }

        @Override
        public void onResume() {
            updatePreferenceStrings();
            super.onResume();
        }

        @Override
        public void onPause() {
            BossTimer.noti_before = Integer.parseInt(sp.getString("pref_noti_delay", "10"));
            super.onPause();
        }
    }
}
