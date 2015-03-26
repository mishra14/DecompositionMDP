package ese.seas.upenn.edu;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class PlanarSeparator 
{
	private static HashSet<String> seenStates;
	private static LinkedHashMap<Integer, Set<String>> layers;
	private static int layerCount;
	
	static void BFSLayerGeneration(String initialState, MDP mdp)
	{
		int vertexCount=mdp.getStates().size();
		int currentCount=0;
		HashSet<String> current=new HashSet<String>();
		HashSet<String> next=new HashSet<String>();
		seenStates=new HashSet<String>();
		layers = new LinkedHashMap<Integer, Set<String>>();	
		current.add(initialState);
		layers.put(layerCount, current);
		layerCount++;
		next.addAll(mdp.getStates().get(initialState).getNextStates());
		while(!current.isEmpty())
		{
			//System.out.println(current);
			for(String state : current)
			{
				if(!seenStates.contains(state))
				{
					next.addAll(mdp.getStates().get(state).getNextStates());
					seenStates.add(state);
				}
			}
			next.removeAll(seenStates);
			if(!next.isEmpty())
			{	
				if((currentCount<(vertexCount/2))&&((currentCount+next.size())>(vertexCount/2)))
				{
					System.out.println("Median Layer : "+layerCount);
				}
				currentCount+=next.size();
				layers.put(layerCount, next);
				if(layers.get(layerCount).size()<5)
				layerCount++;
			}
			current=new HashSet<String>(next);
			next=new HashSet<String>();
		}
		System.out.println(layers);
	}
	
}
