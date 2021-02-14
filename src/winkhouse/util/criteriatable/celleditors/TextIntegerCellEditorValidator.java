package winkhouse.util.criteriatable.celleditors;

import org.eclipse.jface.viewers.ICellEditorValidator;

public class TextIntegerCellEditorValidator implements ICellEditorValidator {

	@Override
	public String isValid(Object value) {
		
		try {
			Integer.valueOf((String)value);
			return null;
		} catch (NumberFormatException e) {
			return "Inserire un numero intero";
		} catch (NullPointerException npe){
			return "Inserire un numero intero";
		}

	}
	
}
