package mainController;

/*
 * @author skylla
 * @see MissionController.setRequestedMission(), ChatUserController.userCommandRequest()
 */

//TODO: skylla: request_mission command;

public class IllegalMissionException extends Exception{

	/**
     * 
     */
    private static final long serialVersionUID = 1394968869933645012L;
    private String wrongMissionName;
	
	public IllegalMissionException(String wrongMissionName) {
		super("Mission not found: " + wrongMissionName);
		this.wrongMissionName = wrongMissionName;
	}
	
	public String getWrongMissionName() {
		return this.wrongMissionName;
	}
}
