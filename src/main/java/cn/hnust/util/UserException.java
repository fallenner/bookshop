package cn.hnust.util;

public class UserException extends Exception{

	private static final long serialVersionUID = 1L;

	public UserException(String message) {
		super(message);
	}
	   public String getMessage() {
	        return super.getMessage();
	    }

}
