package pl.animatedheartbutton;


import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class AnimatedHeartButton extends View {

    // This is a boundary px value for default button Bezier curves
    private final int DEFAULT_PX_DIM = 31;

    private final int UNCHECKED_HEART_COLOR = Color.argb(255, 210, 236, 255);
    private final int CHECKED_HEART_COLOR = Color.argb(255, 106, 193, 255);

    private Path heartPath;
    private Paint heartPaint;

    private Path strokePath;
    private Paint strokePaint;

    private Path tickPath;
    private Paint tickPaint;
    private RectF tickRectF;

    private Path ovalPath;
    private Paint ovalPaint;
    private RectF ovalRectF;

    private Matrix scaleMatrix;
    private float density;

    private boolean checked;
    private boolean animate;
    private ValueAnimator tickAnimator;
    private ValueAnimator colorAnimator;
    private float tickAnimationPercentage = 1;
    private Matrix tickAnimationMatrix;

    private OnHeartButtonCheckedChangeListener onHeartButtonCheckedChangeListener;

    public AnimatedHeartButton(Context context) {
        super(context);
        init(context);
    }

    public AnimatedHeartButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        scaleMatrix = new Matrix();
        density = context.getResources().getDisplayMetrics().density;

        heartPath = new Path();
        heartPaint = new Paint();
        heartPaint.setColor(UNCHECKED_HEART_COLOR);

        strokePath = new Path();
        strokePaint = new Paint();

        tickPath = new Path();
        tickPaint = new Paint();
        tickRectF = new RectF();

        ovalPath = new Path();
        ovalRectF = new RectF();
        ovalPaint = new Paint();

        tickAnimationMatrix = new Matrix();
        tickAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        tickAnimator.setDuration(250);
        colorAnimator = new ValueAnimator();
        colorAnimator.setDuration(70);

        setOnClickListener(v -> {
            if (isChecked()) {
                setChecked(false, true);
            } else {
                setChecked(true, true);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        scaleMatrix.setScale(1f * canvas.getWidth() / DEFAULT_PX_DIM, 1f * canvas.getHeight() / DEFAULT_PX_DIM);

        heartPath.reset();
        heartPath.moveTo(14.89f, 5.63f);
        heartPath.cubicTo(14.89f, 5.63f, 20.04f, -1.56f, 25.79f, 2.19f);
        heartPath.cubicTo(31.22f, 6.13f, 28.68f, 12.06f, 26.07f, 15.14f);
        heartPath.cubicTo(23.46f, 18.22f, 15.71f, 25.08f, 14.89f, 25f);
        heartPath.cubicTo(14.06f, 24.91f, 8.54f, 20.71f, 4.13f, 15.75f);
        heartPath.cubicTo(-0.28f, 10.8f, -0.2f, 3.74f, 5.44f, 1.48f);
        heartPath.cubicTo(11.08f, -0.78f, 14.89f, 5.63f, 14.89f, 5.63f);
        heartPath.close();
        heartPath.setFillType(Path.FillType.EVEN_ODD);
        heartPath.transform(scaleMatrix);
        heartPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        heartPaint.setStyle(Paint.Style.FILL);

        strokePath.reset();
        strokePath.moveTo(14.92f, 4.78f);
        strokePath.cubicTo(14.88f, 4.72f, 14.83f, 4.67f, 14.79f, 4.61f);
        strokePath.cubicTo(14.37f, 4.04f, 13.88f, 3.48f, 13.32f, 2.95f);
        strokePath.cubicTo(10.98f, 0.74f, 8.24f, -0.18f, 5.26f, 1.02f);
        strokePath.cubicTo(-0.56f, 3.35f, -1.04f, 10.7f, 3.76f, 16.09f);
        strokePath.cubicTo(7.67f, 20.47f, 13.58f, 25.37f, 14.83f, 25.5f);
        strokePath.cubicTo(15.91f, 25.61f, 23.66f, 18.76f, 26.45f, 15.46f);
        strokePath.cubicTo(30.29f, 10.93f, 30.96f, 5.32f, 26.08f, 1.78f);
        strokePath.cubicTo(22.89f, -0.29f, 19.67f, 0.49f, 16.71f, 2.99f);
        strokePath.cubicTo(16.1f, 3.5f, 15.56f, 4.04f, 15.08f, 4.59f);
        strokePath.cubicTo(15.02f, 4.66f, 14.97f, 4.72f, 14.92f, 4.78f);
        strokePath.close();
        strokePath.transform(scaleMatrix);
        strokePaint.setColor(Color.argb(255, 0, 53, 123));
        strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(0.9f * density);

        ovalRectF.set(17.04f, 10.66f, 30.96f, 25.17f);
        ovalPath.reset();
        ovalPath.addOval(ovalRectF, Path.Direction.CW);
        ovalPath.transform(scaleMatrix);
        ovalPaint.setColor(Color.argb(255, 231, 240, 247));
        ovalPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        ovalPaint.setStyle(Paint.Style.FILL);

        drawChecked(checked);
        tickPath.transform(scaleMatrix);
        if (animate) {
            tickPath.computeBounds(tickRectF, false);
            tickAnimationMatrix.setScale(tickAnimationPercentage, tickAnimationPercentage,
                    tickRectF.centerX(), tickRectF.centerY());
            tickPath.transform(tickAnimationMatrix);
        }
        tickPaint.setColor(Color.argb(255, 0, 53, 123));
        tickPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        tickPaint.setStrokeCap(Paint.Cap.ROUND);

        canvas.drawPath(heartPath, heartPaint);
        canvas.drawPath(strokePath, strokePaint);
        canvas.drawPath(ovalPath, ovalPaint);
        canvas.drawPath(tickPath, tickPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultWidth = DEFAULT_PX_DIM;
        int defaultHeight = DEFAULT_PX_DIM;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(defaultWidth, widthSize);
        } else {
            width = defaultWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(defaultHeight, heightSize);
        } else {
            height = defaultHeight;
        }

        setMeasuredDimension(width, height);
    }

    private void drawChecked(boolean checked) {
        if (checked) {
            tickPaint.setStyle(Paint.Style.STROKE);
            tickPaint.setStrokeWidth(3f * density);
            tickPath.reset();
            tickPath.moveTo(20.92f, 18.13f);
            tickPath.lineTo(23.45f, 20.57f);
            tickPath.lineTo(28.25f, 15.94f);
        } else {
            tickPaint.setStyle(Paint.Style.FILL);
            tickPaint.setStrokeWidth(1f * density);
            tickPath.reset();
            tickPath.moveTo(25.46f, 17f);
            tickPath.lineTo(25.46f, 14f);
            tickPath.cubicTo(25.46f, 13.46f, 25.01f, 13f, 24.46f, 13f);
            tickPath.cubicTo(23.91f, 13f, 23.46f, 13.45f, 23.46f, 14f);
            tickPath.lineTo(23.46f, 17f);
            tickPath.lineTo(20.46f, 17f);
            tickPath.cubicTo(19.92f, 17f, 19.46f, 17.45f, 19.46f, 18f);
            tickPath.cubicTo(19.46f, 18.56f, 19.91f, 19f, 20.46f, 19f);
            tickPath.lineTo(23.46f, 19f);
            tickPath.lineTo(23.46f, 22f);
            tickPath.cubicTo(23.46f, 22.54f, 23.91f, 23f, 24.46f, 23f);
            tickPath.cubicTo(25.02f, 23f, 25.46f, 22.55f, 25.46f, 22f);
            tickPath.lineTo(25.46f, 19f);
            tickPath.lineTo(28.47f, 19f);
            tickPath.cubicTo(29.01f, 19f, 29.46f, 18.55f, 29.46f, 18f);
            tickPath.cubicTo(29.46f, 17.44f, 29.02f, 17f, 28.47f, 17f);
            tickPath.lineTo(25.46f, 17f);
            tickPath.close();
        }
    }

    private void setHeartColor(boolean checked) {
        if (checked) {
            heartPaint.setColor(CHECKED_HEART_COLOR);
        } else {
            heartPaint.setColor(UNCHECKED_HEART_COLOR);
        }
        invalidate();
    }

    private void setColorIntValues(boolean checked) {
        if (checked) {
            colorAnimator.setIntValues(UNCHECKED_HEART_COLOR, CHECKED_HEART_COLOR);
            colorAnimator.setEvaluator(new ArgbEvaluator());
        } else {
            colorAnimator.setIntValues(CHECKED_HEART_COLOR, UNCHECKED_HEART_COLOR);
            colorAnimator.setEvaluator(new ArgbEvaluator());
        }
    }

    public void setChecked(final boolean checked, final boolean animate) {
        if (checked != this.checked && onHeartButtonCheckedChangeListener != null) {
            onHeartButtonCheckedChangeListener.onCheckedChanged(checked);
        }
        this.checked = checked;
        this.animate = animate;
        if (animate) {
            setEnabled(false);
            setClickable(false);
            tickAnimator.addUpdateListener(animation -> {
                tickAnimationPercentage = (float) animation.getAnimatedValue();
                invalidate();
                if (tickAnimationPercentage == 1) {
                    setColorIntValues(checked);
                    colorAnimator.start();
                }
            });

            colorAnimator.addUpdateListener(animation -> {
                heartPaint.setColor((int) animation.getAnimatedValue());
                invalidate();
            });

            colorAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    setEnabled(true);
                    setClickable(true);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    // onAnimationStart()
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    // onAnimationCancel()
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    // onAnimationRepeat()
                }
            });

            tickAnimator.start();
        } else {
            setHeartColor(checked);
        }
        invalidate();
    }

    private boolean isChecked() {
        return checked;
    }

    public void setOnHeartButtonCheckedChangeListener(
            OnHeartButtonCheckedChangeListener onHeartButtonCheckedChangeListener) {
        this.onHeartButtonCheckedChangeListener = onHeartButtonCheckedChangeListener;
    }
}