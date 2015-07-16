package kr.koinichi.ms2boss;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class BossTimer extends AppCompatActivity {

    public static ArrayList<Boss> bosses = null;
    public static int disp_type = BossAdapter.SORT_BY_TIME;
    public static int noti_before = 15;
    public static int MAX_DISP = 50;
    public static Context ctx;

    SharedPreferences sp;


    private void initializeBossData() {
        bosses = new ArrayList<Boss>();
        bosses.add(new Boss(R.string.baraha, 24, R.string.baraha_loc, R.drawable.baraha, R.array.baraha, R.string.elite_boss));
        bosses.add(new Boss(R.string.boogieccoli_maple, 3, R.string.boogieccoli_maple_loc, R.drawable.boogieccoli_maple, R.array.boogieccoli_maple, R.string.elite_boss));
        bosses.add(new Boss(R.string.boogieccoli_victoria, 3, R.string.boogieccoli_victoria_loc, R.drawable.boogieccoli_victoria, R.array.boogieccoli_victoria, R.string.elite_boss));
        bosses.add(new Boss(R.string.chau_the_guard, 12, R.string.chau_the_guard_loc, R.drawable.chau_the_guard, R.array.chau_the_guard, R.string.elite_boss));
        bosses.add(new Boss(R.string.devlin_chief, 24, R.string.devlin_chief_loc, R.drawable.devlin_chief, R.array.devlin_chief, R.string.unknown_boss));
        bosses.add(new Boss(R.string.devlin_warrior, 21, R.string.devlin_warrior_loc, R.drawable.devlin_warrior, R.array.devlin_warrior, R.string.field_boss));
        bosses.add(new Boss(R.string.devorak, 30, R.string.devorak_loc, R.drawable.devorak, R.array.devorak, R.string.raid_boss));
        bosses.add(new Boss(R.string.dun_dun, 15, R.string.dun_dun_loc, R.drawable.dun_dun, R.array.dun_dun, R.string.field_boss));
        bosses.add(new Boss(R.string.epie, 13, R.string.epie_loc, R.drawable.epie, R.array.epie, R.string.elite_boss));
        bosses.add(new Boss(R.string.frankenette, 26, R.string.frankenette_loc, R.drawable.frankenette, R.array.frankenette, R.string.elite_boss));
        bosses.add(new Boss(R.string.giant_lavaeye, 30, R.string.giant_lavaeye_loc, R.drawable.giant_lavaeye, R.array.giant_lavaeye, R.string.elite_boss));
        bosses.add(new Boss(R.string.giant_turtle, 18, R.string.giant_turtle_loc, R.drawable.giant_turtle, R.array.giant_turtle, R.string.field_boss));
        bosses.add(new Boss(R.string.griffina, 30, R.string.griffina_loc, R.drawable.griffina, R.array.griffina, R.string.field_boss));
        bosses.add(new Boss(R.string.griffon, 23, R.string.griffon_loc, R.drawable.griffon, R.array.griffon, R.string.field_boss));
        bosses.add(new Boss(R.string.hemokan, 28, R.string.hemokan_loc, R.drawable.hemokan, R.array.hemokan, R.string.raid_boss));
        bosses.add(new Boss(R.string.last_bajar, 30, R.string.last_bajar_loc, R.drawable.last_bajar, R.array.last_bajar, R.string.raid_boss));
        bosses.add(new Boss(R.string.madonette, 17, R.string.madonette_loc, R.drawable.madonette, R.array.madonette, R.string.elite_boss));
        bosses.add(new Boss(R.string.mark_alpha, 27, R.string.mark_alpha_loc, R.drawable.mark_alpha, R.array.mark_alpha, R.string.field_boss));
        bosses.add(new Boss(R.string.mark_beta, 30, R.string.mark_beta_loc, R.drawable.mark_beta, R.array.mark_beta, R.string.raid_boss));
        bosses.add(new Boss(R.string.mushmom, 21, R.string.mushmom_loc, R.drawable.mushmom, R.array.mushmom, R.string.elite_boss));
        bosses.add(new Boss(R.string.nixie, 15, R.string.nixie_loc, R.drawable.nixie, R.array.nixie, R.string.elite_boss));
        bosses.add(new Boss(R.string.revenant_zombie, 22, R.string.revenant_zombie_loc, R.drawable.revenant_zombie, R.array.revenant_zombie, R.string.elite_boss));
        bosses.add(new Boss(R.string.urza, 8, R.string.urza_loc, R.drawable.urza, R.array.urza, R.string.elite_boss));
        bosses.add(new Boss(R.string.zirant, 30, R.string.zirant_loc, R.drawable.zirant, R.array.zirant, R.string.elite_boss));
    }

    public static class BossAdapter extends BaseAdapter {
        public ArrayList<SimpleBoss> boss_list;
        public static final int SORT_BY_BOSS = 0;
        public static final int SORT_BY_TIME = 1;

        public BossAdapter(int dispType) {
            boss_list = new ArrayList<SimpleBoss>();
            updateBossList(dispType);
        }

        public void updateBossList(int dispType) {
            boss_list.clear();
            if (dispType == SORT_BY_BOSS) {
                sortByBoss();
            }
            if (dispType == SORT_BY_TIME) {
                sortByTime();
            }
        }


        private void sortByBoss() {
            int size = bosses.size();
            for (int i = 0; i < size; i++) {
                Boss boss = bosses.get(i);
                if (boss.show_flag) {
                    boss_list.add(new SimpleBoss(boss.name, boss.location, boss.getNextSpawnTime(), boss.getNextSpawnIn(1), boss.icon));
                }
            }
        }

        private void sortByTime() {
            int size = bosses.size();
            int count = 0;
            int[] idx = new int[size];
            for (int i = 0; i < size; i++) {
                idx[i] = 1;
            }


            for (int i=0; i<size; i++) {
                Boss boss = bosses.get(i);
                if (!boss.show_flag) {
                    continue;
                }
                int time = boss.getNextSpawnIn(0);
                if (-5 <= time && time < 0) {
                    boss_list.add(new SimpleBoss(boss.name, boss.location, boss.getNextSpawnTime(), time, boss.icon));
                    count++;
                    if (count > MAX_DISP) {
                        break;
                    }
                }

            }

            while (count <= MAX_DISP) {
                int min_v = 0x7fffffff, min_i = -1;
                for (int i=0; i<size; i++) {
                    if (!bosses.get(i).show_flag) {
                        continue;
                    }
                    int time = bosses.get(i).getNextSpawnIn(idx[i]);
                    if (min_v > time) {
                        min_v = time;
                        min_i = i;
                    }
                }

                Boss boss = bosses.get(min_i);
                boss_list.add(new SimpleBoss(boss.name, boss.location, boss.getNextSpawnTime(), min_v, boss.icon));

                idx[min_i]++;
                count++;
            }
        }


        @Override
        public int getCount() {
            return boss_list.size();
        }

        @Override
        public SimpleBoss getItem(int idx) {
            return boss_list.get(idx);
        }

        @Override
        public long getItemId(int id) {
            return id;
        }

        @Override
        public View getView(int id, View view, ViewGroup viewgroup) {
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.listitem, viewgroup, false);
            }

            ImageView boss_icon = (ImageView) view.findViewById(R.id.boss_icon);
            TextView boss_name = (TextView) view.findViewById(R.id.boss_name);
            TextView boss_time = (TextView) view.findViewById(R.id.boss_time);
            TextView boss_loc = (TextView) view.findViewById(R.id.boss_loc);

            SimpleBoss boss = boss_list.get(id);

            boss_name.setText(boss.name);
            boss_time.setText(boss.getTimeString(disp_type));
            boss_time.setTextColor(boss.text_color);
            boss_time.setTypeface(null, boss.text_style);
            boss_loc.setText(boss.location);
            boss_icon.setImageResource(boss.icon);


            return view;
        }
    }


    public static BossAdapter bossAdapter = null;
    int doCleanRefresh = 0;
    public void displayAdapter() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int b = Integer.parseInt(sp.getString("pref_disp_type", "1"));
        if (bossAdapter == null || disp_type != b) {
            disp_type = b;
            bossAdapter = new BossAdapter(disp_type);

            ListView listView = (ListView) findViewById(R.id.boss_list);
            listView.setAdapter(bossAdapter);
        }
        else if (doCleanRefresh == 0) {
            doCleanRefresh = 5;
            bossAdapter.updateBossList(disp_type);
            bossAdapter.notifyDataSetChanged();
        }
        else {
            doCleanRefresh--;
        }
    }

    public void notifyBosses()
    {
        int count = 0;
        int icon = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(noti_before);
        sb.append(getString(R.string.minutes_later));
        sb.append(getString(R.string.noti_message));
        for (int i=0; i<bosses.size(); i++) {
            Boss boss = bosses.get(i);
            if (boss.notifyNow(noti_before)) {
                count++;
                icon = boss.icon;
                sb.append(boss.name);
                sb.append(", ");

            }
        }
        if (count > 0) {
            sb.delete(sb.length() - 2, sb.length());
            Intent intent = new Intent(this, BossTimer.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pintent = PendingIntent.getActivity(this, 0, intent, 0);

            Notification noti = new Notification.Builder(getContext())
                    .setContentTitle(getString(R.string.noti_title))
                    .setSmallIcon(R.drawable.noti_icon)
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setStyle(new Notification.BigTextStyle().bigText(sb.toString()))
                    .build();
            NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            nm.notify(0, noti);
        }
    }



    final Handler handler = new Handler();

    private void createDispTypeTimer() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                displayAdapter();
                notifyBosses();
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        PreferenceManager.setDefaultValues(this, R.xml.notiflag, false);
        PreferenceManager.setDefaultValues(this, R.xml.showflag, false);

        ctx = getApplicationContext();
        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        initializeBossData();
        for (int i=0; i<bosses.size(); i++) {
            Boss boss = bosses.get(i);
            boss.show_flag = sp.getBoolean("show_flag_" + boss.name, true);
        }
        noti_before = Integer.parseInt(sp.getString("pref_noti_delay", "15"));
        MAX_DISP = Integer.parseInt(sp.getString("pref_show_num", "50"));


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_timer);

        createDispTypeTimer();
        displayAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_boss_timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, BasicSettingActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Context getContext() {
        return ctx;
    }


}
