package cn.hnust.exception;

public class FileOperationException  extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public FileOperationException(){
		super();
	}
	
	public FileOperationException(String message) {
		super(message);
	}
	
	public FileOperationException(String message, Throwable t) {
		super(message, t);
	}
}
