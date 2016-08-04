package cn.com.cfca.mobile.apidemos.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.com.cfca.mobile.apidemos.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.main_list);
        ArrayAdapter<CharSequence> adapter =
              ArrayAdapter.createFromResource(this, R.array.demoNames, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
    }
}
