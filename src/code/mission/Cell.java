package code.mission;

public class Cell {
	Occupant occupant;
	Occupant occupant2;
	public Occupant getOccupant2() {
		return occupant2;
	}

	public void setOccupant2(Occupant occupant2) {
		this.occupant2 = occupant2;
	}

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
		String str="";
		if (occupant == null) {
			return "-";
		}
		str+=occupant.toString();
		if(occupant2==null) {
			return str;
		}
		else {
			str+="/"+occupant2.toString();
		}
		return str;
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
