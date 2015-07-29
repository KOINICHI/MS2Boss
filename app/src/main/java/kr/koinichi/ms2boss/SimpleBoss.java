package kr.koinichi.ms2boss;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;

/**
 * Created by KOINICHI on 2015/07/10.
 */
public class SimpleBoss {
    public int id;
    public String name;
    public String location;
    public String time;
    public int next_spawn_in;
    public int text_color = Color.rgb(32, 32, 32);
    public int text_style = Typeface.BOLD;
    public int icon;

    private int notified;

    public SimpleBoss(int id, String name, String location, String time, int next_spawn_in, int icon) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.time = time;
        this.next_spawn_in = next_spawn_in;
        this.icon = icon;
    }

    public String getTimeString(int time_format) {
        StringBuilder sb = new StringBuilder();
        Context c = BossTimer.getContext();
        if (time.length() != 0) {

            if (next_spawn_in <= BossTimer.noti_before) {
                text_color = Color.rgb(255, 160, 0);
            }
            if (next_spawn_in <= 5) {
                text_color = Color.rgb(255, 64, 0);
            }
            if (time_format == BossTimer.BossAdapter.TIME_FORMAT_EXACT) {
                int hr = Integer.parseInt(time.substring(0,2));
                sb.append(hr % 12);
                sb.append(":");
                sb.append(time.substring(2,4));
                sb.append(" ");
                sb.append(c.getString(hr >= 12 ? R.string.pm : R.string.am));
            }
            if (time_format == BossTimer.BossAdapter.TIME_FORMAT_MINUTES) {
                if (next_spawn_in == 0) {
                    sb.append(c.getString(R.string.minutes_now));
                }
                else if (next_spawn_in < 0) {
                    text_color = Color.rgb(189,189,189);
                    sb.append(-next_spawn_in);
                    sb.append(c.getString(next_spawn_in == -1 ? R.string.minutes_before_singular : R.string.minutes_before));
                }
                else {
                    sb.append(next_spawn_in);
                    sb.append(c.getString(next_spawn_in == 1 ? R.string.minutes_before_singular : R.string.minutes_later));
                }
            }
            sb.append(" ");
        }
        return sb.toString();
    }
}
