package cn.com.cfca.mobile.apidemos.animation;

import android.animation.Animator;
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
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import cn.com.cfca.mobile.apidemos.R;

public class AnimationEvents extends AppCompatActivity {

    TextView sequencerStart, sequencerRepeat, sequencerCancel, sequencerEnd;
    TextView animatorStart, animatorRepeat, animatorCancel, animatorEnd;
    Switch endImmediatelySwitch;
    AnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_events);

        sequencerStart = (TextView) findViewById(R.id.animation_sequencer_start);
        sequencerRepeat = (TextView) findViewById(R.id.animation_sequencer_repeat);
        sequencerCancel = (TextView) findViewById(R.id.animation_sequencer_cancel);
        sequencerEnd = (TextView) findViewById(R.id.animation_sequencer_end);

        animatorStart = (TextView) findViewById(R.id.animation_animator_start);
        animatorRepeat = (TextView) findViewById(R.id.animation_animator_repeat);
        animatorCancel = (TextView) findViewById(R.id.animation_animator_cancel);
        animatorEnd = (TextView) findViewById(R.id.animation_animator_end);
        endImmediatelySwitch = (Switch) findViewById(R.id.end_immediately_switch);

        LinearLayout container = (LinearLayout) findViewById(R.id.animation_events_container);
        animationView = new AnimationView(this);
        container.addView(animationView);

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
                animationView.play();
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationView.cancel();
            }
        });

        findViewById(R.id.end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationView.end();
            }
        });

        endImmediatelySwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                animationView.setEndImmediately(b);
            }
        });
    }

    private void reset() {
        setPassed(sequencerStart, false);
        setPassed(animatorStart, false);
        setPassed(sequencerEnd, false);
        setPassed(animatorEnd, false);
        setPassed(sequencerCancel, false);
        setPassed(animatorCancel, false);
        setPassed(sequencerRepeat, false);
        setPassed(animatorRepeat, false);
    }

    private void setPassed(TextView textView, boolean passed) {
        int textAppearance = passed ? R.style.AppTextAppearance_Passed : R.style.AppTextAppearance_NotPassed;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(textAppearance);
        } else {
            textView.setTextAppearance(this, textAppearance);
        }
    }

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, AnimationEvents.class));
    }

    private class AnimationView extends View
          implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
        private ShapeHolder ball;
        private Animator animation;
        private boolean endImmediately;

        public AnimationView(Context context) {
            super(context);

            ball = createBall(25f, 25f);
        }

        public void play() {
            createAnimation();
            animation.start();
        }

        public void cancel() {
            createAnimation();
            animation.cancel();
        }

        public void end() {
            createAnimation();
            animation.end();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.save();
            canvas.translate(ball.getX(), ball.getY());
            ball.getShape().draw(canvas);
            canvas.restore();
        }

        private ShapeHolder createBall(float x, float y) {
            OvalShape ovalShape = new OvalShape();
            ovalShape.resize(50f, 50f);
            ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
            ShapeHolder ball = new ShapeHolder(shapeDrawable);
            ball.setX(x - 25f);
            ball.setY(y - 25f);
            int red = (int) (100 + 155 * Math.random());
            int green = (int) (100 + 155 * Math.random());
            int blue = (int) (100 + 155 * Math.random());
            int color = 0xFF000000 | (red << 16) | (green << 8) | blue;
            int darkColor = 0xFF000000 | (red / 4 << 16) | (green / 4 << 8) | blue / 4;
            RadialGradient gradient = new RadialGradient(37.5f, 12.5f, 50, color, darkColor, Shader.TileMode.CLAMP);
            Paint paint = shapeDrawable.getPaint();
            paint.setShader(gradient);
            ball.setPaint(paint);
            return ball;
        }

        private void createAnimation() {
            if (animation == null) {
                ObjectAnimator animatorY =
                      ObjectAnimator.ofFloat(ball, "y", ball.getY(), getHeight() - ball.getHeight()).setDuration(1500);
                animatorY.setRepeatCount(0);
                animatorY.setRepeatMode(ValueAnimator.REVERSE);
                animatorY.setInterpolator(new AccelerateInterpolator());
                animatorY.addListener(this);
                animatorY.addUpdateListener(this);

                ObjectAnimator animatorX =
                      ObjectAnimator.ofFloat(ball, "x", ball.getX(), ball.getX() + 300).setDuration(1000);
                animatorX.setRepeatCount(0);
                animatorX.setRepeatMode(ValueAnimator.REVERSE);
                animatorX.setInterpolator(new AccelerateInterpolator());
                animatorX.addUpdateListener(this);

                animation = new AnimatorSet();
                ((AnimatorSet) animation).playTogether(animatorX, animatorY);
                animation.addListener(this);
            }
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            invalidate();
        }

        @Override
        public void onAnimationStart(Animator animator) {
            if(animator instanceof AnimatorSet) {
                setPassed(sequencerStart, true);
            } else {
                setPassed(animatorStart, true);
            }
            if (endImmediately) {
                animation.end();
            }
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            if(animator instanceof AnimatorSet) {
                setPassed(sequencerEnd, true);
            } else {
                setPassed(animatorEnd, true);
            }
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            if(animator instanceof AnimatorSet) {
                setPassed(sequencerCancel, true);
            } else {
                setPassed(animatorCancel, true);
            }
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
            if(animator instanceof AnimatorSet) {
                setPassed(sequencerRepeat, true);
            } else {
                setPassed(animatorRepeat, true);
            }
        }

        public void setEndImmediately(boolean endImmediately) {
            this.endImmediately = endImmediately;
        }
    }
}
