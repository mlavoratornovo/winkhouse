package winkhouse.dao.adapters;

import java.time.ZoneId;

import org.apache.cayenne.ObjectContext;

import winkhouse.model.AnagraficheModel;
import winkhouse.orm.Anagrafiche;

public class AnagraficheAdapter {

	public Anagrafiche adapt(AnagraficheModel am, ObjectContext context) {
		
		Anagrafiche anagrafiche = context.newObject(Anagrafiche.class);
		
		anagrafiche.setCap(am.getCap());
		anagrafiche.setCitta(am.getCitta());
		anagrafiche.setCodicefiscale(am.getCodiceFiscale());
		anagrafiche.setCognome(am.getCognome());
		anagrafiche.setCommento(am.getCommento());
		anagrafiche.setDatainserimento((am.getDataInserimento()!=null)?am.getDataInserimento().toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDate():null);
		anagrafiche.setDateupdate((am.getDateUpdate()!=null)?am.getDateUpdate().toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDateTime():null);
		anagrafiche.setIndirizzo(am.getIndirizzo());
		anagrafiche.setNcivico(am.getNcivico());
		anagrafiche.setNome(am.getNome());
		anagrafiche.setPiva(am.getPartitaIva());
		anagrafiche.setProvincia(am.getProvincia());
		anagrafiche.setRagsoc(am.getRagioneSociale());
		anagrafiche.setStorico(am.getStorico());
		
		return anagrafiche;
	}
}
