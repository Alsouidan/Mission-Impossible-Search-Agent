package MissionImpossible;

public class Member extends Occupant {
	int health;
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
//	public Member(int health) {
//		this.health=health;
//	}
	public Member() {
		
		this.health=(int)(Math.random()*99)+1;
	}
	public String toString() {
		return "M,"+health;
	}
}
