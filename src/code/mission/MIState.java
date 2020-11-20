package code.mission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

//import java.util.HashMap;
import code.generic.State;

public class MIState extends State {
//	HashMap <String,String> stateMap;
	Cell[][] field;
//	int ethanRow;
//	int ethanColumn;
//	int[] memberRow;
//	int[] memberColumn;
//	int[] memberHealth;
//	boolean[] isMemberSaved; // Saved means they are on submarine
//	HashSet<Integer> truckMembers;
//	int membersOnTruck;
	public String[] splitted;

	public String getStateForVisitedStates() {
		String stateforVisitedStates = ";";
		String[] splitted = this.getState().split(";");
		splitted[3] = "";
		splitted[4] = "";
		for (String str : splitted) {
			stateforVisitedStates += str += ";";
		}
		return stateforVisitedStates;
	}

	public String returnString(int ethanRow, int ethanColumn, int[] memberRow, int[] memberColumn, int[] memberHealth,
			String isMemberSaved, String truckMembers, int membersOnTruck) {
		String str = "";
		String memberRowString = "";
		String memberColummString = "";
		String memberHealthString = "";
		String isMemberSavedString = "";
		str += ethanRow + "," + ethanColumn + ";";
		memberColummString = (Arrays.toString(memberColumn)).replaceAll("[\\[\\]\\s]", "") + ";";
		memberRowString = (Arrays.toString(memberRow)).replaceAll("[\\[\\]\\s]", "") + ";";
		memberHealthString = (Arrays.toString(memberHealth)).replaceAll("[\\[\\]\\s]", "") + ";";
		isMemberSavedString = isMemberSaved + ";";
		str += memberRowString + memberColummString + memberHealthString + isMemberSavedString + truckMembers + ";"
				+ membersOnTruck;
		splitted = str.split(";");
		return str;
	}

	public MIState(int ethanRow, int ethanColumn, int[] memberRow, int[] memberColumn, int[] memberHealth,
			String isMemberSaved, String truckMembers, int membersOnTruck) {
		this.state = this.returnString(ethanRow, ethanColumn, memberRow, memberColumn, memberHealth, isMemberSaved,
				truckMembers, membersOnTruck);
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
		return Integer.parseInt(splitted[0].split(",")[0]);
	}

	public int getEthanColumn() {
		return Integer.parseInt(splitted[0].split(",")[1]);
	}

	public int[] getMemberRow() {

		int[] intArray = Stream.of(splitted[1].split(",")).mapToInt(Integer::parseInt).toArray();
		return intArray;
	}

	public int[] getMemberColumn() {
		int[] intArray = Stream.of(splitted[2].split(",")).mapToInt(Integer::parseInt).toArray();
		return intArray;
	}

	public int[] getMemberHealth() {
		int[] memberHealth = Stream.of(splitted[3].split(",")).mapToInt(Integer::parseInt).toArray();
		return memberHealth;
	}

	public String getIsMemberSaved() {

		return splitted[4];
	}

	public String getTruckMembers() {
		return splitted[5];

	}

	public int getMembersOnTruck() {
		return Integer.parseInt(splitted[6]);
	}

//	public String getFromState(String key) {
//		return stateMap.get(key);
//	}
	public static void main(String[] args) {
		String string = "2,3;1,2,3;1,2,3;1,2,3;0,0,0;";
		String stateforVisitedStates = "";
		String[] splitted = string.split(";");
		splitted[3] = "";
		for (String str : splitted) {
			System.out.println(str);
			stateforVisitedStates += str + ";";
		}
		System.out.println(stateforVisitedStates);
	}
}
