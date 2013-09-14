package hu.droidium.fitness_app.database;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

/**
 * Convenience class for encapsulating database requests.
 * @author lajthabalazs
 *
 */
public class DatabaseManager {
	private static DatabaseManager instance;
	
	private DatabaseHelper helper;
	
	
	private DatabaseManager(Context context) {
		helper = new DatabaseHelper(context);
	}
		
	public static DatabaseManager getInstance(Context context){
		if (instance == null) {
			instance = new DatabaseManager(context);
		}
		return instance;
	}
	
	public List<ORMExerciseType> getTypes(){
		List<ORMExerciseType> types = null;
		try {
			types = helper.getExerciseTypeDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return types;
	}

	public boolean addExercise(ORMExercise ormExercise) {
		try {
			helper.getExerciseDao().createOrUpdate(ormExercise);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public ORMExercise getExercise(String exerciseId) {
		ORMExercise exercise = null;
		try {
			exercise = helper.getExerciseDao().queryForId(exerciseId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exercise;
	}

	public boolean addBlock(ORMBlock ormBlock) {
		try {
			helper.getBlockDao().createOrUpdate(ormBlock);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public ORMBlock getBlock(String blockId) {
		ORMBlock block = null;
		try {
			 block = helper.getBlockDao().queryForId(blockId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return block;
	}

	public boolean addWorkout(ORMWorkout workout) {
		try {
			helper.getWorkoutDao().createOrUpdate(workout);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public ORMWorkout getWorkout(String workoutId) {
		ORMWorkout workout = null;
		try {
			workout = helper.getWorkoutDao().queryForId(workoutId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return workout;
	}

	public boolean addProgram(ORMProgram program) {
		try {
			helper.getProgramDao().createOrUpdate(program);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public ORMProgram getProgram(String programId) {
		ORMProgram program = null;
		try {
			program = helper.getProgramDao().queryForId(programId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return program;
	}

	public boolean addMuscle(ORMMuscle muscle) {
		try {
			helper.getMuscleDao().createOrUpdate(muscle);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<ORMMuscle> getMuscles() {
		List<ORMMuscle> muscles = null;
		try {
			muscles = helper.getMuscleDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return muscles;
		
	}

	public boolean addExerciseType(ORMExerciseType ormExercise) {
		try {
			helper.getExerciseTypeDao().createOrUpdate(ormExercise);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public List<ORMExerciseType> getExerciseTypes() {
		List<ORMExerciseType> exercises = null;
		try {
			exercises = helper.getExerciseTypeDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exercises;
	}

	public ORMExerciseType getExerciseType(String exerciseId) {
		try {
			return helper.getExerciseTypeDao().queryForId(exerciseId);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public DatabaseHelper getHelper() {
		return helper;
	}
}
