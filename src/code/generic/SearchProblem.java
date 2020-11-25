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
//	General Search Problem implementation (Same as in lecture)
	public STNode generalSearchProcedure(SearchProblem searchProblem, String strategy) {
		System.out.println("Searching");
		STNode curr = null;
		STNode tempNode = null;
		int iterativeDepthSearhCounter = 0; // Max depth to reach in IDF
		int stepSize = 1; // Step size that the counter is incremented by in IDF
		if (strategy.equals("ID")) {
			STNode root = queue.remove(); // Dequeue from the queue and get the root
			while (true) {
				// Empty the queue
				queue = new PriorityQueue<STNode>(new NodeIdComparator());
//				Add the root
				queue.add(root); 
//				The search routine 
				while (!queue.isEmpty()) {
					curr = queue.remove();
					if (goalTest(curr)) {
						System.out.println("Found");
						return curr;
					}
					for (int i = 0; i < operators.size(); i++) {
						tempNode = applyOperator(curr, operators.get(i));

						if (tempNode != null && tempNode.getDepth() <= iterativeDepthSearhCounter) {
							queue.add(tempNode);
						}


					}

				}
//				Increase the step size according to the counter
				stepSize = (1 + (iterativeDepthSearhCounter / 10));
				iterativeDepthSearhCounter += stepSize;
//				Clear visited states for the next loop
				visitedStates.clear();
			}
		} else {
//			Search routine
			while (!queue.isEmpty()) { // Loop until queue is empty
				curr = queue.remove(); // Remove a node
				if (goalTest(curr)) { // See if its a goal node
					return curr;
				}
//				If not a goal node we will apply the operators and enqueue the new nodes
				for (int i = 0; i < operators.size(); i++) {
					tempNode = applyOperator(curr, operators.get(i));

					if (tempNode != null) {
						queue.add(tempNode);
					}

				}

			}
		}
//		If no solution is found we return null
		return null;
	}
}
