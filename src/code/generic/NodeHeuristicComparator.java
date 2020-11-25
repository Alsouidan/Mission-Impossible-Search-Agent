package code.generic;

import java.util.Comparator;

public class NodeHeuristicComparator implements Comparator <STNode>{

	@Override
	public int compare(STNode node, STNode otherNode) {
		if(node.getHeuristicCost()[0]<otherNode.getHeuristicCost()[0]) {
			return -1;
		}
		else 
		{
			if(node.getHeuristicCost()[0]>otherNode.getHeuristicCost()[0]) {
				return 1;
			}
			else {
				if(node.getHeuristicCost()[1]<otherNode.getHeuristicCost()[1]) {
					return -1;
				}
				else {
					if(node.getHeuristicCost()[1]>otherNode.getHeuristicCost()[1]) {
						return 1;
					}
				}
			}
		}
		return 0;
	}

}
