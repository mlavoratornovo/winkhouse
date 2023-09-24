package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.dao.TipologiaStanzeDAO;
import winkhouse.vo.StanzeImmobiliVO;
import winkhouse.vo.TipologiaStanzeVO;


public class StanzeImmobiliModel extends StanzeImmobiliVO {

	private TipologiaStanzeVO tipologia = null;
	private String descrizioneTipologia = null;
	
	public StanzeImmobiliModel() {
		super();
	}
	
	public StanzeImmobiliModel(StanzeImmobiliVO siVO) {
		setCodImmobile(siVO.getCodImmobile());
		setCodStanzeImmobili(siVO.getCodStanzeImmobili());
		setCodTipologiaStanza(siVO.getCodTipologiaStanza());
		setMq(siVO.getMq());
	}	

	public StanzeImmobiliModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	public TipologiaStanzeVO getTipologia() {
		if (tipologia == null){
			if ((super.getCodTipologiaStanza() != null) && 
				(super.getCodTipologiaStanza() != 0)){
				TipologiaStanzeDAO tsDAO = new TipologiaStanzeDAO();
				Object o = tsDAO.getTipologiaStanzaById(TipologiaStanzeVO.class.getName(), super.getCodTipologiaStanza());
				if (o != null){
					tipologia = (TipologiaStanzeVO)o;
				}
			}
		}
		return tipologia;
	}

	public void setTipologia(TipologiaStanzeVO tipologia) {
		this.tipologia = tipologia;
		setCodTipologiaStanza(tipologia.getCodTipologiaStanza());
	}

	@Override
	public String toString() {
		return ((getTipologia()==null)?"":getTipologia().getDescrizione()) + " mq : " + getMq();
	}

	public String getDescrizioneTipologia() {
		if (descrizioneTipologia == null){
			if (getTipologia() != null){
				descrizioneTipologia = getTipologia().getDescrizione();
			}
		}
		return descrizioneTipologia;
	}

}
