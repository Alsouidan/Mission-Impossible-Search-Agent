package code.mission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;

import code.generic.STNode;
import code.generic.SearchProblem;

public class MissionImpossible extends SearchProblem {
	static ArrayList<Occupant> arrayToBeShuffled = new ArrayList<Occupant>();
	static Cell[][] field ;
	static int gridRows ;
	static int gridColumns ;
	static int ethanRow ;
	static int ethanColumn ;
	static int submarineRow;
	static int submarineColumn;
	static int[] memberRow ;
	static int[] memberColumn;
	static int[] memberHealth;
	static boolean[] isMemberSaved; // Saved means they are on submarine
	static int maximumCarry;
	static int numberOfMembers ;
	static HashSet<MIState> visitedStates;
	static PriorityQueue queue;
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
		int[] truckMembers = new int[maximumCarry];
		for (int i = 0; i < maximumCarry; i++) {
			truckMembers[i] = -1;
		}
		for (int i = 0; i < gridRows; i++) {
			for (int j = 0; j < gridColumns; j++) {
				System.out.print(field[i][j]);
			}
			System.out.println("");
		}
		initialState = new MIState(field, ethanRow, ethanColumn, submarineRow, submarineColumn, memberRow, memberColumn,
				memberHealth, isMemberSaved, truckMembers, numberOfMembers);
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
	solve(genGrid(),"",false);
	}

	

	@Override
	public boolean goalTest() {
		for(int i=0;i<numberOfMembers;i++) {
		 if(!isMemberSaved[i])
			 return false;
		}
		return true;
		}
	

	@Override
	public void populateOperators() {
		operators= new Operator[]{Operator.UP,Operator.DOWN,Operator.RIGHT,Operator.LEFT,Operator.CARRY,Operator.DROP};
	}

	@Override
	public int calculateCostOfNode(STNode node) {
		// TODO Auto-generated method stub
		return 0;
	}
}
