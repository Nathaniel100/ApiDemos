package cn.com.cfca.mobile.apidemos.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import cn.com.cfca.mobile.apidemos.R;
import java.util.ArrayList;
import java.util.List;

public class AnimationCloning extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_cloning);

        LinearLayout container = (LinearLayout) findViewById(R.id.animation_cloning_container);
        final MyAnimationView animationView = new MyAnimationView(this);
        container.addView(animationView);

        findViewById(R.id.animation_cloning_start_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationView.startAnimation();
            }
        });
    }

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, AnimationCloning.class));
    }

    public static class MyAnimationView extends View implements ValueAnimator.AnimatorUpdateListener {

        private final List<ShapeHolder> balls = new ArrayList<>();
        private AnimatorSet animatorSet;
        private float density;

        public MyAnimationView(Context context) {
            super(context);

            density = getContext().getResources().getDisplayMetrics().density;

            ShapeHolder ball0 = addBall(50f, 25f);
            ShapeHolder ball1 = addBall(150f, 25f);
            ShapeHolder ball2 = addBall(250f, 25f);
            ShapeHolder ball3 = addBall(350f, 25f);
        }

        private ShapeHolder addBall(float x, float y) {
            OvalShape circle = new OvalShape();
            circle.resize(50f * density, 50f * density);
            ShapeDrawable shapeDrawable = new ShapeDrawable(circle);
            ShapeHolder shapeHolder = new ShapeHolder(shapeDrawable);
            shapeHolder.setX(x - 25f);
            shapeHolder.setY(y - 25f);
            int red = (int)(100 + Math.random() * 155);
            int green = (int)(100 + Math.random() * 155);
            int blue = (int)(100 + Math.random() * 155);
            int color = 0xFF000000 | (red << 16) | (green << 8) | blue;
            Paint paint = shapeDrawable.getPaint();
            int darkColor = 0xFF000000 | (red/4 << 16) | (green/4 << 8) | blue/4;
            RadialGradient radialGradient = new RadialGradient(37.5f, 12.5f, 50f, color, darkColor, Shader.TileMode.CLAMP);
            paint.setShader(radialGradient);
            shapeHolder.setPaint(paint);
            balls.add(shapeHolder);
            return shapeHolder;
        }

        public void startAnimation() {
            createAnimation();
            animatorSet.start();
        }

        private void createAnimation() {
            if (animatorSet == null) {
                ObjectAnimator animator1 =
                      ObjectAnimator.ofFloat(balls.get(0), "Y", 0, getHeight() - balls.get(0).getHeight()).setDuration(500);
                animator1.addUpdateListener(this);
                ObjectAnimator animator2 = animator1.clone();
                animator2.setTarget(balls.get(1));
                AnimatorSet animatorSet1 = new AnimatorSet();
                animatorSet1.play(animator1).with(animator2);

                ObjectAnimator animator3Down =
                      ObjectAnimator.ofFloat(balls.get(2), "Y", 0, getHeight() - balls.get(3).getHeight()).setDuration(500);
                animator3Down.setInterpolator(new AccelerateInterpolator());
                animator3Down.addUpdateListener(this);
                ObjectAnimator animator3Up =
                      ObjectAnimator.ofFloat(balls.get(2), "Y", getHeight() - balls.get(3).getHeight(), 0).setDuration(500);
                animator3Up.setInterpolator(new DecelerateInterpolator());
                animator3Up.addUpdateListener(this);
                AnimatorSet animator3 = new AnimatorSet();
                animator3.playSequentially(animator3Down, animator3Up);

                AnimatorSet animator4 = animator3.clone();
                animator4.setTarget(balls.get(3));

                animatorSet = new AnimatorSet();
                animatorSet.playTogether(animatorSet1, animator3);
                animatorSet.playSequentially(animator3, animator4);
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            for (ShapeHolder ball : balls) {
                canvas.save();
                canvas.translate(ball.getX(), ball.getY());
                ball.getShape().draw(canvas);
                canvas.restore();
            }
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            invalidate();
        }
    }
}
