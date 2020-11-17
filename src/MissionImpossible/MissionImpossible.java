package MissionImpossible;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;

public class MissionImpossible {
	State initialState;
	Cell[][] grid;
	int gridRows;
	int gridColumns;
	int ethanRow;
	int ethanColumn;
	int submarineRow;
	int submarineColumn;
	int[] memberRow;
	int[] memberColumn;
	int[] memberHealth;
	boolean[] isMemberSaved; // Saved means they are on submarine
	int maximumCarry;
	int numberOfMembers;
	HashSet<State> visitedStates;
	ArrayList<Occupant> arrayToBeShuffled;
	PriorityQueue queue;
	public MissionImpossible() {
		genGrid();
		int [] truckMembers=new int[maximumCarry];
		for (int i=0;i<maximumCarry;i++) {
			truckMembers[i]=-1;
		}
		initialState = new State(grid, ethanRow, ethanColumn, submarineRow, submarineColumn, memberRow, memberColumn,
				memberHealth, isMemberSaved,  truckMembers, numberOfMembers);
	}

	public void genGrid() {
		extractingAttributes(genGridHelper());
		grid = new Cell[gridRows][gridColumns];
		for (int i = 0; i < gridRows; i++) {
			for (int j = 0; j < gridColumns; j++) {
				grid[i][j] = new Cell(1, j);
			}
		}
		System.out.println(numberOfMembers);
		System.out.println(arrayToBeShuffled);
		for (int i = 0; i < numberOfMembers; i++) {
			grid[memberRow[i]][memberColumn[i]]
					.setOccupant(arrayToBeShuffled.get((memberRow[i] * gridColumns) + memberColumn[i]));
		}
		grid[ethanRow][ethanColumn].setOccupant(arrayToBeShuffled.get(ethanRow*gridColumns+ethanColumn));
		grid[submarineRow][submarineColumn].setOccupant(arrayToBeShuffled.get(submarineRow*gridColumns+submarineColumn));

	}

	public void extractingAttributes(String inputString) {
		System.out.println(inputString);
		String[] splitted = inputString.split(";");
		String[] temp = splitted[0].split(",");
		gridRows = Integer.parseInt(temp[0]);
		gridColumns = Integer.parseInt(temp[1]);
		temp = splitted[1].split(",");
		ethanRow = Integer.parseInt(temp[0]);
		ethanColumn = Integer.parseInt(temp[1]);
		temp = splitted[2].split(",");
		submarineRow = Integer.parseInt(temp[0]);
		submarineColumn = Integer.parseInt(temp[1]);
		temp = splitted[3].split(",");
		numberOfMembers = temp.length / 2;
		memberRow = new int[numberOfMembers];
		memberColumn = new int[numberOfMembers];
		memberHealth = new int[numberOfMembers];
		isMemberSaved = new boolean[numberOfMembers];
		for (int i = 0, c = 0; c < numberOfMembers; i += 2, c++) {
			memberRow[c] = Integer.parseInt(temp[i]);
			memberColumn[c] = Integer.parseInt(temp[i + 1]);

		}
		temp = splitted[4].split(",");
		for (int i = 0; i < numberOfMembers; i++) {
			memberHealth[i] = Integer.parseInt(temp[i]);
		}
		maximumCarry = Integer.parseInt(splitted[5]);
	}

	public String genGridHelper() {

		int max = 15;
		int min = 5;
		int range = max - min + 1;
		String inputString = "";
		int rowSize = (int) (Math.random() * range) + min;
		int columnSize = (int) (Math.random() * range) + min;
		inputString += rowSize + ",";
		inputString += columnSize + ";";
		System.out.println(inputString);
		arrayToBeShuffled = new ArrayList<Occupant>(rowSize * columnSize);
		Ethan ethan = new Ethan();
		arrayToBeShuffled.add(ethan);
		Submarine submarine = new Submarine();
		arrayToBeShuffled.add(submarine);
		max = 10;
		min = 5;
		range = max - min + 1;
		int numberOfMembers = (int) (Math.random() * range) + min;
		max = 99;
		min = 1;
		range = max - min + 1;
		Member[] membersInShuffledArray = new Member[numberOfMembers];
		for (int i = 0; i < numberOfMembers; i++) {
			membersInShuffledArray[i] = new Member();
			arrayToBeShuffled.add(membersInShuffledArray[i]);
		}
		int initialSize = arrayToBeShuffled.size();
		for (int i = initialSize; i < rowSize * columnSize; i++) {
			arrayToBeShuffled.add(null);
		}
		System.out.println(arrayToBeShuffled.size());
		Collections.shuffle(arrayToBeShuffled);
		System.out.println(arrayToBeShuffled.toString());
		System.out.println();
		int ethanIndex = arrayToBeShuffled.indexOf(ethan);
		inputString += ethanIndex / columnSize + ",";
		inputString += ethanIndex % columnSize + ";";
		int submarineIndex = arrayToBeShuffled.indexOf(submarine);
		inputString += submarineIndex / columnSize + ",";
		inputString += submarineIndex % columnSize + ";";
		String healthString = "";
		int memberIndex;
		for (int i = 0; i < numberOfMembers; i++) {
			memberIndex = arrayToBeShuffled.indexOf(membersInShuffledArray[i]);
			inputString += memberIndex / columnSize + ",";
			inputString += memberIndex % columnSize + ",";
			healthString += ((Member) (membersInShuffledArray[i])).getHealth() + ",";
		}

		// Removing comma at end and replacing it with a semi colon
		inputString = inputString.substring(0, inputString.length() - 1);
		inputString += ";";
		inputString += healthString;
		inputString = inputString.substring(0, inputString.length() - 1);
		inputString += ";";
		max = numberOfMembers;
		min = 1;
		range = max - min + 1;
		inputString += (int) (Math.random() * range) + min;

		return inputString;

	}

	public State getInitialState() {
		return initialState;
	}

	public void setInitialState(State initialState) {
		this.initialState = initialState;
	}

	public Cell[][] getGrid() {
		return grid;
	}

	public void setGrid(Cell[][] grid) {
		this.grid = grid;
	}

	public int getGridRows() {
		return gridRows;
	}

	public void setGridRows(int gridRows) {
		this.gridRows = gridRows;
	}

	public int getGridColumns() {
		return gridColumns;
	}

	public void setGridColumns(int girdColumns) {
		this.gridColumns = girdColumns;
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

	public int getMaximumCarry() {
		return maximumCarry;
	}

	public void setMaximumCarry(int maximumCarry) {
		this.maximumCarry = maximumCarry;
	}

	public static void main(String[] args) {
		MissionImpossible mi = new MissionImpossible();
		for (int i = 0; i < mi.getGridRows(); i++) {
			for (int j = 0; j < mi.getGridColumns(); j++) {
				System.out.print(mi.getGrid()[i][j]);
			}
			System.out.println("");
		}
	}
}
