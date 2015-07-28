package kr.koinichi.ms2boss;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import junit.framework.Assert;

/**
 * Created by KOINICHI on 2015/07/25.
 */
public class BossActivity extends AppCompatActivity {
    private Boss boss;
    private Menu menu = null;
    private boolean noti_flag;
    private SharedPreferences sp;

    ImageView pic;
    TextView level, boss_type, type_label, type, loc_label, loc, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_detail);

        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        pic = (ImageView) findViewById(R.id.boss_detail_pic);
        level = (TextView) findViewById(R.id.boss_detail_level);
        boss_type = (TextView) findViewById(R.id.boss_detail_boss_type);
        type_label = (TextView) findViewById(R.id.boss_detail_type_label);
        type = (TextView) findViewById(R.id.boss_detail_type);
        loc_label = (TextView) findViewById(R.id.boss_detail_loc_label);
        loc = (TextView) findViewById(R.id.boss_detail_loc);
        desc = (TextView) findViewById(R.id.boss_detail_desc);

        int boss_id = getIntent().getIntExtra("boss_id", -1);
        boss = BossTimer.bosses.get(boss_id);
        noti_flag = boss.noti_flag;

        getSupportActionBar().setTitle(boss.name);
        pic.setImageResource(boss.pic);
        level.setText(String.format("Lv. %d", boss.level));
        int boss_type_res_id = 0;
        switch (boss.boss_type)
        {
            case Boss.ELITE: boss_type_res_id = R.string.elite_boss; break;
            case Boss.FIELD: boss_type_res_id = R.string.field_boss; break;
            case Boss.RAID: boss_type_res_id = R.string.raid_boss; break;
            case Boss.UNKNOWN: boss_type_res_id = R.string.unknown_boss; break;
            default:
        }
        boss_type.setText(getString(boss_type_res_id));
        type_label.setText(getString(R.string.boss_type_label));
        type.setText(boss.type);
        loc_label.setText(getString(R.string.boss_loc_label));
        loc.setText(boss.location);
        desc.setText(boss.desc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu = m;

        getMenuInflater().inflate(R.menu.menu_boss_detail, menu);
        MenuItem item = menu.findItem(R.id.menu_toggle_noti);
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
