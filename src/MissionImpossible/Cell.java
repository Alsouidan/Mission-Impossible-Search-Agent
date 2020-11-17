package MissionImpossible;

public class Cell {
	Occupant occupant;
	int row;
	int column;
	public Cell(Occupant occupant,int row,int column) {
		this.occupant=occupant;
		this.row=row;
		this.column=column;
	}
	public Cell(int row,int column) {
		this.row=row;
		this.column=column;
	}
	public String toString() {
		return occupant.toString();
	}
}
