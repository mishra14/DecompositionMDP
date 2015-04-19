package mdp.util;

import java.util.LinkedHashMap;
import java.util.Map;

/** 
* @file SparseMatrixHolder.java 
* 
* @brief This file contains the source code SparseMatrixHolder class
* 
* @author Ankit Mishra
*
* @date May, 1 2015
* 
**/

/** 
* @class SparseMatrixHolder
* 
* @brief This class contains the source code SparseMatrixHolder 
* 
**/

public class SparseMatrixHolder 
{
	private Map<String, LinkedHashMap<String, Float>> matrixHolder ;
	/** 
	* @brief This is the default constructor of SparseMatrixHolder class 
	* 
	**/
	public SparseMatrixHolder()
	{
		matrixHolder=new LinkedHashMap<String, LinkedHashMap<String, Float>>();
	}
	/** 
	* @brief This method is a getter for the matrixHolder HashMap
	* 
	**/
	public Map<String, LinkedHashMap<String, Float>> getMatrixHolder() {
		return matrixHolder;
	}
	/** 
	* @brief This method is a setter for the matrixHolder HashMap
	* 
	**/
	public void setMatrixHolder(Map<String, LinkedHashMap<String, Float>> matrixHolder) {
		this.matrixHolder = matrixHolder;
	}
	/** 
	* @brief This method converts the SparseMatrixHolder object into  STring object for displaying on the console/GUI
	* 
	**/
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
