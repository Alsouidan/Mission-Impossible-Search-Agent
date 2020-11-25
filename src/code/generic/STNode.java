package code.generic;

import code.mission.Operator;

public abstract class STNode implements Comparable {
	State state;
	int id;
	int pathCost; // From root
	protected int costFromParent; // from parent
	int[] heuristicCost;
	protected STNode parent;

	protected abstract int compareToWithCost(STNode otherNode);

	protected abstract int compareAStar(STNode otherNode);

	Operator operator; // applied on parent to get this node

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPathCost() {
		return pathCost;
	}

	public void setPathCost(int pathCost) {
		this.pathCost = pathCost;
	}

	public int getCostFromParent() {
		return costFromParent;
	}

	public void setCostFromParent(int costFromParent) {
		this.costFromParent = costFromParent;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	int depth;

	public int getCostFromRoot() {
		if (parent == null) {
			return 0;
		}
		return costFromParent + parent.getCostFromRoot();
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public int getCost() {
		return pathCost;
	}

	public void setCost(int pathCost) {
		this.pathCost = pathCost;
	}

	public int[] getHeuristicCost() {
		return heuristicCost;
	}

	public void setHeuristicCost(int[] heuristicCost) {
		this.heuristicCost = heuristicCost;
	}

	public STNode getParent() {
		return parent;
	}

	public void setParent(STNode parent) {
		this.parent = parent;
	}

	@Override
	public int compareTo(Object arg0) {
		STNode otherNode = (STNode) arg0;
		if (otherNode.depth < this.depth) {
			return 1;
		} else {
			if (otherNode.depth > this.depth) {
				return -1;
			} else {
				if (otherNode.id < this.id)
					return 1;
				else
					return -1;
			}
		}
	}

	public STNode(State state, int id, int pathCost, int costFromParent, int[] heuristicCost, STNode parent,
			Operator operator, int depth) {
		super();
		this.state = state;
		this.id = id;
		this.pathCost = pathCost;
		this.costFromParent = costFromParent;
		this.heuristicCost = heuristicCost;
		this.parent = parent;
		this.operator = operator;
		this.depth = depth;
	}

	public STNode getNthAncestor(int n) {
		if (n > this.depth) {
			return null; // No Ancestor at N
		}
		if (n == 0) {
			return this;
		}
		return this.parent.getNthAncestor(--n);
	}

	public String getPlan() {
		// Souidan

		if (this.getParent() == null) {
			return "";
		} else {
			return this.getParent().getPlan() + this.getOperator() + ",";
		}

	}

}
