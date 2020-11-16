package MissionImpossible;

import java.util.HashMap;

public class State {
	HashMap <String,String> stateMap;
	
	public State() {
		
	}
	public String getFromState(String key) {
		return stateMap.get(key);
	}

}
