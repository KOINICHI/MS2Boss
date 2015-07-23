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
    public enum BossType {
        ELITE, FIELD, RAID, UNKNOWN;
    }
    public String name;
    public int level;
    public String location;
    public int icon;
    public BossType type;

    public ArrayList<String> spawn_times;
    public int next_spawn_time_idx;

    public Boolean show_flag;
    public Boolean noti_flag;
    private int notified_already;


    public Boss(int name, int level, int location, int icon, int time, int type) {
        Context c = BossTimer.getContext();
        this.name = c.getString(name);
        this.level = level;
        this.location = c.getString(location);
        this.icon = icon;

        if (type == R.string.elite_boss) { this.type = BossType.ELITE; }
        if (type == R.string.field_boss) { this.type = BossType.FIELD; }
        if (type == R.string.raid_boss) { this.type = BossType.RAID; }
        if (type == R.string.unknown_boss) { this.type = BossType.UNKNOWN; }

        this.spawn_times = new ArrayList<String>(Arrays.asList(c.getResources().getStringArray(time)));

        notified_already = 0;
        show_flag = true;
        noti_flag = true;
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
        if (type == BossType.UNKNOWN) {
            return "";
        }
        if (type == BossType.RAID) {
            return "";
        }

        updateNextSpawnTime();
        String next_time = spawn_times.get((next_spawn_time_idx + 1) % spawn_times.size());

        return next_time;
    }

    public int getNextSpawnIn(int n)
    {
        if (!(type == BossType.ELITE || type == BossType.FIELD)) {
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
            if (delay > time_left) {
                break;
            }
        }
        return false;
    }

}

