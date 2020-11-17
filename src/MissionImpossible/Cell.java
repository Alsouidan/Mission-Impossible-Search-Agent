package MissionImpossible;

public class Cell {
	Occupant occupant;
	int row;
	int column;

	public Cell(Occupant occupant, int row, int column) {
		this.occupant = occupant;
		this.row = row;
		this.column = column;
	}

	public Cell(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public String toString() {
		if (occupant == null) {
			return "-";
		}
		return occupant.toString();
	}

	public Occupant getOccupant() {
		return occupant;
	}

	public void setOccupant(Occupant occupant) {
		this.occupant = occupant;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
}
