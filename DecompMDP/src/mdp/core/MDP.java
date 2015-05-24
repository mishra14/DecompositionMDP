package mdp.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import mdp.util.PlanarSeparator;
import mdp.util.SparseMatrixHolder;

import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;



/** 
* @file MDP.java 
* 
* @brief This file contains the source code for the MDP bean and Util methods
* 
* @author Ankit Mishra
*
* @date May, 1 2015
* 
**/

/** 
* @class MDP 
* 
* @brief This class contains the source code for the MDP bean and Util methods
* 
**/
public class MDP 
{
	private Map<String, State> states;
	private Map<String, LinkedHashSet<String>> regions;
	private Map<String, LinkedHashSet<String>> kernels;
	private ArrayList<String> actions;
	private SparseMatrixHolder A;
	private final float gamma=(float)0.9;
	private int regionCount;
	private Map<String, LinkedHashSet<String>> xVector;
	private Map<String, LinkedHashMap<String,Float>> bVector;
	private Map<String, LinkedHashMap<String,Float>> cVector;
	/** 
	* @brief This is a default constructor of the MDP class
	* 
	**/
	public MDP() 
	{
		super();
		states=new LinkedHashMap<String, State>();
		regions=new LinkedHashMap<String, LinkedHashSet<String>>();
		kernels=new LinkedHashMap<String, LinkedHashSet<String>>();
		actions=new ArrayList<String>();
		regionCount=0;
		xVector=new LinkedHashMap<String, LinkedHashSet<String>>();
		bVector=new LinkedHashMap<String, LinkedHashMap<String,Float>>();
		cVector=new LinkedHashMap<String, LinkedHashMap<String,Float>>();
	}
	/** 
	* @brief This is a copy constructor of the MDP class that creates a new MDP object which is a deep copy of the first argument
	* 
	**/
	public MDP(MDP mdp) 
	{
		super();
		states=mdp.states;
		regions=mdp.regions;
		kernels=mdp.kernels;
		actions=mdp.actions;
		regionCount=mdp.regionCount;
		xVector=mdp.xVector;
		bVector=mdp.bVector;
		cVector=mdp.cVector;
	}
	/** 
	* @brief This method is a getter for the number of regions in the decomposition
	* 
	**/
	public int getRegionCount() {
		return regionCount;
	}
	/** 
	* @brief This method is a setter for the number of regions in the decomposition
	* 
	**/
	public void setRegionCount(int regionCount) {
		this.regionCount = regionCount;
	}
	/** 
	* @brief This method is a getter for gamma variable
	* 
	**/
	public float getGamma() {
		return gamma;
	}
	/** 
	* @brief This method is a getter for states in the MDP
	* 
	**/
	public Map<String, State> getStates() 
	{
		return states;
	}
	/** 
	* @brief This method is a setter for states in the MDP
	* 
	**/
	public void setStates(Map<String, State> states) 
	{
		this.states = states;
	}
	/** 
	* @brief This method is a getter for the regions in the decomposition of the MDP
	* 
	**/
	public Map<String,  LinkedHashSet<String>> getRegions() {
		return regions;
	}
	/** 
	* @brief This method is a setter for the regions in the decomposition of the MDP
	* 
	**/
	public void setRegions(Map<String,  LinkedHashSet<String>> regions) {
		this.regions = regions;
	}
	/** 
	* @brief This method is a getter for the generated kernels in the decomposition of the MDP
	* 
	**/
	public Map<String,  LinkedHashSet<String>> getKernels() {
		return kernels;
	}
	/** 
	* @brief This method is a setter for the generated kernels in the decomposition of the MDP
	* 
	**/
	public void setKernels(Map<String,  LinkedHashSet<String>> kernels) {
		this.kernels = kernels;
	}
	/** 
	* @brief This method is a getter for the action set in the MDP
	* 
	**/
	public ArrayList<String> getActions() {
		return actions;
	}
	/** 
	* @brief This method is a getter for the action set in the MDP
	* 
	**/
	public void setActions(ArrayList<String> actions) {
		this.actions = actions;
	}
	/** 
	* @brief This method is a getter for the sparse matrix A used for the LP bsed solution of the MDP
	* 
	**/
	public SparseMatrixHolder getA() {
		return A;
	}
	/** 
	* @brief This method is a setter for the sparse matrix A used for the LP bsed solution of the MDP
	* 
	**/
	public void setA(SparseMatrixHolder a) {
		A = a;
	}
	
	public Map<String, LinkedHashSet<String>> getxVector() {
		return xVector;
	}
	public void setxVector(Map<String, LinkedHashSet<String>> xVector) {
		this.xVector = xVector;
	}
	public Map<String, LinkedHashMap<String, Float>> getbVector() {
		return bVector;
	}
	public void setbVector(Map<String, LinkedHashMap<String, Float>> bVector) {
		this.bVector = bVector;
	}
	public Map<String, LinkedHashMap<String, Float>> getcVector() {
		return cVector;
	}
	public void setcVector(Map<String, LinkedHashMap<String, Float>> cVector) {
		this.cVector = cVector;
	}
	
