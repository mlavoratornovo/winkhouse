package winkhouse.util.criteriatable.celleditors;

import org.eclipse.jface.viewers.ICellEditorValidator;

public class TextDoubleCellEditorValidator implements ICellEditorValidator {

	@Override
	public String isValid(Object value) {
		
		try {
			Double.valueOf((String)value);
			return null;
		} catch (NumberFormatException e) {
			return "Inserire un numero decimale. \n Come virgola usare il .";
		} catch (NullPointerException npe){
			return "Inserire un numero decimale. \n Come virgola usare il .";
		}

	}
	
}