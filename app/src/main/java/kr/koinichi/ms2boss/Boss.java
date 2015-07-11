package kr.koinichi.ms2boss;

import android.content.Context;
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
    public String name;
    public int level;
    public String location;
    public int icon;
    public String type;

    String[] spawn_times;


    public Boss(int name, int level, int location, int icon, int time, String type, Context c) {
        this.name = c.getString(name);
        this.level = level;
        this.location = c.getString(location);
        this.icon = icon;
        this.spawn_times = c.getResources().getStringArray(time);
        this.type = type;
        Log.d("KOINICHI", String.format("boss cstr %s", spawn_times[0]));
    }

    public String getNextSpawnTime() {

        if (type == "Unknown") {
            return "Unknown";
        }
        if (type == "Raid Boss") {
            return "Raid Boss";
        }


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

        String next_time = spawn_times[idx % spawn_times.length];

        StringBuilder sb = new StringBuilder();

        sb.append(next_time.substring(0,2));
        sb.append(":");
        sb.append(next_time.substring(2));

        return sb.toString();

    }

}
