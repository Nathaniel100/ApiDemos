package cn.com.cfca.mobile.apidemos.animation;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import cn.com.cfca.mobile.apidemos.R;

import static cn.com.cfca.mobile.apidemos.animation.TransitionActivity.KEY_ID;

public class TransitionDetailsActivity extends AppCompatActivity {


    private int imageResourceId = R.drawable.ducky;
    private String name = "ducky";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(TransitionActivity.randomColor()));
        setContentView(R.layout.activity_transition_details);
        ImageView titleImage = (ImageView) findViewById(R.id.titleImage);
        titleImage.setImageDrawable(getHeroDrawable());
    }

    private Drawable getHeroDrawable() {
        String name = getIntent().getStringExtra(KEY_ID);
        if(name != null) {
            this.name = name;
            this.imageResourceId = TransitionActivity.getDrawableIdForKey(name);
        }

        return getResources().getDrawable(imageResourceId);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void clicked(View view) {
        Intent intent = new Intent(this, TransitionActivity.class);
        intent.putExtra(KEY_ID, name);
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(this, view, "hero");
        startActivity(intent, activityOptions.toBundle());
    }
}
