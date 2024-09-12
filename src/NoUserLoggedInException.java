
public class NoUserLoggedInException extends Exception {
	
	/**
	 * Thrown when there is no member logged in to the social network and an operation requiring login is executed
	 */
	private static final long serialVersionUID = 00003L;

	public NoUserLoggedInException() {
	}

}
