package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.orm.Agenti;
import winkhouse.vo.AgentiVO;

public class AgentiColloqui extends Agenti {

	public static final int AGENTI_COLLOQUIO_PARTECIPANTI = 0;
	public static final int AGENTI_COLLOQUIO_INSERITORI = 1;
	
	private int agenti_colloquio_type = 0;
	private Agenti agente;
	
	public AgentiColloqui(Agenti agente) {
		this.agente = agente;
	}

	public Agenti getAgente() {
		return agente;
	}

	public int getAgenti_colloquio_type() {
		return agenti_colloquio_type;
	}

	public void setAgenti_colloquio_type(int agenti_colloquio_type) {
		this.agenti_colloquio_type = agenti_colloquio_type;
	}

}
