package exceptions;

public class InvalidSyntaxException extends Exception{
	public InvalidSyntaxException(){}
	
	public InvalidSyntaxException(String instruction){
		super("The line \"" + instruction + "\" is not valid");
	}
}