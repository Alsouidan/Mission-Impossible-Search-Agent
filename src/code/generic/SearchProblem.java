package code.generic;
import code.mission.Operator;

public abstract class SearchProblem {
protected static State initialState;
protected Operator [] operators;
public abstract boolean goalTest();
public abstract void populateOperators();
public abstract int calculateCostOfNode(STNode node);
public int pathCost(STNode node) {
	return node.getCostFromRoot();
}

}
