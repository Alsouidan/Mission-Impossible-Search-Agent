package code.generic;

import java.util.Comparator;

public class NodeCostComparator implements Comparator <STNode> {

	@Override
	public int compare(STNode node, STNode otherNode) {
		return node.compareToWithCost(otherNode);
	}

}
