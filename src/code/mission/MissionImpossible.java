package code.mission;

import java.util.Arrays;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import code.generic.STNode;
import code.generic.SearchProblem;
import code.generic.State;

public class MissionImpossible extends SearchProblem {
	static ArrayList<Occupant> arrayToBeShuffled = new ArrayList<Occupant>();
	static Cell[][] field;
	static int gridRows;
	static int gridColumns;
	static int ethanRow;
	static int ethanColumn;
	static int submarineRow;
	static int submarineColumn;
	static int[] memberRow;
	static int[] memberColumn;
	static int[] memberHealth;
	static boolean[] isMemberSaved; // Saved means they are on submarine
	static int maximumCarry;
	static int numberOfMembers;
	static int membersOnTruck;
	static HashSet<Integer> truckMembers;
	static HashSet<MIState> visitedStates;
	static int healthGained;
	static int memberAtEthan;
	public MissionImpossible() {

	}

	public static String solve(String grid, String strategy, boolean visualize) {

//		****************************************************()		

		System.out.println(grid);
		String[] splitted = grid.split(";");
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

		field = new Cell[gridRows][gridColumns];
		for (int i = 0; i < gridRows; i++) {
			for (int j = 0; j < gridColumns; j++) {
				field[i][j] = new Cell(1, j);
			}
		}
		System.out.println(numberOfMembers);
		System.out.println(arrayToBeShuffled);
		for (int i = 0; i < numberOfMembers; i++) {
			field[memberRow[i]][memberColumn[i]]
					.setOccupant(arrayToBeShuffled.get((memberRow[i] * gridColumns) + memberColumn[i]));
		}
		field[ethanRow][ethanColumn].setOccupant(arrayToBeShuffled.get(ethanRow * gridColumns + ethanColumn));
		field[submarineRow][submarineColumn]
				.setOccupant(arrayToBeShuffled.get(submarineRow * gridColumns + submarineColumn));
		truckMembers = new HashSet<Integer>();
		for (int i = 0; i < gridRows; i++) {
			for (int j = 0; j < gridColumns; j++) {
				System.out.print(field[i][j]);
			}
			System.out.println("");
		}
		initialState = new MIState(field, ethanRow, ethanColumn, memberRow, memberColumn, memberHealth, isMemberSaved,
				truckMembers);
		return null;
	}

	public static String genGrid() {
		int max = 15;
		int min = 5;
		int range = max - min + 1;
		String grid = "";
		int rowSize = (int) (Math.random() * range) + min;
		int columnSize = (int) (Math.random() * range) + min;
		grid += rowSize + ",";
		grid += columnSize + ";";
		System.out.println(grid);
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
		Collections.shuffle(arrayToBeShuffled);
		int ethanIndex = arrayToBeShuffled.indexOf(ethan);
		grid += ethanIndex / columnSize + ",";
		grid += ethanIndex % columnSize + ";";
		int submarineIndex = arrayToBeShuffled.indexOf(submarine);
		grid += submarineIndex / columnSize + ",";
		grid += submarineIndex % columnSize + ";";
		String healthString = "";
		int memberIndex;
		for (int i = 0; i < numberOfMembers; i++) {
			memberIndex = arrayToBeShuffled.indexOf(membersInShuffledArray[i]);
			grid += memberIndex / columnSize + ",";
			grid += memberIndex % columnSize + ",";
			healthString += ((Member) (membersInShuffledArray[i])).getHealth() + ",";
		}

		// Removing comma at end and replacing it with a semi colon
		grid = grid.substring(0, grid.length() - 1);
		grid += ";";
		grid += healthString;
		grid = grid.substring(0, grid.length() - 1);
		grid += ";";
		max = numberOfMembers;
		min = 1;
		range = max - min + 1;
		grid += (int) (Math.random() * range) + min;

		return grid;

	}

