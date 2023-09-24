package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.dao.ColloquiDAO;
import winkhouse.vo.AllegatiColloquiVO;


public class AllegatiColloquiModel extends AllegatiColloquiVO {

	private String descrizioneColloquio = null;
	
	public AllegatiColloquiModel() {
		super();
	}

	public AllegatiColloquiModel(ResultSet rs) throws SQLException {
		super(rs);		
	}

	public String getDesColloquio(){
		if (descrizioneColloquio == null){
			ColloquiDAO cDAO = new ColloquiDAO();
			ColloquiModel cModel = (ColloquiModel)cDAO.getColloquioById(ColloquiModel.class.getName(), 
																		getCodColloquio());
			descrizioneColloquio = cModel.toString();
		}
		return descrizioneColloquio;
	}
}
