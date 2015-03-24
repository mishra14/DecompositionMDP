package ese.seas.upenn.edu;

public class Main 
{
	public static void main(String[] args) 
	{
		MDP testMDP=new MDP(); 
		try 
		{
			testMDP.buildFromPrismExplicit("testMDP2.txt");
			testMDP.createKernels();
			System.out.println(testMDP);
			
			long start=System.nanoTime();
			testMDP.createLPQuick();				//creating A vector
			long end=System.nanoTime();
			
			System.out.println("\nA matrix : (index format - toThenFrom) \n"+testMDP.getA());
			
			//testMDP.createAMatrix();		//TODO converting A vector into Matrix
			
			System.out.println("Time taken for LP creation : "+(float)((end-start)/(1000))+" uSec");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}
