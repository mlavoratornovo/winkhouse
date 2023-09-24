package winkhouse.wizard.colloqui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import winkhouse.model.ColloquiAgentiModel_Age;
import winkhouse.model.ColloquiAnagraficheModel_Ang;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ContattiModel;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.util.WinkhouseUtils;
import winkhouse.util.WinkhouseUtils.ObjectSearchGetters;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.AllegatiColloquiVO;
import winkhouse.vo.ColloquiAgentiVO;
import winkhouse.vo.ColloquiCriteriRicercaVO;
import winkhouse.vo.ContattiVO;
import winkhouse.vo.RiscaldamentiVO;
import winkhouse.vo.StatoConservativoVO;
import winkhouse.vo.TipologiaContattiVO;
import winkhouse.vo.TipologieImmobiliVO;
import winkhouse.wizard.ColloquiWizard;


public class RiassuntoColloquio extends WizardPage {

	private static String TIPOLOGIA_COLLOQUIO = "Tipologia colloquio : ";
	private static String AGENTE_INSERITORE = "Agente inseritore : ";
	private static String DATAORA_COLLOQUIO = "Data e ora colloquio : ";
	private static String DESCRIZIONE_COLLOQUIO = "Descrizione colloquio : ";
	private static String LUOGO_INCONTRO = "Luogo incontro : ";
	private static String ANAGRAFICHE = "Anagrafiche : ";
	private static String RECAPITI = "Recapiti : ";
	private static String CRITERI_RICERCA = "Criteri ricerca : ";
	private static String VARIE_AGENZIA = "Varie agenzia : ";
	private static String VARIE_CLIENTI = "Varie clienti : ";
	private static String ALLEGATI = "Allegati : ";
	private static String AGENTI = "Agenti : ";
	private static String DA = "   da : ";
	private static String A = "  a : ";
	private static String IMMOBILE_ABBINATO = "Immobile : ";
	
	private Composite container = null;
	private StyledText riassunto = null;
	
	public RiassuntoColloquio(String pageName) {
		super(pageName);
	}

