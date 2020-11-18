package code.mission;

//import java.util.HashMap;
import code.generic.*;
public class MIState extends State {
//	HashMap <String,String> stateMap;
	Cell[][] grid;
	int ethanRow;
	int ethanColumn;
	int submarineRow;
	int submarineColumn;
	int[] memberRow;
	int[] memberColumn;
	int[] memberHealth;
	boolean[] isMemberSaved; // Saved means they are on submarine
	int [] truckMembers;

	public MIState(Cell[][] grid, int ethanRow, int ethanColumn, int submarineRow, int submarineColumn, int[] memberRow,
			int[] memberColumn, int[] memberHealth, boolean[] isMemberSaved,
			int[] truckMembers, int numberOfMembers) {
		this.grid = grid;
		this.ethanRow = ethanRow;
		this.ethanColumn = ethanColumn;
		this.submarineRow = submarineRow;
		this.submarineColumn = submarineColumn;
		this.memberRow = memberRow;
		this.memberColumn = memberColumn;
		this.memberHealth = memberHealth;
		this.isMemberSaved = isMemberSaved;
		this.truckMembers = truckMembers;
		this.numberOfMembers = numberOfMembers;
	}

	public MIState() {

	}

	public Cell[][] getGrid() {
		return grid;
	}

	public void setGrid(Cell[][] grid) {
		this.grid = grid;
	}

	public int getEthanRow() {
		return ethanRow;
	}

	public void setEthanRow(int ethanRow) {
		this.ethanRow = ethanRow;
	}

	public int getEthanColumn() {
		return ethanColumn;
	}

	public void setEthanColumn(int ethanColumn) {
		this.ethanColumn = ethanColumn;
	}

	public int getSubmarineRow() {
		return submarineRow;
	}

	public void setSubmarineRow(int submarineRow) {
		this.submarineRow = submarineRow;
	}

	public int getSubmarineColumn() {
		return submarineColumn;
	}

	public void setSubmarineColumn(int submarineColumn) {
		this.submarineColumn = submarineColumn;
	}

	public int[] getMemberRow() {
		return memberRow;
	}

	public void setMemberRow(int[] memberRow) {
		this.memberRow = memberRow;
	}

	public int[] getMemberColumn() {
		return memberColumn;
	}

	public void setMemberColumn(int[] memberColumn) {
		this.memberColumn = memberColumn;
	}

	public int[] getMemberHealth() {
		return memberHealth;
	}

	public void setMemberHealth(int[] memberHealth) {
		this.memberHealth = memberHealth;
	}

	public boolean[] getIsMemberSaved() {
		return isMemberSaved;
	}

	public void setIsMemberSaved(boolean[] isMemberSaved) {
		this.isMemberSaved = isMemberSaved;
	}


	public int getNumberOfMembers() {
		return numberOfMembers;
	}

	public void setNumberOfMembers(int numberOfMembers) {
		this.numberOfMembers = numberOfMembers;
	}

	int numberOfMembers;

	public MIState(Cell[][] grid, int ethan) {

	}
//	public String getFromState(String key) {
//		return stateMap.get(key);
//	}
	public boolean isGoalState() {
		for(int i=0;i<numberOfMembers;i++) {
			if(!isMemberSaved[i])
				return false;
		}
		return true;
	}
}
