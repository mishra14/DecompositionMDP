package ese.seas.upenn.edu;

public class Main 
{
	public static void main(String[] args) 
	{
		MDP testMDP=new MDP();  
		try 
		{
			testMDP.buildFromFile("testMDP.txt");
			System.out.println(testMDP);
			
			long start=System.nanoTime();
			testMDP.createLP();				//creating A vector
			
			System.out.println("\nA matrix : (index format - toThenFrom) \n"+testMDP.getA()); 
			
			testMDP.createSparseAMatrix();		//Convert A Matrix into Mat File
			
			long end=System.nanoTime();
			System.out.println("Time taken for LP creation : "+(float)((end-start)/(1000))+" uSec");
			
						
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}
