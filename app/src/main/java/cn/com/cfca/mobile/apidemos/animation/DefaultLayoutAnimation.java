package cn.com.cfca.mobile.apidemos.animation;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import cn.com.cfca.mobile.apidemos.R;

public class DefaultLayoutAnimation extends AppCompatActivity {

    private GridLayout gridLayout;
    private int serialNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_layout_animation);

        gridLayout = (GridLayout) findViewById(R.id.default_layout_animation_grid);
        serialNumber = 0;
    }

    public void addButtonClicked(View view) {
        Button button = new Button(this);
        button.setText(getSerialNumber());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridLayout.removeView(view);
            }
        });
        gridLayout.addView(button, Math.min(1, gridLayout.getChildCount()));
    }

    public String getSerialNumber() {
        ++serialNumber;
        return Integer.toString(serialNumber);
    }

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, DefaultLayoutAnimation.class));
    }
}
