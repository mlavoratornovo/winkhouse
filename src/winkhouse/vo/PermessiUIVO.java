package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class PermessiUIVO implements Serializable{

	private Integer codPermessoUi = null;
	private Integer codAgente = null;
	private Integer codUserUpdate = null;
	private String perspectiveId = null;
	private String viewId = null;
	private String dialogId = null;
	private Date dateUpdate = null;
	private Boolean isNot = null;
	
	public PermessiUIVO(){
		isNot = false;
	}
	
	public PermessiUIVO(ResultSet rs) throws SQLException{
		codPermessoUi = rs.getInt("CODPERMESSOUI");
		codAgente = rs.getInt("CODAGENTE");
		if (rs.wasNull()){
			codAgente = null;
		}
		
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		perspectiveId = rs.getString("PERSPECTIVEID");
		viewId = rs.getString("VIEWID");
		dialogId = rs.getString("DIALOGID");
		dateUpdate = rs.getTimestamp("DATEUPDATE");
		isNot = rs.getBoolean("ISNOT");		
	}

	public Integer getCodPermessoUi() {
		return codPermessoUi;
	}

	public void setCodPermessoUi(Integer codPermessoUi) {
		this.codPermessoUi = codPermessoUi;
	}

	public Integer getCodAgente() {
		return codAgente;
	}

	public void setCodAgente(Integer codAgente) {
		this.codAgente = codAgente;
	}

	public Integer getCodUserUpdate() {
		return codUserUpdate;
	}

	public void setCodUserUpdate(Integer codUserUpdate) {
		this.codUserUpdate = codUserUpdate;
	}

	public String getPerspectiveId() {
		return perspectiveId;
	}

	public void setPerspectiveId(String perspectiveId) {
		this.perspectiveId = perspectiveId;
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public String getDialogId() {
		return dialogId;
	}

	public void setDialogId(String dialogId) {
		this.dialogId = dialogId;
	}

	public Date getDateUpdate() {
		return dateUpdate;
	}

	public void setDateUpdate(Date dateUpdate) {
		this.dateUpdate = dateUpdate;
	}

	public Boolean getIsNot() {
		return isNot;
	}

	public void setIsNot(Boolean isNot) {
		this.isNot = isNot;
	}
	

	
}
