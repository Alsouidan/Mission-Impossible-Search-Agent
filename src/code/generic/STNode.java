package code.generic;


import code.mission.Operator;

public abstract class STNode implements Comparable {
	State state;
	int pathCost;  //From root
	protected int costFromParent; //from parent
	int heuristicCost;
	protected STNode parent;
	Operator operator;
	int depth;
	public int getCostFromRoot() {
		if(parent==null) {
			return 0;
		}
		return costFromParent+parent.getCostFromRoot();
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

	public int getHeuristicCost() {
		return heuristicCost;
	}

	public void setHeuristicCost(int heuristicCost) {
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
		STNode otherNode=(STNode)arg0;
		return 0;
	}
 public STNode getNthAncestor(int n) {
	 if(n>this.depth) {
		 return null; // No Ancestor at N
	 }
	 if(n==0) {
		 return this;
	 }
	 return this.parent.getNthAncestor(--n);
 } 
}
