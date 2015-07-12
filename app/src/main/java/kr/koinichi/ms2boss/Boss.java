package kr.koinichi.ms2boss;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.sql.Time;
import java.util.ArrayList;
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

    public String[] spawn_times;
    public int next_spawn_time_idx;


    public Boss(int name, int level, int location, int icon, int time, int type) {
        Context c = BossTimer.getContext();
        this.name = c.getString(name);
        this.level = level;
        this.location = c.getString(location);
        this.icon = icon;
        this.spawn_times = c.getResources().getStringArray(time);

        if (type == R.string.elite_boss) { this.type = BossType.ELITE; }
        if (type == R.string.field_boss) { this.type = BossType.FIELD; }
        if (type == R.string.raid_boss) { this.type = BossType.RAID; }
        if (type == R.string.unknown_boss) { this.type = BossType.UNKNOWN; }
    }

    public int updateNextSpawnTime() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Calendar c = Calendar.getInstance(tz);
        c.add(Calendar.HOUR_OF_DAY, 9);

        int c_time = c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE);
        int idx = 0;

        for (int i=0; i<=spawn_times.length; i++) {
            int p_time = Integer.parseInt(spawn_times[i].substring(0,2)) * 60 + Integer.parseInt(spawn_times[i].substring(2,4));
            int n_time = Integer.parseInt(spawn_times[(i+1)%spawn_times.length].substring(0,2)) * 60 + Integer.parseInt(spawn_times[(i+1)%spawn_times.length].substring(2,4));
            if (p_time <= c_time && c_time < n_time) {
                idx = i+1;
                break;
            }
        }
        next_spawn_time_idx = idx % spawn_times.length;
        return next_spawn_time_idx;
    }

    public String getNextSpawnTime() {
        if (type == BossType.UNKNOWN) {
            return "";
        }
        if (type == BossType.RAID) {
            return "";
        }

        updateNextSpawnTime();
        String next_time = spawn_times[next_spawn_time_idx];

        return next_time;
    }

    public int getNextSpawnIn(int n)
    {
        if (type == BossType.UNKNOWN) {
            return 0x7fffffff;
        }
        if (type == BossType.RAID) {
            return 0x7fffffff;
        }

        TimeZone tz = TimeZone.getTimeZone("UTC");
        Calendar c = Calendar.getInstance(tz);
        c.add(Calendar.HOUR_OF_DAY, 9);

        int idx = (updateNextSpawnTime() + n) % spawn_times.length;
        String next_time = spawn_times[idx];

        int ret = Integer.parseInt(next_time.substring(0,2)) * 60  + Integer.parseInt(next_time.substring(2,4))
                    - (c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE));
        return ret;
    }
}

