public class Validation 
{
	public boolean isValidEmailAddress(String emailAddress)
	{  
	    String  expression="^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";  
	    CharSequence inputStr = emailAddress;  
	    Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);  
	    Matcher matcher = pattern.matcher(inputStr);  
	    return matcher.matches();  
	 }
	
	public static boolean isPhoneNumberValid(String phoneNumber)
	{
		boolean isValid = false;
		String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
		CharSequence inputStr = phoneNumber;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if(matcher.matches())
			isValid = true;
		return isValid;
	}
	
	public boolean isNumeric(String number)
	{
		boolean isValid = false;
		String expression = "^[-+]?[0-9]*\\.?[0-9]+$";
		
		CharSequence inputStr = number;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if(matcher.matches())
		isValid = true;
		return isValid;
	}
}