	/** 
	* @brief This method adds the argument State s in the Map of States of the MDP
	* 
	**/
	
	//Util code begins
	public State addState(State s)
	{
		return states.put(s.getLabel(),s);
	}
	
	
	/** 
	* @brief This method is used to create an MDP object based a txt file
	* 
	* This method creates an MDP object by reading a txt file of the following format - <br>
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
	* 
	* Initially the txt file is parsed till the keyword <b>States</b> is encounterd. Then the Line is parsed to consctruct the states in the MDP.<br>
	* Then the file is parsed till the keyword <b>Transitions</b> is detected. 
	* Then onwards each new line is parsed to contruct a new transition and added to the Map of transitions of the state from which the transition begins.
	* This continues till the keyword <b>end</b> is detected.<br>
	* Then the file is parsed till the keyword <b>Regions</b> is detected. This is used to denote the number of regions wanted in the MDP decomposition.<br>
	* Based on the above information, the MDP is the decomposed into regions via a DFS based interative algorithm.<br>
	* Further, based on the decomposition the MDP kernels are generated.<br>
	* At last the txt file is closed.<br>
	*/ 
	public void buildFromFile(String filename) throws Exception
	{
		BufferedReader in;
		String line, ss[];
		String maxState ="";
		float maxProbability=0;
		try 
		{
			// Open file
			in = new BufferedReader(new FileReader(new File(filename)));
			// Parse first line to get num states
			line = in.readLine();
			if (line == null) 
			{
				in.close();
				throw new Exception("Empty File");
			}
			while(line.contains("//") || ( line != null && line.isEmpty()) || (!line.toLowerCase().contains("states")))	//ignore initial set of comments
			{
				line=in.readLine();
			}
			//create states			
			line=line.replaceAll("\\s+","");
			ss=line.subSequence(line.indexOf("{")+1, line.indexOf("}")).toString().split(",");
			for(String stateLabel : ss)
			{		
				State s=new State(stateLabel);
				if(!states.containsValue(s))
				{
					states.put(s.getLabel(),s);
					//System.out.println(stateLabel+" Added");
				}
				else
				{
					System.out.println(stateLabel+" Already present");
				}
			}
			
			//code for reding initial probability distributions
			while(line.contains("//") || ( line != null && line.isEmpty()) || (!line.toLowerCase().contains("initial")))	//ignore initial set of comments
			{
				line=in.readLine();
			}
			//create transitions
			while(!(line = in.readLine()).toLowerCase().contains("end")) 
			{

				line=line.replaceAll("\\s+","");
		        ss=line.subSequence(line.indexOf("{")+1, line.indexOf("}")).toString().split(",");
		        if(!states.containsKey(ss[0]))
		        {
		        	System.out.println("Undeclared state initialized - "+ss[0]);		 
		        }
		        else
		        {
		        	if(ss.length<2)
		        	{
		        		System.out.println("State initialization to 0 probability - "+ss[0]);
		        	}
		        	else
		        	{
		        		states.get(ss[0]).setInital(Float.parseFloat(ss[1]));
		        		if(Float.parseFloat(ss[1])>maxProbability)
		        		{
		        			maxState=ss[0];
		        			maxProbability=Float.parseFloat(ss[1]);
		        		}
		        	}
		        }
			}
			
			while(line.contains("//") || ( line != null && line.isEmpty()) || (!line.toLowerCase().contains("transitions")))	//ignore initial set of comments
			{
				line=in.readLine();
			}
			//create transitions
			while(!(line = in.readLine()).toLowerCase().contains("end")) 
			{
		        
		        line=line.replaceAll("\\s+","");
		        ss=line.subSequence(line.indexOf("{")+1, line.indexOf("}")).toString().split(",");
		        Transition t=new Transition(Float.parseFloat(ss[2]),ss[3],ss[1]);
		        //keeping track of all possible actions
		        if(!actions.contains(ss[1]))
		        {
		        	actions.add(ss[1]);
		        }
		        State s = states.get(ss[0]);
		        if(!s.getTransitions().containsValue(t))		        
		        {
		        	s.getTransitions().put(t.toString(),t);
		        	//System.out.println(t+" Added");
	        		if(!s.getActionCounts().containsKey(ss[1]))
	        		{
	        			Action a=new Action();
	        			a.setCount(1);
	        			s.getActionCounts().put(ss[1], a);
	        		}
	        		else
	        		{
	        			s.getActionCounts().get(ss[1]).setCount(s.getActionCounts().get(ss[1]).getCount()+1);
	        		}

		        }
		        else
		        {
		        	System.out.println(t+" Already Present");
		        }
		    }
			
			while(line.contains("//") || ( line != null && line.isEmpty()) || (!line.toLowerCase().contains("rewards")))	//ignore initial set of comments
			{
				line=in.readLine();
			}
			//create transitions
			while(!(line = in.readLine()).toLowerCase().contains("end")) 
			{
				line=line.replaceAll("\\s+","");
		        ss=line.subSequence(line.indexOf("{")+1, line.indexOf("}")).toString().split(",");
		        if(!states.containsKey(ss[0]))
		        {
		        	System.out.println("Reward for undeclraed state - "+ss[0]);		  
		        }
		        else
		        {
		        	if(!states.get(ss[0]).getActionCounts().containsKey(ss[1]))
		        	{
		        		System.out.println("Reward for unknown action - "+ss[0]+" "+ss[1]);
		        	}
		        	else
		        	{
		        		states.get(ss[0]).getActionCounts().get(ss[1]).setReward(Float.parseFloat(ss[2]));
		        	}
		        }
			}
			
			
			while(line.contains("//") || ( line != null && line.isEmpty()) || (!line.toLowerCase().contains("regions")))	//ignore initial set of comments
			{
				line=in.readLine();
			}
			line=line.replaceAll("\\s+","");
			ss=line.split("=");
			//System.out.println(line+" "+ss.length);
			if(ss.length>1)
			{
				//System.out.println(ss[1]);
				try
				{
					regionCount=new Integer(ss[1]);
				}
				catch(NumberFormatException e)
				{
					System.out.println(e.toString());
					System.out.println("Region Count is not an Integer - Exiting....");
					System.exit(1);
				}
				catch (Exception e) 
				{
					System.out.println(e.toString());
					System.exit(1);
				}
				//System.out.println(regionCount);
				//create regions
				//System.out.println("Max State  - "+maxState);
				PlanarSeparator.init();							//reset the static variables for Planar Separator 
				//PlanarSeparator.DFSDecomposition(maxState, this, 1);	// create regions
				PlanarSeparator.DFSDecompositionIterative(maxState, this);	// create regions
				regions=PlanarSeparator.getLayers();
				//System.out.println(regions);
				createKernels();				//create kernels based on the regions generated
				//System.out.println(kernels);
				System.out.println("Original K0 size = "+kernels.get("k0").size());
				Map<String, LinkedHashSet<String>> newRegionsOne=new LinkedHashMap<String,  LinkedHashSet<String>>();
				Map<String, LinkedHashSet<String>> newRegionsTwo=new LinkedHashMap<String,  LinkedHashSet<String>>();
				Map<String, LinkedHashSet<String>> newRegionsThree=new LinkedHashMap<String,  LinkedHashSet<String>>();
				MDP tempMDP=new MDP(this);
				//creating a deep copy of the original mdp regions
				while(true)
				{
					for(Map.Entry<String, LinkedHashSet<String>> region : tempMDP.getRegions().entrySet())
					{
						String key=new String(region.getKey());
						LinkedHashSet<String> value=new LinkedHashSet<String>();
						for(String s : region.getValue())
						{
							value.add(new String(s));
						}
						newRegionsOne.put(key, value);
					}
					newRegionsTwo=PlanarSeparator.improveDecomposition(tempMDP);
					if(newRegionsTwo.equals(newRegionsOne))
					{
						regions=newRegionsOne;
						break;
					}
					tempMDP.setRegions(newRegionsTwo);
					newRegionsThree=PlanarSeparator.improveDecomposition(tempMDP);
					if(newRegionsThree.equals(newRegionsTwo))
					{
						regions=newRegionsThree;
						break;
					} 
					else if(newRegionsThree.equals(newRegionsOne))
					{
						//assign the region which gives smaller k0
						int countTwo,countThree;
						tempMDP.setRegions(newRegionsTwo);
						tempMDP.createKernels();
						countTwo=tempMDP.getKernels().get("k0").size();
						tempMDP.setRegions(newRegionsThree);
						tempMDP.createKernels();
						countThree=tempMDP.getKernels().get("k0").size();
						regions=(countTwo<=countThree)?newRegionsTwo:newRegionsThree;
						break;
					}
					tempMDP.setRegions(newRegionsThree);
				}
				//System.out.println(regions);
				System.out.println("Final K0 size = "+kernels.get("k0").size());
			}
			else
			{
				/*
				 * Part of code to read regions from the text file
				 */
				while(!(line = in.readLine()).toLowerCase().contains("end")) 
				{
					line=line.replaceAll("\\s+","");
					//System.out.println(line);
					String regionLabel=line.subSequence(0, line.indexOf("=")).toString();
					if(!regions.containsKey(regionLabel))
					{
						//System.out.println(regionLabel);
						ss=line.subSequence(line.indexOf("{")+1, line.indexOf("}")).toString().split(",");
						boolean flagStateInRegion=false;
						LinkedHashSet<String> statesNotInAnyRegion=new LinkedHashSet<String>();
						for(String stateLabel : ss)
						{
							String parentRegion="NA";						
							if(states.containsKey(stateLabel))
							{
								for (Map.Entry<String,  LinkedHashSet<String>> entry : regions.entrySet())
								{
									//System.out.println(entry.getKey()+" - "+entry.getValue() );
									if(entry.getValue().contains(stateLabel))
									{
										flagStateInRegion=true;
										parentRegion=entry.getKey();
										break;
									}
								}
								if(flagStateInRegion)
								{
									System.out.println(stateLabel+" Already present in "+parentRegion+"; Not Added");
								}
								else
								{
									statesNotInAnyRegion.add(stateLabel);
								}
							}
							else
							{
								System.out.println(stateLabel+ " : Invalid State");
							}
						}
						if(statesNotInAnyRegion.size()!=0)
						{
							regions.put(regionLabel, statesNotInAnyRegion);
						}
						else
						{
							System.out.println(regionLabel+ " Contains no new states");
						}
					}
					else
					{
						System.out.println(regionLabel+" Already Present");
					}
					
			    }
			}
			createKernels();				//create kernels based on the improved regions generated
			createXBCVector();
		    
			// Close file
			in.close();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		} 

	}
	/** 
	* @brief This method creates Kernels based on the Decomposition of the MDP
	* Kernels are created such that each region is associated with its own kernel and all the states that are in a region, by default become a part of the corresponding kernel<br>
	* But each state that has incoming transitions from any other region besides its own regions is removed from its original kernel and placed into a base kernel <b>K0</b>.<br>
	* 
	**/
	public void createKernels()
	{
		LinkedHashSet<String> k0=new LinkedHashSet<String>();
		kernels.put("k0",k0);
		String kernelName="";
		for (Map.Entry<String,  LinkedHashSet<String>> region : regions.entrySet())
		{
			for(String s: region.getValue())
			{
				State state=states.get(s);
				for (Map.Entry<String,  Transition> transition : state.getTransitions().entrySet())
				{
					if(!region.getValue().contains(transition.getValue().getToState()))
					{
						//System.out.println(transition.getValue()+ " from state "+state.getLabel()+" crosses region "+region.getKey());
						//check if state already in k0						
						//add ToState to k0
						if(!k0.contains(transition.getValue().getToState()))
						{
							k0.add(transition.getValue().getToState());
							states.get(transition.getValue().getToState()).setKernelLabel("k0");
						}
						else
						{
							//System.out.println("State "+transition.getValue().getToState()+" already in k0");
						}
					}
				}
			}
			kernelName=region.getKey().replace('r', 'k');
			kernels.put(kernelName, new LinkedHashSet<String>(region.getValue()));
			
		}
		kernels.put("k0",k0);
		
		for(String state : k0)
		{
			for (Map.Entry<String,  LinkedHashSet<String>> region : regions.entrySet())
			{
				kernelName=region.getKey().replace('r', 'k');
				//System.out.println("Kernel : "+kernelName+" "+kernels.get(kernelName));
				if(region.getValue().contains(state))
				{					
					kernels.get(kernelName).remove(state);
					//System.out.println("Removing "+state+" from Kernel "+kernelName +" : "+kernels.get(kernelName));
					break;
				}
				else
				{
					//System.out.println(state+" not in "+region.getKey());
				}
			}
		}

	}
//	
//	String createXia()
//	{
//		
//		String result="X = \n";
//		for(Map.Entry<String, LinkedHashSet<String>> kernel : kernels.entrySet() )
//		{
//			
//			XVector.put(kernel.getKey(),  new LinkedHashMap<String, LinkedHashMap<String, Float>>());
//			result+=kernel.getKey()+" = ";
//			for(Map.Entry<String, State> state : states.entrySet())
//			{
//				LinkedHashMap<String, Float> temp = new LinkedHashMap<String, Float>();
//				for(String actionLabel : actions)
//				{
//					if(kernel.getValue().contains(state.getKey()))
//					{
//						temp.put(actionLabel, returnXia(state.getValue().getLabel(), actionLabel));
//						result+="("+state.getValue().getLabel()+","+actionLabel+") = ";
//						result+=returnXia(state.getValue().getLabel(), actionLabel)+" ";
//					}
//					else
//					{
//						temp.put(actionLabel, (float)0);
//						result+="("+state.getValue().getLabel()+","+actionLabel+") = ";
//						result+="0.0 ";
//					}
//				}
//				XVector.get(kernel.getKey()).put(state.getKey(),temp);
//			}
//			result+="\n";
//		}
//		return result;
//	}
//	/** 
//	* @brief This is a cpoy constructor of the MDP class
//	* s
//	**/
//	float returnXia(String stateName, String actionlabel)
//	{
//		return states.get(stateName).getActionProbability(actionlabel);
//	}
	/** 
	* @brief This method is used to create X, B and C vectors in the MDP
	* 
	* This method creates a X, B and C vectors based on the ordering of the states and actions in the the MDP.
	* 
	**/
	public void createXBCVector()
	{
		for(Map.Entry<String, LinkedHashSet<String>> ki : kernels.entrySet())
		{
			LinkedHashSet<String> xSet=new LinkedHashSet<String>();
			LinkedHashMap<String, Float> bMap=new LinkedHashMap<String, Float>();
			LinkedHashMap<String, Float> cMap=new LinkedHashMap<String, Float>();
			for(String stateKi : ki.getValue())
			{
				State s=states.get(stateKi);
				bMap.put(stateKi, s.getInitialProbability());
				for(Map.Entry<String, Action> action : s.getActionCounts().entrySet())
				{
					xSet.add("("+stateKi+","+action.getKey()+")");
					cMap.put(stateKi+action.getKey(),action.getValue().getReward());
				}
			}
			if(!xSet.isEmpty())
				xVector.put("x"+ki.getKey().substring(1), xSet);
			if(!bMap.isEmpty())
				bVector.put("b"+ki.getKey().substring(1), bMap);
			if(!cMap.isEmpty())
				cVector.put("c"+ki.getKey().substring(1), cMap);
		}
		/*System.out.println("X Vector ");
		for(Map.Entry<String, LinkedHashSet<String>> xSet : xVector.entrySet())
		{
			System.out.println(xSet.getKey());
			for(String value : xSet.getValue())
			{
				System.out.print(value+ "\t");
			}
			System.out.println();
		}
		System.out.println("B Vector ");
		for(Map.Entry<String, LinkedHashMap<String,Float>> bMap : bVector.entrySet())
		{
			System.out.println(bMap.getKey());
			for(Map.Entry<String, Float> value : bMap.getValue().entrySet())
			{
				System.out.print(value+ "\t");
			}
			System.out.println();
		}*/
	}
	/** 
	* @brief This method is used to create A sparse matrix which is used to solve the MDP via LP
	* 
	* This method creates a SparseMatrixHolder to hold the A matrix for the LP of the MDP. The SparseMatrixHolder is created such that it has a map of maps,
	* where each map contains the an Aij for every Ki and Kj if the resultant Aij is a non zero matrix.<br>
	* 
	* NOTE - This method is currently not used.
	* 
	**/
	public void createLP()
	{
		//create f(i,a) = (prob of taking action a from state i) for each state
		//done under State-action count
		//f(i,a)=(State(i).getActionCounts().getValue(key=a))/(State(i).getActionCounts.count());
		
		//create pij(a) = (prob of reaching state j from i on taking action i) for each state
		//pij(a) = State(i).getTransitions().getTransitions(j) where action=a;
		
		//create pfij(a) = sum over all a in A(i) (f(i,a)*pfij(a))\
		//int n=0;
		A=new SparseMatrixHolder();
		//long t1;
		LinkedHashMap<String,Float> temp;
		/*String actionKey=new String();
		String stateActionStateKey=new String();*/
		State s=new State("s0");
		for(Map.Entry<String, LinkedHashSet<String>> ki : kernels.entrySet())
		{
			//t1=System.currentTimeMillis();
			//System.out.print(ki.getKey()+" - " );
			for(Map.Entry<String, LinkedHashSet<String>> kj : kernels.entrySet())
			{
				//System.out.print(kj.getKey()+" ");
				if (ki.getKey().equals("k0") || kj.getKey().equals("k0") || ki.getKey().equals(kj.getKey()))
				{
					//System.out.println("calculating for  : "+ki.getKey()+" "+kj.getKey());
					temp=new LinkedHashMap<String,Float>();
					for(String stateKi : ki.getValue())
					{
						for(String stateKj : kj.getValue())
						{
							s= states.get(stateKj);
							for(Map.Entry<String, Action> action : s.getActionCounts().entrySet())
							{
								/*System.out.println("\n\nstateKi : "+stateKi);
								System.out.println("stateKj : "+stateKj);
								System.out.println("Action : "+action.getKey());
								System.out.println("transition from "+s.getLabel()+" to "+stateKi);
								*/
								//n++;
								/*actionKey=action.getKey();
								stateActionStateKey=stateKj+action.getKey()+stateKi;*/
								if(stateKi.equals(stateKj)) 
								{
									//System.out.println("state ki and kj match; writing : "+(1-(float) (gamma*s.getProbabilityToState(stateKi, action.getKey())))+" at "+stateKj+action.getKey()+stateKi);
									temp.put(stateKj+action.getKey()+stateKi,(1-(float) (gamma*s.getProbabilityToState(stateKi, action.getKey()))));
								}
								else
								{
									//System.out.println("state ki and kj match; writing : "+(0-(float) (gamma*s.getProbabilityToState(stateKi, action.getKey())))+" at "+stateKj+action.getKey()+stateKi);
									temp.put(stateKj+action.getKey()+stateKi,(0-(float) (gamma*s.getProbabilityToState(stateKi, action.getKey()))));
								}
							}
						}
					}
					if(!temp.isEmpty())
					{ 
						//System.out.println("Temp - "+temp);
						A.getMatrixHolder().put((ki.getKey()+kj.getKey()), temp);
					}
				}
			}
			//System.out.println();
			//System.out.println("loop "+ki.getKey()+" time - "+(System.currentTimeMillis()-t1)+" msec");
		}
		/*System.out.println(n);
		System.out.println("Matrices : "+A.getElementCount(1));
		System.out.println("Elements : "+A.getElementCount(2));
		int count=0;
		for (Map.Entry<String, State> entry : states.entrySet())
		{
			count+=entry.getValue().getTransitions().size();
		}
		System.out.println(count*states.size());*/
	}
	/** 
	* @brief This method is used to create A sparse matrix  and generates the MATLAB file A_B_C.mat which is used to solve the MDP via LP
	* 
	* This method creates a SparseMatrixHolder to hold the A matrix for the LP of the MDP. The SparseMatrixHolder is created such that it has a map of maps,
	* where each map contains the an Aij for every Ki and Kj if the resultant Aij is a non zero matrix. 
	* Finally, the values of A SparseMatrixHolder and of B,C and X vectors are written into a .mat file.
	**/
	public void createLPandMatrix()
	{
		//create f(i,a) = (prob of taking action a from state i) for each state
		//done under State-action count
		//f(i,a)=(State(i).getActionCounts().getValue(key=a))/(State(i).getActionCounts.count());
		
		//create pij(a) = (prob of reaching state j from i on taking action i) for each state
		//pij(a) = State(i).getTransitions().getTransitions(j) where action=a;
		
		//create pfij(a) = sum over all a in A(i) (f(i,a)*pfij(a))\
		//int n=0;
		//A=new SparseMatrixHolder();
		ArrayList<MLArray> list = new ArrayList<MLArray>();	// List that has to be added to the Mat file
		//long t1;
		LinkedHashMap<String,Float> temp;
		/*String actionKey=new String();
		String stateActionStateKey=new String();*/
		State s=new State("s0");
		for(Map.Entry<String, LinkedHashSet<String>> ki : kernels.entrySet())
		{
			//t1=System.currentTimeMillis();
			//System.out.print(ki.getKey()+" - " );
			for(Map.Entry<String, LinkedHashSet<String>> kj : kernels.entrySet())
			{
				//System.out.print(kj.getKey()+" ");
				if (ki.getKey().equals("k0") || kj.getKey().equals("k0") || ki.getKey().equals(kj.getKey()))
				{
					//System.out.println("calculating for  : "+ki.getKey()+" "+kj.getKey());
					temp=new LinkedHashMap<String,Float>();
					for(String stateKi : ki.getValue())
					{
						for(String stateKj : kj.getValue())
						{
							s= states.get(stateKj);
							for(Map.Entry<String, Action> action : s.getActionCounts().entrySet())
							{
								/*System.out.println("\n\nstateKi : "+stateKi);
								System.out.println("stateKj : "+stateKj);
								System.out.println("Action : "+action.getKey());
								System.out.println("transition from "+s.getLabel()+" to "+stateKi);
								*/
								//n++;
								/*actionKey=action.getKey();
								stateActionStateKey=stateKj+action.getKey()+stateKi;*/
								if(stateKi.equals(stateKj)) 
								{
									//System.out.println("state ki and kj match; writing : "+(1-(float) (gamma*s.getProbabilityToState(stateKi, action.getKey())))+" at "+stateKj+action.getKey()+stateKi);
									temp.put(stateKj+action.getKey()+stateKi,(1-(float) (gamma*s.getProbabilityToState(stateKi, action.getKey()))));
									
								}
								else
								{
									//System.out.println("state ki and kj match; writing : "+(0-(float) (gamma*s.getProbabilityToState(stateKi, action.getKey())))+" at "+stateKj+action.getKey()+stateKi);
									temp.put(stateKj+action.getKey()+stateKi,(0-(float) (gamma*s.getProbabilityToState(stateKi, action.getKey()))));
								}
							}
						}
					}
					if(!temp.isEmpty())
					{ 
						//System.out.println("Matrix  - "+matrix);
						String Ki=ki.getKey();
						String Kj=kj.getKey();
						int m=kernels.get(Ki).size();
						int n=getStateActionPairCount(Kj);
						int size=0;
						for(Map.Entry<String, Float> entry : temp.entrySet() )
						{
							if(entry.getValue()!=0)
							{
								size++;
							}
						}
						double rowVector[]=new double[size];
						double columnVector[]=new double[size];
						double valueVector[]=new double[size];
						double rowCount[]=new double[1];
						double columnCount[]=new double[1];
						rowCount[0]=m;
						columnCount[0]=n;
						int i,j,k;
						i=0;
						j=0;
						k=0;
						for(Map.Entry<String, Float> entry : temp.entrySet() )
						{
							if(entry.getValue()!=0)
							{
								rowVector[k]=i;
								columnVector[k]=j;
								valueVector[k]= entry.getValue();
								//System.out.println(valueVector[k]+" "+rowVector[k]+" "+columnVector[k]);
								//System.out.println(entry.getValue()+" "+i+" "+j+" "+k);
								k++;

							}
							if(j<n-1)
							{
								j++;
							}
							else
							{
								j=0;
								i++;
							}
						}		
						MLDouble mlDouble = new MLDouble("A"+Ki.substring(1)+Kj.substring(1)+"i",rowVector,1);
						list.add(mlDouble);
						mlDouble = new MLDouble("A"+Ki.substring(1)+Kj.substring(1)+"j",columnVector,1);
						list.add(mlDouble);
						mlDouble = new MLDouble("A"+Ki.substring(1)+Kj.substring(1)+"v",valueVector,1);
						list.add(mlDouble);
						mlDouble = new MLDouble("A"+Ki.substring(1)+Kj.substring(1)+"row",rowCount,1);
						list.add(mlDouble);
						mlDouble = new MLDouble("A"+Ki.substring(1)+Kj.substring(1)+"col",columnCount,1);
						list.add(mlDouble);
						//A.getMatrixHolder().put((ki.getKey()+kj.getKey()), temp);
						
					}
				}
			}
			//System.out.println();
			//System.out.println("loop "+ki.getKey()+" time - "+(System.currentTimeMillis()-t1)+" msec");
		}
		try 
		{
			PrintWriter writer;
			writer = new PrintWriter("XVector.txt", "UTF-8");
			writer.println("X Vector - ");
			for(Map.Entry<String, LinkedHashSet<String>> xSet : this.xVector.entrySet())
			{
				writer.println(xSet.getKey());
				for(String value : xSet.getValue())
				{
					writer.print(value+ "\t");
				}
				writer.println();
			}
			writer.close();
			
			MLDouble mlDouble;
			int i;
			for(Map.Entry<String, LinkedHashMap<String,Float>> bMap : bVector.entrySet())
			{
				double bVector[]=new double[bMap.getValue().size()];
				i=0;
				for(Map.Entry<String, Float> value : bMap.getValue().entrySet())
				{
					bVector[i++]=value.getValue();
				}
				mlDouble = new MLDouble("B"+bMap.getKey().substring(1),bVector,1);
				list.add(mlDouble);
			}
			for(Map.Entry<String, LinkedHashMap<String,Float>> cMap : cVector.entrySet())
			{
				double cVector[]=new double[cMap.getValue().size()];
				i=0;
				for(Map.Entry<String, Float> value : cMap.getValue().entrySet())
				{
					cVector[i++]=value.getValue();
				}
				mlDouble = new MLDouble("C"+cMap.getKey().substring(1),cVector,1);
				list.add(mlDouble);
			}
			new MatFileWriter( "A_B_C.mat", list );
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		/*System.out.println(n);
		System.out.println("Matrices : "+A.getElementCount(1));
		System.out.println("Elements : "+A.getElementCount(2));
		int count=0;
		for (Map.Entry<String, State> entry : states.entrySet())
		{
			count+=entry.getValue().getTransitions().size();
		}
		System.out.println(count*states.size());*/
	}
	
	
	/** 
	* @brief This method returns the number of integer count of the number of state-action pairs in a kernel.
	* 
	**/
	public int getStateActionPairCount(String kernel)
	{
		int result=0;
		for(String state : kernels.get(kernel))
		{
			result+=states.get(state).getActionCounts().size();
		}
		return result;
	}
	
	/** 
	* @brief This method converts the SparseMatrixHolder A into a .Mat file.
	* 
	**/
	public void createAMatrix()
	{
		ArrayList<MLArray> AList = new ArrayList<MLArray>();	// List that has to be added to the Mat file
		for(Map.Entry<String, LinkedHashMap<String, Float>> matrix : A.getMatrixHolder().entrySet() )
		{
			String Ki,Kj;
			Ki=matrix.getKey().substring(0, 2);
			Kj=matrix.getKey().substring(2);
			int m=kernels.get(Ki).size();
			int n=getStateActionPairCount(Kj);
			double src[][]=new double[m][n];
			int i,j;
			i=0;
			j=0;
			for(Map.Entry<String, Float> entry : matrix.getValue().entrySet() )
			{
				src[i][j]=entry.getValue();
				if(j<n-1)
				{
					j++;
				}
				else
				{
					j=0;
					i++;
				}
			}
			MLDouble mlDouble = new MLDouble("A"+Ki.substring(1,2)+Kj.substring(1,2),src);
			AList.add(mlDouble);
		}
		try 
		{
			new MatFileWriter( "A.mat", AList );
		} 
		catch (IOException e) 
		{
			System.out.println(e.toString());
		}
	}
	/** 
	* @brief This method converts the SparseMatrixHolder A into a .Mat file with sparse matrix representation used in MATLAB.
	* 
	**/
	public void createSparseAMatrix()
	{
		ArrayList<MLArray> list = new ArrayList<MLArray>();	// List that has to be added to the Mat file
		for(Map.Entry<String, LinkedHashMap<String, Float>> matrix : A.getMatrixHolder().entrySet() )
		{
			//System.out.println("Matrix  - "+matrix);
			String Ki,Kj;
			Ki=matrix.getKey().substring(0, matrix.getKey().lastIndexOf('k'));
			Kj=matrix.getKey().substring(matrix.getKey().lastIndexOf('k'));
			int m=kernels.get(Ki).size();
			int n=getStateActionPairCount(Kj);
			int size=0;
			for(Map.Entry<String, Float> entry : matrix.getValue().entrySet() )
			{
				if(entry.getValue()!=0)
				{
					size++;
				}
			}
			double rowVector[]=new double[size];
			double columnVector[]=new double[size];
			double valueVector[]=new double[size];
			double rowCount[]=new double[1];
			double columnCount[]=new double[1];
			rowCount[0]=m;
			columnCount[0]=n;
			int i,j,k;
			i=0;
			j=0;
			k=0;
			for(Map.Entry<String, Float> entry : matrix.getValue().entrySet() )
			{
				if(entry.getValue()!=0)
				{
					rowVector[k]=i;
					columnVector[k]=j;
					valueVector[k]= entry.getValue();
					//System.out.println(valueVector[k]+" "+rowVector[k]+" "+columnVector[k]);
					//System.out.println(entry.getValue()+" "+i+" "+j+" "+k);
					k++;

				}
				if(j<n-1)
				{
					j++;
				}
				else
				{
					j=0;
					i++;
				}
			}		
			/*System.out.println("A"+Ki.substring(1,2)+Kj.substring(1,2) + " m*n = "+(m*n)+" k = "+k);
			System.out.print("Row Vector : ");
			for(int l=0;l<k;l++)
			{
				System.out.print(rowVector[l]+"\t");
			}
			System.out.println();
			System.out.print("Column Vector : ");
			for(int l=0;l<k;l++)
			{
				System.out.print(columnVector[l]+"\t");
			}
			System.out.println();
			System.out.print("Value Vector : ");
			for(int l=0;l<k;l++)
			{
				System.out.print(valueVector[l]+"\t");
			}
			System.out.println();*/
			//System.out.println("m * n = "+(m*n)+"\t k = "+k);
			MLDouble mlDouble = new MLDouble("A"+Ki.substring(1)+Kj.substring(1)+"i",rowVector,1);
			list.add(mlDouble);
			mlDouble = new MLDouble("A"+Ki.substring(1)+Kj.substring(1)+"j",columnVector,1);
			list.add(mlDouble);
			mlDouble = new MLDouble("A"+Ki.substring(1)+Kj.substring(1)+"v",valueVector,1);
			list.add(mlDouble);
			mlDouble = new MLDouble("A"+Ki.substring(1)+Kj.substring(1)+"row",rowCount,1);
			list.add(mlDouble);
			mlDouble = new MLDouble("A"+Ki.substring(1)+Kj.substring(1)+"col",columnCount,1);
			list.add(mlDouble);
		}
		try 
		{
			PrintWriter writer;
			writer = new PrintWriter("XVector.txt", "UTF-8");
			writer.println("X Vector - ");
			for(Map.Entry<String, LinkedHashSet<String>> xSet : this.xVector.entrySet())
			{
				writer.println(xSet.getKey());
				for(String value : xSet.getValue())
				{
					writer.print(value+ "\t");
				}
				writer.println();
			}
			writer.close();
			
			MLDouble mlDouble;
			int i;
			for(Map.Entry<String, LinkedHashMap<String,Float>> bMap : bVector.entrySet())
			{
				double bVector[]=new double[bMap.getValue().size()];
				i=0;
				for(Map.Entry<String, Float> value : bMap.getValue().entrySet())
				{
					bVector[i++]=value.getValue();
				}
				mlDouble = new MLDouble("B"+bMap.getKey().substring(1),bVector,1);
				list.add(mlDouble);
			}
			for(Map.Entry<String, LinkedHashMap<String,Float>> cMap : cVector.entrySet())
			{
				double cVector[]=new double[cMap.getValue().size()];
				i=0;
				for(Map.Entry<String, Float> value : cMap.getValue().entrySet())
				{
					cVector[i++]=value.getValue();
				}
				mlDouble = new MLDouble("C"+cMap.getKey().substring(1),cVector,1);
				list.add(mlDouble);
			}
			new MatFileWriter( "A_B_C.mat", list );
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/** 
	* @brief This method converts the MDP object into a String object for convinient displaying on the console/GUI.
	* 
	**/
	@Override
	public String toString()
	{
		String result="MDP \n";
		for (Map.Entry<String, State> entry : states.entrySet())
		{
			result+=entry.getValue().toString();
		}
		result+="Regions - \n";
		for (Map.Entry<String,  LinkedHashSet<String>> entry : regions.entrySet())
		{
			result+=entry.getKey()+"\n";
			for(String s : entry.getValue())
			{
				result+=s+" ";
			}
			result+="\n";
		
		}
		result+="Kernels - \n";
		for (Map.Entry<String,  LinkedHashSet<String>> entry : kernels.entrySet())
		{
			result+=entry.getKey()+"\n";
			for(String s : entry.getValue())
			{
				result+=s+" ";
			}
			result+="\n";
		
		}
		result+=actions;
		return result;
	}
	
}
