package code.generic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import code.mission.Operator;

public abstract class SearchProblem {
	protected State initialState;
	protected State[] stateSpace;
	protected PriorityQueue<STNode> queue;
	protected ArrayList<Operator> operators;
	protected HashSet<String> visitedStates;
	protected static int expandedNodes;

	public SearchProblem(State initialState, State[] stateSpace, PriorityQueue<STNode> queue,
			ArrayList<Operator> operators) {
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
		System.out.println("Searching");
		STNode curr = null;
		STNode tempNode = null;
		int iterativeDepthSearhCounter = 0;
		int stepSize=1;
		if (strategy.equals("ID")) {
			STNode root = queue.remove();
			System.out.println(root);
			while (true) {
				System.out.println("Loop number: " + iterativeDepthSearhCounter);
				queue = new PriorityQueue<STNode>(new NodeIdComparator());
				queue.add(root);
				while (!queue.isEmpty()) {
//					System.out.println("Removing head");
					curr = queue.remove();
//			visitedStates.add(curr.getState().getState());
					if (goalTest(curr)) {
						System.out.println("Found");
						return curr;
					}
//					Collections.shuffle(operators);
					for (int i = 0; i < operators.size(); i++) {
						tempNode = applyOperator(curr, operators.get(i));

						if (tempNode != null && tempNode.getDepth() <= iterativeDepthSearhCounter) {
//							System.out.println("Adding node");
							queue.add(tempNode);
						}
//						else {
//							if(tempNode!= null && tempNode.getDepth()>iterativeDepthSearhCounter)
//								break;
//						}

					}

				}
				stepSize=(1+(iterativeDepthSearhCounter/10));
				iterativeDepthSearhCounter+=stepSize;
				visitedStates.clear();
			}
		} else {
			while (!queue.isEmpty()) {
				curr = queue.remove();
//		visitedStates.add(curr.getState().getState());
				if (goalTest(curr)) {
					return curr;
				}
//				Collections.shuffle(operators);
				for (int i = 0; i < operators.size(); i++) {
					tempNode = applyOperator(curr, operators.get(i));
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
