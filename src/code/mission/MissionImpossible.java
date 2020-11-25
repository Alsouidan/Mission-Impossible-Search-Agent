package code.mission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;

import code.generic.NodeAStarComparator;
import code.generic.NodeCostComparator;
import code.generic.NodeHeuristicComparator;
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
	static boolean includeHeuristic;
	static boolean heuristicType;

	public static String solve(String grid, String strategy, boolean visualize) {

//		****************************************************()	
		expandedNodes = 0;
		includeHeuristic = false;
		heuristicType = false; // Heuristic One is true
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
		zeroString = "";
		for (int i = 0; i < numberOfMembers; i++) {
			zeroString += "0";
		}
		// Making the initial state from the variables acquired from the grid from the
		// steps above
		State initialState = new MIState(ethanRow, ethanColumn, memberRow, memberColumn, memberHealth, zeroString,
				zeroString, 0);
		PriorityQueue<STNode> queue = null;
		switch (strategy) {
		case "DF":
			// Using the ID comparator , higher priority for greater values of ID
			queue = new PriorityQueue<STNode>(new NodeIdComparator());
			break;
		case "BF":
			// No comparator used as to use the priority queue as a regular queue
			queue = new PriorityQueue<STNode>();
			break;
		case "ID":
			// Using same comparator as DF
			queue = new PriorityQueue<STNode>(new NodeIdComparator());

			break;
		case "UC":
			// The comparator gives a higher priority for less costs
			queue = new PriorityQueue<STNode>(new NodeCostComparator());
			break;
		case "GR1":
			heuristicType = true;

		case "GR2":
			includeHeuristic = true;
			// comparator gives higher priority for less heuristic
			queue = new PriorityQueue<STNode>(new NodeHeuristicComparator());
			break;
		case "AS1":
			heuristicType = true;
		case "AS2":
			includeHeuristic = true;
			// Comparator gives higher priority for less cost+heuristic
			queue = new PriorityQueue<STNode>(new NodeAStarComparator());
			break;

		}
		int[] heuristicCost = null;
		if (includeHeuristic) {
			if (heuristicType) {
				heuristicCost = calculateHeuristicOne(initialState);
			} else {
				heuristicCost = calculateHeuristicTwo(initialState);
			}
		}
		int totalHealths = 0;
		for (int i = 0; i < memberHealth.length; i++) {
			totalHealths += memberHealth[i];
		}
//		Initialize the initlaNode with the initlastate
		STNode initialNode = new MINode(initialState, idSoFar++, totalHealths, 0, heuristicCost, null, null, 0,
				getDead((MIState) initialState));
//		Add initialNode in queue
		queue.add(initialNode);
		MissionImpossible missionImpossible = new MissionImpossible(initialState, null, queue, null);
		missionImpossible.populateOperators();
		missionImpossible.visitedStates = new HashSet<String>();
//		add initial state in visited states
		missionImpossible.visitedStates.add(((MIState) initialState).getStateForVisitedStates());
//		Call the generalSearchProblem to return the solution Node
		STNode solution = missionImpossible.generalSearchProcedure(missionImpossible, strategy);
		if (solution == null) {
			System.out.println("Failure");
		} else {
//			System.out.println(solution.getId());
//			System.out.println(solution.getPlan());
//			System.out.println(solution.getDepth());
			MIState mi = (MIState) solution.getState();
			System.out.println("Deaths:");
			System.out.println(getDead((MIState) solution.getState()));
			System.out.println("Damage");
			System.out.println(solution.getPathCost());
			System.out.println("Expanded nodes");
			System.out.println(expandedNodes);
		}
		MIState miSolution = (MIState) solution.getState();
		STNode currNode = solution;
		String visuals = "";
		if (visualize) {
			printFlow(solution);
		}
//		get plan and return the output string
		String plan = solution.getPlan().toLowerCase();
		return plan.substring(0, plan.length() - 1) + ";" + getDead(miSolution) + ";"
				+ Arrays.toString(miSolution.getMemberHealth()).replaceAll("[\\[\\]\\s]", "") + ";" + expandedNodes;
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
//		Make ArrayList so we would add the occupants adjacent in it then shuffle to get a random grid
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
//		Pad the arraylist with null values to be the same size as the grid flattened
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
		String grid6 = "6,6;1,1;3,3;3,5,0,1,2,4,4,3,1,5;4,43,94,40,92;3";
		String grid7 = "7,7;1,6;5,4;2,2,1,4,0,3,2,3,0,1,4,5;6,44,82,49,24,54;4";
		String grid8 = "8,8;4,2;7,4;5,1,7,7,4,0,6,7;93,85,72,78;1";
		String grid9 = "9,9;8,7;5,0;0,8,2,6,5,6,1,7,5,5,8,3,2,2,2,5,0,7;11,13,75,50,56,44,26,77,18;2";
		String grid10 = "10,10;6,3;4,8;9,1,2,4,4,0,3,9,6,4,3,4,0,5,1,6,1,9;97,49,25,17,94,3,96,35,98;3";
		String grid11 = "11,11;7,7;8,8;9,7,7,4,7,6,9,6,9,5,9,1,4,5,3,10,5,10;14,3,96,89,61,22,17,70,83;5";
		String grid12 = "12,12;7,7;10,6;0,4,2,2,1,3,8,2,4,2,9,3;95,4,68,2,94,91;5";
		String grid13 = "13,13;7,4;4,0;9,3,3,9,12,7,7,9,3,12,11,8,4,2,12,6;22,62,74,56,43,70,17,14;4";
		String grid14 = "14,14;13,9;1,13;5,3,9,7,11,10,8,3,10,7,13,6,11,1,5,2;76,30,2,49,63,43,72,1;6";
		String grid15 = "15,15;5,10;14,14;0,0,0,1,0,2,0,3,0,4,0,5,0,6,0,7,0,8;81,13,40,38,52,63,66,36,13;1";

		System.out.println("UCS 5");
//		System.out.println(solve(grid5, "UC", false));

//		System.out.println("UCS 14");
//		System.out.println();
		System.out.println(solve(grid5, "UC", false));
		System.out.println(solve(grid5, "AS2", false));
		System.out.println(solve(grid5, "GR2", false));
//		System.out.println("UCS 10");
//		System.out.println(solve(grid10,"UC",false));
		System.out.println("A Star");
//		System.out.println(solve(grid5, "A1", false));
		System.out.println("Greedy");
//		System.out.println(solve(grid5, "G1", false));
	}

	@Override
	public boolean goalTest(STNode node) {
		expandedNodes++;
		MINode miNode = (MINode) node;
		MIState miState = (MIState) miNode.getState();
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
//		Check if the operator can be applied on the state
		if (!isApplicable(state, operator)) {
			return null; // Return null if not applicable
		}
//		initilaize temp variables to store the state variables in for computations
		MIState currentState = (MIState) state;
		int[] tempHealth = null;
		String tempIsMemberSaved = null;
		int[] tempMemberRow = null;
		int[] tempMemberColumn = null;
		int tempMembersOnTruck = -1;
		int tempEthanRow = -1;
		int tempEthanColumn = -1;
		String tempTruckMembers = null;
		switch (operator) {
		case UP:
//			Get relevant state variables from state
			tempMemberColumn = currentState.getMemberColumn();
			tempMemberRow = currentState.getMemberRow();
			tempIsMemberSaved = currentState.getIsMemberSaved();
			tempMembersOnTruck = currentState.getMembersOnTruck();
			tempHealth = currentState.getMemberHealth();
			tempTruckMembers = currentState.getTruckMembers();

			healthGained = 0;
//			loop to inflict damage on member's healths 
			for (int i = 0; i < numberOfMembers; i++) {

				if (tempTruckMembers.charAt(i) == '0' && tempIsMemberSaved.charAt(i) == '0' && tempHealth[i] <= 99) {
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
//			move Ethan 
			tempEthanRow = (currentState.getEthanRow() - 1);
			tempEthanColumn = (currentState.getEthanColumn());
			break;
		case DOWN:
			tempMembersOnTruck = currentState.getMembersOnTruck();
			tempMemberColumn = currentState.getMemberColumn();
			;
			tempTruckMembers = currentState.getTruckMembers();
			tempMemberRow = currentState.getMemberRow();
			;
			tempIsMemberSaved = currentState.getIsMemberSaved();
			tempHealth = currentState.getMemberHealth();
			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (tempTruckMembers.charAt(i) == '0' && tempIsMemberSaved.charAt(i) == '0' && tempHealth[i] <= 99) {
					if (tempHealth[i] == 99) {
						tempHealth[i] += 1;
						healthGained++;
					} else {
						tempHealth[i] += 2;
						healthGained += 2;
					}
				}
			}
			tempEthanRow = (currentState.getEthanRow() + 1);
			tempEthanColumn = (currentState.getEthanColumn());
			break;
		case LEFT:
			tempMembersOnTruck = currentState.getMembersOnTruck();
			tempMemberColumn = currentState.getMemberColumn();
			tempMemberRow = currentState.getMemberRow();
			tempIsMemberSaved = currentState.getIsMemberSaved();
			tempHealth = currentState.getMemberHealth();
			tempTruckMembers = currentState.getTruckMembers();
			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (tempTruckMembers.charAt(i) == '0' && tempIsMemberSaved.charAt(i) == '0' && tempHealth[i] <= 99) {
					if (tempHealth[i] == 99) {
						tempHealth[i] += 1;
						healthGained++;
					} else {
						tempHealth[i] += 2;
						healthGained += 2;
					}
				}
			}
			tempEthanRow = (currentState.getEthanRow());
			tempEthanColumn = (currentState.getEthanColumn() - 1);
			break;
		case RIGHT:
			tempMembersOnTruck = currentState.getMembersOnTruck();
			tempMemberColumn = currentState.getMemberColumn();
			tempMemberRow = currentState.getMemberRow();
			tempIsMemberSaved = currentState.getIsMemberSaved();
			tempHealth = currentState.getMemberHealth();
			tempTruckMembers = currentState.getTruckMembers();

			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (tempTruckMembers.charAt(i) == '0' && tempIsMemberSaved.charAt(i) == '0' && tempHealth[i] <= 99) {
					if (tempHealth[i] == 99) {
						tempHealth[i] += 1;
						healthGained++;
					} else {
						tempHealth[i] += 2;
						healthGained += 2;
					}
				}
			}
			tempEthanRow = (currentState.getEthanRow());
			tempEthanColumn = (currentState.getEthanColumn() + 1);
			break;
		case CARRY:
			tempMembersOnTruck = currentState.getMembersOnTruck() + 1;
			tempMemberColumn = currentState.getMemberColumn();
			tempMemberRow = currentState.getMemberRow();
			tempIsMemberSaved = currentState.getIsMemberSaved();
			tempTruckMembers = currentState.getTruckMembers();
			tempEthanRow = (currentState.getEthanRow());
			tempEthanColumn = (currentState.getEthanColumn());
//			Make Carried people's location on grid = -1
			tempMemberColumn[memberAtEthan] = -1;
			tempMemberRow[memberAtEthan] = -1;
//			Make the character in the string truck members (String of zeros and ones to be one for the member carried)
			tempTruckMembers = tempTruckMembers.substring(0, memberAtEthan) + '1'
					+ tempTruckMembers.substring(memberAtEthan + 1);
			tempHealth = currentState.getMemberHealth();
			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (tempTruckMembers.charAt(i) == '0' && tempIsMemberSaved.charAt(i) == '0' && tempHealth[i] <= 99) {
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

			break;
		case DROP:
			tempMembersOnTruck = 0;
			tempMemberColumn = currentState.getMemberColumn();
			tempMemberRow = currentState.getMemberRow();
			tempIsMemberSaved = currentState.getIsMemberSaved();
			tempTruckMembers = currentState.getTruckMembers();
//			Binary Oring of the two strings (Members on truck and isMemberSaved) to get the new isMemberSaved
//			Both these strings are strings of zeros and ones to represent at each index which member is on truck or saved
			tempIsMemberSaved = binaryOr(tempIsMemberSaved, tempTruckMembers);
//			Reset truck members to all zeros string
			tempTruckMembers = zeroString;
			tempEthanRow = (currentState.getEthanRow());
			tempEthanColumn = (currentState.getEthanColumn());
			tempHealth = currentState.getMemberHealth();
			healthGained = 0;
			for (int i = 0; i < numberOfMembers; i++) {
				if (tempTruckMembers.charAt(i) == '0' && tempIsMemberSaved.charAt(i) == '0' && tempHealth[i] <= 99) {
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
		}
//		Make the next state with the new variables
		MIState nextState = new MIState(tempEthanRow, tempEthanColumn, tempMemberRow, tempMemberColumn, tempHealth,
				tempIsMemberSaved, tempTruckMembers, tempMembersOnTruck);
//		Make sure that this state is not visited
		if (((MIState) node.getState()).getStateForVisitedStates().equals(nextState.getStateForVisitedStates())) {
			return null; //return null if visited
		}
		if (visitedStates.contains(nextState.getStateForVisitedStates())) {
			return null;
		} else {
//			else we add in visited states
			visitedStates.add(nextState.getStateForVisitedStates());
		}
		int[] heuristicCost = null;
//		Calculate heuristic for the new state
		if (includeHeuristic) {
			if (heuristicType) {
				heuristicCost = calculateHeuristicOne(nextState);
			} else {
				heuristicCost = calculateHeuristicTwo(nextState);
			}
		}
//		System.out.println(Arrays.toString(tempHealth));
//		System.out.println(healthGained);
//		System.out.println(node.getCost());
//		System.out.println("Health incurred is : "+(node.getCost() + healthGained));
//		System.out.println("Deaths:" +getDead(nextState));
//		Create the new node and return to be added in the queue
		return new MINode(nextState, idSoFar++, node.getCost() + healthGained, healthGained, heuristicCost, node,
				operator, node.getDepth() + 1, getDead(nextState));
	}

//	Checks if the operator can be applied on the state
	public static boolean isApplicable(State state, Operator operator) {
		MIState currentState = (MIState) state;
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
			if (currentState.getEthanColumn() == gridColumns - 1)
				return false;
			else {
				return true;
			}
		case CARRY:
			if (currentState.getMembersOnTruck() == maximumCarry) {
				return false;
			} else {
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

		default:
			return true;

		}
	}
//	Gets the member at ethan's position and returns -1 if there are no members at ethan
	public static int findMember(int x, int y, MIState currentState) { // gets index of member
		for (int i = 0; i < numberOfMembers; i++) {
			if (currentState.getIsMemberSaved().charAt(i) == '0' && x == currentState.getMemberRow()[i]
					&& y == currentState.getMemberColumn()[i]) {
				return i;
			}
		}
		return -1;
	}
//	Binary Oring of two equal lenghted strings of 1's and 0's
	public static String binaryOr(String s1, String s2) {
		String s3 = "";
		for (int i = 0; i < numberOfMembers; i++) {
			if (s1.charAt(i) == '1' || s2.charAt(i) == '1') {
				s3 += "1";
			} else {
				s3 += "0";
			}
		}
		return s3;
	}
//	Gets deaths at a state
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
	
	public static int calculateManhattanDistance(int x1, int x2, int y1, int y2) {
		int deltaX = Math.abs(x2 - x1);
		int deltaY = Math.abs(y2 - y1);
		return deltaX + deltaY;

	}
	
	public static int calculateEuclideanDistance(int x1, int x2, int y1, int y2) {
		int deltaX = Math.abs(x2 - x1);
		int deltaY = Math.abs(y2 - y1);
		int deltaXSquared = deltaX * deltaX;
		int deltaYSquared = deltaY * deltaY;
		return (int) Math.sqrt(deltaXSquared + deltaYSquared);
	}
// Finds nearest member to ethan using euclidean distance (returns distance and index of the member)
	
	public static int[] findNearestMember(MIState miState) {
		int[] tempMemberRow = miState.getMemberRow();
		int[] tempMemberColumn = miState.getMemberColumn();
		int tempDistance = 0;
		int minDistance = 15 * 16;
		int minIndex = -1;
		for (int i = 0; i < tempMemberRow.length; i++) {
			if (tempMemberRow[i] != -1) {
				tempDistance = calculateEuclideanDistance(miState.getEthanRow(), tempMemberRow[i],
						miState.getEthanColumn(), tempMemberColumn[i]);
				if (tempDistance < minDistance) {
					minDistance = tempDistance;
					minIndex = i;
				}
			}
			if (minDistance == 15 * 16) {
				minDistance = 0;
			}
		}
//		System.out.println(minDistance);
		return new int[] { minDistance, minIndex };

	}
// Finds nearest member to Ethan using Manhattan distance (returns distance and index of the member)

	public static int[] findNearestMember2(MIState miState) {
		int[] tempMemberRow = miState.getMemberRow();
		int[] tempMemberColumn = miState.getMemberColumn();
		int tempDistance = 0;
		int minDistance = 15 * 16;
		int minIndex = -1;
		for (int i = 0; i < tempMemberRow.length; i++) {
			if (tempMemberRow[i] != -1) {
				tempDistance = calculateManhattanDistance(miState.getEthanRow(), tempMemberRow[i],
						miState.getEthanColumn(), tempMemberColumn[i]);
				if (tempDistance < minDistance) {
					minDistance = tempDistance;
					minIndex = i;
				}
			}
			if (minDistance == 15 * 16) {
				minDistance = 0;
			}
		}
//		System.out.println(minDistance);
		return new int[] { minDistance, minIndex };

	}

//	Assume that all members are adjacent and that they are all adjacent to the nearest member from ethan
	public static int[] calculateHeuristicOne(State state) {
		MIState miState = (MIState) state;
		int deathEstimate = 0;
		int damageEstimate = 0;
		int tempDamage = 0;
		int[] tempMemberRow = miState.getMemberRow();
		int[] tempHealth = miState.getMemberHealth();
		int counter = 0;
		String tempIsMemberSaved = miState.getIsMemberSaved();
		String tempTruckMembers = miState.getTruckMembers();
		int[] nearestResult = findNearestMember(miState);
		int nearestMember = nearestResult[0];
		int nearestIndex = nearestResult[1];
		for (int i = 0; i < tempMemberRow.length; i++) {
			if (tempMemberRow[i] != -1 && tempIsMemberSaved.charAt(i) == '0' && tempTruckMembers.charAt(i) == '0'
					&& tempHealth[i] < 100) {
				tempDamage = nearestMember * 2;
				if (tempHealth[i] + tempDamage >= 100) {
					damageEstimate += (100 - tempHealth[i]);
					deathEstimate++;
				} else {
					damageEstimate += tempDamage;
				}
			}

		}
		if (nearestIndex != -1)
			tempMemberRow[nearestIndex] = -1;
		for (int i = 0; i < tempHealth.length; i++) {
			if (tempMemberRow[i] != -1 && tempIsMemberSaved.charAt(i) == '0' && tempTruckMembers.charAt(i) == '0'
					&& tempHealth[i] < 100) {
				tempDamage = 2 * counter;
				if (tempDamage + tempHealth[i] >= 100) {
					damageEstimate += (100 - tempHealth[i]);
					deathEstimate++;
				} else {
					damageEstimate += tempDamage;
				}
				counter++;
			}
		}
		return new int[] { deathEstimate, damageEstimate };

	}
//	Same as heuristic one but with manhattan distance
	public static int[] calculateHeuristicTwo(State state) {
		MIState miState = (MIState) state;
		int deathEstimate = 0;
		int damageEstimate = 0;
		int tempDamage = 0;
		int[] tempMemberRow = miState.getMemberRow();
		int[] tempHealth = miState.getMemberHealth();
		int counter = 0;
		String tempIsMemberSaved = miState.getIsMemberSaved();
		String tempTruckMembers = miState.getTruckMembers();
		int[] nearestResult = findNearestMember2(miState);
		int nearestMember = nearestResult[0];
		int nearestIndex = nearestResult[1];
		for (int i = 0; i < tempMemberRow.length; i++) {
			if (tempMemberRow[i] != -1 && tempIsMemberSaved.charAt(i) == '0' && tempTruckMembers.charAt(i) == '0'
					&& tempHealth[i] < 100) {
				tempDamage = nearestMember * 2;
				if (tempHealth[i] + tempDamage >= 100) {
					damageEstimate += (100 - tempHealth[i]);
					deathEstimate++;
				} else {
					damageEstimate += tempDamage;
				}
			}

		}
		if (nearestIndex != -1)
			tempMemberRow[nearestIndex] = -1;
		for (int i = 0; i < tempHealth.length; i++) {
			if (tempMemberRow[i] != -1 && tempIsMemberSaved.charAt(i) == '0' && tempTruckMembers.charAt(i) == '0'
					&& tempHealth[i] < 100) {
				tempDamage = 2 * counter;
				if (tempDamage + tempHealth[i] >= 100) {
					damageEstimate += (100 - tempHealth[i]);
					deathEstimate++;
				} else {
					damageEstimate += tempDamage;
				}
				counter++;
			}
		}
		return new int[] { deathEstimate, damageEstimate };

	}

// Recursive call to print the grids of a flow
	public static void printFlow(STNode node) {
		if (node == null) {
			return;
		} else {
			printFlow(node.getParent());
			printGrid(node.getState());
		}
	}
//prints grid of a given state
	public static void printGrid(State state) {
		MIState nextState = (MIState) state;
		Cell[][] tempField = new Cell[gridRows][gridColumns];
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
		System.out.println("********************************************");
		for (int i = 0; i < gridRows; i++) {
			for (int j = 0; j < gridColumns; j++) {
				System.out.print(tempField[i][j]);
			}
			System.out.println("");
		}
		System.out.println("***************************************");
	}
}
