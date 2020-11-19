package code.mission;

import java.util.Arrays;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;

import code.generic.NodeIdComparator;
import code.generic.STNode;
import code.generic.SearchProblem;
import code.generic.State;

public class MissionImpossible extends SearchProblem {
	public MissionImpossible(State initialState, State[] stateSpace, PriorityQueue<STNode> queue,
			Operator[] operators) {
		super(initialState, stateSpace, queue, operators);
		// TODO Auto-generated constructor stub
	}

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
	static int[] isMemberSaved; // Saved means they are on submarine
	static int maximumCarry;
	static int numberOfMembers;
	static int membersOnTruck;
	static ArrayList<Integer> truckMembers;
	static int healthGained;
	static int memberAtEthan;
	static int expandedNodes;
	static int idSoFar;

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
		isMemberSaved = new int[numberOfMembers];
		for (int i = 0, c = 0; c < numberOfMembers; i += 2, c++) {
			memberRow[c] = Integer.parseInt(temp[i]);
			memberColumn[c] = Integer.parseInt(temp[i + 1]);

		}
		temp = splitted[4].split(",");
		for (int i = 0; i < numberOfMembers; i++) {
			memberHealth[i] = Integer.parseInt(temp[i]);
		}
		maximumCarry = Integer.parseInt(splitted[5]);

//		field = new Cell[gridRows][gridColumns];
//		for (int i = 0; i < gridRows; i++) {
//			for (int j = 0; j < gridColumns; j++) {
//				field[i][j] = new Cell(1, j);
//			}
//		}
//
//		for (int i = 0; i < numberOfMembers; i++) {
//			field[memberRow[i]][memberColumn[i]]
//					.setOccupant(arrayToBeShuffled.get((memberRow[i] * gridColumns) + memberColumn[i]));
//		}
//		field[ethanRow][ethanColumn].setOccupant(arrayToBeShuffled.get(ethanRow * gridColumns + ethanColumn));
//		field[submarineRow][submarineColumn]
//				.setOccupant(arrayToBeShuffled.get(submarineRow * gridColumns + submarineColumn));
		truckMembers = new ArrayList<Integer>();
//		for (int i = 0; i < gridRows; i++) {
//			for (int j = 0; j < gridColumns; j++) {
//				System.out.print(field[i][j]);
//			}
//			System.out.println("");
//		}
		State initialState = new MIState(ethanRow, ethanColumn, memberRow, memberColumn, memberHealth, isMemberSaved,
				truckMembers);
		PriorityQueue<STNode> queue = null;
		switch (strategy) {
		case "DF":
			queue = new PriorityQueue<STNode>(new NodeIdComparator());
			System.out.println("DFS");
			break;
		case "BF":
			queue = new PriorityQueue<STNode>();
		case "IDF":
			queue = new PriorityQueue<STNode>(new NodeIdComparator());

			break;
		}
		STNode initialNode = new MINode(initialState, idSoFar++, 0, 0, 0, null, null, 0);
		queue.add(initialNode);
		MissionImpossible missionImpossible = new MissionImpossible(initialState, null, queue, null);
		missionImpossible.populateOperators();
		missionImpossible.visitedStates = new HashSet<String>();
		missionImpossible.visitedStates.add(initialState.getState());
		STNode solution = missionImpossible.generalSearchProcedure(missionImpossible, "DF");
		if (solution == null) {
			System.out.println("Failure");
		} else
			System.out.println(solution.getId());

		return null;
	}

	public static String genGrid() {
		int max = 15;
		int min = 5;
		int range = max - min + 1;
		String grid = "";
		int rowSize = (int) (Math.random() * range) + min;
		int columnSize = (int) (Math.random() * range) + min;
//		int rowSize = 15;
//		int columnSize = 15;
		grid += rowSize + ",";
		grid += columnSize + ";";
		System.out.println(grid);
		arrayToBeShuffled = new ArrayList<Occupant>(rowSize * columnSize);
		Ethan ethan = new Ethan();
		arrayToBeShuffled.add(ethan);
		Submarine submarine = new Submarine();
		arrayToBeShuffled.add(submarine);
		max = 10; // edited
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
		grid += (int) (Math.random() * range) + min; // maxXarry
//		grid += 10;

		return grid;

	}

	public static void main(String[] args) {
		solve(genGrid(), "IDF", false);
	}
