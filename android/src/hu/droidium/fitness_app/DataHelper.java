package hu.droidium.fitness_app;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import android.util.Pair;

public class DataHelper {

	/**
	 * Sorts the exercises in descending order based on reps and returns a sorted String list
	 * @param totalReps A hash map containing exercise ids as keys and reps as values
	 * @return
	 */
	public static List<Pair<String, Integer>> getSortedExercises(HashMap<String, Integer> totalReps) {
		TreeSet<Pair<String, Integer>> orderer = new TreeSet<Pair<String,Integer>>(new Comparator<Pair<String, Integer>>() {

			@Override
			public int compare(Pair<String, Integer> lhs,
					Pair<String, Integer> rhs) {
				if (lhs.second > rhs.second) {
					return 1;
				} else if (lhs.second < rhs.second){
					return -1;
				}
				else {
					return lhs.first.compareTo(rhs.first);
				}
			}
		});
		for (String key : totalReps.keySet()){
			orderer.add(new Pair<String, Integer>(key, totalReps.get(key)));
		}
		ArrayList<Pair<String, Integer>> extractor = new ArrayList<Pair<String,Integer>>(orderer);
		return extractor;
	}

}
