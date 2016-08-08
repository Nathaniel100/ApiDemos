package cn.com.cfca.mobile.apidemos.animation;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import cn.com.cfca.mobile.apidemos.R;

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
            case 0:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    TransitionActivity.start(this);
                } else {
                    Toast.makeText(this, "Transition not support", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                AnimationBouncingBalls.start(this);
                break;
            case 2:
                AnimationCloning.start(this);
                break;
            case 3:
                CustomEvaluator.start(this);
                break;
            case 4:
                DefaultLayoutAnimation.start(this);
                break;
            default:
                break;
        }
    }
}
