package ds;

import java.io.Serializable;

public class ObjectDoesNotExist extends Exception implements Serializable {
	public ObjectDoesNotExist(String s){
		super(s);
	}
}
