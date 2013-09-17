package hu.droidium.fitness_app;

import hu.droidium.fitness_app.database.Block;
import hu.droidium.fitness_app.database.Exercise;
import hu.droidium.fitness_app.database.Workout;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class WorkoutProgressView extends View {

	private static final float BLOCK_BREAK_WIDTH = 1f;
	private static final float EXERCISE_BREAK_WIDTH = 0.2f;
	private Workout workout;
	private int width;
	private int height;
	private int x;
	private int y;

	//private Paint bgPaint = new Paint();
	private Paint actualPaint = new Paint();
	private Paint nextPaint = new Paint();
	private Paint donePaint = new Paint();
	private Paint remainingPaint = new Paint();
	private int actualBlock;
	private int actualExercise;
	private boolean inExercise;
	{
		nextPaint.setColor(Color.BLUE);
		actualPaint.setColor(Color.RED);
		donePaint.setColor(Color.GRAY);
		remainingPaint.setColor(Color.LTGRAY);
	}

	public WorkoutProgressView(Context context) {
		super(context);
	}

	public WorkoutProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WorkoutProgressView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setWorkout(Workout workout) {
		this.workout = workout;
	}

	public void setActiveExcercise(int blockIndex, int excerciseIndex) {
		this.actualBlock = blockIndex;
		this.actualExercise = excerciseIndex;
		this.inExercise = true;
		invalidate();
	}

	public void setActiveBreak(int blockIndex, int excerciseIndex) {
		this.actualBlock = blockIndex;
		this.actualExercise = excerciseIndex;
		this.inExercise = false;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//canvas.drawRect(x, y, x + width, y + height, bgPaint);
		if (workout != null) {
			int blockCount = workout.getNumberOfBlocks();
			int exerciseCount = workout.getNumberOfExercises();
			// One unit break between blocks, one unit for each exercise, 0.2 unit of break between each exercise 
			float total = (blockCount - 1) * BLOCK_BREAK_WIDTH + exerciseCount + EXERCISE_BREAK_WIDTH * (exerciseCount - blockCount);
			float unit = this.width / total;
			int maxRep = workout.getMaxRep();
			float offset = x;
			List<Block> blocks = workout.getBlocks();
			for (int i = 0; i < blocks.size(); i++) {
				if (i != 0) {
					offset += unit * BLOCK_BREAK_WIDTH;
				}
				List<Exercise> exercises = blocks.get(i).getExercises();
				for (int j = 0; j < exercises.size(); j++) {
					if (j != 0) {
						offset += unit * EXERCISE_BREAK_WIDTH;
					}
					Paint paint = remainingPaint;
					if (actualBlock > i) {
						paint = donePaint;
					} else if (actualBlock == i) {
						if (actualExercise > j) {
							paint = donePaint;
						} else if (actualExercise == j) {
							if (inExercise) {
								paint = actualPaint;
							} else {
								paint = nextPaint;
							}
						}
					}
					canvas.drawRect(offset, y + height - (height * exercises.get(j).getReps()) / maxRep, offset + unit, y + height, paint);
					offset += unit;
				}
			}
		}
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

	public void done() {
		this.actualBlock = workout.getNumberOfBlocks() - 1;
		this.actualExercise = workout.getBlocks().get(actualBlock).getExercises().size() - 1;
		this.inExercise = false;
		invalidate();
	}
}