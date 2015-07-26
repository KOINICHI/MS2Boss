package kr.koinichi.ms2boss;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import junit.framework.Assert;

/**
 * Created by KOINICHI on 2015/07/25.
 */
public class BossActivity extends AppCompatActivity {
    private Boss boss;
    private Menu menu = null;
    private boolean noti_flag;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_detail);

        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        int boss_id = getIntent().getIntExtra("boss_id", -1);
        boss = BossTimer.bosses.get(boss_id);
        getSupportActionBar().setTitle(boss.name);

        noti_flag = boss.noti_flag;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu = m;

        getMenuInflater().inflate(R.menu.menu_boss_detail, menu);
        MenuItem item = menu.findItem(R.id.menu_toggle_noti);
        Log.d("KOINICHI", String.format("%s %s",boss.name, boss.noti_flag?"on":"off" ));
        item.setIcon(boss.noti_flag ? R.drawable.noti_on : R.drawable.noti_off);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toggle_noti:
                noti_flag = !noti_flag;
                BossTimer.toggleBossNotiFlag(boss.id);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("noti_flag_" + boss.name, noti_flag);
                editor.commit();
                item.setIcon(noti_flag ? R.drawable.noti_on : R.drawable.noti_off);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
