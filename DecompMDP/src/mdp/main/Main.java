package mdp.main;

import java.util.Scanner;

import mdp.core.MDP;


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
* 2. Transitions in the MDP<br>
* transitions<br>
*{s0, a, 1, s1}<br>
*{s1, b, 1, s0s}<br>
*end<br>
*Where each transition has the format - {fromState, action, probabilityOfTransition, ResultState}<br>
* 3. Decomposition count<br>
* Regions=2<br>
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
		String filename=new String("testMDP.txt");
		try 
		{
			//read the filename from the console
			System.out.println("Please enter the MDP source file name : ");
			Scanner scanIn = new Scanner(System.in);
			//filename = scanIn.nextLine();
		    scanIn.close();            

		    testMDP.buildFromFile(filename);
			System.out.println(testMDP);
			
			long start=System.nanoTime();
			testMDP.createLP();				//creating Sparse A Matrix
			
			System.out.println("\nA matrix : (index format - toThenFrom) \n"+testMDP.getA()); 
			
			testMDP.createSparseAMatrix();		//Convert A Matrix into Sparse Mat File
			
			long end=System.nanoTime();
			System.out.println("Time taken for LP creation : "+(float)((end-start)/(1000))+" uSec");
			
						
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}
