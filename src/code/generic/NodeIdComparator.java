package code.generic;

import java.util.Comparator;

public class NodeIdComparator implements Comparator <STNode>{

	@Override
	public int compare(STNode node, STNode otherNode) {
		if(node.getId()>otherNode.getId()) { // Priority for greater than
			return -1;
		}
		else {
			if(node.getId()<otherNode.getId()) {
				return 1;
			}
		}
		return 0;
	}

}
