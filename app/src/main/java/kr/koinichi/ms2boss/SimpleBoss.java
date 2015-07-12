package kr.koinichi.ms2boss;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by Sachika on 2015/07/10.
 */
public class SimpleBoss {
    public String name;
    public String location;
    public String time;
    public int icon;

    public SimpleBoss(String name, String location, String time, int icon) {
        this.name = name;
        this.location = location;
        this.time = time;
        this.icon = icon;
    }

    public String getTimeLocString(int disp_type) {
        StringBuilder sb = new StringBuilder();
        Context c = BossTimer.getContext();
        if (time.length() != 0) {
            if (disp_type == BossTimer.BossAdapter.SORT_BY_BOSS) {
                sb.append(time.substring(0,2));
                sb.append(":");
                sb.append(time.substring(2,4));
            }
            if (disp_type == BossTimer.BossAdapter.SORT_BY_TIME) {
                sb.append(time);
                sb.append(c.getString(R.string.minutes_later));
            }
            sb.append(" ");
        }
        sb.append(location);
        return sb.toString();
    }
}
