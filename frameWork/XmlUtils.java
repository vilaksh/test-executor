package frameWork;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtils 
{
	private static boolean _verifyNode(Node node)
	{
		if(node.getNodeType() != Node.ELEMENT_NODE)
		{
			Logger.LogError("Xml document passed was not paresed properly");
			Logger.LogError("Some error occured during parsing of " + node.getNodeName() + " tag");
			return false;					
		}
		return true;
	}
	
	private static void _writeNode(BufferedWriter writer, String nodeName, String nodeValue) throws Exception
	{
		writer.write("<" + nodeName + ">");
		writer.write(nodeValue);
		writer.write("</" + nodeName + ">");
		writer.newLine();
	}
	
	private static String _getNodeValue(Node node)
	{
		String value = null;
		Element element;
		
		if(!_verifyNode(node))			
			return null;			
		
		element = (Element) node;
		value = ((Node) element.getChildNodes().item(0)).getNodeValue();
		
		return value;
	}
	
	public static TestUnit ParseXml(String fileName)
	{
		ArrayList<TestCase> result = new ArrayList<TestCase>();
		
		TestCase []ret = null;
		String inputfile = null;
		
		try
		{
			File inputFile = new File(fileName);
			NodeList nodeList, idList, inputList, outputList;
			Node node;
			Element element;
			
			String testId;
			ArrayList<String> input;
			ArrayList<String> output;
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(inputFile);

			doc.getDocumentElement().normalize();			
			Element rootElement = doc.getDocumentElement();
			
			nodeList = rootElement.getElementsByTagName(Constants.INPUT_EXE);
			node = nodeList.item(0);
			
			inputfile = _getNodeValue(node);
			
			nodeList = rootElement.getElementsByTagName(Constants.TESTCASE);
			for(int i=0;i<nodeList.getLength(); i++)
			{
				node = nodeList.item(i);
				if(!_verifyNode(node))
					return null;					
								
				element = (Element) node;
				
				idList = element.getElementsByTagName(Constants.TESTCASE_ID);
				inputList = element.getElementsByTagName(Constants.TESTCASE_INPUT);
				outputList = element.getElementsByTagName(Constants.TESTCASE_EXP_OUTPUT);
								
				testId = _getNodeValue(idList.item(0));
				
				input = new ArrayList<String>();
				output = new ArrayList<String>();
				
				for(int j=0;j<inputList.getLength(); j++)
					input.add(_getNodeValue(inputList.item(j)));
				
				for(int k=0;k<outputList.getLength(); k++)
					output.add(_getNodeValue(outputList.item(k)));
				
				result.add(new TestCase(testId, input, output));				
			}
			
			ret = new TestCase[result.size()];							
			for(int i=0; i<result.size(); i++)
				ret[i] = (TestCase)result.get(i);			
		}
		catch(Exception e)
		{
			Logger.LogError(e.getMessage());
		}
				
		return new TestUnit(inputfile, ret);
	}
	
	public static boolean CreateXml(String fileName, TestUnit testResult)
	{
		boolean finalResult = true;
		
		try
		{			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileName)));
			
			writer.write(Constants.XML_HEADER);
			writer.newLine();
			writer.write("<" + Constants.TESTRESULTS + ">");
			writer.newLine();
			
			for(int i=0;i<testResult._testCases.length; i++)
			{
				TestCase current = testResult._testCases[i];
				boolean testfailed = false;
				
				if(!current._execute)
					continue;
				
				writer.write("<" + Constants.TESTRESULT + ">");
				writer.newLine();
				_writeNode(writer, Constants.TESTCASE_ID, current._id);
				
				// Generating the result node
				if(!testfailed && current._actualOutput.size() != current._expectedOutput.size())
				{
					_writeNode(writer, Constants.TESTCASE_RESULT, Constants.TESTCASE_FAIL);
					testfailed = true;
				}
				
				for(int j=0; j<current._actualOutput.size(); j++)
				{
					String actual = (String) current._actualOutput.get(j);
					String expected = (String) current._expectedOutput.get(j);
					
					if(!testfailed && (actual.compareToIgnoreCase(expected)) != 0)
					{
						testfailed = true;
						_writeNode(writer, Constants.TESTCASE_RESULT, Constants.TESTCASE_FAIL);
					}
						
				}
				
				finalResult = finalResult & !testfailed;
				if(!testfailed)
					_writeNode(writer, Constants.TESTCASE_RESULT, Constants.TESTCASE_PASS);
				
				for(int j=0; j<current._input.size(); j++)
					_writeNode(writer, Constants.TESTCASE_INPUT, (String) current._input.get(j));
				
				for(int j=0; j<current._expectedOutput.size(); j++)
					_writeNode(writer, Constants.TESTCASE_EXP_OUTPUT, (String) current._expectedOutput.get(j));
				
				for(int j=0; j<current._actualOutput.size(); j++)
					_writeNode(writer, Constants.TESTCASE_ACT_OUTPUT, (String) current._actualOutput.get(j));
				
				writer.write("</" + Constants.TESTRESULT + ">");
				writer.newLine();
			}
			
			writer.write("</" + Constants.TESTRESULTS + ">");
			writer.newLine();
			
			writer.close();
		}
		catch(Exception e)
		{
			Logger.LogError(e.getMessage());
		}		
		
		return finalResult;
	}
}
