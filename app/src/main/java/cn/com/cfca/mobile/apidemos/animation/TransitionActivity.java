package cn.com.cfca.mobile.apidemos.animation;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import cn.com.cfca.mobile.apidemos.R;
import java.util.List;
import java.util.Map;

public class TransitionActivity extends AppCompatActivity {

    public static final String KEY_ID = "ViewTransitionValues:id";

    private ImageView hero;

    public static final int[] DRAWABLES = {
          R.drawable.ball,
          R.drawable.block,
          R.drawable.ducky,
          R.drawable.jellies,
          R.drawable.mug,
          R.drawable.pencil,
          R.drawable.scissors,
          R.drawable.woot,
    };

    public static final int[] IDS = {
          R.id.ball,
          R.id.block,
          R.id.ducky,
          R.id.jellies,
          R.id.mug,
          R.id.pencil,
          R.id.scissors,
          R.id.woot,
    };

    public static final String[] NAMES = {
          "ball",
          "block",
          "ducky",
          "jellies",
          "mug",
          "pencil",
          "scissors",
          "woot",
    };

    public static int getIdForKey(String id) {
        return IDS[getIndexForKey(id)];
    }

    public static int getDrawableIdForKey(String id) {
        return DRAWABLES[getIndexForKey(id)];
    }

    public static int getIndexForKey(String id) {
        for (int i = 0; i < NAMES.length; i++) {
            String name = NAMES[i];
            if (name.equals(id)) {
                return i;
            }
        }
        return 2;
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(randomColor()));
        setContentView(R.layout.activity_transition);
        setupHero();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupHero() {
        String name = getIntent().getStringExtra(KEY_ID);
        hero = null;
        if(name != null) {
            hero = (ImageView) findViewById(getIdForKey(name));
            setEnterSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    sharedElements.put("hero", hero);
                }
            });
        }
    }

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, TransitionActivity.class));

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void clicked(View view) {
        hero = (ImageView) view;
        Intent intent = new Intent(this, TransitionDetailsActivity.class);
        intent.putExtra(KEY_ID, view.getTransitionName());
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(this, hero, "hero");
        startActivity(intent, activityOptions.toBundle());
    }

    public static int randomColor() {
        int red = (int)(Math.random() * 128);
        int green = (int)(Math.random() * 128);
        int blue = (int)(Math.random() * 128);
        return 0xFF000000 | (red << 16) | (green << 8) | blue;
    }
}
