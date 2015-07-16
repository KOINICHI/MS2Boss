package kr.koinichi.ms2boss;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;

/**
 * Created by Sachika on 2015/07/10.
 */
public class SimpleBoss {
    public String name;
    public String location;
    public String time;
    public int next_spawn_in;
    public int text_color = Color.WHITE;
    public int text_style = Typeface.BOLD;
    public int icon;

    public SimpleBoss(String name, String location, String time, int next_spawn_in, int icon) {
        this.name = name;
        this.location = location;
        this.time = time;
        this.next_spawn_in = next_spawn_in;
        this.icon = icon;
    }

    public String getTimeString(int disp_type) {
        StringBuilder sb = new StringBuilder();
        Context c = BossTimer.getContext();
        if (time.length() != 0) {

            if (next_spawn_in <= BossTimer.noti_before) {
                text_color = Color.YELLOW;
            }
            if (next_spawn_in <= 5) {
                text_color = Color.rgb(255, 64, 0);
            }
            if (disp_type == BossTimer.BossAdapter.SORT_BY_BOSS) {
                int hr = Integer.parseInt(time.substring(0,2));
                sb.append(c.getString(hr >= 12 ? R.string.pm : R.string.am));
                sb.append(" ");
                sb.append(hr % 12);
                sb.append(":");
                sb.append(time.substring(2,4));
            }
            if (disp_type == BossTimer.BossAdapter.SORT_BY_TIME) {
                if (next_spawn_in == 0) {
                    sb.append(c.getString(R.string.minutes_now));
                }
                else if (next_spawn_in < 0) {
                    text_color = Color.rgb(189,189,189);
                    sb.append(-next_spawn_in);
                    sb.append(c.getString(R.string.minutes_before));
                }
                else {
                    sb.append(next_spawn_in);
                    sb.append(c.getString(R.string.minutes_later));
                }
            }
            sb.append(" ");
        }
        return sb.toString();
    }
}
