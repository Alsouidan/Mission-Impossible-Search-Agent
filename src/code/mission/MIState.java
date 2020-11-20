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
	public String getStateForVisitedStates() {
		String stateforVisitedStates=";";
		String [] splitted=this.getState().split(";");
		splitted[3]="";
		for(String str : splitted) {
			stateforVisitedStates+=str+=";";
		}
		return stateforVisitedStates;
	}
	public String returnString(int ethanRow, int ethanColumn, int[] memberRow, int[] memberColumn, int[] memberHealth,
		int[] isMemberSaved, ArrayList<Integer> truckMembers) {
		String str = "";
		String memberRowString = "";
		String memberColummString = "";
		String memberHealthString = "";
		String isMemberSavedString = "";
		str += ethanRow + "," + ethanColumn + ";";
		memberColummString = (Arrays.toString(memberColumn)).replaceAll("[\\[\\]\\s]", "") + ";";
		memberRowString = (Arrays.toString(memberRow)).replaceAll("[\\[\\]\\s]", "") + ";";
		memberHealthString = (Arrays.toString(memberHealth)).replaceAll("[\\[\\]\\s]", "") + ";";
		isMemberSavedString = (Arrays.toString(isMemberSaved)).replaceAll("[\\[\\]\\s]", "") + ";";
		if(truckMembers.size()==0) {
			str += memberRowString + memberColummString + memberHealthString + isMemberSavedString
					+ "e"; //e for empty	
		}
		else {
		str += memberRowString + memberColummString + memberHealthString + isMemberSavedString
				+ truckMembers.toString().replaceAll("[\\[\\]\\s]", "");
		}
		return str;
	}

	public MIState(int ethanRow, int ethanColumn, int[] memberRow, int[] memberColumn, int[] memberHealth,
			int[] isMemberSaved, ArrayList<Integer> truckMembers) {
		this.state = this.returnString(ethanRow, ethanColumn, memberRow, memberColumn, memberHealth, isMemberSaved,
				truckMembers);
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
		return Integer.parseInt(state.split(";")[0].split(",")[0]);
	}

	public int getEthanColumn() {
		return Integer.parseInt(state.split(";")[0].split(",")[1]);
	}

	public int[] getMemberRow() {

		int[] intArray = Stream.of(state.split(";")[1].split(",")).mapToInt(Integer::parseInt).toArray();
		return intArray;
	}

	public int[] getMemberColumn() {
		int[] intArray = Stream.of(state.split(";")[2].split(",")).mapToInt(Integer::parseInt).toArray();
		return intArray;
	}

	public int[] getMemberHealth() {
		int[] memberHealth = Stream.of(state.split(";")[3].split(",")).mapToInt(Integer::parseInt).toArray();
		return memberHealth;
	}

	public int[] getIsMemberSaved() {
		int[] isMemberSaved = Stream.of(state.split(";")[4].split(",")).mapToInt(Integer::parseInt).toArray();
		return isMemberSaved;
	}

	public ArrayList<Integer> getTruckMembers() {
		String splitted=state.split(";")[5];
		if(splitted.equals("e")) {
			return new ArrayList<Integer>();
		}
		else {
		int[] truckMembersInt = Stream.of(splitted.split(",")).mapToInt(Integer::parseInt).toArray();
		ArrayList<Integer> truckMembers = new ArrayList<Integer>();
		for (int i : truckMembersInt) {
			truckMembers.add(i);
		}
		return truckMembers;
		}
	}

	public int getMembersOnTruck() {
		return getTruckMembers().size();
	}

//	public String getFromState(String key) {
//		return stateMap.get(key);
//	}
public static void main (String [] args) {
	String string="2,3;1,2,3;1,2,3;1,2,3;0,0,0;";
	String stateforVisitedStates="";
	String [] splitted=string.split(";");
	splitted[3]="";
	for(String str : splitted) {
		System.out.println(str);
		stateforVisitedStates+=str+";";
	}
System.out.println(stateforVisitedStates);
}
}
