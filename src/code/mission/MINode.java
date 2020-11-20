package code.mission;

import code.generic.STNode;
import code.generic.State;

public class MINode  extends STNode {
int dead;
	public MINode(State state, int id, int pathCost, int costFromParent, int heuristicCost, STNode parent,
			Operator operator, int depth,int dead) {
		super(state, id, pathCost, costFromParent, heuristicCost, parent, operator, depth);
		this.dead=dead;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int compareToWithCost(STNode otherNode) {
		MINode other=((MINode)otherNode);
		if(this.dead<other.dead) {
			return -1;
		}
		else {
			if(this.dead>other.dead) 
			{
				return 1;
			}
			else {if(this.getCost()<other.getCost()) {
				return -1;
				
			}
			
				}
		}
		return 0;
		// TODO Auto-generated method stub
	}



}
