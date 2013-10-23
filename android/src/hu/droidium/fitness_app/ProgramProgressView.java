package hu.droidium.fitness_app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * A simple background progress bar
 * @author Balazs Lajtha
 *
 */
public class ProgramProgressView extends View {

	private static final String TAG = "ProgramProgressView";
	private int width;
	private int height;
	private int x;
	private int y;

	private int progress = 0;
	
	private Paint actualPaint = new Paint();

	public ProgramProgressView(Context context) {
		super(context);
		initPaint(context);
	}

	public ProgramProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint(context);
	}

	public ProgramProgressView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initPaint(context);
	}

	private void initPaint(Context context) {
		actualPaint.setColor(context.getResources().getColor(R.color.doWorkoutActualColor));
	}

	public void setProgress(int progress) {
		Log.e(TAG, "Progress set " + progress);
		this.progress = progress;
		invalidate();
	}

	public void setColor(int color) {
		Log.e(TAG, "Color set " + color);
		actualPaint.setColor(color);
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.e(TAG, "Draw");
		canvas.drawRect(x, y, x + (width * progress) / 100, y + height, actualPaint);
		Log.e("ProgramProgressView", "Drawing progress " + progress + "(size: " + height + ", " + width + ")");
	}
	

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// Account for padding
		this.width = w - (getPaddingLeft() + getPaddingRight());
		this.height = h - (getPaddingTop() + getPaddingBottom());
		this.x = getPaddingLeft();
		this.y = getPaddingTop();
	}
}