package MissionImpossible;
public class MissionImpossible {
	State initialState;
	Cell [][] grid;
	int gridRows;
	int gridColumns;
	int ethanRow;
	int ethanColumn;
	int submarineRow;
	int submarineColumn;
	int [] memberRow;
	int [] memberColumn;
	int [] memberHealth;
	boolean [] isMemberSaved; // Saved means they are on submarine
	int maximumCarry;
	int numberOfMembers;
	
	public MissionImpossible() {	
		genGrid();		
	}
	public void genGrid(){
		extractingAttributes(genGridHelper());
		grid=new Cell [gridRows][gridColumns];
		for(int i=0;i<gridRows;i++) {
			for(int j=0;j<gridColumns;j++) {
				grid[i][j]=new Cell(1,j);
			}
		}
		grid[ethanRow][ethanColumn]=new Cell(new Ethan(),ethanRow,ethanColumn);
		grid[submarineRow][submarineColumn]=new Cell(new Submarine(),submarineRow,submarineColumn);
		for (int i=0;i<numberOfMembers;i++) {
			grid[memberRow[i]][memberColumn[i]]=new Cell(new Member(memberHealth[i]),memberRow[i],memberColumn[i]);
			
		}
	}
	public void extractingAttributes(String inputString) {
		String [] splitted=inputString.split(";");
		String [] temp=splitted[0].split(",");
		gridRows=Integer.parseInt(temp[0]);
		gridColumns=Integer.parseInt(temp[1]);
		temp=splitted[1].split(",");
		ethanRow=Integer.parseInt(temp[0]);
		ethanColumn=Integer.parseInt(temp[1]);
		temp=splitted[2].split(",");
		submarineRow=Integer.parseInt(temp[0]);
		submarineColumn=Integer.parseInt(temp[1]);
		temp=splitted[3].split(",");
		numberOfMembers=temp.length/2;
		memberRow=new int[numberOfMembers];
		memberColumn=new int[numberOfMembers];
		memberHealth=new int[numberOfMembers];
		isMemberSaved=new boolean[numberOfMembers];
		for(int i=0, c=0;i<numberOfMembers;i+=2,c++) {
			memberRow[c]=Integer.parseInt(temp[i]);
			memberColumn[c]=Integer.parseInt(temp[i+1]);
			
		}
		temp=splitted[4].split(",");
		for(int i=0;i<numberOfMembers;i++) {
			memberHealth[i]=Integer.parseInt(temp[i]);
		}
		maximumCarry=Integer.parseInt(splitted[5]);
	}
	public String genGridHelper() {
		
		int max = 15; 
        int min = 5; 
        int range = max - min + 1;
        String inputString="";
        inputString+=(int)(Math.random() * range) + min+","; 
        inputString+= (int)(Math.random() * range) + min+";";
        
        
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

}
