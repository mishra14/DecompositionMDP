package ese.seas.upenn.edu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;

public class MDP 
{
	private Map<String, State> states;
	private Map<String, SortedSet<String>> regions;
	private Map<String, SortedSet<String>> kernels;
	private ArrayList<String> actions;
	private Map<String, LinkedHashMap<String,LinkedHashMap<String, Float>>> XVector;
	private SparseMatrixHolder A;
	private final float gamma=(float)0.9;
	
	public MDP() 
	{
		super();
		states=new LinkedHashMap<String, State>();
		regions=new LinkedHashMap<String,  SortedSet<String>>();
		kernels=new LinkedHashMap<String,  SortedSet<String>>();
		actions=new ArrayList<String>();
		XVector=new LinkedHashMap<String, LinkedHashMap<String,LinkedHashMap<String,Float>>>();
	}
	public Map<String, State> getStates() 
	{
		return states;
	}
	public void setStates(Map<String, State> states) 
	{
		this.states = states;
	}
	
	public Map<String,  SortedSet<String>> getRegions() {
		return regions;
	}

	public void setRegions(Map<String,  SortedSet<String>> regions) {
		this.regions = regions;
	}

	public Map<String,  SortedSet<String>> getKernels() {
		return kernels;
	}

	public void setKernels(Map<String,  SortedSet<String>> kernels) {
		this.kernels = kernels;
	}

	public ArrayList<String> getActions() {
		return actions;
	}

	public void setActions(ArrayList<String> actions) {
		this.actions = actions;
	}
	
	public Map<String, LinkedHashMap<String, LinkedHashMap<String, Float>>> getXVector() {
		return XVector;
	}

	public void setXVector(
			Map<String, LinkedHashMap<String, LinkedHashMap<String, Float>>> xVector) {
		XVector = xVector;
	}
	public SparseMatrixHolder getA() {
		return A;
	}
	public void setA(SparseMatrixHolder a) {
		A = a;
	}
	
	//Util code begins
	public State addState(State s)
	{
		return states.put(s.getLabel(),s);
	}
	
