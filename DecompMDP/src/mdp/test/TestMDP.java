package mdp.test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class TestMDP 
{
	private static Map<Integer, LinkedHashMap<String, LinkedHashMap<Integer, Double>>> transitions = new LinkedHashMap<Integer, LinkedHashMap<String, LinkedHashMap<Integer,Double>>>();
	private static Set<Integer> blockedStates=new HashSet<Integer>();
	private static int heaven = 0;
	private static int hell =0;
	public static void init(int n)
	{
		heaven= n * (n-1);
		hell=n;
		for(int i=0;i<n-1;i++)
		{
			int rand=(int) (Math.random()*(n*n));
			while(rand==hell || rand==heaven || blockedStates.contains(rand))
			{
				rand=(int) (Math.random()*(n*n));
			}
			blockedStates.add(rand);
		}
		System.out.println("-------GRID WORLD--------");
		System.out.println("Size - "+(n*n));
		System.out.println("End State -"+(n*n));
		System.out.println("Heaven State - "+heaven);
		System.out.println("Hell State - "+hell);
		System.out.println("Blocked States - ");
		for(int s : blockedStates)
		{
			System.out.print(s+"\t");
		}
		System.out.println();
		System.out.println("-------------------------");
	}
	public static void createMDP()
	{
		String actionList[]={"up","down","left","right"};
		double probList[]={0.8,0.1,0.1};
		PrintWriter writer;
		try 
		{
			writer = new PrintWriter("testMDP4.txt", "UTF-8");
			int stateCount=15;
			writer.print("states {");
			for (int i=0;i<stateCount;i++)
			{
				writer.print("s"+i);
				if(i<(stateCount-1))
					writer.print(",");
			}
			writer.println("}");
			writer.println("initial\n{s0,1}\nend");
			String to, from;
			writer.println("transitions");
			for (int i=0;i<stateCount;i++)
			{
				from="s"+i;
				if(i==4 || i==10)
				{
					to="s14";
					writer.println("{"+from+",end,1,"+to+"}");
					continue;
				}
				for(String action : actionList)
				{
					for(int j=0;j<3;j++)
					{
						double prob=probList[j];
						int res=i;
						if(j==0)
						{
							if(action.equals("up"))
							{
								if(i>=3 && i<=8)
								{
									res=i+3;
								}
								else
								{
									if(i==2)
										res=i;
									else
										res=i+4;
								}
							}
							else if(action.equals("down"))
							{
								if(i>=6 && i<=11)
								{
									res=i-3;
								}
								else
								{
									if(i==12)
										res=i;
									else
										res=i-4;
								}
							}
							else if(action.equals("left"))
							{
								if(i==3||i==5||i==6||i==8||i==9||i==13)
								{
									res=i;
								}
								else
								{
									res=i+1;
								}
							}
							else
							{
								if(i==0||i==4||i==6||i==7||i==9||i==10)
								{
									res=i;
								}
								else
								{
									res=i-1;
								}
							}
						}
						else if (j==1)
						{
							if(action.equals("up"))
							{
								if(i==3||i==5||i==6||i==8||i==9||i==13)
								{
									res=i;
								}
								else
								{
									res=i+1;
								}
							}
							else if(action.equals("down"))
							{
								if(i==0||i==4||i==6||i==7||i==9||i==10)
								{
									res=i;
								}
								else
								{
									res=i-1;
								}
							}
							else if(action.equals("left"))
							{
								if(i>=6 && i<=11)
								{
									res=i-3;
								}
								else
								{
									if(i==12)
										res=i;
									else
										res=i-4;
								}
							}
							else
							{
								if(i>=3 && i<=8)
								{
									res=i+3;
								}
								else
								{
									if(i==2)
										res=i;
									else
										res=i+4;
								}
							}
						}
						else
						{
							if(action.equals("up"))
							{
								if(i==0||i==4||i==6||i==7||i==9||i==10)
								{
									res=i;
								}
								else
								{
									res=i-1;
								}
								
							}
							else if(action.equals("down"))
							{
								if(i==3||i==5||i==6||i==8||i==9||i==13)
								{
									res=i;
								}
								else
								{
									res=i+1;
								}
								
							}
							else if(action.equals("left"))
							{
								if(i>=3 && i<=8)
								{
									res=i+3;
								}
								else
								{
									if(i==2)
										res=i;
									else
										res=i+4;
								}
								
							}
							else
							{
								if(i>=6 && i<=11)
								{
									res=i-3;
								}
								else
								{
									if(i==12)
										res=i;
									else
										res=i-4;
								}
							}
						}
						if(res<0 || res>14)
							to="s"+i;
						else
							to="s"+res;
						writer.println("{"+from+","+action+","+Double.toString(prob)+","+to+"}");
					}
					
				}
				
			}
			writer.println("end");
			
			writer.println("rewards");
			for (int i=0;i<stateCount;i++)
			{
				from="s"+i;
				if(i==4 || i==10)
				{
					writer.println("{"+from+",end,1}");
					continue;
				}
				for(String action : actionList)
				{
					writer.println("{"+from+","+action+",-0.1}");
					
				}
				
			}
			writer.println("end");
			writer.println("regions=3");
			writer.println("end");
			writer.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void createMDP(int n, int r)
	{
		init(n);
		String actionList[]={"up","down","left","right"};
		double probList[]={0.8,0.1,0.1};
		PrintWriter writer;
		try 
		{
			writer = new PrintWriter("testMDP4.txt", "UTF-8");
			int stateCount=n*n;
			writer.print("states {");
			for (int i=0;i<=stateCount;i++)
			{
				writer.print("s"+i);
				if(i<(stateCount))
					writer.print(",");
			}
			writer.println("}");
			writer.println("initial\n{s0,1}\nend");
			int res=0;
			for (int i=0;i<stateCount;i++)
			{
				transitions.put(i,new LinkedHashMap<String, LinkedHashMap<Integer, Double>>());
				for(String action:actionList)
				{
					for(int j=0;j<3;j++)
					{
						if(!transitions.get(i).containsKey(action))
						{
							transitions.get(i).put(action, new LinkedHashMap<Integer,Double>());
						}
						if((action.equals("down") && (i<n)) || (action.equals("left") && (i%n==3)) || (action.equals("up") && ((i<n*n) && (i>=n*(n-1)))) || (action.equals("right") && (i%n==0) && (i!=n*n)))
						{
							transitions.get(i).get(action).put(i, 1.0);
							continue;
						}
						else
						{
							
							if(action.equals("up"))
							{							
								res=(j==0)?(i+n):((j==1)?(i+n+1):(i+n-1));
								if(res<0 || res>stateCount || (res/n!=(i/n + 1)) || blockedStates.contains(res))
								{
									res=i;
								}								
							}
							else if(action.equals("down"))
							{
								res=(j==0)?(i-n):((j==1)?(i-n+1):(i-n-1));
								if(res<0 || res>stateCount || (res/n!=(i/n - 1)) || blockedStates.contains(res))
								{
									res=i;
								}
							}
							else if(action.equals("left"))
							{
								res=(j==0)?(i+1):((j==1)?(i+n+1):(i-n+1));
								if(res<0 || res>stateCount || (res%n != i%n + 1) || blockedStates.contains(res))
								{
									res=i;
								}
							}
							else
							{
								res=(j==0)?(i-1):((j==1)?(i+n-1):(i-n-1));
								if(res<0 || res>stateCount || (res%n != i%n - 1) || blockedStates.contains(res))
								{
									res=i;
								}
							}
							if(transitions.get(i).get(action).containsKey(res))
							{
								transitions.get(i).get(action).put(res, (transitions.get(i).get(action).get(res)+probList[j]));
							}
							else
							{
								transitions.get(i).get(action).put(res,probList[j]);
							}
							
						}
					}
				}	
			}
			writer.println("transitions");
			for(Map.Entry<Integer, LinkedHashMap<String, LinkedHashMap<Integer, Double>>> from : transitions.entrySet())
			{
				for(Map.Entry<String, LinkedHashMap<Integer, Double>> action : from.getValue().entrySet())
				{
					for(Map.Entry<Integer, Double> to : action.getValue().entrySet())
					{
						writer.println("{s"+from.getKey()+","+action.getKey()+","+Double.toString(to.getValue())+",s"+to.getKey()+"}");
					}
				}
			}
			//writer.println("{"+from+","+action+","+Double.toString(prob)+","+to+"}");
			writer.println("end");
			writer.println("rewards");
			for (int i=0;i<stateCount;i++)
			{
				if(i==heaven)
				{
					writer.println("{s"+i+",end,1}");
					continue;
				}
				else if(i==hell)
				{
					writer.println("{s"+i+",end,-1}");
					continue;
				}
				for(String action : actionList)
				{
					writer.println("{s"+i+","+action+",-0.1}");
					
				}
			}
			writer.println("end");
			writer.println("regions="+r);
			writer.println("end");
			writer.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
