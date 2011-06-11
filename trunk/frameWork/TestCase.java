package frameWork;

import java.util.ArrayList;

public class TestCase 
{
	public String _id;
	public ArrayList<String> _input;
	public ArrayList<String> _expectedOutput;
	public ArrayList<String> _actualOutput;
	public double _executionTime;
	public boolean _execute;
	
	public TestCase(String id, ArrayList<String> input, ArrayList<String> expOutput)
	{
		_id = id;
		_input = input;
		_expectedOutput = expOutput;
	}
}
