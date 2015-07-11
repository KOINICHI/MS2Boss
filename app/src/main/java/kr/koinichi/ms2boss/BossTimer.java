package kr.koinichi.ms2boss;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class BossTimer extends AppCompatActivity {

    ArrayList<Boss> bosses = null;

    private void initializeBossData() {
        Context c = this.getApplicationContext();

        bosses = new ArrayList<Boss>();
        bosses.add(new Boss(R.string.baraha, 23, R.string.baraha_loc, R.drawable.baraha, R.array.baraha, "Elite", c));
        bosses.add(new Boss(R.string.boogieccoli_maple, 23, R.string.boogieccoli_maple_loc, R.drawable.boogieccoli_maple, R.array.boogieccoli_maple, "Elite", c));
        bosses.add(new Boss(R.string.boogieccoli_victoria, 23, R.string.boogieccoli_victoria_loc, R.drawable.boogieccoli_victoria, R.array.boogieccoli_victoria, "Elite", c));
        bosses.add(new Boss(R.string.chau_the_guard, 23, R.string.chau_the_guard_loc, R.drawable.chau_the_guard, R.array.chau_the_guard, "Elite", c));
        bosses.add(new Boss(R.string.devlin_chief, 23, R.string.devlin_chief_loc, R.drawable.devlin_chief, R.array.devlin_chief, "Unknown", c));
        bosses.add(new Boss(R.string.devlin_warrior, 23, R.string.devlin_warrior_loc, R.drawable.devlin_warrior, R.array.devlin_warrior, "Field Boss", c));
        bosses.add(new Boss(R.string.devorak, 23, R.string.devorak_loc, R.drawable.devorak, R.array.devorak, "Raid Boss", c));
        bosses.add(new Boss(R.string.dun_dun, 23, R.string.dun_dun_loc, R.drawable.dun_dun, R.array.dun_dun, "Field Boss", c));
        bosses.add(new Boss(R.string.epie, 23, R.string.epie_loc, R.drawable.epie, R.array.epie, "Elite", c));
        bosses.add(new Boss(R.string.frankenette, 23, R.string.frankenette_loc, R.drawable.frankenette, R.array.frankenette, "Elite", c));
        bosses.add(new Boss(R.string.giant_lavaeye, 23, R.string.giant_lavaeye_loc, R.drawable.giant_lavaeye, R.array.giant_lavaeye, "Elite", c));
        bosses.add(new Boss(R.string.giant_turtle, 23, R.string.giant_turtle_loc, R.drawable.giant_turtle, R.array.giant_turtle, "Field Boss", c));
        bosses.add(new Boss(R.string.griffina, 23, R.string.griffina_loc, R.drawable.griffina, R.array.griffina, "Field Boss", c));
        bosses.add(new Boss(R.string.griffon, 23, R.string.griffon_loc, R.drawable.griffon, R.array.griffon, "Field Boss", c));
        bosses.add(new Boss(R.string.hemokan, 23, R.string.hemokan_loc, R.drawable.hemokan, R.array.hemokan, "Raid Boss", c));
        bosses.add(new Boss(R.string.last_bajar, 23, R.string.last_bajar_loc, R.drawable.last_bajar, R.array.last_bajar, "Raid Boss", c));
        bosses.add(new Boss(R.string.madonette, 23, R.string.madonette_loc, R.drawable.madonette, R.array.madonette, "Elite", c));
        bosses.add(new Boss(R.string.mark_alpha, 23, R.string.mark_alpha_loc, R.drawable.mark_alpha, R.array.mark_alpha, "Field Boss", c));
        bosses.add(new Boss(R.string.mark_beta, 23, R.string.mark_beta_loc, R.drawable.mark_beta, R.array.mark_beta, "Raid Boss", c));
        bosses.add(new Boss(R.string.mushmom, 23, R.string.mushmom_loc, R.drawable.mushmom, R.array.mushmom, "Elite", c));
        bosses.add(new Boss(R.string.nixie, 23, R.string.nixie_loc, R.drawable.nixie, R.array.nixie, "Elite", c));
        bosses.add(new Boss(R.string.revenant_zombie, 23, R.string.revenant_zombie_loc, R.drawable.revenant_zombie, R.array.revenant_zombie, "Elite", c));
        bosses.add(new Boss(R.string.urza, 23, R.string.urza_loc, R.drawable.urza, R.array.urza, "Elite", c));
        bosses.add(new Boss(R.string.zirant, 23, R.string.zirant_loc, R.drawable.zirant, R.array.zirant, "Elite", c));


    }

    public class BossAdapter extends BaseAdapter {
        ArrayList<Boss> boss_list = bosses;

        @Override
        public int getCount() {
            Log.d("KOINICHI", String.format("count %d", boss_list.size()));
            return boss_list.size();
        }

        @Override
        public Boss getItem(int idx) {
            Log.d("KOINICHI", "name " + boss_list.get(idx).name);
            return boss_list.get(idx);
        }

        @Override
        public long getItemId(int id) {
            Log.d("KOINICHI", String.format("id %d", id));
            return id;
        }

        @Override
        public View getView(int id, View view, ViewGroup viewgroup) {
            Log.d("KOINICHI", String.format("view %d", id));
            if (view == null) {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listitem, viewgroup, false);
            }
            Log.d("KOINICHI", String.format("view 1111"));

            ImageView boss_icon = (ImageView) view.findViewById(R.id.boss_icon);
            TextView boss_name = (TextView) view.findViewById(R.id.boss_name);
            TextView boss_time = (TextView) view.findViewById(R.id.boss_time);
            Log.d("KOINICHI", String.format("view 2222"));

            Boss boss = bosses.get(id);
            Log.d("KOINICHI", String.format("view 3333 " + boss.name));

            boss_name.setText(boss.name);
            StringBuilder sb = new StringBuilder();
            sb.append(boss.getNextSpawnTime());
            sb.append(" at ");
            sb.append(boss.location);
            boss_time.setText(sb.toString());
            boss_icon.setImageResource(boss.icon);

            Log.d("KOINICHI", boss.name);

            return view;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeBossData();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_timer);

        BossAdapter bossAdapter = new BossAdapter();

        ListView listView = (ListView) findViewById(R.id.boss_list);
        listView.setAdapter(bossAdapter);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