	public static void main(String[] args) {
		solve(genGrid(), "", false);
		MIState newState = (MIState) applyOperator(initialState, Operator.LEFT);
		System.out.println("********************************************");
		for (int i = 0; i < gridRows; i++) {
			for (int j = 0; j < gridColumns; j++) {
				System.out.print(newState.getField()[i][j]);
			}
			System.out.println("");
		}
		System.out.println("***************************************");
		newState = (MIState) applyOperator(newState, Operator.CARRY);
		for (int i = 0; i < gridRows; i++) {
			for (int j = 0; j < gridColumns; j++) {
				System.out.print(newState.getField()[i][j]);
			}
			System.out.println("");
		}
	}

	@Override
	public boolean goalTest() {
		for (int i = 0; i < numberOfMembers; i++) {
			if (!isMemberSaved[i])
				return false;
		}
		return true;
	}

	@Override
	public void populateOperators() {
		operators = new Operator[] { Operator.UP, Operator.DOWN, Operator.RIGHT, Operator.LEFT, Operator.CARRY,
				Operator.DROP };
	}

	@Override
	public int pathCost(STNode node) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static State applyOperator(State state, Operator operator) {
		if (!isApplicable(state, operator)) {
			return null;
		}
		MIState currentState = (MIState) state;
		MIState nextState = new MIState();
		int[] tempHealth;
		Cell[][] tempField;
		int[] tempArr;
		HashSet<Integer> tempHash;
		switch (operator) {
		case UP:
			nextState.setMemberColumn(Arrays.copyOf(currentState.getMemberColumn(), numberOfMembers));
			nextState.setMemberRow(Arrays.copyOf(currentState.getMemberRow(), numberOfMembers));
			nextState.setIsMemberSaved(Arrays.copyOf(currentState.getIsMemberSaved(), numberOfMembers));
			tempHealth = Arrays.copyOf(currentState.getMemberHealth(), numberOfMembers);
			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (!currentState.getTruckMembers().contains(i) && !currentState.getIsMemberSaved()[i]
						&& tempHealth[i] <= 99) {
					if (tempHealth[i] == 99) {
						tempHealth[i] += 1;
						healthGained++;
					} else {
						tempHealth[i] += 2;
						healthGained += 2;
					}
				}
			}
			nextState.setMemberHealth(tempHealth);
			nextState.setMembersOnTruck(currentState.getMembersOnTruck());
			nextState.setTruckMembers((HashSet<Integer>) currentState.getTruckMembers().clone());
			nextState.setEthanRow(currentState.getEthanRow() - 1);
			nextState.setEthanColumn(currentState.getEthanColumn());
			// Can be optimized
			tempField = new Cell[gridRows][gridColumns];
			for (int i = 0; i < gridRows; i++) {
				for (int j = 0; j < gridColumns; j++) {
					tempField[i][j] = new Cell(i, j);
				}
			}
			for (int i = 0; i < numberOfMembers; i++) {
				if (nextState.getMemberRow()[i] == -1)
					continue;
				tempField[nextState.getMemberRow()[i]][nextState.getMemberColumn()[i]]
						.setOccupant(new Member(nextState.getMemberHealth()[i]));
			}
			tempField[submarineRow][submarineColumn].setOccupant(new Submarine());
			if (tempField[nextState.getEthanRow()][nextState.getEthanColumn()].getOccupant() != null) {
				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant2(new Ethan());
			} else {
				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant(new Ethan());
			}
			nextState.setField(tempField);

			break;
		case DOWN:
			nextState.setMemberColumn(Arrays.copyOf(currentState.getMemberColumn(), numberOfMembers));
			nextState.setMemberRow(Arrays.copyOf(currentState.getMemberRow(), numberOfMembers));
			nextState.setIsMemberSaved(Arrays.copyOf(currentState.getIsMemberSaved(), numberOfMembers));
			tempHealth = Arrays.copyOf(currentState.getMemberHealth(), numberOfMembers);
			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (!currentState.getTruckMembers().contains(i) && !currentState.getIsMemberSaved()[i]
						&& tempHealth[i] <= 99) {
					if (tempHealth[i] == 99) {
						tempHealth[i] += 1;
						healthGained++;
					} else {
						tempHealth[i] += 2;
						healthGained += 2;
					}
				}
			}
			nextState.setMemberHealth(tempHealth);
			nextState.setMembersOnTruck(currentState.getMembersOnTruck());
			nextState.setTruckMembers((HashSet<Integer>) currentState.getTruckMembers().clone());
			nextState.setEthanRow(currentState.getEthanRow() + 1);
			nextState.setEthanColumn(currentState.getEthanColumn());
			// Can be optimized
			tempField = new Cell[gridRows][gridColumns];
			for (int i = 0; i < gridRows; i++) {
				for (int j = 0; j < gridColumns; j++) {
					tempField[i][j] = new Cell(i, j);
				}
			}
			for (int i = 0; i < numberOfMembers; i++) {
				if (nextState.getMemberRow()[i] == -1)
					continue;
				tempField[nextState.getMemberRow()[i]][nextState.getMemberColumn()[i]]
						.setOccupant(new Member(nextState.getMemberHealth()[i]));
			}
			tempField[submarineRow][submarineColumn].setOccupant(new Submarine());
			if (tempField[nextState.getEthanRow()][nextState.getEthanColumn()].getOccupant() != null) {
				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant2(new Ethan());
			} else {
				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant(new Ethan());
			}
			nextState.setField(tempField);
			break;
		case LEFT:
			nextState.setMemberColumn(Arrays.copyOf(currentState.getMemberColumn(), numberOfMembers));
			nextState.setMemberRow(Arrays.copyOf(currentState.getMemberRow(), numberOfMembers));
			nextState.setIsMemberSaved(Arrays.copyOf(currentState.getIsMemberSaved(), numberOfMembers));
			tempHealth = Arrays.copyOf(currentState.getMemberHealth(), numberOfMembers);
			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (!currentState.getTruckMembers().contains(i) && !currentState.getIsMemberSaved()[i]
						&& tempHealth[i] <= 99) {
					if (tempHealth[i] == 99) {
						tempHealth[i] += 1;
						healthGained++;
					} else {
						tempHealth[i] += 2;
						healthGained += 2;
					}
				}
			}
			nextState.setMemberHealth(tempHealth);
			nextState.setMembersOnTruck(currentState.getMembersOnTruck());
			nextState.setTruckMembers((HashSet<Integer>) currentState.getTruckMembers().clone());
			nextState.setEthanRow(currentState.getEthanRow());
			nextState.setEthanColumn(currentState.getEthanColumn() - 1);
			// Can be optimized
			tempField = new Cell[gridRows][gridColumns];
			for (int i = 0; i < gridRows; i++) {
				for (int j = 0; j < gridColumns; j++) {
					tempField[i][j] = new Cell(i, j);
				}
			}
			for (int i = 0; i < numberOfMembers; i++) {
				if (nextState.getMemberRow()[i] == -1)
					continue;
				tempField[nextState.getMemberRow()[i]][nextState.getMemberColumn()[i]]
						.setOccupant(new Member(nextState.getMemberHealth()[i]));
			}
			tempField[submarineRow][submarineColumn].setOccupant(new Submarine());
			if (tempField[nextState.getEthanRow()][nextState.getEthanColumn()].getOccupant() != null) {
				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant2(new Ethan());
			} else {
				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant(new Ethan());
			}
			nextState.setField(tempField);
			break;
		case RIGHT:
			nextState.setMemberColumn(Arrays.copyOf(currentState.getMemberColumn(), numberOfMembers));
			nextState.setMemberRow(Arrays.copyOf(currentState.getMemberRow(), numberOfMembers));
			nextState.setIsMemberSaved(Arrays.copyOf(currentState.getIsMemberSaved(), numberOfMembers));
			tempHealth = Arrays.copyOf(currentState.getMemberHealth(), numberOfMembers);
			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (!currentState.getTruckMembers().contains(i) && !currentState.getIsMemberSaved()[i]
						&& tempHealth[i] <= 99) {
					if (tempHealth[i] == 99) {
						tempHealth[i] += 1;
						healthGained++;
					} else {
						tempHealth[i] += 2;
						healthGained += 2;
					}
				}
			}
			nextState.setMemberHealth(tempHealth);
			nextState.setMembersOnTruck(currentState.getMembersOnTruck());
			nextState.setTruckMembers((HashSet<Integer>) currentState.getTruckMembers().clone());
			nextState.setEthanRow(currentState.getEthanRow());
			nextState.setEthanColumn(currentState.getEthanColumn() + 1);
			// Can be optimized
			tempField = new Cell[gridRows][gridColumns];
			for (int i = 0; i < gridRows; i++) {
				for (int j = 0; j < gridColumns; j++) {
					tempField[i][j] = new Cell(i, j);
				}
			}
			for (int i = 0; i < numberOfMembers; i++) {
				if (nextState.getMemberRow()[i] == -1)
					continue;
				tempField[nextState.getMemberRow()[i]][nextState.getMemberColumn()[i]]
						.setOccupant(new Member(nextState.getMemberHealth()[i]));
			}
			tempField[submarineRow][submarineColumn].setOccupant(new Submarine());
			if (tempField[nextState.getEthanRow()][nextState.getEthanColumn()].getOccupant() != null) {
				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant2(new Ethan());
			} else {
				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant(new Ethan());
			}
			nextState.setField(tempField);
			break;
		case CARRY:
			nextState.setEthanRow(currentState.getEthanRow());
			nextState.setEthanColumn(currentState.getEthanColumn());
			tempArr = Arrays.copyOf(currentState.getMemberColumn(), numberOfMembers);
			tempArr[memberAtEthan] = -1;
			nextState.setMemberColumn(tempArr);
			tempArr = Arrays.copyOf(currentState.getMemberRow(), numberOfMembers);
			tempArr[memberAtEthan] = -1;
			nextState.setMemberRow(tempArr);
			nextState.setIsMemberSaved(Arrays.copyOf(currentState.getIsMemberSaved(), numberOfMembers));
			nextState.setMembersOnTruck(currentState.getMembersOnTruck() + 1);
			tempHash = (HashSet<Integer>) currentState.getTruckMembers().clone();
			tempHash.add(memberAtEthan);
			nextState.setTruckMembers(tempHash);
			tempHealth = Arrays.copyOf(currentState.getMemberHealth(), numberOfMembers);
			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (!nextState.getTruckMembers().contains(i) && !nextState.getIsMemberSaved()[i]
						&& tempHealth[i] <= 99) {
					if (tempHealth[i] == 99) {
						tempHealth[i] += 1;
						healthGained++;
					} else {
						tempHealth[i] += 2;
						healthGained += 2;
					}
				}
			}
			nextState.setMemberHealth(tempHealth);

			// Can be optimized
			tempField = new Cell[gridRows][gridColumns];
			for (int i = 0; i < gridRows; i++) {
				for (int j = 0; j < gridColumns; j++) {
					tempField[i][j] = new Cell(i, j);
				}
			}
			for (int i = 0; i < numberOfMembers; i++) {
				if (nextState.getMemberRow()[i] == -1)
					continue;
				tempField[nextState.getMemberRow()[i]][nextState.getMemberColumn()[i]]
						.setOccupant(new Member(nextState.getMemberHealth()[i]));
			}
			tempField[submarineRow][submarineColumn].setOccupant(new Submarine());
			if (tempField[nextState.getEthanRow()][nextState.getEthanColumn()].getOccupant() != null) {
				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant2(new Ethan());
			} else {
				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant(new Ethan());
			}
			nextState.setField(tempField);

			break;
		case DROP:
			nextState.setEthanRow(currentState.getEthanRow());
			nextState.setEthanColumn(currentState.getEthanColumn());
			tempArr = Arrays.copyOf(currentState.getMemberColumn(), numberOfMembers);
			nextState.setMemberColumn(tempArr);
			tempArr = Arrays.copyOf(currentState.getMemberRow(), numberOfMembers);
			nextState.setMemberRow(tempArr);
			tempHash = (HashSet<Integer>) currentState.getTruckMembers().clone();
			boolean[] tempBoolArr = Arrays.copyOf(currentState.getIsMemberSaved(), numberOfMembers);
			tempHash.forEach(index -> tempBoolArr[index] = true);
			tempHash.clear();
			nextState.setMembersOnTruck(0);
			nextState.setTruckMembers(tempHash);
			tempHealth = Arrays.copyOf(currentState.getMemberHealth(), numberOfMembers);
			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (!nextState.getTruckMembers().contains(i) && !nextState.getIsMemberSaved()[i]
						&& tempHealth[i] <= 99) {
					if (tempHealth[i] == 99) {
						tempHealth[i] += 1;
						healthGained++;
					} else {
						tempHealth[i] += 2;
						healthGained += 2;
					}
				}
			}
			nextState.setMemberHealth(tempHealth);

			// Can be optimized
			tempField = new Cell[gridRows][gridColumns];
			for (int i = 0; i < gridRows; i++) {
				for (int j = 0; j < gridColumns; j++) {
					tempField[i][j] = new Cell(i, j);
				}
			}
			for (int i = 0; i < numberOfMembers; i++) {
				if (nextState.getMemberRow()[i] == -1)
					continue;
				tempField[nextState.getMemberRow()[i]][nextState.getMemberColumn()[i]]
						.setOccupant(new Member(nextState.getMemberHealth()[i]));
			}
			tempField[submarineRow][submarineColumn].setOccupant(new Submarine());
			if (tempField[nextState.getEthanRow()][nextState.getEthanColumn()].getOccupant() != null) {
				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant2(new Ethan());
			} else {
				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant(new Ethan());
			}
			nextState.setField(tempField);
			;
			break;
		}
		return nextState;
	}

	public static boolean isApplicable(State state, Operator operator) {
		MIState currentState = (MIState) state;
		switch (operator) {
		case UP:
			if (currentState.getEthanRow() == 0)
				return false;
			break;
		case DOWN:
			if (currentState.getEthanRow() == gridRows - 1)
				return false;
			break;
		case LEFT:
			if (currentState.getEthanColumn() == 0)
				return false;
			break;
		case RIGHT:
			if (currentState.getEthanColumn() == gridColumns - 1)
				return false;
			break;
		case CARRY:
			
			memberAtEthan = findMember(currentState.getEthanRow(), currentState.getEthanColumn(),currentState);
			System.out.println(memberAtEthan);
			if (memberAtEthan == -1)
				return false;
			break;
		case DROP:
			if (!(currentState.getEthanColumn() == submarineColumn && currentState.getEthanRow() == submarineRow)
					|| currentState.getMembersOnTruck() == 0)
				return false;
			break;

		}
		return true;
	}
	
	public static int findMember(int x, int y, MIState currentState) { // gets index of member
		for (int i = 0; i < numberOfMembers; i++) {
			System.out.println("=========================");
			System.out.println(currentState.getIsMemberSaved()[i]);
			System.out.println(currentState.getMemberRow()[i]);
			System.out.println(currentState.getMemberColumn()[i]);
			System.out.println("Ethan");
			System.out.println(x);
			System.out.println(y);
			if (!currentState.getIsMemberSaved()[i] && x == currentState.getMemberRow()[i]
					&& y == currentState.getMemberColumn()[i]) {
				return i;
			}
		}
		return -1;
	}

}
