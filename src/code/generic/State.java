package code.generic;

public abstract class State {
protected String state; //String representation of the state

public String getState() {
	return state;
}

public void setState(String state) {
	this.state = state;
}
}
