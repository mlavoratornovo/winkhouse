package winkhouse.util.criteriatable;

import java.util.ArrayList;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class StandardContentProvider implements IStructuredContentProvider {

	public StandardContentProvider(){}
	
	@Override
	public Object[] getElements(Object inputElement) {
		return ((ArrayList)inputElement).toArray();
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput,Object newInput) {
		
	}

}