	public void buildFromFile(String filename) throws Exception
	{
		BufferedReader in;
		String line, ss[];

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
			while(line.contains("//") || ( line != null && line.isEmpty()) || (!line.toLowerCase().contains("transitions")))	//ignore initial set of comments
			{
				line=in.readLine();
			}
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
		        	if(s.getActionCounts().containsKey(ss[1]))
		        	{
		        		s.getActionCounts().put(ss[1], s.getActionCounts().get(ss[1]).intValue()+1);
		        	}
		        	else
		        	{
		        		s.getActionCounts().put(ss[1], 1);
		        	}
		        }
		        else
		        {
		        	System.out.println(t+" Already Present");
		        }
		    }
			while(line.contains("//") || ( line != null && line.isEmpty()) || (!line.toLowerCase().contains("regions")))	//ignore initial set of comments
			{
				line=in.readLine();
			}
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
					Set<String> statesNotInAnyRegion=new TreeSet<String>();
					for(String stateLabel : ss)
					{
						String parentRegion="NA";						
						if(states.containsKey(stateLabel))
						{
							for (Map.Entry<String,  SortedSet<String>> entry : regions.entrySet())
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
						regions.put(regionLabel, (SortedSet<String>) statesNotInAnyRegion);
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
			// Close file
			in.close();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		} 

	}
	
	void createKernels()
	{
		Set<String> k0=new TreeSet<String>();
		kernels.put("K0",(SortedSet<String>) k0);
		String kernelName="";
		for (Map.Entry<String,  SortedSet<String>> region : regions.entrySet())
		{
			for(String s: region.getValue())
			{
				State state=states.get(s);
				for (Map.Entry<String,  Transition> transition : state.getTransitions().entrySet())
				{
					if(!region.getValue().contains(transition.getValue().getToState()))
					{
						//System.out.println(transition.getValue()+ " from state "+state.getLabel()+" crosses region "+region.getKey());
						//check if state already in K0						
						//add ToState to K0
						if(!k0.contains(transition.getValue().getToState()))
						{
							k0.add(transition.getValue().getToState());
						}
						else
						{
							System.out.println("State "+transition.getValue().getToState()+" already in K0");
						}
					}
				}
			}
			kernelName=region.getKey().replace('r', 'k');
			kernels.put(kernelName, new TreeSet<String>(region.getValue()));
			
		}
		kernels.put("K0",(SortedSet<String>) k0);
		
		for(String state : k0)
		{
			for (Map.Entry<String,  SortedSet<String>> region : regions.entrySet())
			{
				kernelName=region.getKey().replace('r', 'k');
				//System.out.println("Kernel : "+kernels.get(kernelName));
				if(region.getValue().contains(state))
				{					
					kernels.get(kernelName).remove(state);
					//System.out.println("Removing "+state+" from Kernel "+kernelName +" : "+kernels.get(kernelName));
					break;
				}
				else
				{
					System.out.println(state+" not in "+region.getKey());
				}
			}
		}

	}
	
	//TODO check if this func is used
	String createXia()
	{
		//TODO remove the display part
		
		String result="X = \n";
		for(Map.Entry<String, SortedSet<String>> kernel : kernels.entrySet() )
		{
			
			XVector.put(kernel.getKey(),  new LinkedHashMap<String, LinkedHashMap<String, Float>>());
			result+=kernel.getKey()+" = ";
			for(Map.Entry<String, State> state : states.entrySet())
			{
				LinkedHashMap<String, Float> temp = new LinkedHashMap<String, Float>();
				for(String actionLabel : actions)
				{
					if(kernel.getValue().contains(state.getKey()))
					{
						temp.put(actionLabel, returnXia(state.getValue().getLabel(), actionLabel));
						result+="("+state.getValue().getLabel()+","+actionLabel+") = ";
						result+=returnXia(state.getValue().getLabel(), actionLabel)+" ";
					}
					else
					{
						temp.put(actionLabel, (float)0);
						result+="("+state.getValue().getLabel()+","+actionLabel+") = ";
						result+="0.0 ";
					}
				}
				XVector.get(kernel.getKey()).put(state.getKey(),temp);
			}
			result+="\n";
		}
		return result;
	}
	
	float returnXia(String stateName, String actionlabel)
	{
		return states.get(stateName).getActionProbability(actionlabel);
	}
	
	void createLP()
	{
		//create f(i,a) = (prob of taking action a from state i) for each state
		//done under State-action count
		//f(i,a)=(State(i).getActionCounts().getValue(key=a))/(State(i).getActionCounts.count());
		
		//create pij(a) = (prob of reaching state j from i on taking action i) for each state
		//pij(a) = State(i).getTransitions().getTransitions(j) where action=a;
		
		//create pfij(a) = sum over all a in A(i) (f(i,a)*pfij(a))\
				 
		A=new SparseMatrixHolder();
		
		for(Map.Entry<String, SortedSet<String>> ki : kernels.entrySet())
		{
			for(Map.Entry<String, SortedSet<String>> kj : kernels.entrySet())
			{
				LinkedHashMap<String,Float> temp=new LinkedHashMap<String,Float>();
				for(String stateKi : ki.getValue())
				{
					for(String stateKj : kj.getValue())
					{
						State s= states.get(stateKj);
						for(Map.Entry<String, Integer> action : s.getActionCounts().entrySet())
						{
							/*System.out.println("\n\nstateKi : "+stateKi);
							System.out.println("stateKj : "+stateKj);
							System.out.println("Action : "+action.getKey());
							System.out.println("transition from "+s.getLabel()+" to "+stateKi);*/
							if(stateKi.equals(stateKj))
							{
								//System.out.println("state ki and kj match; writing : "+(1-(float) (gamma*s.getProbabilityToState(stateKi, action.getKey())))+" at "+stateKi+action.getKey()+stateKj);
								temp.put(stateKi+action.getKey()+stateKj,(1-(float) (gamma*s.getProbabilityToState(stateKi, action.getKey()))));
							}
							else
							{
								//System.out.println("state ki and kj match; writing : "+(0-(float) (gamma*s.getProbabilityToState(stateKi, action.getKey())))+" at "+stateKi+action.getKey()+stateKj);
								temp.put(stateKi+action.getKey()+stateKj,(0-(float) (gamma*s.getProbabilityToState(stateKi, action.getKey()))));
							}
						}
					}
				}
				if(!temp.isEmpty())
				{
					A.getMatrixHolder().put((ki.getKey()+kj.getKey()), temp);
				}
			}
		}		
	}
	
	
	public void createLPQuick()
	{

		//create f(i,a) = (prob of taking action a from state i) for each state
		//done under State-action count
		//f(i,a)=(State(i).getActionCounts().getValue(key=a))/(State(i).getActionCounts.count());
		
		//create pij(a) = (prob of reaching state j from i on taking action i) for each state
		//pij(a) = State(i).getTransitions().getTransitions(j) where action=a;
		
		//create pfij(a) = sum over all a in A(i) (f(i,a)*pfij(a))\
				
		A=new SparseMatrixHolder();
		
		for(Map.Entry<String, SortedSet<String>> ki : kernels.entrySet())
		{
			for(Map.Entry<String, SortedSet<String>> kj : kernels.entrySet())
			{
				LinkedHashMap<String,Float> temp=new LinkedHashMap<String,Float>();
				for(String stateKi : ki.getValue())
				{
					for(String stateKj : kj.getValue())
					{
						State s= states.get(stateKj);
						for(Map.Entry<String, Integer> action : s.getActionCounts().entrySet())
						{
							/*System.out.println("\n\nstateKi : "+stateKi);
							System.out.println("stateKj : "+stateKj);
							System.out.println("Action : "+action.getKey());
							System.out.println("transition from "+s.getLabel()+" to "+stateKi);*/
							if(stateKi.equals(stateKj))
							{
								//System.out.println("state ki and kj match; writing : "+(1-(float) (gamma*s.getProbabilityToState(stateKi, action.getKey())))+" at "+stateKi+action.getKey()+stateKj);
								temp.put(stateKi+action.getKey()+stateKj,(1-(float) (gamma*s.getProbabilityToState(stateKi, action.getKey()))));
							}
							else
							{
								//System.out.println("state ki and kj match; writing : "+(0-(float) (gamma*s.getProbabilityToState(stateKi, action.getKey())))+" at "+stateKi+action.getKey()+stateKj);
								temp.put(stateKi+action.getKey()+stateKj,(0-(float) (gamma*s.getProbabilityToState(stateKi, action.getKey()))));
							}
						}
					}
				}
				if(!temp.isEmpty())
				{
					A.getMatrixHolder().put((ki.getKey()+kj.getKey()), temp);
				}
			}
		}		
	
	}
	
	public int getStateActionPairCount(String kernel)
	{
		int result=0;
		for(String state : kernels.get(kernel))
		{
			result+=states.get(state).getActionCounts().size();
		}
		return result;
	}
	
	public void createAMatrix()
	{
		ArrayList<MLArray> list = new ArrayList<MLArray>();	// List that has to be added to the Mat file
		for(Map.Entry<String, LinkedHashMap<String, Float>> matrix : A.getMatrixHolder().entrySet() )
		{
			String Ki,Kj;
			Ki=matrix.getKey().substring(0, 2);
			Kj=matrix.getKey().substring(2);
			System.out.println(matrix.getKey()+" - "+Ki+" "+Kj);
			int m=kernels.get(Ki).size();
			int n=getStateActionPairCount(Kj);
			double src[][]=new double[m][n];
			int i,j;
			i=0;
			j=0;
			System.out.println("Hash Length : "+matrix.getValue().size()+" Array Size : "+m+" x "+n+"="+(m*n));
			System.out.println(matrix.getValue());
			for(Map.Entry<String, Float> entry : matrix.getValue().entrySet() )
			{
				src[i][j]=entry.getValue();
				System.out.print(src[i][j]);
				if(j<n-1)
				{
					System.out.print("\t");
					j++;
				}
				else
				{
					System.out.println();
					j=0;
					i++;
				}
			}
			MLDouble mlDouble = new MLDouble(Ki.substring(1,2)+Kj.substring(1,2),src);
			list.add(mlDouble);
		}
		try 
		{
			new MatFileWriter( "A.mat", list );
		} 
		catch (IOException e) 
		{
			System.out.println(e.toString());
		}
	}
	
	@Override
	public String toString()
	{
		String result="MDP \n";
		for (Map.Entry<String, State> entry : states.entrySet())
		{
			result+=entry.getValue().toString();
		}
		for (Map.Entry<String,  SortedSet<String>> entry : regions.entrySet())
		{
			result+=entry.getKey()+"\n";
			for(String s : entry.getValue())
			{
				result+=s+" ";
			}
			result+="\n";
		
		}
		for (Map.Entry<String,  SortedSet<String>> entry : kernels.entrySet())
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
