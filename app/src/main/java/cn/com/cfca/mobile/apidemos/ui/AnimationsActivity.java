package cn.com.cfca.mobile.apidemos.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.com.cfca.mobile.apidemos.R;
import cn.com.cfca.mobile.apidemos.animation.AnimationCloning;

public class AnimationsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_list);

        ListView listView = (ListView) findViewById(R.id.simple_list);
        ArrayAdapter<CharSequence> adapter =
              ArrayAdapter.createFromResource(this, R.array.animationItems, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, AnimationsActivity.class));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 2:
                AnimationCloning.start(this);
                break;
            default:
                break;
        }
    }
}