//	public static void main(String[] args) {
//		solve(genGrid(), "", false);
//		MIState newState = (MIState) applyOperator(initialState, Operator.LEFT);
//		System.out.println("********************************************");
//		for (int i = 0; i < gridRows; i++) {
//			for (int j = 0; j < gridColumns; j++) {
//				System.out.print(newState.getField()[i][j]);
//			}
//			System.out.println("");
//		}
//		System.out.println("***************************************");
//		newState = (MIState) applyOperator(newState, Operator.CARRY);
//		for (int i = 0; i < gridRows; i++) {
//			for (int j = 0; j < gridColumns; j++) {
//				System.out.print(newState.getField()[i][j]);
//			}
//			System.out.println("");
//		}
//	}

	@Override
	public boolean goalTest(STNode node) {
		expandedNodes++;
//		System.out.println(node.getId());
		MINode miNode = (MINode) node;
		MIState miState = (MIState) miNode.getState();
//		System.out.println("*************************************************************");
//		System.out.println(expandedNodes);
//		for (int i = 0; i < numberOfMembers; i++) {
//			System.out.println(miState.getIsMemberSaved()[i]);
//		}
//		System.out.println("*************************************************************");
//		for (int i = 0; i < gridRows; i++) {
//			for (int j = 0; j < gridColumns; j++) {
//				System.out.print(miState.getField()[i][j]);
//			}
//			System.out.println("");
//		}
		for (int i = 0; i < numberOfMembers; i++) {
			if (miState.getIsMemberSaved()[i] == 0)
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

	public STNode applyOperator(STNode node, Operator operator) {
		State state = node.getState();
		if (!isApplicable(state, operator)) {
			return null;
		}
//		System.out.println(operator);
		MIState currentState = (MIState) state;
		int[] tempHealth = null;
		int[] tempIsMemberSaved = null;
		int[] tempMemberRow = null;
		int[] tempMemberColumn = null;
//		Cell[][] tempField=null;
//		int[] tempArr;
		int tempEthanRow = -1;
		int tempEthanColumn = -1;
		ArrayList<Integer> tempTruckMembers = null;
		switch (operator) {
		case UP:
			tempMemberColumn = Arrays.copyOf(currentState.getMemberColumn(), numberOfMembers);
			tempMemberRow = (Arrays.copyOf(currentState.getMemberRow(), numberOfMembers));
			tempIsMemberSaved = (Arrays.copyOf(currentState.getIsMemberSaved(), numberOfMembers));
			tempHealth = Arrays.copyOf(currentState.getMemberHealth(), numberOfMembers);
			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (!currentState.getTruckMembers().contains(i) && currentState.getIsMemberSaved()[i] == 0
						&& tempHealth[i] <= 99) {
					if (tempHealth[i] == 99) {
						tempHealth[i] += 1;
						healthGained++;
					} else {
						tempHealth[i] += 2;
						healthGained += 2;
					}
				} else {
					if (tempHealth[i] >= 100) {
//						System.out.println("Dead");
					} else {
//					System.out.println("Saved");	
					}

				}
			}
			tempTruckMembers = ((ArrayList<Integer>) (currentState.getTruckMembers().clone()));
			tempEthanRow = (currentState.getEthanRow() - 1);
			tempEthanColumn = (currentState.getEthanColumn());
			// Can be optimized
//			tempField = new Cell[gridRows][gridColumns];
//			for (int i = 0; i < gridRows; i++) {
//				for (int j = 0; j < gridColumns; j++) {
//					tempField[i][j] = new Cell(i, j);
//				}
//			}
//			for (int i = 0; i < numberOfMembers; i++) {
//				if (nextState.getMemberRow()[i] == -1)
//					continue;
//				tempField[nextState.getMemberRow()[i]][nextState.getMemberColumn()[i]]
//						.setOccupant(new Member(nextState.getMemberHealth()[i]));
//			}
//			tempField[submarineRow][submarineColumn].setOccupant(new Submarine());
//			if (tempField[nextState.getEthanRow()][nextState.getEthanColumn()].getOccupant() != null) {
//				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant2(new Ethan());
//			} else {
//				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant(new Ethan());
//			}
//			nextState.setField(tempField);

			break;
		case DOWN:
			tempMemberColumn = Arrays.copyOf(currentState.getMemberColumn(), numberOfMembers);
			tempMemberRow = (Arrays.copyOf(currentState.getMemberRow(), numberOfMembers));
			tempIsMemberSaved = (Arrays.copyOf(currentState.getIsMemberSaved(), numberOfMembers));
			tempHealth = Arrays.copyOf(currentState.getMemberHealth(), numberOfMembers);
			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (!currentState.getTruckMembers().contains(i) && currentState.getIsMemberSaved()[i] == 0
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
			tempTruckMembers = (ArrayList<Integer>) (currentState.getTruckMembers().clone());
			tempEthanRow = (currentState.getEthanRow() + 1);
			tempEthanColumn = (currentState.getEthanColumn());
			// Can be optimized
//			tempField = new Cell[gridRows][gridColumns];
//			for (int i = 0; i < gridRows; i++) {
//				for (int j = 0; j < gridColumns; j++) {
//					tempField[i][j] = new Cell(i, j);
//				}
//			}
//			for (int i = 0; i < numberOfMembers; i++) {
//				if (nextState.getMemberRow()[i] == -1)
//					continue;
//				tempField[nextState.getMemberRow()[i]][nextState.getMemberColumn()[i]]
//						.setOccupant(new Member(nextState.getMemberHealth()[i]));
//			}
//			tempField[submarineRow][submarineColumn].setOccupant(new Submarine());
//			if (tempField[nextState.getEthanRow()][nextState.getEthanColumn()].getOccupant() != null) {
//				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant2(new Ethan());
//			} else {
//				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant(new Ethan());
//			}
//			nextState.setField(tempField);
			break;
		case LEFT:
			tempMemberColumn = Arrays.copyOf(currentState.getMemberColumn(), numberOfMembers);
			tempMemberRow = (Arrays.copyOf(currentState.getMemberRow(), numberOfMembers));
			tempIsMemberSaved = (Arrays.copyOf(currentState.getIsMemberSaved(), numberOfMembers));
			tempHealth = Arrays.copyOf(currentState.getMemberHealth(), numberOfMembers);
			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (!currentState.getTruckMembers().contains(i) && currentState.getIsMemberSaved()[i] == 0
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
			tempTruckMembers = (ArrayList<Integer>) (currentState.getTruckMembers().clone());
			tempEthanRow = (currentState.getEthanRow());
			tempEthanColumn = (currentState.getEthanColumn() - 1);
			// Can be optimized
//			tempField = new Cell[gridRows][gridColumns];
//			for (int i = 0; i < gridRows; i++) {
//				for (int j = 0; j < gridColumns; j++) {
//					tempField[i][j] = new Cell(i, j);
//				}
//			}
//			for (int i = 0; i < numberOfMembers; i++) {
//				if (nextState.getMemberRow()[i] == -1)
//					continue;
//				tempField[nextState.getMemberRow()[i]][nextState.getMemberColumn()[i]]
//						.setOccupant(new Member(nextState.getMemberHealth()[i]));
//			}
//			tempField[submarineRow][submarineColumn].setOccupant(new Submarine());
//			if (tempField[nextState.getEthanRow()][nextState.getEthanColumn()].getOccupant() != null) {
//				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant2(new Ethan());
//			} else {
//				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant(new Ethan());
//			}
//			nextState.setField(tempField);
			break;
		case RIGHT:
			tempMemberColumn = Arrays.copyOf(currentState.getMemberColumn(), numberOfMembers);
			tempMemberRow = (Arrays.copyOf(currentState.getMemberRow(), numberOfMembers));
			tempIsMemberSaved = (Arrays.copyOf(currentState.getIsMemberSaved(), numberOfMembers));
			tempHealth = Arrays.copyOf(currentState.getMemberHealth(), numberOfMembers);
			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (!currentState.getTruckMembers().contains(i) && currentState.getIsMemberSaved()[i] == 0
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
			tempTruckMembers = (ArrayList<Integer>) (currentState.getTruckMembers().clone());
			tempEthanRow = (currentState.getEthanRow());
			tempEthanColumn = (currentState.getEthanColumn() + 1);
			// Can be optimized
//			tempField = new Cell[gridRows][gridColumns];
//			for (int i = 0; i < gridRows; i++) {
//				for (int j = 0; j < gridColumns; j++) {
//					tempField[i][j] = new Cell(i, j);
//				}
//			}
//			for (int i = 0; i < numberOfMembers; i++) {
//				if (nextState.getMemberRow()[i] == -1)
//					continue;
//				tempField[nextState.getMemberRow()[i]][nextState.getMemberColumn()[i]]
//						.setOccupant(new Member(nextState.getMemberHealth()[i]));
//			}
//			tempField[submarineRow][submarineColumn].setOccupant(new Submarine());
//			if (tempField[nextState.getEthanRow()][nextState.getEthanColumn()].getOccupant() != null) {
//				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant2(new Ethan());
//			} else {
//				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant(new Ethan());
//			}
//			nextState.setField(tempField);
			break;
		case CARRY:
			tempMemberColumn = Arrays.copyOf(currentState.getMemberColumn(), numberOfMembers);
			tempMemberRow = (Arrays.copyOf(currentState.getMemberRow(), numberOfMembers));
			tempIsMemberSaved = (Arrays.copyOf(currentState.getIsMemberSaved(), numberOfMembers));
			tempTruckMembers = (ArrayList<Integer>) (currentState.getTruckMembers().clone());
			tempEthanRow = (currentState.getEthanRow());
			tempEthanColumn = (currentState.getEthanColumn());
			tempMemberColumn[memberAtEthan] = -1;
			tempMemberRow[memberAtEthan] = -1;
//			System.out.println("Carrying");
//			System.out.println(Arrays.toString(tempMemberRow));
//			System.out.println(Arrays.toString(tempMemberColumn));
			tempTruckMembers.add(memberAtEthan);
//			System.out.println(tempTruckMembers);
			Collections.sort(tempTruckMembers);
			tempHealth = Arrays.copyOf(currentState.getMemberHealth(), numberOfMembers);
			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (!currentState.getTruckMembers().contains(i) && currentState.getIsMemberSaved()[i] == 0
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
			memberAtEthan = -1;
			// Can be optimized
//			tempField = new Cell[gridRows][gridColumns];
//			for (int i = 0; i < gridRows; i++) {
//				for (int j = 0; j < gridColumns; j++) {
//					tempField[i][j] = new Cell(i, j);
//				}
//			}
//			for (int i = 0; i < numberOfMembers; i++) {
//				if (nextState.getMemberRow()[i] == -1)
//					continue;
//				tempField[nextState.getMemberRow()[i]][nextState.getMemberColumn()[i]]
//						.setOccupant(new Member(nextState.getMemberHealth()[i]));
//			}
//			tempField[submarineRow][submarineColumn].setOccupant(new Submarine());
//			if (tempField[nextState.getEthanRow()][nextState.getEthanColumn()].getOccupant() != null) {
//				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant2(new Ethan());
//			} else {
//				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant(new Ethan());
//			}
//			nextState.setField(tempField);
			break;
		case DROP:
			tempMemberColumn = Arrays.copyOf(currentState.getMemberColumn(), numberOfMembers);
			tempMemberRow = (Arrays.copyOf(currentState.getMemberRow(), numberOfMembers));
			tempIsMemberSaved = (Arrays.copyOf(currentState.getIsMemberSaved(), numberOfMembers));
			tempTruckMembers = (ArrayList<Integer>) (currentState.getTruckMembers().clone());
			System.out.println("**********************");
			System.out.println(Arrays.toString(tempIsMemberSaved));
			System.out.println(tempTruckMembers);
//			System.out.println(tempTruckMembers.size());
			for (int i = 0; i < tempTruckMembers.size(); i++) {
				tempIsMemberSaved[tempTruckMembers.get(i)] = 1;
			}
			System.out.println(Arrays.toString(tempIsMemberSaved));
			tempTruckMembers.clear();
			tempEthanRow = (currentState.getEthanRow());
			tempEthanColumn = (currentState.getEthanColumn());
			tempHealth = Arrays.copyOf(currentState.getMemberHealth(), numberOfMembers);
			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (!currentState.getTruckMembers().contains(i) && currentState.getIsMemberSaved()[i] == 0
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
			memberAtEthan = -1;
			// Can be optimized
//			tempField = new Cell[gridRows][gridColumns];
//			for (int i = 0; i < gridRows; i++) {
//				for (int j = 0; j < gridColumns; j++) {
//					tempField[i][j] = new Cell(i, j);
//				}
//			}
//			for (int i = 0; i < numberOfMembers; i++) {
//				if (nextState.getMemberRow()[i] == -1)
//					continue;
//				tempField[nextState.getMemberRow()[i]][nextState.getMemberColumn()[i]]
//						.setOccupant(new Member(nextState.getMemberHealth()[i]));
//			}
//			tempField[submarineRow][submarineColumn].setOccupant(new Submarine());
//			if (tempField[nextState.getEthanRow()][nextState.getEthanColumn()].getOccupant() != null) {
//				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant2(new Ethan());
//			} else {
//				tempField[nextState.getEthanRow()][nextState.getEthanColumn()].setOccupant(new Ethan());
//			}
//			nextState.setField(tempField);
			break;
		}
		MIState nextState = new MIState(tempEthanRow, tempEthanColumn, tempMemberRow, tempMemberColumn, tempHealth,
				tempIsMemberSaved, tempTruckMembers);
		if (visitedStates.contains(nextState.getState())) {
//			System.out.println(nextState.getState());
			return null;
		} else {
			visitedStates.add(nextState.getState());
		}

		return new MINode(nextState, idSoFar++, node.getCost() + healthGained, healthGained, 0, node, operator,
				node.getDepth() + 1);
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
			if (currentState.getMembersOnTruck() == maximumCarry) {
				return false;
			}
			memberAtEthan = findMember(currentState.getEthanRow(), currentState.getEthanColumn(), currentState);
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
			if (currentState.getIsMemberSaved()[i] == 0 && x == currentState.getMemberRow()[i]
					&& y == currentState.getMemberColumn()[i]) {
				return i;
			}
		}
		return -1;
	}

}
