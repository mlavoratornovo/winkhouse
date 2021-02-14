package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.vo.AttributeValueVO;

public class AttributeValueModel extends AttributeValueVO {

	public AttributeValueModel() {}

	public AttributeValueModel(ResultSet rs) throws SQLException {
		super(rs);
	}

}
