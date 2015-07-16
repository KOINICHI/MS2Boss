package kr.koinichi.ms2boss;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by KOINICHI on 2015/07/15.
 */
public class ShowFlagSettingActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new ShowFlagFragment()).commit();
    }

    public static class ShowFlagFragment extends PreferenceFragment {

        PreferenceCategory list_pref;
        Preference show_num;
        SharedPreferences sp;
        Context c;


        private void updatePreferenceStrings() {
            String num = sp.getString("pref_show_num", "50");

            EditTextPreference numsp = (EditTextPreference) findPreference("pref_show_num");
            numsp.setSummary(num);
        }

        private Preference createBossPreference(final Boss boss)
        {
            SwitchPreference pref = new SwitchPreference(c);

            pref.setKey("show_flag_" + boss.name);
            pref.setTitle(boss.name);
            pref.setEnabled(true);
            pref.setDefaultValue(boss.show_flag);
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    boss.show_flag = !boss.show_flag;
                    Log.d("KOINICHI", String.format("%s now %s", boss.name, boss.show_flag ? "on" : "off"));
                    return true;
                }
            });

            return pref;
        }

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.showflag);


            list_pref = (PreferenceCategory) findPreference("pref_show_flag_boss_list");
            show_num = findPreference("pref_show_num");
            sp = getPreferenceScreen().getSharedPreferences();
            c = BossTimer.getContext();

            ArrayList<Boss> bosses = BossTimer.bosses;
            for (int i=0; i<bosses.size(); i++) {
                list_pref.addPreference(createBossPreference(bosses.get(i)));
            }


            show_num.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference pref, Object newValue) {
                    pref.setSummary((String) newValue);
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
            BossTimer.MAX_DISP = Integer.parseInt(sp.getString("pref_show_num", "50"));
            super.onPause();
        }
    }
}
