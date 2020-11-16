package MissionImpossible;

public class STNode implements Comparable {
	State state;
	int cost;
	int heuristicCost;
	STNode parent;
	Operator operatorFromParent;
	int depth;
	
	
	public STNode()  {
	}

	
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
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
