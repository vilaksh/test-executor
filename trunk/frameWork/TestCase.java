package frameWork;

import java.util.ArrayList;

public class TestCase 
{
	public String _id;
	public ArrayList _input;
	public ArrayList _expectedOutput;
	public ArrayList _actualOutput;
	public double _executionTime;
	public boolean _execute;
	
	public TestCase(String id, ArrayList input, ArrayList expOutput)
	{
		_id = id;
		_input = input;
		_expectedOutput = expOutput;
	}
}
