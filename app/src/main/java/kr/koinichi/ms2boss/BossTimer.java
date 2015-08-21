package kr.koinichi.ms2boss;

import android.app.ActionBar;
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
import android.os.Build;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;


public class BossTimer extends AppCompatActivity {

    public static ArrayList<Boss> bosses = null;
    public static int sort_by = BossAdapter.SORT_BY_TIME;
    public static int time_format = BossAdapter.TIME_FORMAT_MINUTES;
    public static boolean noti_flag = true;
    public static int noti_before = 10;
    public static int MAX_DISP = 50;
    public static Context ctx;
    public static String packageName;
    private static SharedPreferences sp;
    private static String[] boss_ids;


    private void initializeBossData() {
        bosses = new ArrayList<Boss>();
        boss_ids = getResources().getStringArray(R.array.boss_ids);
        for (int i=0; i<boss_ids.length; i++) {
            bosses.add(new Boss(boss_ids[i]));
        }


        Collections.sort(bosses, new Comparator<Boss>() {
            @Override
            public int compare(Boss lhs, Boss rhs) {
                return lhs.name.compareToIgnoreCase(rhs.name);
            }
        });
        for (int i=0; i<bosses.size(); i++) {
            bosses.get(i).id = i;
        }
    }

    public static void toggleBossNotiFlag(int id) {
        bosses.get(id).toggleNotiFlag();
    }

    public static class BossAdapter extends BaseAdapter {
        public ArrayList<SimpleBoss> boss_list;
        public static final int SORT_BY_BOSS = 0;
        public static final int SORT_BY_TIME = 1;

        public static final int TIME_FORMAT_MINUTES = 0;
        public static final int TIME_FORMAT_EXACT = 1;

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
                    boss_list.add(new SimpleBoss(boss.id,
                                                boss.name,
                                                boss.location,
                                                boss.getNextSpawnTime(),
                                                boss.getNextSpawnIn(1),
                                                boss.icon));
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


            for (int i = 0; i < size; i++) {
                Boss boss = bosses.get(i);
                if (!boss.show_flag) {
                    continue;
                }
                int time = boss.getNextSpawnIn(0);
                if (-5 <= time && time < 0) {
                    boss_list.add(new SimpleBoss(boss.id, boss.name, boss.location, boss.getNextSpawnTime(), time, boss.icon));
                    count++;
                    if (count > MAX_DISP) {
                        break;
                    }
                }

            }

            while (count <= MAX_DISP) {
                int min_v = 0x7fffffff, min_i = -1;
                for (int i = 0; i < size; i++) {
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
                boss_list.add(new SimpleBoss(boss.id, boss.name, boss.location, boss.getNextSpawnTime(), min_v, boss.icon));

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
            boss_time.setText(boss.getTimeString(time_format));
            boss_time.setTextColor(boss.text_color);
            boss_time.setTypeface(null, boss.text_style);
            boss_loc.setText(boss.location);
            boss_icon.setImageResource(boss.icon);


            return view;
        }
    }


    public static BossAdapter bossAdapter = null;
    private int doCleanRefresh = 5;

    public void displayAdapter() {
        int new_sort_by = Integer.parseInt(sp.getString("pref_sort_by", "1"));
        int new_time_format = Integer.parseInt(sp.getString("pref_time_format", "1"));
        //Log.d("KOINICHI", String.format("%d,%d  %d,%d",sort_by,new_sort_by,time_format,new_time_format));
        boolean forceUpdateList = ((new_sort_by != sort_by) || (new_time_format != time_format));
        sort_by = new_sort_by;
        time_format = new_time_format;

        if (bossAdapter == null || forceUpdateList) {
            bossAdapter = new BossAdapter(sort_by);
            forceUpdateList = false;

            ListView listView = (ListView) findViewById(R.id.boss_list);
            listView.setAdapter(bossAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(BossTimer.getContext(), BossActivity.class);
                    SimpleBoss boss = (SimpleBoss) parent.getAdapter().getItem(position);
                    i.putExtra("boss_id", boss.id);
                    startActivity(i);
                }
            });
            bossAdapter.updateBossList(sort_by);
        } else if (doCleanRefresh == 0) {
            doCleanRefresh = 5;
            forceUpdateList = false;
            bossAdapter.updateBossList(sort_by);
            bossAdapter.notifyDataSetChanged();
        } else {
            doCleanRefresh--;
        }
    }

    public void notifyBosses() {
        if (!noti_flag) {
            return;
        }
        int count = 0;
        int icon = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(noti_before);
        sb.append(getString(R.string.minutes_later));
        sb.append(" : ");
        sb.append(getString(R.string.noti_message));
        sb.append(" \n");
        for (int i = 0; i < bosses.size(); i++) {
            Boss boss = bosses.get(i);
            if (boss.notifyNow(noti_before)) {
                if (count > 0) {
                    sb.append(", ");
                }
                count++;
                icon = boss.icon;
                sb.append(boss.name);
                Log.d("KOINICHI", "notify " + boss.name);
            }
        }
        if (count > 0) {
            Intent intent = new Intent(this, BossTimer.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent contentintent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationCompat.Builder noti = new NotificationCompat.Builder(getContext())
                    .setContentTitle(getString(R.string.noti_title))
                    .setSmallIcon(R.drawable.noti_icon)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(contentintent)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000})
                    .setDefaults(Notification.DEFAULT_LIGHTS)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(sb.toString()));
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(0, noti.build());
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

        packageName = getPackageName();
        ctx = getApplicationContext();
        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        bossAdapter = null;

        initializeBossData();
        for (int i = 0; i < bosses.size(); i++) {
            Boss boss = bosses.get(i);
            boss.show_flag = sp.getBoolean("show_flag_" + boss.name, true);
            boss.noti_flag = sp.getBoolean("noti_flag_" + boss.name, false);
        }
        noti_flag = sp.getBoolean("pref_noti_flag", true);
        noti_before = Integer.parseInt(sp.getString("pref_noti_delay", "10"));
        MAX_DISP = Integer.parseInt(sp.getString("pref_show_num", "50"));
        sort_by = Integer.parseInt(sp.getString("pref_sort_by", "1"));
        time_format = Integer.parseInt(sp.getString("pref_time_format", "1"));


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

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    public static Context getContext() {
        return ctx;
    }
    public static ActionBar getAcionBar() { return getAcionBar(); }

}
