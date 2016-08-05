package cn.com.cfca.mobile.apidemos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.com.cfca.mobile.apidemos.animation.AnimationsActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_list);

        ListView listView = (ListView) findViewById(R.id.simple_list);
        ArrayAdapter<CharSequence> adapter =
              ArrayAdapter.createFromResource(this, R.array.demoItems, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                break;
            case 1:
                AnimationsActivity.start(this);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            default:
                break;
        }
    }
}
