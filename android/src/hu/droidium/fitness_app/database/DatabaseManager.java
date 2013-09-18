package hu.droidium.fitness_app.database;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;

/**
 * Convenience class for encapsulating database requests.
 * @author lajthabalazs
 *
 */
public class DatabaseManager {
	private static final String TAG = "DatabaseManager";

	private static DatabaseManager instance;
	
	private DatabaseHelper helper;
	
	
	private DatabaseManager(Context context) {
		helper = new DatabaseHelper(context.getApplicationContext());
	}
		
	public static DatabaseManager getInstance(Context context){
		if (instance == null) {
			instance = new DatabaseManager(context);
		}
		return instance;
	}
	
	public List<ExerciseType> getTypes(){
		List<ExerciseType> types = null;
		try {
			types = helper.getExerciseTypeDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return types;
	}

	public boolean addExercise(Exercise ormExercise) {
		try {
			helper.getExerciseDao().createOrUpdate(ormExercise);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Exercise getExercise(String exerciseId) {
		Exercise exercise = null;
		try {
			exercise = helper.getExerciseDao().queryForId(exerciseId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exercise;
	}

	public boolean addBlock(Block ormBlock) {
		try {
			helper.getBlockDao().createOrUpdate(ormBlock);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Block getBlock(String blockId) {
		Block block = null;
		try {
			 block = helper.getBlockDao().queryForId(blockId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return block;
	}

	public boolean addWorkout(Workout workout) {
		try {
			helper.getWorkoutDao().createOrUpdate(workout);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Workout getWorkout(String workoutId) {
		Workout workout = null;
		try {
			workout = helper.getWorkoutDao().queryForId(workoutId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return workout;
	}

	public boolean addProgram(Program program) {
		try {
			helper.getProgramDao().createOrUpdate(program);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Program getProgram(String programId) {
		Program program = null;
		try {
			program = helper.getProgramDao().queryForId(programId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return program;
	}

	public List<Program> getPrograms() {
		List<Program> programs = null;
		try {
			programs = helper.getProgramDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return programs;
		
	}
	public boolean addMuscle(Muscle muscle) {
		try {
			helper.getMuscleDao().createOrUpdate(muscle);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Muscle> getMuscles() {
		List<Muscle> muscles = null;
		try {
			muscles = helper.getMuscleDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return muscles;
		
	}

	public boolean addExerciseType(ExerciseType ormExercise) {
		try {
			helper.getExerciseTypeDao().createOrUpdate(ormExercise);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void associateMuscleWithExercise(ExerciseType exerciseType, Muscle muscle) {
		ExerciseTypeMuscle exerciseTypeMuscle = new ExerciseTypeMuscle(muscle, exerciseType);
		try {
			helper.getExerciseTypeMuscleDao().createOrUpdate(exerciseTypeMuscle );
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<ExerciseType> getExerciseTypes() {
		List<ExerciseType> exercises = null;
		try {
			exercises = helper.getExerciseTypeDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exercises;
	}

	public ExerciseType getExerciseType(String exerciseId) {
		try {
			return helper.getExerciseTypeDao().queryForId(exerciseId);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/* **************************************************************** */
	/* **********************   PROGRESS   **************************** */
	/* **************************************************************** */
	
	public List<ProgramProgress> getProgressList() {
		List<ProgramProgress> exercises = null;
		try {
			exercises = helper.getProgramProgressDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exercises;
	}

	public ProgramProgress getProgress(long programId) {
		try {
			return helper.getProgramProgressDao().queryForId(programId);
		} catch (SQLException e) {
			Log.e(TAG, "Couldn't load progress " + programId + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean startProgram(long id, Program program, String progressName) {
		ProgramProgress programProgress = new ProgramProgress(id, progressName, program);
		try {
			helper.getProgramProgressDao().create(programProgress);
			return true;
		} catch (SQLException e) {
			Log.e(TAG, "Couldn't add program " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
