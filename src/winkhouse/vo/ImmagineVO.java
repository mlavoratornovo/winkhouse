package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ImmagineVO implements Serializable{

	private Integer codImmagine = null;
	private Integer codImmobile = null;
	private Integer ordine = null;
	private String pathImmagine = null;
	private String imgPropsStr = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	
	public ImmagineVO() {
		super();
		ordine = 0;
		pathImmagine = "";
		imgPropsStr = "";
	}
	
	public ImmagineVO(ResultSet rs) throws SQLException {
		super();
		codImmagine = rs.getInt("CODIMMAGINE");
		codImmobile = rs.getInt("CODIMMOBILE");
		if (rs.wasNull()){
			codImmobile = null;
		}
		
		ordine = rs.getInt("ORDINE");
		pathImmagine = rs.getString("PATHIMMAGINE");
		imgPropsStr = rs.getString("IMGPROPS");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		

	}
	
	public Integer getCodImmagine() {
		return codImmagine;
	}
	
	public void setCodImmagine(Integer codImmagine) {
		this.codImmagine = codImmagine;
	}
	
	public Integer getCodImmobile() {
		return codImmobile;
	}
	
	public void setCodImmobile(Integer codImmobile) {
		this.codImmobile = codImmobile;
	}
	
	public Integer getOrdine() {
		return ordine;
	}
	
	public void setOrdine(Integer ordine) {
		this.ordine = ordine;
	}
	
	public String getPathImmagine() {
		return pathImmagine;
	}
	
	public void setPathImmagine(String pathImmagine) {
		this.pathImmagine = pathImmagine;
	}

	public String getImgPropsStr() {
		return imgPropsStr;
	}

	public void setImgPropsStr(String imgPropsStr) {
		this.imgPropsStr = imgPropsStr;
	}

	@Override
	public String toString() {
		return getPathImmagine();
	}

	
	public Integer getCodUserUpdate() {
		return codUserUpdate;
	}

	
	public void setCodUserUpdate(Integer codUserUpdate) {
		this.codUserUpdate = codUserUpdate;
	}

	
	public Date getDateUpdate() {
		return dateUpdate;
	}

	
	public void setDateUpdate(Date dateUpdate) {
		this.dateUpdate = dateUpdate;
	}
		

}
