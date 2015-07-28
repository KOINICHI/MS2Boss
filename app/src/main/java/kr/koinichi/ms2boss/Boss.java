package kr.koinichi.ms2boss;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by KOINICHI on 2015/07/10.
 */
public class Boss {
    public static final int ELITE = 0;
    public static final int FIELD = 1;
    public static final int RAID = 2;
    public static final int UNKNOWN = 3;

    public int id;

    public String name;
    public String location;
    public String type;
    public String desc;

    public int level;
    public int boss_type;

    public int icon;
    public int pic;

    public ArrayList<String> spawn_times;
    public int next_spawn_time_idx;

    public Boolean show_flag;
    public Boolean noti_flag;
    private int notified_already;


    // bosses.add(new Boss(R.string.baraha, 24, R.string.baraha_loc, R.drawable.baraha, R.array.baraha, R.string.elite_boss));
    public Boss(String id) {
        Context c = BossTimer.getContext();
        this.name = c.getString(c.getResources().getIdentifier(id, "string", BossTimer.packageName));
        this.location = c.getString(c.getResources().getIdentifier(id + "_loc", "string", BossTimer.packageName));
        this.type = c.getString(c.getResources().getIdentifier(id + "_type", "string", BossTimer.packageName));
        this.desc = c.getString(c.getResources().getIdentifier(id + "_desc", "string", BossTimer.packageName));

        this.icon = c.getResources().getIdentifier(id + "_icon", "drawable", BossTimer.packageName);
        this.pic = c.getResources().getIdentifier(id + "_big", "drawable", BossTimer.packageName);

        this.level = Integer.parseInt(c.getString(c.getResources().getIdentifier(id + "_level", "string", BossTimer.packageName)));
        this.boss_type = Integer.parseInt(c.getString(c.getResources().getIdentifier(id + "_boss_type", "string", BossTimer.packageName)));

        int time = c.getResources().getIdentifier(id + "_time", "array", BossTimer.packageName);
        this.spawn_times = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(time)));

        notified_already = 0;
        show_flag = true;
        noti_flag = false;
    }

    private int timeToMinutes(String time)
    {
        return Integer.parseInt(time.substring(0, 2)) * 60 + Integer.parseInt(time.substring(2,4));
    }

    private Calendar getCurrentTime()
    {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Calendar c = Calendar.getInstance(tz);
        c.add(Calendar.HOUR_OF_DAY, 9);
        //c.add(Calendar.MINUTE, -1);
        return c;
    }



    public void updateNextSpawnTime() {

        Calendar c = getCurrentTime();
        int c_time = c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE);
        int idx = 0;

        int size=spawn_times.size();
        for (int i=0; i<size; i++) {
            int j=(i+1) % size;
            int p_time = timeToMinutes(spawn_times.get(i));
            int n_time = timeToMinutes(spawn_times.get(j));
            if (j == 0) {
                n_time += 24*60;
            }
            if (p_time < c_time && c_time <= n_time) {
                idx = i;
                break;
            }
        }

        next_spawn_time_idx = idx;
    }

    public String getNextSpawnTime() {
        if (boss_type == Boss.UNKNOWN) {
            return "";
        }
        if (boss_type == Boss.RAID) {
            return "";
        }

        updateNextSpawnTime();
        String next_time = spawn_times.get((next_spawn_time_idx + 1) % spawn_times.size());

        return next_time;
    }

    public int getNextSpawnIn(int n)
    {
        if (!(boss_type == Boss.ELITE || boss_type == Boss.FIELD)) {
            return 0x7fffffff;
        }

        Calendar c = getCurrentTime();

        int c_time = c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE);
        int n_time = 0;
        updateNextSpawnTime();
        int size = spawn_times.size();
        int idx = next_spawn_time_idx + n;
        if (0 <= idx && idx < size) {
            n_time = 0;
        }
        if (idx < 0) {
            n_time = -24*60;
            idx += size;
        }
        if (idx >= size) {
            n_time = (24*60) * (idx/size);
            idx %= size;
        }
        n_time += timeToMinutes(spawn_times.get(idx));

        int ret = n_time - c_time;
        return ret;
    }

    public boolean notifyNow(int delay)
    {
        if (!noti_flag) {
            return false;
        }
        //Log.d("KOINICHI", String.format("%s : %d %d", name, notified_already, getNextSpawnIn(1)));
        if (notified_already > 0) {
            notified_already--;
            return false;
        }
        for (int i=1; i<10; i++) {
            int time_left = getNextSpawnIn(i);
            if (delay == time_left) {
                notified_already = 180;
                return true;
            }
        }
        return false;
    }

    public void toggleNotiFlag() {
        noti_flag = !noti_flag;
    }

}

