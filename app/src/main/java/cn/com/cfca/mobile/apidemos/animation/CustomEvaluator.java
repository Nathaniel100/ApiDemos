package cn.com.cfca.mobile.apidemos.animation;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
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
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import cn.com.cfca.mobile.apidemos.R;

public class CustomEvaluator extends AppCompatActivity {

    private AnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_evaluator);

        LinearLayout container = (LinearLayout) findViewById(R.id.custom_evaluator_container);
        animationView = new AnimationView(this);
        container.addView(animationView);
    }

    public void playClicked(View view) {
        animationView.startAnimation();
    }

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, CustomEvaluator.class));
    }

    private static class XYHolder {
        private final float x;
        private final float y;

        private XYHolder(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
    }

    private static class CustomTypeEvaluator implements TypeEvaluator<XYHolder> {

        @Override
        public XYHolder evaluate(float fraction, XYHolder start, XYHolder end) {
            float x = start.getX() + fraction * (end.getX() - start.getX());
            float y = start.getY() + fraction * (end.getY() - start.getY());
            return new XYHolder(x, y);
        }
    }

    private static class BallXYHolder {
        private ShapeHolder ball;

        public BallXYHolder(ShapeHolder ball) {
            this.ball = ball;
        }

        public void setXY(XYHolder xyHolder) {
            ball.setX(xyHolder.getX());
            ball.setY(xyHolder.getY());
        }

        public XYHolder getXY() {
            return new XYHolder(ball.getX(), ball.getY());
        }

        public ShapeHolder getBall() {
            return ball;
        }
    }

    private static class AnimationView extends View implements ValueAnimator.AnimatorUpdateListener {

        private final BallXYHolder ballXYHolder;
        private ObjectAnimator animator;

        public AnimationView(Context context) {
            super(context);

            ballXYHolder = new BallXYHolder(addBall(25f, 25f));
            createAnimation();
        }

        public void startAnimation() {
            createAnimation();
            animator.start();
        }

        private ShapeHolder addBall(float x, float y) {
            OvalShape circle = new OvalShape();
            circle.resize(50f, 50f);
            ShapeDrawable shapeDrawable = new ShapeDrawable(circle);
            ShapeHolder shapeHolder = new ShapeHolder(shapeDrawable);
            shapeHolder.setX(x - 25f);
            shapeHolder.setY(y - 25f);
            int red = (int) (100 + 155 * Math.random());
            int green = (int) (100 + 155 * Math.random());
            int blue = (int) (100 + 155 * Math.random());
            int color = 0xFF000000 | (red << 16) | (green << 8) | blue;
            int darkColor = 0xFF000000 | (red / 4 << 16) | (green / 4 << 8) | blue / 4;
            Paint paint = shapeDrawable.getPaint();
            RadialGradient radialGradient =
                  new RadialGradient(37.5f, 12.5f, 50f, color, darkColor, Shader.TileMode.CLAMP);
            paint.setShader(radialGradient);
            shapeHolder.setPaint(paint);

            return shapeHolder;
        }

        private void createAnimation() {
            if (animator == null) {
                animator = ObjectAnimator.ofObject(ballXYHolder, "XY", new CustomTypeEvaluator(), new XYHolder(0f, 0f),
                      new XYHolder(300f, 500f));
                animator.setDuration(1500);
                animator.setInterpolator(new LinearInterpolator());
                animator.addUpdateListener(this);
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.save();
            canvas.translate(ballXYHolder.getXY().getX(), ballXYHolder.getXY().getY());
            ballXYHolder.getBall().getShape().draw(canvas);
            canvas.restore();
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            invalidate();
        }
    }
}
