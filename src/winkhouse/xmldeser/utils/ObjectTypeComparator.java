package winkhouse.xmldeser.utils;

import java.util.Comparator;

public class ObjectTypeComparator implements Comparator<ObjectTypeCompare> {

	@Override
	public int compare(ObjectTypeCompare arg0, ObjectTypeCompare arg1) {
		
		if (arg0.getTypeValue() == arg1.getTypeValue()){
			return 0;
		}else if (arg0.getTypeValue() < arg1.getTypeValue()){
			return -1;
		}else{
			return 1;
		}

	}

}
