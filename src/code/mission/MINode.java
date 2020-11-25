package code.mission;
// Subclass of the STNode , specific for the Mission Impossible problem
import code.generic.STNode;
import code.generic.State;

public class MINode extends STNode {
	int dead;

	public MINode(State state, int id, int pathCost, int costFromParent, int[] heuristicCost, STNode parent,
			Operator operator, int depth, int dead) {
		super(state, id, pathCost, costFromParent, heuristicCost, parent, operator, depth);
		this.dead = dead;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int compareToWithCost(STNode otherNode) {
		MINode other = ((MINode) otherNode);
		if (this.dead < other.dead) {
			return -1;
		} else {
			if (this.dead > other.dead) {
				return 1;
			} else {
				if (this.getCost() < other.getCost()) {
					return -1;

				} else {
					return 1;
				}

			}
		}
	}

	@Override
	protected int compareAStar(STNode otherNode) {
		int nodeDeath = this.dead + this.getHeuristicCost()[0];
		int otherNodeDeath = ((MINode) otherNode).dead + otherNode.getHeuristicCost()[0];
		int nodeDamage = this.getCost() + this.getHeuristicCost()[1];
		int otherNodeDamage = otherNode.getCost() + otherNode.getHeuristicCost()[1];
		if (nodeDeath < otherNodeDeath) {
			return -1;
		} else {
			if (nodeDeath > otherNodeDeath) {
				return 1;
			} else {
				if (nodeDamage < otherNodeDamage) {
					return -1;
				} else {
					return 1;
				}
			}
		}
	}

}
