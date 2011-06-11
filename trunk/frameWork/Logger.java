package frameWork;

public class Logger 
{
	private static void _writeToOutput(String m)
	{
		System.out.print(m + "\n");
	}
	
	public static void LogMessage(String m)
	{		
		_writeToOutput("[INFO]" + m);
	}
	
	public static void LogError(String m)
	{
		_writeToOutput("!!!!![ERROR]" + m);
	}
}
