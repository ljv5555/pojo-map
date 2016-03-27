/**
 * 
 */
package com.stc.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @author joe
 * @param <V>
 * @param <K>
 *
 */
public class JSONMap<K> extends Hashtable<String,Object> {
	private K o = null;
	public JSONMap(K o)
	{
		super();
		this.o=o;
	}
	public JSONMap<K> withField(String fieldName)
	{
		String fieldNameUpper = fieldName.substring(1,2).toUpperCase()+fieldName.substring(2);
		Method[] methods = o.getClass().getMethods();
		try {
		for(Method m : methods)
		{
			if(("is"+fieldNameUpper).equals(m.getName())||("get"+fieldNameUpper).equals(m.getName()))
			{ 
				this.put(fieldName, m.invoke(o));
			}
		}
		Field[] fields = o.getClass().getFields();
		for(Field field : fields)
		{
			if(field.getName().equals(fieldName))
			{
				this.put(fieldName, field.get(o));
			}
		}
		}catch(Exception e){e.printStackTrace();}
		return this;
	}
	public JSONMap<K> withAllFields()
	{
		JSONMap<K> jm = new JSONMap<K>(this.o);
		ArrayList<String> fieldNames=new ArrayList<String>();
		for(Method m : o.getClass().getMethods())
		{
			if(m.getName().startsWith("is"))
			{
				fieldNames.add(m.getName().substring(3,4).toLowerCase()+m.getName().substring(4));
			}
			else if(m.getName().startsWith("get"))
			{
				fieldNames.add(m.getName().substring(4,5).toLowerCase()+m.getName().substring(5));
				
			}
		}
		for(Field f : o.getClass().getFields()){fieldNames.add(f.getName());}
		for(String fieldName : fieldNames)
		{
			jm = jm.withField(fieldName);
		}
		this.putAll(jm);
		return this;
	}

}
