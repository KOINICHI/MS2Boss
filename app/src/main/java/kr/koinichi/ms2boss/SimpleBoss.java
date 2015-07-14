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
    public int text_color = Color.WHITE;
    public int text_style = Typeface.BOLD;
    public int icon;

    public SimpleBoss(String name, String location, String time, int icon) {
        this.name = name;
        this.location = location;
        this.time = time;
        this.icon = icon;
    }

    public String getTimeString(int disp_type) {
        StringBuilder sb = new StringBuilder();
        Context c = BossTimer.getContext();
        if (time.length() != 0) {
            if (disp_type == BossTimer.BossAdapter.SORT_BY_BOSS) {
                sb.append(time.substring(0,2));
                sb.append(":");
                sb.append(time.substring(2,4));
            }
            if (disp_type == BossTimer.BossAdapter.SORT_BY_TIME) {
                int t = Integer.parseInt(time);
                if (t <= BossTimer.noti_before) {
                    text_color = Color.YELLOW;
                }
                if (t <= 5) {
                    text_color = Color.rgb(255, 64, 0);
                }
                sb.append(time);
                sb.append(c.getString(R.string.minutes_later));
            }
            sb.append(" ");
        }
        return sb.toString();
    }
}
