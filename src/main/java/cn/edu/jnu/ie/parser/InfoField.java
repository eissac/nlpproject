package cn.edu.jnu.ie.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class represents a multi-valued field with a weight. 
 * Values are arbitrary objects.
 */
public class InfoField  {
  private float weight;
  private List<Object> values = new ArrayList<Object>();
  
  public InfoField() { }
  
  public InfoField(Object value) {
  if (value instanceof Collection) {
      values.addAll((Collection<?>)value);
    } else {
      values.add(value);
    }
  }
    
  public void add(Object value) {
    values.add(value);
  }
  
  public List<Object> getValues() {
    return values;
  }
  public void reset() {
    values.clear();
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    InfoField result = (InfoField)super.clone();
    result.values = values;
    return result;
  }
  public String toString() {
	  StringBuilder sb = new StringBuilder();
	  if(values.size()==1)
		  return values.get(0).toString();
    return values.toString();
  }

}
