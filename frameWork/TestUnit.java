package frameWork;

import java.io.*;
import java.util.ArrayList;

public class TestUnit 
{
	public String _executableFile;
	public TestCase []_testCases;
	
	private ArrayList<String> _runTest(ArrayList<?> input)
	{
		ArrayList<String> result = new  ArrayList<String>();
		
		StringBuffer strBuffer= new StringBuffer();
        String line= null;
        
        try
        {
            Process process = Runtime.getRuntime().exec(_executableFile);
            
            BufferedReader reader =
            new BufferedReader(new InputStreamReader(process.getInputStream()));
            
            BufferedReader errorReader =
            new BufferedReader(new InputStreamReader(process.getErrorStream()));
            
            PrintWriter writer = new PrintWriter(process.getOutputStream());
            
            for (int i=0; i<input.size();i++)
    			writer.println(input.get(i).toString());
            
            writer.close();
           
            try
            {
                // we need the process to end, else we'll get an
                // illegal Thread State Exception
                line = reader.readLine();
                while (line != null)
                {
                    result.add(line);
                    line = reader.readLine();
                }
                process.waitFor();
            }
            catch (Exception inte)
            {
                Logger.LogError(inte.getMessage());
            }
           
            int ret = process.exitValue();            
            if ( ret != Constants.S_OK)
            {
                Logger.LogError(strBuffer.toString());
            }
            
            try
            {
            	line = errorReader.readLine();
            }
            catch (Exception ex)
            {
            	Logger.LogError(ex.getMessage());
            }
            
            process.destroy();           
        }
        catch (Exception ioe)
        {
            Logger.LogError(ioe.getMessage());
        }
		return result;	
	}
	
	public TestUnit(String executable, TestCase []testcaselist)
	{
		_executableFile = executable;
		_testCases = testcaselist;
	}
	
	public void runTests()
	{
		for(int i=0; i<_testCases.length; i++)
			if(_testCases[i]._execute)
				_testCases[i]._actualOutput = _runTest(_testCases[i]._input);
	}
}