	public RiassuntoColloquio(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	@Override
	public void createControl(Composite parent) {
		setTitle(getName());
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;
		
		container = new Composite(parent,SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(gdFillHV);

		riassunto = new StyledText(container,SWT.MULTI|SWT.WRAP);
		riassunto.setLayoutData(gdFillHV);
		riassunto.setEditable(false);
		riassunto.setBackground(new Color(null,250,248,184));
		
		setControl(container);
	}
	
	public void showRiassunto(){

		ColloquiModel cVO = ((ColloquiWizard)getWizard()).getColloquio();
		riassunto.setText("");
		String riassuntoContent = "";
		ArrayList<StyleRange> stili = new ArrayList<StyleRange>();
		
		if (cVO.getTipologia().getCodTipologiaColloquio() == 1){
			
			int cursorPosition = 0;
			
			riassunto.setText(riassunto.getText() + TIPOLOGIA_COLLOQUIO);
			
			StyleRange labelTipologia = new StyleRange();
			labelTipologia.start = cursorPosition;
			labelTipologia.length = TIPOLOGIA_COLLOQUIO.length();
			labelTipologia.fontStyle = SWT.BOLD;
			
			cursorPosition += TIPOLOGIA_COLLOQUIO.length();
			
			stili.add(labelTipologia);
			
			riassunto.setText(riassunto.getText() + cVO.getTipologia().getDescrizione() + "\n");
			
			cursorPosition += (cVO.getTipologia().getDescrizione() + "\n").length();
				
			riassunto.setText(riassunto.getText() + AGENTE_INSERITORE);
			
			StyleRange labelAgenteInseritore = new StyleRange();
			labelAgenteInseritore.start = cursorPosition;
			labelAgenteInseritore.length = AGENTE_INSERITORE.length();
			labelAgenteInseritore.fontStyle = SWT.BOLD;
			
			stili.add(labelAgenteInseritore);
			
			cursorPosition += AGENTE_INSERITORE.length();
						
			riassunto.setText(riassunto.getText() + cVO.getAgenteInseritore().getCognome() + "  " + cVO.getAgenteInseritore().getNome() + "\n");
			
			cursorPosition += (cVO.getAgenteInseritore().getCognome() + "  " + cVO.getAgenteInseritore().getNome() + "\n").length();
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			
			riassunto.setText(riassunto.getText() + DATAORA_COLLOQUIO);

			StyleRange labelDataOra = new StyleRange();
			labelDataOra.start = cursorPosition;
			labelDataOra.length = DATAORA_COLLOQUIO.length();
			labelDataOra.fontStyle = SWT.BOLD;
			
			stili.add(labelDataOra);
			
			cursorPosition += DATAORA_COLLOQUIO.length();
			
			riassunto.setText(riassunto.getText() + formatter.format(cVO.getDataColloquio()) + "\n");
			
			cursorPosition += (formatter.format(cVO.getDataColloquio()) + "\n").length();
			
			riassunto.setText(riassunto.getText() + DESCRIZIONE_COLLOQUIO);

			StyleRange labelDescrizione = new StyleRange();
			labelDescrizione.start = cursorPosition;
			labelDescrizione.length = DESCRIZIONE_COLLOQUIO.length();
			labelDescrizione.fontStyle = SWT.BOLD;
			
			stili.add(labelDescrizione);
			
			cursorPosition += DESCRIZIONE_COLLOQUIO.length();
			
			riassunto.setText(riassunto.getText() + cVO.getDescrizione() + "\n");
			
			cursorPosition += (cVO.getDescrizione() + "\n").length();
			
			riassunto.setText(riassunto.getText() + LUOGO_INCONTRO);

			StyleRange labelLuogoIncontro = new StyleRange();
			labelLuogoIncontro.start = cursorPosition;
			labelLuogoIncontro.length = LUOGO_INCONTRO.length();
			labelLuogoIncontro.fontStyle = SWT.BOLD;
			
			stili.add(labelLuogoIncontro);
			
			cursorPosition += LUOGO_INCONTRO.length();
			
			riassunto.setText(riassunto.getText() + cVO.getLuogoIncontro() + "\n");
			
			cursorPosition += (cVO.getLuogoIncontro() + "\n").length();
			
			riassunto.setText(riassunto.getText() + ANAGRAFICHE + "\n");
			
			StyleRange labelAnagrafiche = new StyleRange();
			labelAnagrafiche.start = cursorPosition;
			labelAnagrafiche.length = (ANAGRAFICHE + "\n").length();
			labelAnagrafiche.fontStyle = SWT.BOLD;
			
			stili.add(labelAnagrafiche);
			
			cursorPosition += ANAGRAFICHE.length();
			
			Iterator it = cVO.getAnagrafiche().iterator();
			
			while (it.hasNext()){
				ColloquiAnagraficheModel_Ang caVO = (ColloquiAnagraficheModel_Ang)it.next();
				riassunto.setText(riassunto.getText() + "     " + caVO.getAnagrafica().getCognome() + "  " + 
						  		  caVO.getAnagrafica().getNome() + "\n");
				
				cursorPosition += ("     " + caVO.getAnagrafica().getCognome() + "  " + 
						  caVO.getAnagrafica().getNome() + "\n").length();
				
				if (caVO.getAnagrafica().getContatti().size() > 0){
				
					riassunto.setText(riassunto.getText() + "          " + RECAPITI + "\n");
	
					StyleRange labelRecapiti = new StyleRange();
					labelRecapiti.start = cursorPosition;
					labelRecapiti.length = ("          " + RECAPITI + "\n").length();
					labelRecapiti.fontStyle = SWT.BOLD;
					
					stili.add(labelRecapiti);
					
					cursorPosition += labelRecapiti.length;
					
					Iterator recapiti = caVO.getAnagrafica().getContatti().iterator();
					while (recapiti.hasNext()){
						ContattiModel contattiVO = (ContattiModel)recapiti.next();
						TipologiaContattiVO tcVO =  contattiVO.getTipologia();
						riassunto.setText(riassunto.getText() + "               " + contattiVO.getContatto() + "  " + 
								  		  ((tcVO != null)?contattiVO.getTipologia().getDescrizione():"") + "\n");
	
						
						cursorPosition += ("               " + contattiVO.getContatto() + "  " + 
								          ((tcVO != null)?contattiVO.getTipologia().getDescrizione():"") + "\n").length();
					}
				}
			}
			
			riassunto.setText(riassunto.getText() + CRITERI_RICERCA + "\n");
			
			StyleRange labelCriteriRicerca = new StyleRange();
			labelCriteriRicerca.start = cursorPosition;
			labelCriteriRicerca.length = (CRITERI_RICERCA + "\n").length();
			labelCriteriRicerca.fontStyle = SWT.BOLD;
			
			stili.add(labelCriteriRicerca);
			
			cursorPosition += labelCriteriRicerca.length;			
			
			if (cVO.getCriteriRicerca() != null){
				Iterator itcrVO = cVO.getCriteriRicerca().iterator();
				
				while (itcrVO.hasNext()) {
					String fromValue = "";
					ColloquiCriteriRicercaVO colloquiCriteriRicercaVO = (ColloquiCriteriRicercaVO)itcrVO.next();
					ObjectSearchGetters isg = WinkhouseUtils.getInstance()
															  .findObjectSearchGettersByMethodName(colloquiCriteriRicercaVO.getGetterMethodName(), 
																								   WinkhouseUtils.IMMOBILI, 
																								   WinkhouseUtils.FUNCTION_SEARCH);
					if (colloquiCriteriRicercaVO.getGetterMethodName().equalsIgnoreCase("getCodTipologia")){
						Integer cod = 0;
						try {
							cod = Integer.valueOf(colloquiCriteriRicercaVO.getFromValue());							
						} catch (NumberFormatException e) {
							cod = 0;
						}
						TipologieImmobiliVO tiVO = MobiliaDatiBaseCache.getInstance().findTipologiaByCod(cod);
						fromValue = (tiVO != null)?tiVO.getDescrizione(): "";
					}else if (colloquiCriteriRicercaVO.getGetterMethodName().equalsIgnoreCase("getCodStato")){
						Integer cod = 0;
						try {
							cod = Integer.valueOf(colloquiCriteriRicercaVO.getFromValue());
						} catch (NumberFormatException e) {
							cod = 0;
						}						
						StatoConservativoVO scVO = MobiliaDatiBaseCache.getInstance().findStatoConservativoByCod(cod);
						fromValue = (scVO != null)?scVO.getDescrizione(): "";
					}else if (colloquiCriteriRicercaVO.getGetterMethodName().equalsIgnoreCase("getCodRiscaldamento")){
						Integer cod = 0;
						try {
							cod = Integer.valueOf(colloquiCriteriRicercaVO.getFromValue());
						} catch (NumberFormatException e) {
							cod = 0;
						}						
						RiscaldamentiVO rVO = MobiliaDatiBaseCache.getInstance().findRiscaldamentiByCod(cod);
						fromValue = (rVO != null)?rVO.getDescrizione(): "";
					}else if (colloquiCriteriRicercaVO.getGetterMethodName().equalsIgnoreCase("getCodAgenteInseritore")){
						Integer cod = 0;
						try {
							cod = Integer.valueOf(colloquiCriteriRicercaVO.getFromValue());
						} catch (NumberFormatException e) {
							cod = 0;
						}
						
						AgentiVO aVO = MobiliaDatiBaseCache.getInstance().findAgentiByCod(cod);
						fromValue = (aVO != null)?aVO.getCognome() + " " + aVO.getNome(): "";					
					}else{
						fromValue = colloquiCriteriRicercaVO.getFromValue();
					}
					if (isg != null){
						riassunto.setText(riassunto.getText() + "     " + isg.getDescrizione() + 
										    DA + 
										    fromValue + 
											A + 
											((colloquiCriteriRicercaVO.getToValue() == null)? "" : colloquiCriteriRicercaVO.getToValue().toString()) + "\n");
						
						cursorPosition += ("     " + colloquiCriteriRicercaVO.getGetterMethodName() + 
							    DA + 
							    fromValue + 
								A + 
								((colloquiCriteriRicercaVO.getToValue() == null)? "" : colloquiCriteriRicercaVO.getToValue().toString()) + "\n").length();
					}
				}
			}
			
			riassunto.setText(riassunto.getText() + VARIE_AGENZIA + "\n");

			StyleRange labelVarieAgenzia = new StyleRange();
			labelVarieAgenzia.start = cursorPosition;
			labelVarieAgenzia.length = (VARIE_AGENZIA + "\n").length();
			labelVarieAgenzia.fontStyle = SWT.BOLD;
			
			stili.add(labelVarieAgenzia);
			
			cursorPosition += labelVarieAgenzia.length;			
			
			riassunto.setText(riassunto.getText() + cVO.getCommentoAgenzia() + "\n");
			
			cursorPosition += (cVO.getCommentoAgenzia() + "\n").length();
			
			riassunto.setText(riassunto.getText() + VARIE_CLIENTI + "\n");
			
			StyleRange labelVarieCliente = new StyleRange();
			labelVarieCliente.start = cursorPosition;
			labelVarieCliente.length = (VARIE_CLIENTI + "\n").length();
			labelVarieCliente.fontStyle = SWT.BOLD;
			
			stili.add(labelVarieCliente);
			
			cursorPosition += labelVarieCliente.length;			
			
			riassunto.setText(riassunto.getText() + cVO.getCommentoCliente() + "\n");
			
			cursorPosition += (cVO.getCommentoCliente() + "\n").length();
			if ((cVO.getAllegati() != null) && (cVO.getAllegati().size() > 0)){
				riassunto.setText(riassunto.getText() + ALLEGATI + "\n");
				
				StyleRange labelAllegati = new StyleRange();
				labelAllegati.start = cursorPosition;
				labelAllegati.length = (ALLEGATI + "\n").length();
				labelAllegati.fontStyle = SWT.BOLD;
				
				stili.add(labelAllegati);
				
				cursorPosition += labelAllegati.length;			
			
				Iterator<AllegatiColloquiVO> itAllegati = cVO.getAllegati().iterator();
				while (itAllegati.hasNext()) {
					AllegatiColloquiVO allegatiColloquiVO = itAllegati.next();
					riassunto.setText(riassunto.getText() + "     " + allegatiColloquiVO.getDescrizione() + "  " + allegatiColloquiVO.getFromPath() + "\n");
				}				
			}
		}
		
		if (cVO.getCodTipologiaColloquio() == 2){
			
			riassuntoContent += TIPOLOGIA_COLLOQUIO + cVO.getTipologia().getDescrizione() + "\n";
			riassuntoContent += AGENTE_INSERITORE + cVO.getAgenteInseritore().getCognome() + "  " + cVO.getAgenteInseritore().getNome() + "\n"; 
			//riassuntoContent += "Agente inseritore : " + cVO.getTipologia().getDescrizione() + "\n";
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			riassuntoContent += DATAORA_COLLOQUIO + formatter.format(cVO.getDataColloquio()) + "\n";
			riassuntoContent += DESCRIZIONE_COLLOQUIO + cVO.getDescrizione() + "\n";
			riassuntoContent += IMMOBILE_ABBINATO + cVO.getImmobileAbbinato().getCitta() +" "+ cVO.getImmobileAbbinato().getIndirizzo() + "\n";
			riassuntoContent += LUOGO_INCONTRO + cVO.getLuogoIncontro() + "\n";
			riassuntoContent += ANAGRAFICHE + "\n";
			
			Iterator<ColloquiAnagraficheModel_Ang> it = cVO.getAnagrafiche().iterator();
			
			while (it.hasNext()){
				ColloquiAnagraficheModel_Ang caVO = (ColloquiAnagraficheModel_Ang)it.next();
				riassuntoContent += "     " + caVO.getAnagrafica().getCognome() + "  " + 
											  caVO.getAnagrafica().getNome() + "\n";
				riassuntoContent += "          " + RECAPITI + "\n";
				
				Iterator <ContattiVO> recapiti = caVO.getAnagrafica().getContatti().iterator();
				while (recapiti.hasNext()){
					ContattiModel contattiVO = (ContattiModel)recapiti.next();
					riassuntoContent += "     " + contattiVO.getContatto() + "  " + 
												  ((contattiVO.getTipologia() != null)
												  ?contattiVO.getTipologia().getDescrizione() + "\n"
												  :"");
				}
				
			}
			//debuggare agenti
			riassuntoContent += AGENTI + "\n";
			
			Iterator<ColloquiAgentiModel_Age> itcrVO = cVO.getAgenti().iterator();
			
			while (itcrVO.hasNext()) {
				ColloquiAgentiModel_Age colloquioAgentiVO = new ColloquiAgentiModel_Age((ColloquiAgentiVO)itcrVO.next());
				riassuntoContent += "     " + colloquioAgentiVO.getAgente().getCognome() + "  " + 
									colloquioAgentiVO.getAgente().getNome() + "\n";
			}
			
			riassuntoContent += VARIE_AGENZIA + "\n";
			riassuntoContent += "     " + cVO.getCommentoAgenzia() + "\n";
			riassuntoContent += VARIE_CLIENTI + "\n";
			riassuntoContent += "     " + cVO.getCommentoCliente() + "\n";; 
			if ((cVO.getAllegati() != null) && (cVO.getAllegati().size() > 0)){			
				riassuntoContent += ALLEGATI + "\n";
				
				Iterator<AllegatiColloquiVO> itAllegati = cVO.getAllegati().iterator();
				while (itAllegati.hasNext()) {
					AllegatiColloquiVO allegatiColloquiVO = itAllegati.next();
					riassuntoContent += "     " + allegatiColloquiVO.getDescrizione() + "  " + allegatiColloquiVO.getFromPath() + "\n";
				}
			}
			riassunto.setText(riassuntoContent);
			
		}
		
		if (cVO.getTipologia().getCodTipologiaColloquio() == 3){
			
			riassuntoContent += TIPOLOGIA_COLLOQUIO + cVO.getTipologia().getDescrizione() + "\n";
			riassuntoContent += AGENTE_INSERITORE + cVO.getAgenteInseritore().getCognome() + "  " + cVO.getAgenteInseritore().getNome() + "\n"; 
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			riassuntoContent += DATAORA_COLLOQUIO + formatter.format(cVO.getDataColloquio()) + "\n";
			riassuntoContent += DESCRIZIONE_COLLOQUIO + cVO.getDescrizione() + "\n";			
			riassuntoContent += LUOGO_INCONTRO + cVO.getLuogoIncontro() + "\n";
			riassuntoContent += ANAGRAFICHE + "\n";
			
			Iterator<ColloquiAnagraficheModel_Ang> it = cVO.getAnagrafiche().iterator();
			
			while (it.hasNext()){
				ColloquiAnagraficheModel_Ang caVO = it.next();
				riassuntoContent += "     " + caVO.getAnagrafica().getCognome() + "  " + 
											  caVO.getAnagrafica().getNome() + "\n";
				riassuntoContent += "          " + RECAPITI + "\n";
				
				Iterator recapiti = caVO.getAnagrafica().getContatti().iterator();
				while (recapiti.hasNext()){
					ContattiModel contattiVO = (ContattiModel)recapiti.next();
					riassuntoContent += "     " + contattiVO.getContatto() + "  " + 
										((contattiVO.getTipologia() != null)
										 ?contattiVO.getTipologia().getDescrizione() + "\n"
										 :"");
				}
				
			}
			
			riassuntoContent += AGENTI + "\n";
			
			Iterator itcrVO = cVO.getAgenti().iterator();
			
			while (itcrVO.hasNext()) {
				ColloquiAgentiModel_Age colloquioAgentiVO = new ColloquiAgentiModel_Age((ColloquiAgentiVO)itcrVO.next());
				riassuntoContent += "     " + colloquioAgentiVO.getAgente().getCognome() + "  " + 
									colloquioAgentiVO.getAgente().getNome() + "\n";
			}
			
			riassuntoContent += VARIE_AGENZIA + "\n";
			riassuntoContent += "     " + cVO.getCommentoAgenzia() + "\n";
			riassuntoContent += VARIE_CLIENTI + "\n";
			riassuntoContent += "     " + cVO.getCommentoCliente() + "\n";
			if ((cVO.getAllegati() != null) && (cVO.getAllegati().size() > 0)){
				riassuntoContent += ALLEGATI + "\n";
				
				Iterator<AllegatiColloquiVO> itAllegati = cVO.getAllegati().iterator();
				while (itAllegati.hasNext()) {
					AllegatiColloquiVO allegatiColloquiVO = itAllegati.next();
					riassuntoContent += "     " + allegatiColloquiVO.getDescrizione() + "  " + allegatiColloquiVO.getFromPath() + "\n";
				}
			}
			riassunto.setText(riassuntoContent);
			
		}	
		
		if (stili.size() > 0){
			StyleRange sr = ((StyleRange)stili.get(stili.size()-1));
			if (riassunto.getCharCount() >= (sr.start + sr.length)){
				riassunto.setStyleRanges(stili.toArray(new StyleRange[stili.size()]));
			}
		}
	}

}
