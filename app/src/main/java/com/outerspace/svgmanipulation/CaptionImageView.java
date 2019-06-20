package com.outerspace.svgmanipulation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CaptionImageView extends AppCompatImageView {

    public interface CaptionDirection {
        int ABOVE = 0;
        int BELOW = 1;
        int RIGHT = 2;
        int LEFT = 3;
    }

    public static class PointF {
        public float x;
        public float y;

        public PointF(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class CaptionDefinition {
        private boolean hasPointerVisibilityBeenSet = false;
        private boolean isPointerVisible;
        private LinkedList<PointF> points = new LinkedList<>();
        private float xPointer;
        private float yPointer;
        private String text;
        private float xText;
        private float yText;
        private float textWidth;
        private boolean hasTextWidthBeenSet = false;
        private boolean hasCaptionDirectionBeenSet = false;
        private int captionDirection;

        public float getXPointer() { return xPointer; }

        public float getYPointer() { return yPointer; }

        public String getText() { return text; }

        public float getXText() { return xText; }

        public float getYText() { return yText; }

        public float getTextWidth() { return textWidth; }

        public CaptionDefinition setPointer(float xPointer, float yPointer) {
            this.xPointer = xPointer;
            this.yPointer = yPointer;
            to(xPointer, yPointer);
            if( !hasPointerVisibilityBeenSet) {
                isPointerVisible = true;
            }
            return this;
        }

        public CaptionDefinition to(float xToPoint, float yToPoint) {
            xText = xToPoint;
            yText = yToPoint;
            points.add(new PointF(xToPoint, yToPoint));
            return this;
        }

        public CaptionDefinition moveBy(float xDelta, float yDelta) {
            return to(points.getLast().x + xDelta,
                    points.getLast().y + yDelta);
        }

        public CaptionDefinition setPointerVisible(boolean visible) {
            isPointerVisible = visible;
            hasPointerVisibilityBeenSet = true;
            return this;
        }

        public CaptionDefinition setCaptionText(String text) {
            this.text = text;
            if( !hasCaptionDirectionBeenSet) {
                captionDirection = CaptionDirection.ABOVE;
            }
            if (!hasTextWidthBeenSet) {
                textWidth = 0.15F;
            }
            return this;
        }

        public CaptionDefinition setTextWidth(float textWidth) {
            this.textWidth = textWidth;
            hasTextWidthBeenSet = true;
            return this;
        }

        public CaptionDefinition setCaptionDirection(int captionDirection) {
            this.captionDirection = captionDirection;
            hasCaptionDirectionBeenSet = true;
            return this;
        }

        public int getCaptionDirection() {
            return captionDirection;
        }

        public boolean isPointerVisible() {
            return isPointerVisible;
        }

        public LinkedList<PointF> getPoints() {
            return points;
        }
    }

    private static int BORDER;

    private static ArrayList<CaptionDefinition> captions = new ArrayList<>();

    private int width, height;
    private float xFactor = 1.0F;
    private int desiredWidth, desiredHeight;
    private Paint paintBrush = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paintDimBrush = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paintMarkBrush = new Paint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint paintText = new TextPaint();

    // Constructors
    public CaptionImageView(Context context) {
        super(context);
        init();
    }

    public CaptionImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CaptionImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        paintBrush.setStyle(Paint.Style.FILL_AND_STROKE);
        paintBrush.setColor(Color.LTGRAY);
        paintBrush.setAntiAlias(true);
        paintBrush.setStrokeWidth(5);  // todo Luis: should be 5

        paintDimBrush.setStyle(Paint.Style.FILL_AND_STROKE);
        paintDimBrush.setColor(Color.LTGRAY);
        paintDimBrush.setAntiAlias(true);
        paintDimBrush.setPathEffect(new DashPathEffect(new float[] {5, 15}, 0));
        paintDimBrush.setStrokeWidth(3);

        paintMarkBrush.setStyle(Paint.Style.STROKE);
        paintMarkBrush.setColor(Color.MAGENTA);
        paintMarkBrush.setAntiAlias(true);
        paintMarkBrush.setPathEffect(new DashPathEffect(new float[] {2, 4}, 0));
        paintMarkBrush.setStrokeWidth(1);

        Typeface typeFace = ResourcesCompat.getFont(getContext(), R.font.lominoui_medium);
        paintText.setStyle(Paint.Style.FILL_AND_STROKE);
        paintText.setTypeface(typeFace);
        paintText.setAntiAlias(true);
        paintText.setColor(Color.BLACK);
        paintText.setTextSize(16 * metrics.density); // Todo Luis: should be 16 * ...

        desiredWidth = metrics.widthPixels;
        desiredHeight = metrics.heightPixels;

        Rect rect = new Rect();
        paintText.getTextBounds("O", 0, 1, rect);
        BORDER = (int) (rect.height() * 0.4F);  // captions are 40% away from the line
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        xFactor = (float) height / (float) width;
        setMeasuredDimension(width, height);
    }

    private float xTransform(float x) {
        return x * xFactor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(width / 2.0F, height / 2.0F);

        float d = -0.5F;
        for(int i = 0; i < 10; i++) {
            drawRelativeLine(canvas, paintDimBrush, -0.5F, d, 0.5F, d);
            drawRelativeLine(canvas, paintDimBrush, d, -0.5F, d, 0.5F);
            d += 0.1F;
        }

        for(CaptionDefinition caption : captions) {
            drawRelativeCaption(canvas, paintBrush, paintText, caption);
        }
        canvas.restore();
    }

    private void drawRelativeCircle(Canvas canvas, Paint paint, float xPercent, float yPercent, float radiusPercent) {
        canvas.drawCircle(xTransform(xPercent) * width, yPercent * height, radiusPercent * height, paint);
    }

    private void drawRelativeLine(Canvas canvas, Paint paint, float x1Percent, float y1Percent, float x2Percent, float y2Percent) {
        canvas.drawLine( xTransform(x1Percent) * width, y1Percent * height, xTransform(x2Percent) * width, y2Percent * height, paint);
    }

    private void drawRelativeText(Canvas canvas, TextPaint paint, int position, String text, float x1Percent, float y1Percent, float textWidthPercent) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);

        StaticLayout.Builder builder = StaticLayout.Builder
                .obtain(text, 0, text.length(), paint, (int) (height * textWidthPercent))
                .setAlignment(Layout.Alignment.ALIGN_CENTER);
        StaticLayout staticLayout = null;

        paint.getTextBounds(text, 0, text.length(), rect);
        float dx = 0, dy = 0;
        switch (position) {
            case CaptionDirection.ABOVE:
                builder.setAlignment(Layout.Alignment.ALIGN_CENTER);
                staticLayout = builder.build();
                dx = (xTransform(x1Percent) * (float) width) - staticLayout.getWidth() / 2.0F;
                dy = (y1Percent * (float) height) - staticLayout.getHeight() - BORDER;
                break;
            case CaptionDirection.BELOW:
                builder.setAlignment(Layout.Alignment.ALIGN_CENTER);
                staticLayout = builder.build();
                dx = (xTransform(x1Percent) * (float) width) - staticLayout.getWidth() / 2.0F;
                dy = (y1Percent * (float) height) + BORDER;
                break;
            case CaptionDirection.RIGHT:
                builder.setAlignment(Layout.Alignment.ALIGN_NORMAL);
                staticLayout = builder.build();
                dx = (xTransform(x1Percent) * (float) width) + BORDER;
                dy = (y1Percent * (float) height) - staticLayout.getHeight() / 2.0F;
                break;
            case CaptionDirection.LEFT:
                builder.setAlignment(Layout.Alignment.ALIGN_OPPOSITE);
                staticLayout = builder.build();
                dx = (xTransform(x1Percent) * (float) width) - staticLayout.getWidth() - BORDER;
                dy = (y1Percent * (float) height) - staticLayout.getHeight() / 2.0F;

                break;
        }
        canvas.save();
        canvas.translate(dx, dy);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    private void drawRelativeCaption(Canvas canvas, Paint paint, TextPaint textPaint, CaptionDefinition caption) {
        if(caption.isPointerVisible()) {
            drawRelativeCircle(canvas, paint, caption.getXPointer(), caption.getYPointer(), 0.01f);
        }
        List<PointF> points = caption.getPoints();
        PointF prevPoint = points.get(0);
        for(int i = 1; i < points.size(); i++) {
            PointF currentPoint = points.get(i);
            drawRelativeLine(canvas, paint, prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
            prevPoint = currentPoint;
        }
        drawRelativeText(canvas, textPaint, caption.getCaptionDirection(), caption.getText(), caption.getXText(), caption.getYText(), caption.getTextWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int drawableWidth = getDrawable().getIntrinsicWidth();
        int drawableHeight = getDrawable().getIntrinsicHeight();
        Log.d("Intrinsic: ", "Width=" + drawableWidth + " - Height=" + drawableHeight);
        int dirtyWidth = getDrawable().getDirtyBounds().width();
        int dirtyHeight = getDrawable().getDirtyBounds().height();
        Log.d("Dirty: ", "Width=" + dirtyWidth + " - Height=" + dirtyHeight);
        int boundWidth = getDrawable().getBounds().width();
        int boundHeight = getDrawable().getBounds().height();
        Log.d("Bounds: ", "Width=" + boundWidth + " - Height=" + boundHeight);
    }

    public void addCaption(CaptionDefinition caption) {
        captions.add(caption);
    }

    public void resetCaptions() {
        captions.clear();
    }
}
