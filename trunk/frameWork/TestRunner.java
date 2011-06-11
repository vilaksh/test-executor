package frameWork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TestRunner 
{
	private TestUnit _testCases;
	private String _result;
	
	public TestRunner(String inputFile)
	{
		_testCases = XmlUtils.ParseXml(inputFile);
		_result = null;		
	}
	
	public String GetResult()
	{
		return _result;
	}
	
	public void RunTests()
	{
		for(int i=0; i<_testCases._testCases.length; i++)
			_testCases._testCases[i]._execute = true;
		_testCases.runTests();
	}
	
	public void RunTests(String []Id)
	{
		for(int i=0; i<_testCases._testCases.length; i++)
			for(int j=0; j<Id.length; j++)
				if(Id[j].equalsIgnoreCase(_testCases._testCases[i]._id))
					_testCases._testCases[i]._execute = true;
		_testCases.runTests();
	}
	
	public void GenerateResult(String outputFile, boolean onScreen)
	{
		_result = XmlUtils.CreateXml(outputFile, _testCases)? Constants.TESTCASE_PASS : Constants.TESTCASE_FAIL;
		System.out.println(_result);
		
		if(onScreen)
		{
			try
			{
				BufferedReader reader = new BufferedReader(new FileReader(new File(outputFile)));
				
				String line = reader.readLine();
				while(line != null)
				{
					System.out.println(line);
					line = reader.readLine();
				}
			}
			catch(Exception e)
			{
				Logger.LogError(e.getMessage());
			}
			
		}
	}
}
