package code.generic;

import java.util.HashSet;
import java.util.PriorityQueue;

import code.mission.Operator;

public abstract class SearchProblem {
	protected State initialState;
	protected State[] stateSpace;
	protected PriorityQueue<STNode> queue;
	protected Operator[] operators;
	protected HashSet<String> visitedStates;

	public SearchProblem(State initialState, State[] stateSpace, PriorityQueue<STNode> queue, Operator[] operators) {
		super();
		this.initialState = initialState;
		this.stateSpace = stateSpace;
		this.queue = queue;
		this.operators = operators;
	}

	public abstract boolean goalTest(STNode node);

	public abstract void populateOperators();

	public abstract int pathCost(STNode node);

	public abstract STNode applyOperator(STNode node, Operator operator);

	public STNode generalSearchProcedure(SearchProblem searchProblem, String strategy) {
		STNode curr = null;
		STNode tempNode = null;
		int iterativeDepthSearhCounter = 0;
		if (strategy.equals("IDF")) {
			STNode root = queue.remove();
			while (true) {
				queue = new PriorityQueue<STNode>(new NodeIdComparator());
				queue.add(root);
				while (!queue.isEmpty()) {
					curr = queue.remove();
//			visitedStates.add(curr.getState().getState());
					if (goalTest(curr)) {
						return curr;
					}
					for (int i = 0; i < operators.length; i++) {
						tempNode = applyOperator(curr, operators[i]);
						if (tempNode != null && tempNode.getDepth() <= iterativeDepthSearhCounter) {
							queue.add(tempNode);
						}

					}

				}
				iterativeDepthSearhCounter++;
			}
		}
		else {
		while (!queue.isEmpty()) {
			curr = queue.remove();
//		visitedStates.add(curr.getState().getState());
			if (goalTest(curr)) {
				return curr;
			}
			for (int i = 0; i < operators.length; i++) {
				tempNode = applyOperator(curr, operators[i]);
				if (strategy.equals("IDF")) {
					if (tempNode != null && tempNode.getDepth() <= iterativeDepthSearhCounter) {
						queue.add(tempNode);
					}
				} else {
					if (tempNode != null) {
						queue.add(tempNode);
					}
				}

			}

		}
		}
		return null;
	}
}
