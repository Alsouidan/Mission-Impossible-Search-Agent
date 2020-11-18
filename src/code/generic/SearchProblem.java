package code.generic;
import java.util.PriorityQueue;

import code.mission.Operator;

public abstract class SearchProblem {
protected static State initialState;
protected static State[] stateSpace;
protected static PriorityQueue queue;
protected static Operator [] operators;
public abstract boolean goalTest();
public abstract void populateOperators();
public abstract int pathCost(STNode node); 
public STNode generalSearchProcedure(SearchProblem searchProblem,String strategy) {
	return null;
}

}
