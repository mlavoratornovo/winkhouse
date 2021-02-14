package winkhouse.xmlser.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import winkhouse.model.ColloquiAgentiModel_Age;
import winkhouse.model.CriteriRicercaModel;
import winkhouse.vo.AbbinamentiVO;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.AllegatiColloquiVO;
import winkhouse.vo.AllegatiImmobiliVO;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ClasseEnergeticaVO;
import winkhouse.vo.ClassiClientiVO;
import winkhouse.vo.ColloquiVO;
import winkhouse.vo.ContattiVO;
import winkhouse.vo.DatiCatastaliVO;
import winkhouse.vo.ImmagineVO;
import winkhouse.vo.ImmobiliVO;
import winkhouse.vo.RiscaldamentiVO;
import winkhouse.vo.StanzeImmobiliVO;
import winkhouse.vo.StatoConservativoVO;
import winkhouse.vo.TipologiaContattiVO;
import winkhouse.vo.TipologiaStanzeVO;
import winkhouse.vo.TipologieImmobiliVO;

public class ObjectFinder {

	ArrayList items_list = null;
	
	public ObjectFinder() {
	}
	
	Comparator searcher = new Comparator() {

		@Override
		public int compare(Object o1, Object o2) {
			
			if ((o1 instanceof ImmobiliVO) && (o2 instanceof ImmobiliVO)){
				return ((ImmobiliVO)o1).getCodImmobile().compareTo(((ImmobiliVO)o2).getCodImmobile());
			}else if ((o1 instanceof AnagraficheVO) && (o2 instanceof AnagraficheVO)){
				return ((AnagraficheVO)o1).getCodAnagrafica().compareTo(((AnagraficheVO)o2).getCodAnagrafica());
			}else if ((o1 instanceof AgentiVO) && (o2 instanceof AgentiVO)){
				return ((AgentiVO)o1).getCodAgente().compareTo(((AgentiVO)o2).getCodAgente());
			}else if ((o1 instanceof RiscaldamentiVO) && (o2 instanceof RiscaldamentiVO)){
				return ((RiscaldamentiVO)o1).getCodRiscaldamento().compareTo(((RiscaldamentiVO)o2).getCodRiscaldamento());
			}else if ((o1 instanceof ClasseEnergeticaVO) && (o2 instanceof ClasseEnergeticaVO)){
				return ((ClasseEnergeticaVO)o1).getCodClasseEnergetica().compareTo(((ClasseEnergeticaVO)o2).getCodClasseEnergetica());
			}else if ((o1 instanceof StatoConservativoVO) && (o2 instanceof StatoConservativoVO)){
				return ((StatoConservativoVO)o1).getCodStatoConservativo().compareTo(((StatoConservativoVO)o2).getCodStatoConservativo());
			}else if ((o1 instanceof TipologieImmobiliVO) && (o2 instanceof TipologieImmobiliVO)){
				return ((TipologieImmobiliVO)o1).getCodTipologiaImmobile().compareTo(((TipologieImmobiliVO)o2).getCodTipologiaImmobile());
			}else if ((o1 instanceof ColloquiVO) && (o2 instanceof ColloquiVO)){
				return ((ColloquiVO)o1).getCodColloquio().compareTo(((ColloquiVO)o2).getCodColloquio());
			}else if ((o1 instanceof ImmagineVO) && (o2 instanceof ImmagineVO)){
				return ((ImmagineVO)o1).getCodImmagine().compareTo(((ImmagineVO)o2).getCodImmagine());
			}else if ((o1 instanceof AllegatiImmobiliVO) && (o2 instanceof AllegatiImmobiliVO)){
				return ((AllegatiImmobiliVO)o1).getCodAllegatiImmobili().compareTo(((AllegatiImmobiliVO)o2).getCodAllegatiImmobili());
			}else if ((o1 instanceof DatiCatastaliVO) && (o2 instanceof DatiCatastaliVO)){
				return ((DatiCatastaliVO)o1).getCodDatiCatastali().compareTo(((DatiCatastaliVO)o2).getCodDatiCatastali());
			}else if ((o1 instanceof StanzeImmobiliVO) && (o2 instanceof StanzeImmobiliVO)){
				return ((StanzeImmobiliVO)o1).getCodStanzeImmobili().compareTo(((StanzeImmobiliVO)o2).getCodStanzeImmobili());
			}else if ((o1 instanceof ClassiClientiVO) && (o2 instanceof ClassiClientiVO)){
				return ((ClassiClientiVO)o1).getCodClasseCliente().compareTo(((ClassiClientiVO)o2).getCodClasseCliente());
			}else if ((o1 instanceof ContattiVO) && (o2 instanceof ContattiVO)){
				return ((ContattiVO)o1).getCodContatto().compareTo(((ContattiVO)o2).getCodContatto());
			}else if ((o1 instanceof AbbinamentiVO) && (o2 instanceof AbbinamentiVO)){
				return ((AbbinamentiVO)o1).getCodAbbinamento().compareTo(((AbbinamentiVO)o2).getCodAbbinamento());
			}else if ((o1 instanceof CriteriRicercaModel) && (o2 instanceof CriteriRicercaModel)){
				return ((CriteriRicercaModel)o1).getCodCriterioRicerca().compareTo(((CriteriRicercaModel)o2).getCodCriterioRicerca());
			}else if ((o1 instanceof TipologiaStanzeVO) && (o2 instanceof TipologiaStanzeVO)){
				return ((TipologiaStanzeVO)o1).getCodTipologiaStanza().compareTo(((TipologiaStanzeVO)o2).getCodTipologiaStanza());
			}else if ((o1 instanceof TipologiaContattiVO) && (o2 instanceof TipologiaContattiVO)){
				return ((TipologiaContattiVO)o1).getCodTipologiaContatto().compareTo(((TipologiaContattiVO)o2).getCodTipologiaContatto());
			}else if ((o1 instanceof ColloquiAgentiModel_Age) && (o2 instanceof ColloquiAgentiModel_Age)){
				return ((ColloquiAgentiModel_Age)o1).getCodColloquioAgenti().compareTo(((ColloquiAgentiModel_Age)o2).getCodColloquioAgenti());
			}else{
				return -1;
			}

		}
		
	};
	
	public boolean exist(ArrayList items_list,Object item){
		
		this.items_list = (ArrayList)items_list.clone();
		Collections.sort(this.items_list, searcher);

		boolean return_value = false;
		if (Collections.binarySearch(this.items_list, item, searcher) > -1){
			return_value = false;
		}else{
			return_value = true;
		}
		return return_value;
	}

}
