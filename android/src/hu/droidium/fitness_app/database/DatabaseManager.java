package hu.droidium.fitness_app.database;

import hu.droidium.fitness_app.R;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.j256.ormlite.table.TableUtils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
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
	
	protected DatabaseManager(Context context) {
		Log.e(TAG, "create database manager");
		helper = new DatabaseHelper(context.getApplicationContext());
	}
		
	public static DatabaseManager getInstance(Context context){
		if (instance == null) {
			instance = new DatabaseManager(context);
		}
		return instance;
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
	
	public Muscle getMuscle(String id) {
		try {
			return helper.getMuscleDao().queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
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
	
	public ExerciseTypeMuscle getExerciseTypeMuscle(String id) {
		try {
			return helper.getExerciseTypeMuscleDao().queryForId(id);
		} catch (SQLException e) {
			return null;
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
	
	public List<ProgramProgress> getProgressList(boolean showDone) {
		List<ProgramProgress> programProgresses = null;
		if (showDone) {
			try {
				programProgresses = helper.getProgramProgressDao().queryForAll();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Map<String, Object> values = new HashMap<String, Object>();
				values.put("terminationDate", new Long(-1));
				programProgresses = helper.getProgramProgressDao().queryForFieldValues(values);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return programProgresses;
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
	
	public boolean startProgram(ProgramProgress programProgress) {
		programProgress.setTerminationDate(-1);
		try {
			helper.getProgramProgressDao().create(programProgress);
			return true;
		} catch (SQLException e) {
			Log.e(TAG, "Couldn't add program " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean addWorkoutProgress(WorkoutProgress workoutProgress) {
		workoutProgress.setFinishDate(-1);
		try {
			helper.getWorkoutProgressDao().create(workoutProgress);
			return true;
		}catch (SQLException e) {
			Log.e(TAG, "Couldn't add workout progress " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public WorkoutProgress getWorkoutProgress(long id) {
		try {
			return helper.getWorkoutProgressDao().queryForId(id);
		} catch (SQLException e) {
			Log.e(TAG, "Couldn't load workout progress " + id + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public boolean updateProgress(ProgramProgress progress) {
		try {
			helper.getProgramProgressDao().update(progress);
			return true;
		} catch (SQLException e) {
			Log.e(TAG, "Couldn't update program " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean addExerciseProgress(ExerciseProgress exerciseProgress) {
		try {
			helper.getExerciseProgressDao().create(exerciseProgress);
			return true;
		} catch (SQLException e) {
			Log.e(TAG, "Couldn't add exercise progress " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateWorkoutProgress(WorkoutProgress workoutProgress) {
		try {
			helper.getWorkoutProgressDao().update(workoutProgress);
			return true;
		} catch (SQLException e) {
			Log.e(TAG, "Couldn't update workout " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public ExerciseProgress getExerciseProgress(long id) {
		try {
			return helper.getExerciseProgressDao().queryForId(id);
		} catch (SQLException e) {
			Log.e(TAG, "Couldn't load exercise progress " + id + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public boolean addTranslation(Translation translaiton) {
		try {
			helper.getTranslationDao().create(translaiton);
			return true;
		}catch (SQLException e) {
			Log.e(TAG, "Couldn't add workout progress " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	public String getTranslation(String string, String languageCode) {
		try {
			String id = Translation.getKey(string, languageCode);
			Translation translation = helper.getTranslationDao().queryForId(id);
			if (translation == null) {
				return string;
			} else {
				return translation.getValue();
			}
		} catch (SQLException e) {
			Log.e(TAG, "Couldn't load translation for " + string + " language: " + languageCode + " " + e.getMessage());
			e.printStackTrace();
			return string;
		}
	}

	public void removeProgramProgress(ProgramProgress progress) {
		try {
			helper.getProgramProgressDao().delete(progress);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removeAllUserData(Activity activity) {
		Builder builder = new Builder(activity);
		builder.setTitle(R.string.deleteAllProgramProgressSecondTitle);
		builder.setMessage(R.string.deleteAllProgramProgressSecondMessage);
		builder.setPositiveButton(R.string.deleteAllProgramProgressSecondDelete, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				removeAllUserData();
			}
		});
		builder.setNegativeButton(R.string.deleteAllProgramProgressSecondCancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.create().show();
	}

	protected void removeAllUserData() {
		try {
			TableUtils.clearTable(helper.getConnectionSource(), ProgramProgress.class);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			TableUtils.clearTable(helper.getConnectionSource(), WorkoutProgress.class);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			TableUtils.clearTable(helper.getConnectionSource(), ExerciseProgress.class);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}