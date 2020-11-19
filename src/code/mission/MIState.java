package code.mission;

import java.util.HashSet;

//import java.util.HashMap;
import code.generic.State;

public class MIState extends State {
//	HashMap <String,String> stateMap;
	String state;
	Cell[][] field;
//	int ethanRow;
//	int ethanColumn;
//	int[] memberRow;
//	int[] memberColumn;
//	int[] memberHealth;
//	boolean[] isMemberSaved; // Saved means they are on submarine
//	HashSet<Integer> truckMembers;
//	int membersOnTruck;

	public HashSet<Integer> getTruckMembers() {
		return truckMembers;
	}

	public void setTruckMembers(HashSet<Integer> truckMembers) {
		this.truckMembers = truckMembers;
	}

	public int getMembersOnTruck() {
		return membersOnTruck;
	}

	public void setMembersOnTruck(int membersOnTruck) {
		this.membersOnTruck = membersOnTruck;
	}

	public String returnString(Cell[][] field, int ethanRow, int ethanColumn, int[] memberRow, int[] memberColumn,
			int[] memberHealth, boolean[] isMemberSaved, HashSet<Integer> truckMembers) {
		return null;
	}

	public MIState(String state) {
		this.state=state;

	}

	public MIState() {

	}

//	public Cell[][] getField() {
//		return field;
//	}

//	public void setField(Cell[][] field) {
//		this.field = field;
//	}

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

	public MIState(Cell[][] grid, int ethan) {

	}

//	public String getFromState(String key) {
//		return stateMap.get(key);
//	}
	public boolean isGoalState() {
		for (int i = 0; i < memberColumn.length; i++) {
			if (!isMemberSaved[i])
				return false;
		}
		return true;
	}

	public String toString() {
		String str = "";
		str += ethanRow + "," + ethanColumn + ";";
		for (int i = 0; i < memberColumn.length; i++) {
			str += memberRow[i] + "," + memberColumn[i] + "," + memberHealth[i] + "," + isMemberSaved[i] + ";";
		}
		str += truckMembers.toString() + ";";
		return str;
		
	}
}
