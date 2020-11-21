package code.mission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;

import code.generic.NodeCostComparator;
import code.generic.NodeIdComparator;
import code.generic.STNode;
import code.generic.SearchProblem;
import code.generic.State;

public class MissionImpossible extends SearchProblem {
	public MissionImpossible(State initialState, State[] stateSpace, PriorityQueue<STNode> queue,
			ArrayList<Operator> operators) {
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
	static int idSoFar;
	static String zeroString;

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
		for (int i = 0, c = 0; c < numberOfMembers; i += 2, c++) {
			memberRow[c] = Integer.parseInt(temp[i]);
			memberColumn[c] = Integer.parseInt(temp[i + 1]);

		}
		temp = splitted[4].split(",");
		for (int i = 0; i < numberOfMembers; i++) {
			memberHealth[i] = Integer.parseInt(temp[i]);
		}
		maximumCarry = Integer.parseInt(splitted[5]);
		zeroString="";
		for(int i=0;i<numberOfMembers;i++) {
			zeroString+="0";
		}
		State initialState = new MIState(ethanRow, ethanColumn, memberRow, memberColumn, memberHealth, zeroString,
				zeroString,0);
		PriorityQueue<STNode> queue = null;
		switch (strategy) {
		case "DF":
			queue = new PriorityQueue<STNode>(new NodeIdComparator());
			System.out.println("DFS");
			break;
		case "BF":
			queue = new PriorityQueue<STNode>();
			break;
		case "ID":
			queue = new PriorityQueue<STNode>(new NodeIdComparator());

			break;
		case "UC":
			queue = new PriorityQueue<STNode>(new NodeCostComparator());
			break;
		}
		STNode initialNode = new MINode(initialState, idSoFar++, 0, 0, 0, null, null, 0,getDead((MIState) initialState));
		queue.add(initialNode);
		MissionImpossible missionImpossible = new MissionImpossible(initialState, null, queue, null);
		missionImpossible.populateOperators();
		missionImpossible.visitedStates = new HashSet<String>();
		missionImpossible.visitedStates.add(((MIState) initialState).getStateForVisitedStates());
		STNode solution = missionImpossible.generalSearchProcedure(missionImpossible, strategy);
		if (solution == null) {
			System.out.println("Failure");
		} else {
			System.out.println(solution.getId());
			System.out.println(solution.getPlan());
			System.out.println(solution.getDepth());
			System.out.println(getDead((MIState) solution.getState()));
		}
		MIState miSolution=(MIState) solution.getState();
		String plan=solution.getPlan().toLowerCase();
		return plan.substring(0,plan.length()-1)+";"+getDead(miSolution)+";"+Arrays.toString(miSolution.getMemberHealth()).replaceAll("[\\[\\]\\s]", "")+";"+expandedNodes;
	}

	public static String genGrid() {
		int max = 15;
		int min = 5;
		int range = max - min + 1;
		String grid = "";
		int rowSize = (int) (Math.random() * range) + min;
		int columnSize = (int) (Math.random() * range) + min;
//		 rowSize = 15;
//		 columnSize = 15;
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
//		numberOfMembers=7;
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
//			healthString+=50+",";
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
//		grid += 6;

		return grid;

	}

