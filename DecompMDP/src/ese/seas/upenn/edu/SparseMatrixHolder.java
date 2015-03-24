package ese.seas.upenn.edu;

import java.util.LinkedHashMap;
import java.util.Map;

public class SparseMatrixHolder 
{
	private Map<String, LinkedHashMap<String, Float>> matrixHolder;
	
	public SparseMatrixHolder()
	{
		matrixHolder=new LinkedHashMap<String, LinkedHashMap<String, Float>>();
	}

	public Map<String, LinkedHashMap<String, Float>> getMatrixHolder() {
		return matrixHolder;
	}

	public void setMatrixHolder(Map<String, LinkedHashMap<String, Float>> matrixHolder) {
		this.matrixHolder = matrixHolder;
	}

	@Override
	public String toString() 
	{
		String result="";
		for(Map.Entry<String, LinkedHashMap<String, Float>> matrix : matrixHolder.entrySet())
		{
			result+=matrix.getKey()+"=";
			for(Map.Entry<String, Float> value : matrix.getValue().entrySet())
			{
				result+=value.getKey()+"="+value.getValue()+ " ";
			}
			result+="\n";
		}
		return result;
	}
	
	
}
