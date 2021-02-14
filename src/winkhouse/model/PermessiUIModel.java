package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.vo.PermessiUIVO;

public class PermessiUIModel extends PermessiUIVO {
	
	private boolean selected = false;
	private short type;
	
	public PermessiUIModel() {
		// TODO Auto-generated constructor stub
	}

	public PermessiUIModel(ResultSet rs) throws SQLException {
		super(rs);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isSelected() {
		return selected;
	}

	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	
	public short getType() {
		return type;
	}

	
	public void setType(short type) {
		this.type = type;
	}

}