	public static void main(String[] args) {
		String grid5 = "5,5;2,1;1,0;1,3,4,2,4,1,3,1;54,31,39,98;2";
		String grid14 = "14,14;13,9;1,13;5,3,9,7,11,10,8,3,10,7,13,6,11,1,5,2;76,30,2,49,63,43,72,1;6";
		String grid10 = "10,10;6,3;4,8;9,1,2,4,4,0,3,9,6,4,3,4,0,5,1,6,1,9;97,49,25,17,94,3,96,35,98;3";

//		solve(genGrid(), "UCS", false);
		System.out.println(solve(grid10, "BF", false));
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
			if (miState.getIsMemberSaved().charAt(i) == '0')
				return false;
		}
		return true;
	}

	@Override
	public void populateOperators() {
		operators = new ArrayList<Operator>();
		operators.add(Operator.UP);
		operators.add(Operator.DOWN);
		operators.add(Operator.RIGHT);
		operators.add(Operator.LEFT);
		operators.add(Operator.CARRY);
		operators.add(Operator.DROP);
	}

	@Override
	public int pathCost(STNode node) {
		// TODO Auto-generated method stub
		return 0;
	}

	public STNode applyOperator(STNode node, Operator operator) {
		State state = node.getState();
		if (!isApplicable(state, operator)) {
//			System.out.println("Not appl");
			return null;
		}
//		System.out.println(operator);
		MIState currentState = (MIState) state;
		int[] tempHealth = null;
		String tempIsMemberSaved = null;
		int[] tempMemberRow = null;
		int[] tempMemberColumn = null;
		int tempMembersOnTruck=-1;
//		Cell[][] tempField=null;
//		int[] tempArr;
		int tempEthanRow = -1;
		int tempEthanColumn = -1;
		String tempTruckMembers = null;
		switch (operator) {
		case UP:
			tempMemberColumn = currentState.getMemberColumn();
			tempMemberRow = currentState.getMemberRow();
			tempIsMemberSaved = currentState.getIsMemberSaved();
			tempMembersOnTruck=currentState.getMembersOnTruck();
			tempHealth = Arrays.copyOf(currentState.getMemberHealth(), numberOfMembers);
			healthGained = 0;
			
			for (int i = 0; i < numberOfMembers; i++) {
				
				if (currentState.getTruckMembers().charAt(i)=='0' && currentState.getIsMemberSaved().charAt(i) == '0'
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
			tempTruckMembers = currentState.getTruckMembers();
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
			tempMembersOnTruck=currentState.getMembersOnTruck();
			tempMemberColumn = currentState.getMemberColumn();;
			tempMemberRow =currentState.getMemberRow();;
			tempIsMemberSaved = currentState.getIsMemberSaved();
			tempHealth = currentState.getMemberHealth();
			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (currentState.getTruckMembers().charAt(i)=='0' && currentState.getIsMemberSaved().charAt(i) == '0'
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
			tempTruckMembers = currentState.getTruckMembers();
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
			tempMembersOnTruck=currentState.getMembersOnTruck();
			tempMemberColumn =currentState.getMemberColumn();
			tempMemberRow = currentState.getMemberRow();
			tempIsMemberSaved = currentState.getIsMemberSaved();
			tempHealth = currentState.getMemberHealth();
			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (currentState.getTruckMembers().charAt(i)=='0' && currentState.getIsMemberSaved().charAt(i) == '0'
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
			tempTruckMembers = currentState.getTruckMembers();
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
			tempMembersOnTruck=currentState.getMembersOnTruck();
			tempMemberColumn = currentState.getMemberColumn();
			tempMemberRow = currentState.getMemberRow();
			tempIsMemberSaved =currentState.getIsMemberSaved();
			tempHealth = currentState.getMemberHealth();
			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (currentState.getTruckMembers().charAt(i)=='0' && currentState.getIsMemberSaved().charAt(i) == '0'
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
			tempTruckMembers = currentState.getTruckMembers();
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
			tempMembersOnTruck=currentState.getMembersOnTruck()+1;
			tempMemberColumn = currentState.getMemberColumn();
			tempMemberRow = currentState.getMemberRow();
			tempIsMemberSaved = currentState.getIsMemberSaved();
			tempTruckMembers = currentState.getTruckMembers();
			tempEthanRow = (currentState.getEthanRow());
			tempEthanColumn = (currentState.getEthanColumn());
			tempMemberColumn[memberAtEthan] = -1;
			tempMemberRow[memberAtEthan] = -1;
//			System.out.println("Carrying");
//			System.out.println(Arrays.toString(tempMemberRow));
//			System.out.println(Arrays.toString(tempMemberColumn));
			tempTruckMembers=tempTruckMembers.substring(0,memberAtEthan)+'1'+tempTruckMembers.substring(memberAtEthan+1);
//			System.out.println(tempTruckMembers);
			tempHealth = Arrays.copyOf(currentState.getMemberHealth(), numberOfMembers);
			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (currentState.getTruckMembers().charAt(i)=='0' && currentState.getIsMemberSaved().charAt(i) == '0'
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
			tempMembersOnTruck=0;
			tempMemberColumn = currentState.getMemberColumn();
			tempMemberRow = currentState.getMemberRow();
			tempIsMemberSaved = currentState.getIsMemberSaved();
			tempTruckMembers = currentState.getTruckMembers();
//			System.out.println("**********************");
//			System.out.println(Arrays.toString(tempIsMemberSaved));
//			System.out.println(tempTruckMembers);
//			System.out.println(tempTruckMembers.size());
			
//			System.out.println(Arrays.toString(tempIsMemberSaved));
			tempIsMemberSaved=binaryOr(tempIsMemberSaved, tempTruckMembers);
			tempTruckMembers=zeroString;
			tempEthanRow = (currentState.getEthanRow());
			tempEthanColumn = (currentState.getEthanColumn());
			tempHealth = Arrays.copyOf(currentState.getMemberHealth(), numberOfMembers);
			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (currentState.getTruckMembers().charAt(i)=='0' && currentState.getIsMemberSaved().charAt(i) == '0'
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
				tempIsMemberSaved, tempTruckMembers,tempMembersOnTruck);
		if(((MIState) node.getState()).getStateForVisitedStates().equals(nextState.getStateForVisitedStates())) {
			return null;
		}
		if (visitedStates.contains(nextState.getStateForVisitedStates())) {
//			System.out.println(nextState.getState());
			return null;
		} else {
			visitedStates.add(nextState.getStateForVisitedStates());
		}
//		System.out.println("Making a node");
		return new MINode(nextState, idSoFar++, node.getCost() + healthGained, healthGained, 0, node, operator,
				node.getDepth() + 1,getDead(nextState));
	}

	public static boolean isApplicable(State state, Operator operator) {
		MIState currentState = (MIState) state;
//		System.out.println(currentState.getState());
//		System.out.println(operator);
		switch (operator) {
		case UP:
			if (currentState.getEthanRow() == 0)
				return false;
			else
				return true;
			
		case DOWN:
			if (currentState.getEthanRow() == gridRows - 1)
				return false;
			else
				return true;
		case LEFT:
			if (currentState.getEthanColumn() == 0) 
				return false;
			else
				return true;
		case RIGHT:
//			System.out.println("I am going right");
			if (currentState.getEthanColumn() == gridColumns - 1)
				return false;
			else {
//				System.out.println("returning true");
				return true
						;}
		case CARRY:
			if (currentState.getMembersOnTruck() == maximumCarry) {
				return false;
			}
			else {
				memberAtEthan = findMember(currentState.getEthanRow(), currentState.getEthanColumn(), currentState);
				if (memberAtEthan == -1)
					return false;
				else
					return true;
			}
			
			
		
		case DROP:
			if (!(currentState.getEthanColumn() == submarineColumn && currentState.getEthanRow() == submarineRow)
					|| currentState.getMembersOnTruck() == 0)
				return false;
			else
				return true;
			
		default:return true;

		}
	}

	public static int findMember(int x, int y, MIState currentState) { // gets index of member
		for (int i = 0; i < numberOfMembers; i++) {
			if (currentState.getIsMemberSaved().charAt(i) == '0' && x == currentState.getMemberRow()[i]
					&& y == currentState.getMemberColumn()[i]) {
				return i;
			}
		}
		return -1;
	}

public static String binaryOr(String s1,String s2) {
	String s3="";
	for(int i=0;i<numberOfMembers;i++) {
		if(s1.charAt(i)=='1' ||s2.charAt(i)=='1') {
			s3+="1";
		}
		else {
			s3+="0";
		}
	}
	return s3;
}
	public static int getDead(MIState state) {
		int counter = 0;
		int[] allMemHealth = state.getMemberHealth();
		for (int i = 0; i < allMemHealth.length; i++) {
			if (allMemHealth[i] == 100) {
				counter++;
			}
		}
		return counter;
	}
}
