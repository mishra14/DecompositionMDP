package ese.seas.upenn.edu;

public class Main 
{
	public static void main(String[] args) 
	{
		MDP testMDP=new MDP();  
		try 
		{
			testMDP.buildFromFile("testMDP2.txt");
			testMDP.createKernels();
			System.out.println(testMDP);
			
			long start=System.nanoTime();
			testMDP.createLP();				//creating A vector
			long end=System.nanoTime();
			
			System.out.println("\nA matrix : (index format - toThenFrom) \n"+testMDP.getA());
			
			testMDP.createAMatrix();		//Convert A Matrix into Mat File
			
			System.out.println("Time taken for LP creation : "+(float)((end-start)/(1000))+" uSec");
			
			PlanarSeparator.BFSLayerGeneration("s0", testMDP);
			
			//new MatFileWriter("A.mat", testMDP.getA().getMatrixHolder());
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}
