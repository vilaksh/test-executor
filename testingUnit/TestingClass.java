package testingUnit;

import frameWork.TestRunner;

public class TestingClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		
		System.out.print("Starting tests....\n");
		
		TestRunner tr = new TestRunner("C:\\viji\\spoj problema\\MOBILEINPUT.xml");
		tr.RunTests();
		tr.GenerateResult("C:\\viji\\spoj problema\\MOBILEOUTPUT.xml", false);
	}
}


