package cn.com.cfca.mobile.apidemos.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import cn.com.cfca.mobile.apidemos.R;
import java.util.HashSet;
import java.util.Set;

public class AnimationBouncingBalls extends AppCompatActivity {

    private static final String TAG = AnimationBouncingBalls.class.getSimpleName();

    AnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_bouncing_balls);

        FrameLayout container = (FrameLayout) findViewById(R.id.activity_animation_bouncing_balls);

        animationView = new AnimationView(this);
        container.addView(animationView,
              new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, AnimationBouncingBalls.class));
    }

    private static class AnimationView extends View {

        private static final int RED = 0xffFF8080;
        private static final int BLUE = 0xff8080FF;
        private static final int CYAN = 0xff80ffff;
        private static final int GREEN = 0xff80ff80;

        private Set<ShapeHolder> balls = new HashSet<>();
        private ValueAnimator backgroundAnimator;
        private AnimatorSet animatorSet;

        public AnimationView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            for (ShapeHolder shapeHolder : balls) {
                canvas.save();
                canvas.translate(shapeHolder.getX(), shapeHolder.getY());
                shapeHolder.getShape().draw(canvas);
                canvas.restore();
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            Log.i(TAG, "MotionEvent: " + event.toString());
            int action = event.getAction();
            if (action != MotionEvent.ACTION_DOWN && action != MotionEvent.ACTION_MOVE) {
                return false;
            }

            addBall(event.getX(), event.getY());
            return true;
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();

            // Animate background color
            // Note that setting the background color will automatically invalidate the
            // view, so that the animated color, and the bouncing balls, get redisplayed on
            // every frame of the animation.
            backgroundAnimator = ObjectAnimator.ofInt(this, "backgroundColor", RED, BLUE);
            backgroundAnimator.setDuration(3000);
            backgroundAnimator.setRepeatCount(ValueAnimator.INFINITE);
            backgroundAnimator.setRepeatMode(ValueAnimator.REVERSE);
            backgroundAnimator.setEvaluator(new ArgbEvaluator());
            backgroundAnimator.start();
        }

        @Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (backgroundAnimator != null && backgroundAnimator.isRunning()) {
                backgroundAnimator.cancel();
                backgroundAnimator = null;
            }
        }

        private void addBall(float x, float y) {
            OvalShape circle = new OvalShape();
            circle.resize(50f, 50f);
            ShapeDrawable shapeDrawable = new ShapeDrawable(circle);
            ShapeHolder shapeHolder = new ShapeHolder(shapeDrawable);
            shapeHolder.setX(x - 25);
            shapeHolder.setY(y - 25);
            int red = (int) (100 + Math.random() * 155);
            int green = (int) (100 + Math.random() * 155);
            int blue = (int) (100 + Math.random() * 155);
            int color = 0xFF000000 | (red << 16) | (green << 8) | blue;
            int darkColor = 0xFF000000 | (red / 4 << 16) | (green / 4 << 8) | blue / 4;
            RadialGradient radialGradient =
                  new RadialGradient(37.5f, 12.5f, 50f, color, darkColor, Shader.TileMode.CLAMP);
            Paint paint = shapeDrawable.getPaint();
            paint.setShader(radialGradient);
            balls.add(shapeHolder);

            setAnimation(shapeHolder, y);
        }

        private void setAnimation(ShapeHolder shapeHolder, float eventY) {

            float startY = shapeHolder.getY();
            float endY = getHeight() - shapeHolder.getHeight();
            float h = (float) getHeight();
            int duration = (int) (500 * (h - eventY) / h);

            ObjectAnimator bounceAnim = ObjectAnimator.ofFloat(shapeHolder, "y", startY, endY);
            bounceAnim.setDuration(duration);
            bounceAnim.setInterpolator(new AccelerateInterpolator());

            ObjectAnimator squashAnim1 =
                  ObjectAnimator.ofFloat(shapeHolder, "x", shapeHolder.getX(), shapeHolder.getX() - 25f);
            squashAnim1.setDuration(duration / 4);
            squashAnim1.setRepeatCount(1);
            squashAnim1.setRepeatMode(ValueAnimator.REVERSE);
            squashAnim1.setInterpolator(new DecelerateInterpolator());

            ObjectAnimator squashAnim2 =
                  ObjectAnimator.ofFloat(shapeHolder, "width", shapeHolder.getWidth(), shapeHolder.getWidth() + 50);
            squashAnim2.setDuration(duration / 4);
            squashAnim2.setRepeatCount(1);
            squashAnim2.setRepeatMode(ValueAnimator.REVERSE);
            squashAnim2.setInterpolator(new DecelerateInterpolator());

            ObjectAnimator stretchAnim1 = ObjectAnimator.ofFloat(shapeHolder, "y", endY, endY + 25f);
            stretchAnim1.setDuration(duration / 4);
            stretchAnim1.setRepeatCount(1);
            stretchAnim1.setRepeatMode(ValueAnimator.REVERSE);
            stretchAnim1.setInterpolator(new DecelerateInterpolator());

            ObjectAnimator stretchAnim2 =
                  ObjectAnimator.ofFloat(shapeHolder, "height", shapeHolder.getHeight(), shapeHolder.getHeight() - 25);
            stretchAnim2.setDuration(duration / 4);
            stretchAnim2.setRepeatCount(1);
            stretchAnim2.setRepeatMode(ValueAnimator.REVERSE);
            stretchAnim2.setInterpolator(new DecelerateInterpolator());

            ObjectAnimator backAnim = ObjectAnimator.ofFloat(shapeHolder, "y", endY, startY);
            backAnim.setDuration(duration);
            backAnim.setInterpolator(new DecelerateInterpolator());

            AnimatorSet bouncer = new AnimatorSet();
            bouncer.play(bounceAnim).before(squashAnim1);
            bouncer.play(squashAnim1).with(squashAnim2);
            bouncer.play(squashAnim1).with(stretchAnim1);
            bouncer.play(squashAnim1).with(stretchAnim2);
            bouncer.play(backAnim).after(stretchAnim2);

            ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(shapeHolder, "alpha", 1f, 0f);
            fadeAnim.setDuration(250);
            fadeAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    balls.remove(((ObjectAnimator) animator).getTarget());
                }
            });

            animatorSet = new AnimatorSet();
            animatorSet.play(bouncer).before(fadeAnim);

            animatorSet.start();
        }

    }
}
