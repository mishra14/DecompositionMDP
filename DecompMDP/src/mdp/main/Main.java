package mdp.main;

import java.util.Scanner;
import mdp.core.MDP;
import mdp.test.TestMDP;


/** 
* @mainpage  Toolbox for Optimal Control in Markov Decision Processes via Distributed Optimization 
* 
* This project is used to prove the results of the paper - Optimal Control in Markov Decision Processes via Distributed Optimization by Jie Fu, Shuo Han, and Ankit Mishra and Ufuk Topcu
* <br>This project is a java based tool box that allows formulation of a MDP and creates a decomposition and formulates sparse matrices that are used for solving the MDP.  
* @author Ankit Mishra
*/ 

/** 
* @file Main.java 
* 
* @brief This file is used to create an MDP object from a text file and create a decomposition within that object and output the results i a .Mat file.
* 
* The Main.Java file encapsulates the flow of the tool box. It initially creates an MDP object by reading a txt file of the following format - <br>
* 1. States of the MDP<br>
* states {s0, s1}<br>
* 2. Initial Distribution -<br> 
* initial<br>
* {s0, 1.0}<br>
* Where the format is - {state, probability}<br>
* 3. Transitions in the MDP<br>
* transitions<br>
*{s0, a, 1, s1}<br>
*{s1, b, 1, s0s}<br>
*end<br>
*Where each transition has the format - {fromState, action, probabilityOfTransition, ResultState}<br>
* 4. Rewards - <br>
* rewards<br>
* {s0,a,0}<br>
* {s0,b,0}<br>
* {s1,a,-0.7}<br>
* {s1,b,0}<br>
* {s2,b,-0.5}<br>
* end<br>
* Where the format is - {state, action, reward}<br>
* 5. Decomposition count or decomposition<br>
* Regions=2<br>
* or<br>
* Regions<br>
* r1={s0}<br>
* r2={s1}<br>
* end<br>
* <br>
* Then the MDP is first converted into an LP problem by constructing the A, B and C matrices, 
* then generating a .Mat file that hols the sparse matric representation of the three matrices in a Matlab Sparse matrix format. 
**/ 

/** 
* @class Main 
* 
* @brief This class is used to create an MDP object from a text file and create a decomposition within that object and output the results i a .Mat file.
* 
**/

public class Main 
{
	/** 
	* 
	* @brief This method is used to create an MDP object from a text file and create a decomposition within that object and output the results i a .Mat file.
	* 
	* 
	**/
	public static void main(String[] args) 
	{
		MDP testMDP=new MDP();  
		String filename=new String("testMDP2.txt");
		boolean grid=false;
		try 
		{
			//read the filename from the console
			System.out.println("Please enter the MDP source file name or enter Grid(n,r) to run on a gridworld problem : ");
			Scanner scanIn = new Scanner(System.in);
			filename = scanIn.nextLine();
		    scanIn.close();            
		    if(filename.contains("Grid"))
		    {
		    	grid=true;
		    	int n,r;
		    	filename=filename.replaceAll("\\s+","");
		    	n=Integer.parseInt(filename.substring(filename.indexOf("(")+1, filename.indexOf(",")));
		    	r=Integer.parseInt(filename.substring(filename.indexOf(",")+1, filename.indexOf(")")));
		    	TestMDP.createMDP(n,r);
		    	filename="testMDP4.txt";
		    }
		    long start=System.currentTimeMillis();
		    testMDP.buildFromFile(filename);
			//System.out.println(testMDP);
			
			//testMDP.createLP();				//creating Sparse A Matrix
		    testMDP.createLPandMatrix();
			//System.out.println("\nA matrix : (index format - toThenFrom) \n"+testMDP.getA()); 
			
			//testMDP.createSparseAMatrix();		//Convert A Matrix into Sparse Mat File
			
			long end=System.currentTimeMillis();
			System.out.println("\nCreated LP - ");
			if(grid)
				System.out.println("Grid World Source MDP - testMDP4.txt");
			System.out.println("Xvector - XVector.txt");
			System.out.println("A, B, C Vectors - A_B_C.mat");
/*			System.out.println("Regions - "+testMDP.getRegions().size());
			System.out.println("Kernels - "+testMDP.getKernels().size());
			System.out.println("B - "+testMDP.getbVector().size());
			System.out.println("C - "+testMDP.getcVector().size());*/
			System.out.println("Time taken for LP creation : "+((end-start))+" mSec");
			System.out.println("-----------------------------------------------------");
						
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}

}
