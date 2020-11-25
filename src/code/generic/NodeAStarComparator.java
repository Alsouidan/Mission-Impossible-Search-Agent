package code.generic;

import java.util.Comparator;

public class NodeAStarComparator implements Comparator<STNode> {

	@Override
	public int compare(STNode node, STNode otherNode) {
		return node.compareAStar(otherNode);
	}

}
