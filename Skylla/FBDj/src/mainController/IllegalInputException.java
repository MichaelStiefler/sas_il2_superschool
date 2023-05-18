package mainController;

/*
 * @author skylla
 * @see: MissionController.blockVote(), AdminCommandController.adminCommandVetoTime();
 */

public class IllegalInputException extends Exception{

	/**
     * 
     */
    private static final long serialVersionUID = -1486285654782114714L;
    private String illegalInput;
	private String reason;
	
	public IllegalInputException(String illegalInput, String reason) {
		super("Illegal Input: " + illegalInput + "; Reason: " + reason);
		this.illegalInput = illegalInput;
		this.reason = reason;
	}
	
	public String getIllegalInput() {
		return this.illegalInput;
	}
	
	public String getReason() {
		return this.reason;
	}
}
