package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Pattern;

import winkhouse.vo.ImmagineVO;


public class ImmagineModel extends ImmagineVO {
	
	HashMap propieta = null;

	public ImmagineModel() {
	}

	public ImmagineModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	public HashMap getPropieta() {
		if (propieta == null){
			propieta = new HashMap<String, String>();
			Pattern p = Pattern.compile("\\|");
			if (getImgPropsStr() != null){
			    String[] immagineStr = p.split(getImgPropsStr());					
			    for (int i = 0; i < immagineStr.length; i++) {
			    	if (!immagineStr[i].trim().equalsIgnoreCase("")){
			    		propieta.put(immagineStr[i], null);
			    	}
				}
			}
		}
		return propieta;
	}

}
