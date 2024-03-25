package winkhouse.orm;

import winkhouse.orm.auto._Permessiui;

public class Permessiui extends _Permessiui {

	private static final long serialVersionUID = 1L; 
	
	private boolean selected = false;
	
    public boolean isSelected() {
		return selected;
	}
    
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
    
}
