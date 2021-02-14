package winkhouse.wizard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.action.anagrafiche.ApriDettaglioAnagraficaAction;
import winkhouse.action.colloqui.ApriDettaglioColloquioAction;
import winkhouse.action.immobili.ApriDettaglioImmobileAction;
import winkhouse.dao.AbbinamentiDAO;
import winkhouse.dao.RicercheDAO;
import winkhouse.engine.search.SearchEngineAnagrafiche;
import winkhouse.engine.search.SearchEngineColloqui;
import winkhouse.engine.search.SearchEngineImmobili;
import winkhouse.engine.search.SearchEngineImmobiliAffitti;
import winkhouse.helper.ProfilerHelper;
import winkhouse.helper.RicercheHelper;
import winkhouse.model.AbbinamentiModel;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.model.RicercheModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.common.AbbinamentiView;
import winkhouse.view.permessi.DettaglioPermessiAgenteView;
import winkhouse.vo.AbbinamentiVO;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.ColloquiCriteriRicercaVO;
import winkhouse.vo.RicercaVO;
import winkhouse.wizard.ricerca.ListaCriteriAnagrafiche;
import winkhouse.wizard.ricerca.ListaCriteriColloqui;
import winkhouse.wizard.ricerca.ListaCriteriImmobili;
import winkhouse.wizard.ricerca.ListaCriteriImmobiliAffitti;
import winkhouse.wizard.ricerca.ListaRisultatiAnagrafiche;
import winkhouse.wizard.ricerca.ListaRisultatiColloqui;
import winkhouse.wizard.ricerca.ListaRisultatiImmobili;
import winkhouse.wizard.ricerca.SelezioneTipologiaRicerca;

public class RicercaWizard extends Wizard {
	
	public final static String ID = "winkhouse.wizardricerca";
	
	public static final int IMMOBILI = 1;
	public static final int ANAGRAFICHE = 2;
	public static final int ABBINAMENTI_IMMOBILI = 3;
	public static final int ABBINAMENTI_ANAGRAFICHE = 4;
	public static final int AFFITTI = 5;	
	public static final int PROMEMORIA = 6;	
	public static final int PERMESSI = 7;
	public static final int STRUTTURA_RELAZIONI = 8;
	public static final int COLLOQUI = 9;
	public static final int RICERCACLOUD = 10;
	public static final int RICERCAFAST_IMMOBILI = 11;
	public static final int RICERCAFAST_AFFITTI = 12;
	public static final int RICERCAFAST_ANAGRAFICHE = 13;
	public static final int RICERCAFAST_COLLOQUI = 14;
	
	private int wiztype = 0;
	
	private RicercaVO ricerca = null;
	private WizardPage currentPage = null;
	private ListaCriteriImmobili listaCriteriImmobili = null;
	private ListaCriteriAnagrafiche listaCriteriAnagrafiche = null;
	private ListaCriteriImmobiliAffitti listaCriteriImmobiliAffitti = null;
	private ListaCriteriColloqui listaCriteriColloqui = null;
	private ListaRisultatiImmobili listaRisultatiImmobili = null;
	private ListaRisultatiColloqui listaRisultatiColloqui = null;
	private ListaRisultatiAnagrafiche listaRisultatiAnagrafiche = null;
	private SelezioneTipologiaRicerca selezioneTipologiaRicerca = null;
	private AgentiVO agente = null;
	
	private Object returnObject = null;
	private Class returnType = null;
	private String returnObjectMethodName = null;
	

	public RicercaWizard(int type) {
		getRicerca().setType(type);
	}
		
	@Override
	public boolean performFinish() {
		
		if (wiztype == PERMESSI){
			IViewReference iv = (IViewReference)PlatformUI.getWorkbench()
														  .getActiveWorkbenchWindow()
														  .getActivePage()
														  .findViewReference(DettaglioPermessiAgenteView.ID,getAgente().getCodAgente().toString());
			
			
			DettaglioPermessiAgenteView dpav = (DettaglioPermessiAgenteView)iv.getView(true);
			if (getRicerca().getRicerca() == null){
				
			
//				if (getRicerca().getRicerca().getCriteri().size() > 0){
				
					RicercheHelper rh = new RicercheHelper();
					RicercheModel rm = rh.saveNewRicercaFromWizardRicerca(getRicerca());
					if (rm != null){
						dpav.addPermessoDatiByRicerca(rm);
					}else{
						MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
												"Errore creazione ricerca",
												"Si è verificato un errore durante il salvataggio della ricerca");
					}
//				}else{
//					dpav.getTvRegole().refresh();
//				}
				
			}else{
				
				RicercheDAO rDAO = new RicercheDAO();
				RicercheModel rmDB = (RicercheModel)rDAO.getRicercaById(RicercheModel.class.getName(), getRicerca().getRicerca().getCodRicerca());
				
				if (!getRicerca().getRicerca().equals(rmDB)){
					MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
							 								 "Winkhouse : la regola è stata modificata", null,
							 								 "Vuoi usare la regola originale, salvare ed usare una nuova regola o \n" +
							 								 "aggiornare ed usare la regola originale ?",
							 								 MessageDialog.WARNING, new String[] {"Usa originale","Salva ed usa nuova","Aggiorna ed usa originale"}, 0);
					
					switch (dialog.open()){
					case 0 : dpav.addPermessoDatiByRicerca(getRicerca().getRicerca());
							 break;
					case 1 : RicercheHelper rh = new RicercheHelper();
							 RicercheModel rm = rh.saveNewRicercaFromWizardRicerca(getRicerca());
							 if (rm != null){
							 	dpav.addPermessoDatiByRicerca(rm);
							 }else{
								MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
														"Errore creazione ricerca",
														"Si è verificato un errore durante il salvataggio della ricerca");
							 }
							 break;
					case 2 : RicercheHelper rh1 = new RicercheHelper();
							 if (rh1.saveUpdateRicerca(getRicerca().getRicerca())){
								 dpav.addPermessoDatiByRicerca(getRicerca().getRicerca());
							 }else{
								 MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
										 				 "Errore creazione ricerca",
										 				 "Si è verificato un errore durante il salvataggio della ricerca");
								 
							 }
					default : break;
					} 		
				}
				
			}
			
		}else if (wiztype == PROMEMORIA){

			
				if ((returnObject != null) && (returnObjectMethodName != null)){
					
					try {
						Method m = returnObject.getClass().getMethod(returnObjectMethodName, returnType);
						m.invoke(returnObject, getRicerca().getRisultati());			
						dispose();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				
				}
			

			
		}else if (wiztype == RICERCACLOUD){
			
			if ((returnObject != null) && (returnObjectMethodName != null)){
				
				try {
					Method m = returnObject.getClass().getMethod(returnObjectMethodName, returnType);
					if (getRicerca().getType() == IMMOBILI){
						m.invoke(returnObject, getRicerca().getCriteriImmobili());
					}
					if (getRicerca().getType() == ANAGRAFICHE){
						m.invoke(returnObject, getRicerca().getCriteriAnagrafiche());
					}
					if (getRicerca().getType() == AFFITTI){
						m.invoke(returnObject, getRicerca().getCriteriImmobiliAffitti());
					}
					if (getRicerca().getType() == COLLOQUI){
						m.invoke(returnObject, getRicerca().getCriteriColloqui());
					}
								
					dispose();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			
			}
			
		}else{
			
			if (getRicerca().getType() == RicercaWizard.ABBINAMENTI_ANAGRAFICHE){
				
				AbbinamentiView av = (AbbinamentiView)PlatformUI.getWorkbench()
																.getActiveWorkbenchWindow()
																.getActivePage()
																.findView(AbbinamentiView.ID);
	
				if (av != null){
					if ((av.getAnagrafica() != null) &&
						(av.getAnagrafica().getCodAnagrafica() != 0)
						){
						ProgressMonitorDialog pmd = new ProgressMonitorDialog(this.getShell());
						IRunnableWithProgress irwpAddAbbinamentiAnagrafiche = new IRunnableWithProgress() {
	
							@Override
							public void run(IProgressMonitor monitor) throws InvocationTargetException,
																			 InterruptedException {
	
								AbbinamentiView av = (AbbinamentiView)PlatformUI.getWorkbench()
															.getActiveWorkbenchWindow()
															.getActivePage()
															.findView(AbbinamentiView.ID);
								
								Integer codAnagrafica = av.getAnagrafica().getCodAnagrafica();
								
								Display d = PlatformUI.getWorkbench().getDisplay();
								ArrayList alImmobili = getRicerca().getRisultati();
								Iterator it = alImmobili.iterator();
								monitor.beginTask("salvataggio abbinamenti immobili : ", alImmobili.size());
								d.readAndDispatch();
								AbbinamentiDAO aDAO = new AbbinamentiDAO();
								
								while(it.hasNext()){
									ImmobiliModel im = (ImmobiliModel)it.next();
									if (aDAO.findAbbinamentiByCodImmobileCodAnagrafica(AbbinamentiModel.class.getName(),
																					   codAnagrafica, 
																					   im.getCodImmobile()) == null){
										AbbinamentiVO aVO  = new AbbinamentiVO();
										aVO.setCodImmobile(im.getCodImmobile());
										aVO.setCodAnagrafica(codAnagrafica);
						
										if (aDAO.saveUpdate(aVO, null, true)){
											monitor.setTaskName("abbinamento con : " + im.toString());
										}
										d.readAndDispatch();
										monitor.worked(1);
										d.readAndDispatch();						
									}
						
								}
								monitor.setTaskName("aggiornamento abbinamenti");
								ArrayList al = (ArrayList)aDAO.findAbbinamentiByCodAnagrafica(AbbinamentiModel.class.getName(), codAnagrafica);
								av.setManuale(al);
							}
						};
						try {
							pmd.run(false, true, irwpAddAbbinamentiAnagrafiche);
						} catch (InvocationTargetException e) {
							e.printStackTrace();
							MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
							mb.setText("impossibile salvare gli abbinamenti con gli immobili");
							mb.setMessage("impossibile salvare gli abbinamenti con gli immobili");			
							mb.open();													
	
						} catch (InterruptedException e) {
							e.printStackTrace();
							MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
							mb.setText("impossibile salvare gli abbinamenti con gli immobili");
							mb.setMessage("impossibile salvare gli abbinamenti con gli immobili");			
							mb.open();																	
						}
	
					}else{
						MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
						mb.setText("impossibile salvare gli abbinamenti con gli immobili");
						mb.setMessage("impossibile salvare gli abbinamenti con gli immobili");			
						mb.open();										
	
					}
				}else{
					MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
					mb.setText("impossibile salvare gli abbinamenti con gli immobili");
					mb.setMessage("impossibile salvare gli abbinamenti con gli immobili");			
					mb.open();										
	
				}
			}
			if (getRicerca().getType() == RicercaWizard.ABBINAMENTI_IMMOBILI){
				AbbinamentiView av = (AbbinamentiView)PlatformUI.getWorkbench()
						  										.getActiveWorkbenchWindow()
						  										.getActivePage()
						  										.findView(AbbinamentiView.ID);
				
				if (av != null){
					if ((av.getImmobile() != null) &&
						(av.getImmobile().getCodImmobile() != null)
					    ){
						ProgressMonitorDialog pmd = new ProgressMonitorDialog(this.getShell());
						IRunnableWithProgress irwpAddAbbinamentiAnagrafiche = new IRunnableWithProgress() {
							
							@Override
							public void run(IProgressMonitor monitor) throws InvocationTargetException,
																			 InterruptedException {
								
								AbbinamentiView av = (AbbinamentiView)PlatformUI.getWorkbench()
																				.getActiveWorkbenchWindow()
																				.getActivePage()
																				.findView(AbbinamentiView.ID);
	
								Integer codImmobile = av.getImmobile().getCodImmobile();
								
								Display d = PlatformUI.getWorkbench().getDisplay();
								ArrayList alAnagrafiche = getRicerca().getRisultati();
								Iterator it = alAnagrafiche.iterator();
								monitor.beginTask("salvataggio abbinamenti anagrafiche : ", alAnagrafiche.size());
								d.readAndDispatch();
								AbbinamentiDAO aDAO = new AbbinamentiDAO();
								
								while(it.hasNext()){
									AnagraficheModel am = (AnagraficheModel)it.next();
									if (aDAO.findAbbinamentiByCodImmobileCodAnagrafica(AbbinamentiModel.class.getName(),
																					   am.getCodAnagrafica(), codImmobile) == null){
										AbbinamentiVO aVO  = new AbbinamentiVO();
										aVO.setCodImmobile(codImmobile);
										aVO.setCodAnagrafica(am.getCodAnagrafica());
										
										if (aDAO.saveUpdate(aVO, null, true)){
											monitor.setTaskName("abbinamento con : " + am.toString());
										}
										d.readAndDispatch();
										monitor.worked(1);
										d.readAndDispatch();						
									}
									
								}
								monitor.setTaskName("aggiornamento abbinamenti");
								ArrayList al = (ArrayList)aDAO.findAbbinamentiByCodImmobile(AbbinamentiModel.class.getName(), codImmobile);
								av.setManuale(al);
							}
						};
						try {
							pmd.run(false, true, irwpAddAbbinamentiAnagrafiche);
						} catch (InvocationTargetException e) {
							e.printStackTrace();
							MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
							mb.setText("impossibile salvare gli abbinamenti con le anagrafiche");
							mb.setMessage("impossibile salvare gli abbinamenti con le anagrafiche");			
							mb.open();													
	
						} catch (InterruptedException e) {
							e.printStackTrace();
							MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
							mb.setText("impossibile salvare gli abbinamenti con le anagrafiche");
							mb.setMessage("impossibile salvare gli abbinamenti con le anagrafiche");			
							mb.open();																	
						}
	 
					}else{
						MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
						mb.setText("impossibile salvare gli abbinamenti con le anagrafiche");
						mb.setMessage("impossibile salvare gli abbinamenti con le anagrafiche");			
						mb.open();										
	
					}
				}else{
					MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
					mb.setText("impossibile salvare gli abbinamenti con le anagrafiche");
					mb.setMessage("impossibile salvare gli abbinamenti con le anagrafiche");			
					mb.open();										
					
				}
				
			}
			if ((getRicerca().getType() == RicercaWizard.IMMOBILI) || 
			    (getRicerca().getType() == RicercaWizard.AFFITTI) || 
			    (getRicerca().getType() == RicercaWizard.RICERCAFAST_IMMOBILI) || 
			    (getRicerca().getType() == RicercaWizard.RICERCAFAST_AFFITTI)){
				
				ProgressMonitorDialog pmd = new ProgressMonitorDialog(this.getShell());
				IRunnableWithProgress irwpOpenImmobile = new IRunnableWithProgress() {
				 	
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException,
																	 InterruptedException {
						
						Display d = PlatformUI.getWorkbench().getDisplay();
						ArrayList alImmobili = getRicerca().getRisultati();
						if (alImmobili != null && alImmobili.size() > 0){
							Iterator it = alImmobili.iterator();
							monitor.beginTask("apertura dettagli", alImmobili.size());
							d.readAndDispatch();
							while(it.hasNext()){
								ImmobiliModel im = (ImmobiliModel)it.next();
								ApriDettaglioImmobileAction adia = new ApriDettaglioImmobileAction(im,null);
								adia.run();							
								monitor.setTaskName("dettaglio : " + im.toString());
								d.readAndDispatch();
								monitor.worked(1);
								d.readAndDispatch();						
								
							}
						}else{
							MessageDialog.openWarning(getShell(), "Apertura dettagli immobili", "Nessun immobile selezionato");
						}
						
					}
				};
				try {
					pmd.run(false, true, irwpOpenImmobile);
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
					mb.setText("errore apertura dettaglio immobile");
					mb.setMessage("errore apertura dettaglio immobile");			
					mb.open();													
	
				} catch (InterruptedException e) {
					e.printStackTrace();
					MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
					mb.setText("errore apertura dettaglio immobile");
					mb.setMessage("errore apertura dettaglio immobile");			
					mb.open();																	
				}
				
			}
			
			if ((getRicerca().getType() == RicercaWizard.ANAGRAFICHE) ||
				(getRicerca().getType() == RicercaWizard.RICERCAFAST_ANAGRAFICHE)){
				
				ProgressMonitorDialog pmd = new ProgressMonitorDialog(this.getShell());
				IRunnableWithProgress irwpOpenAnagrafica = new IRunnableWithProgress() {
				 	
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException,
																	 InterruptedException {
						Display d = PlatformUI.getWorkbench().getDisplay();
						ArrayList alAnagrafiche = getRicerca().getRisultati();
						Iterator it = alAnagrafiche.iterator();
						monitor.beginTask("apertura dettagli", alAnagrafiche.size());
						d.readAndDispatch();
						while(it.hasNext()){
							AnagraficheModel am = (AnagraficheModel)it.next();
							ApriDettaglioAnagraficaAction adaa = new ApriDettaglioAnagraficaAction(am, null);
							adaa.run();
							monitor.setTaskName("dettaglio : " + am.toString());
							d.readAndDispatch();
							monitor.worked(1);
							d.readAndDispatch();						
							
						}
						
					}
				};
				try {
					pmd.run(false, true, irwpOpenAnagrafica);
				} catch (InvocationTargetException e) {
					MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
					mb.setText("errore apertura dettaglio anagrafica");
					mb.setMessage("errore apertura dettaglio anagrafica");			
					mb.open();													
	
				} catch (InterruptedException e) {
					MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
					mb.setText("errore apertura dettaglio anagrafica");
					mb.setMessage("errore apertura dettaglio anagrafica");			
					mb.open();
				}
							
			}
			if ((getRicerca().getType() == RicercaWizard.COLLOQUI) ||
				(getRicerca().getType() == RicercaWizard.RICERCAFAST_COLLOQUI)){
				
				ProgressMonitorDialog pmd = new ProgressMonitorDialog(this.getShell());
				IRunnableWithProgress irwpOpenColloquio = new IRunnableWithProgress() {
				 	
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException,
																	 InterruptedException {
						
						Display d = PlatformUI.getWorkbench().getDisplay();
						ArrayList alColloqui = getRicerca().getRisultati();
						Iterator it = alColloqui.iterator();
						monitor.beginTask("apertura dettagli", alColloqui.size());
						d.readAndDispatch();
						while(it.hasNext()){
							ColloquiModel cm = (ColloquiModel)it.next();
							ApriDettaglioColloquioAction adaa = new ApriDettaglioColloquioAction(cm);
							adaa.run();
							monitor.setTaskName("dettaglio : " + cm.toString());
							d.readAndDispatch();
							monitor.worked(1);
							d.readAndDispatch();						
							
						}
	
						
					}
				};
				try {
					pmd.run(false, true, irwpOpenColloquio);
				} catch (InvocationTargetException e) {
					MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
					mb.setText("errore apertura dettaglio colloquio");
					mb.setMessage("errore apertura dettaglio colloquio");			
					mb.open();													
	
				} catch (InterruptedException e) {
					MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
					mb.setText("errore apertura dettaglio colloquio");
					mb.setMessage("errore apertura dettaglio colloquio");			
					mb.open();																	
				}
							
			}
			if (getRicerca().getType() == RicercaWizard.RICERCACLOUD){
				
//				ProgressMonitorDialog pmd = new ProgressMonitorDialog(this.getShell());
//				IRunnableWithProgress irwpOpenColloquio = new IRunnableWithProgress() {
//				 	
//					@Override
//					public void run(IProgressMonitor monitor) throws InvocationTargetException,
//																	 InterruptedException {
//						
//						Display d = PlatformUI.getWorkbench().getDisplay();
//						ArrayList alColloqui = getRicerca().getRisultati();
//						Iterator it = alColloqui.iterator();
//						monitor.beginTask("apertura dettagli", alColloqui.size());
//						d.readAndDispatch();
//						while(it.hasNext()){
//							ColloquiModel cm = (ColloquiModel)it.next();
//							ApriDettaglioColloquioAction adaa = new ApriDettaglioColloquioAction(cm);
//							adaa.run();
//							monitor.setTaskName("dettaglio : " + cm.toString());
//							d.readAndDispatch();
//							monitor.worked(1);
//							d.readAndDispatch();						
//							
//						}
//	
//						
//					}
//				};
//				try {
//					pmd.run(false, true, irwpOpenColloquio);
//				} catch (InvocationTargetException e) {
//					MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
//					mb.setText("errore apertura dettaglio colloquio");
//					mb.setMessage("errore apertura dettaglio colloquio");			
//					mb.open();													
//	
//				} catch (InterruptedException e) {
//					MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
//					mb.setText("errore apertura dettaglio colloquio");
//					mb.setMessage("errore apertura dettaglio colloquio");			
//					mb.open();																	
//				}
							
			}
			
		}
		return true;
	}

	@Override
	public void addPages() {
		if ((getRicerca().getType() == RicercaWizard.RICERCAFAST_IMMOBILI) || 
			(getRicerca().getType() == RicercaWizard.RICERCAFAST_ANAGRAFICHE) || 
			(getRicerca().getType() == RicercaWizard.RICERCAFAST_AFFITTI) || 
			(getRicerca().getType() == RicercaWizard.RICERCAFAST_COLLOQUI)){
			
			switch (getRicerca().getType()) {
			
			case RicercaWizard.RICERCAFAST_IMMOBILI:{
				
				listaCriteriImmobili = new ListaCriteriImmobili("Lista criteri selezione immobili",
																"Lista criteri selezione immobili",
																Activator.getImageDescriptor("icons/wizardricerca/filefind.png"));
				listaCriteriImmobili.setDescription("Inserire i criteri di ricerca per gli immobili");
				addPage(listaCriteriImmobili);
				
				listaRisultatiImmobili = new ListaRisultatiImmobili("Risultati ricerca immobili",
																	"Risultati ricerca immobili",
																	Activator.getImageDescriptor("icons/wizardricerca/gohome.png"));
				listaRisultatiImmobili.setDescription("Lista immobili corrispondenti ai criteri di ricerca inseriti, \n premere Finish per aprire i dettagli selezionati");
				addPage(listaRisultatiImmobili);

				break;
			}

			case RicercaWizard.RICERCAFAST_ANAGRAFICHE:{
				
				listaCriteriAnagrafiche = new ListaCriteriAnagrafiche("Lista criteri selezione anagrafiche",
																	  "Lista criteri selezione anagrafiche",
																	  Activator.getImageDescriptor("icons/wizardricerca/filefind.png"));
				listaCriteriAnagrafiche.setDescription("Inserire i criteri di ricerca per le anagrafiche");
				addPage(listaCriteriAnagrafiche);

				listaRisultatiAnagrafiche = new ListaRisultatiAnagrafiche("Risultati ricerca anagrafiche",
																		  "Risultati ricerca anagrafiche",
																		  Activator.getImageDescriptor("icons/wizardricerca/ktqueuemanager.png"));
				listaRisultatiAnagrafiche.setDescription("Lista anagrafiche corrispondenti ai criteri di ricerca inseriti, \n premere Finish per aprire i dettagli selezionati");
				addPage(listaRisultatiAnagrafiche);

				break;
			}
			
			case RicercaWizard.RICERCAFAST_AFFITTI:{
				listaCriteriImmobiliAffitti = new ListaCriteriImmobiliAffitti("Lista criteri selezione immobili per affitto",
																			  "Lista criteri selezione immobili per affitto",
																			  Activator.getImageDescriptor("icons/wizardricerca/filefind.png"));
				listaCriteriImmobiliAffitti.setDescription("Inserire i criteri di ricerca per gli immobili per affitto");
				addPage(listaCriteriImmobiliAffitti);

				listaRisultatiImmobili = new ListaRisultatiImmobili("Risultati ricerca immobili",
																	"Risultati ricerca immobili",
																	Activator.getImageDescriptor("icons/wizardricerca/gohome.png"));
				listaRisultatiImmobili.setDescription("Lista immobili corrispondenti ai criteri di ricerca inseriti, \n premere Finish per aprire i dettagli selezionati");
				addPage(listaRisultatiImmobili);

				break;	
			}

			case RicercaWizard.RICERCAFAST_COLLOQUI:{
				listaCriteriColloqui = new ListaCriteriColloqui("Lista criteri selezione colloqui",
																"Lista criteri selezione colloqui",
																Activator.getImageDescriptor("icons/wizardricerca/filefind.png"));
				listaCriteriColloqui.setDescription("Inserire i criteri di ricerca colloqui");
				addPage(listaCriteriColloqui);

				listaRisultatiColloqui = new ListaRisultatiColloqui("Risultati ricerca colloqui",
																	"Risultati ricerca colloqui",
																	Activator.getImageDescriptor("icons/wizardricerca/colloqui64.png"));
				listaRisultatiColloqui.setDescription("Lista colloqui corrispondenti ai criteri di ricerca inseriti, \n premere Finish per aprire i dettagli selezionati");
				addPage(listaRisultatiColloqui);

				break;	
			}

			default:
				break;
			}
		}else{
			if ((getRicerca().getType() != RicercaWizard.ABBINAMENTI_ANAGRAFICHE) && 
					(getRicerca().getType() != RicercaWizard.ABBINAMENTI_IMMOBILI)){
					selezioneTipologiaRicerca = new SelezioneTipologiaRicerca ("Selezione tipologia ricerca",
																			   "Selezione tipologia ricerca",
																			   Activator.getImageDescriptor("icons/wizardricerca/kfind.png"));
					selezioneTipologiaRicerca.setDescription("Selezionare la tipologia di oggetti su cui effettuare la ricerca");		
					addPage(selezioneTipologiaRicerca);
				}
				if (getRicerca().getType() != RicercaWizard.ABBINAMENTI_ANAGRAFICHE){
					listaCriteriAnagrafiche = new ListaCriteriAnagrafiche("Lista criteri selezione anagrafiche",
																		  "Lista criteri selezione anagrafiche",
																		  Activator.getImageDescriptor("icons/wizardricerca/filefind.png"));
					listaCriteriAnagrafiche.setDescription("Inserire i criteri di ricerca per le anagrafiche");
					addPage(listaCriteriAnagrafiche);
				}
				if (getRicerca().getType() != RicercaWizard.ABBINAMENTI_IMMOBILI){
					listaCriteriImmobili = new ListaCriteriImmobili("Lista criteri selezione immobili",
																	"Lista criteri selezione immobili",
																	Activator.getImageDescriptor("icons/wizardricerca/filefind.png"));
					listaCriteriImmobili.setDescription("Inserire i criteri di ricerca per gli immobili");
					addPage(listaCriteriImmobili);
					
					listaCriteriImmobiliAffitti = new ListaCriteriImmobiliAffitti("Lista criteri selezione immobili per affitto",
																		   		  "Lista criteri selezione immobili per affitto",
																		   		  Activator.getImageDescriptor("icons/wizardricerca/filefind.png"));
					listaCriteriImmobiliAffitti.setDescription("Inserire i criteri di ricerca per gli immobili per affitto");
					addPage(listaCriteriImmobiliAffitti);
					
					listaCriteriColloqui = new ListaCriteriColloqui("Lista criteri selezione colloqui",
																	"Lista criteri selezione colloqui",
																	Activator.getImageDescriptor("icons/wizardricerca/filefind.png"));
					listaCriteriColloqui.setDescription("Inserire i criteri di ricerca colloqui");
					addPage(listaCriteriColloqui);
					
				}
				
				listaRisultatiAnagrafiche = new ListaRisultatiAnagrafiche("Risultati ricerca anagrafiche",
																		  "Risultati ricerca anagrafiche",
																		  Activator.getImageDescriptor("icons/wizardricerca/ktqueuemanager.png"));
				if ((getRicerca().getType() != RicercaWizard.ABBINAMENTI_IMMOBILI) &&
					(getRicerca().getType() != RicercaWizard.ABBINAMENTI_ANAGRAFICHE)){
					listaRisultatiAnagrafiche.setDescription("Lista anagrafiche corrispondenti ai criteri di ricerca inseriti, \n premere Finish per aprire i dettagli selezionati");
				}else{
					listaRisultatiAnagrafiche.setDescription("Lista anagrafiche corrispondenti ai criteri di ricerca inseriti, \n premere Finish per eseguire gli abbinamenti con le anagrafiche selezionate");
				}
				addPage(listaRisultatiAnagrafiche);
				
				listaRisultatiImmobili = new ListaRisultatiImmobili("Risultati ricerca immobili",
																	"Risultati ricerca immobili",
																	Activator.getImageDescriptor("icons/wizardricerca/gohome.png"));
				if ((getRicerca().getType() != RicercaWizard.ABBINAMENTI_IMMOBILI) &&
					(getRicerca().getType() != RicercaWizard.ABBINAMENTI_ANAGRAFICHE)){
					listaRisultatiImmobili.setDescription("Lista immobili corrispondenti ai criteri di ricerca inseriti, \n premere Finish per aprire i dettagli selezionati");
				}else{
					listaRisultatiAnagrafiche.setDescription("Lista anagrafiche corrispondenti ai criteri di ricerca inseriti, \n premere Finish per eseguire gli abbinamenti con gli immobili selezionati");
				}
				addPage(listaRisultatiImmobili);
				
				listaRisultatiColloqui = new ListaRisultatiColloqui("Risultati ricerca colloqui",
																	"Risultati ricerca colloqui",
																	Activator.getImageDescriptor("icons/wizardricerca/colloqui64.png"));
				listaRisultatiColloqui.setDescription("Lista colloqui corrispondenti ai criteri di ricerca inseriti, \n premere Finish per aprire i dettagli selezionati");
				addPage(listaRisultatiColloqui);
			
		}
		
	}

	public RicercaVO getRicerca() {
		if (ricerca == null){
			ricerca = new RicercaVO();
		}
		return ricerca;
	}

	public void setRicerca(RicercaVO ricerca) {
		this.ricerca = ricerca;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		
		IWizardPage returnValue = null;
		
		if ((page instanceof SelezioneTipologiaRicerca) &&
			(ricerca.getType() == RicercaWizard.IMMOBILI)){
	//		currentPage = listaCriteriImmobili;
			returnValue = listaCriteriImmobili;
		}
		if ((page instanceof SelezioneTipologiaRicerca) &&
			(ricerca.getType() == RicercaWizard.ANAGRAFICHE)){
		//		currentPage = listaCriteriAnagrafiche;
			returnValue = listaCriteriAnagrafiche;
		}
		if ((page instanceof SelezioneTipologiaRicerca) &&
			(ricerca.getType() == RicercaWizard.AFFITTI)){
			//		currentPage = listaCriteriAnagrafiche;
			returnValue = listaCriteriImmobiliAffitti;
		}
		if ((page instanceof SelezioneTipologiaRicerca) &&
				(ricerca.getType() == RicercaWizard.COLLOQUI)){
				//		currentPage = listaCriteriAnagrafiche;
			returnValue = listaCriteriColloqui;
		}		
		
		if (page instanceof ListaCriteriImmobili){
			if (ricerca.getCriteriImmobili() != null){
				if (ricerca.getCriteriImmobili().size() > 0){
					if (ricerca.getCriteriImmobili().size() == 1){
						if (((ColloquiCriteriRicercaVO)ricerca.getCriteriImmobili()
																.get(0)).getGetterMethodName()
																		.equalsIgnoreCase("(") ||
							((ColloquiCriteriRicercaVO)ricerca.getCriteriImmobili()
																.get(0)).getGetterMethodName()
																		.equalsIgnoreCase(")") ||
							((ColloquiCriteriRicercaVO)ricerca.getCriteriImmobili()
																.get(0)).getGetterMethodName()
																		.equalsIgnoreCase("")																		
						   ){
							
						}else{
							if (checkCriteriSyntax(false,WinkhouseUtils.IMMOBILI)){
								SearchEngineImmobili sei = new SearchEngineImmobili(ricerca.getCriteriImmobili());
								ArrayList results = sei.find();
								if (getWiztype() != PERMESSI){
									results = ProfilerHelper.getInstance().filterImmobili(results, false);
								}
								if (getWiztype() != this.RICERCACLOUD){
									listaRisultatiImmobili.setRisultati(results);
									returnValue = listaRisultatiImmobili;
								}
							}
						} 
					}else{
						if (checkCriteriSyntax(false,WinkhouseUtils.IMMOBILI)){
							SearchEngineImmobili sei = new SearchEngineImmobili(ricerca.getCriteriImmobili());
							ArrayList results = sei.find();
							if (getWiztype() != PERMESSI){
								results = ProfilerHelper.getInstance().filterImmobili(results, false);
							}
							if (getWiztype() != this.RICERCACLOUD){
								listaRisultatiImmobili.setRisultati(results);
								returnValue = listaRisultatiImmobili;
							}

						}
					}						
				}
			}			
			
		}
		
		if (page instanceof ListaCriteriImmobiliAffitti){
			if (ricerca.getCriteriImmobiliAffitti() != null){
				if (ricerca.getCriteriImmobiliAffitti().size() > 0){
					if (ricerca.getCriteriImmobiliAffitti().size() == 1){
						if (((ColloquiCriteriRicercaVO)ricerca.getCriteriImmobiliAffitti()
																.get(0)).getGetterMethodName()
																		.equalsIgnoreCase("(") ||
							((ColloquiCriteriRicercaVO)ricerca.getCriteriImmobiliAffitti()
																.get(0)).getGetterMethodName()
																		.equalsIgnoreCase(")") ||
							((ColloquiCriteriRicercaVO)ricerca.getCriteriImmobiliAffitti()
																.get(0)).getGetterMethodName()
																		.equalsIgnoreCase("")																		
						   ){
							
						}else{
							if (checkCriteriSyntax(false,WinkhouseUtils.AFFITTI)){
								SearchEngineImmobiliAffitti seia = new SearchEngineImmobiliAffitti(ricerca.getCriteriImmobiliAffitti());
								ArrayList results = seia.find();
								
								if (getWiztype() != PERMESSI){
									results = ProfilerHelper.getInstance().filterAffitti(results, false);
								}								
								
								if (getWiztype() != this.RICERCACLOUD){
									listaRisultatiImmobili.setRisultati(results);
									returnValue = listaRisultatiImmobili;
								}
							}							
						} 
					}else{
						if (checkCriteriSyntax(false,WinkhouseUtils.AFFITTI)){
							SearchEngineImmobiliAffitti seia = new SearchEngineImmobiliAffitti(ricerca.getCriteriImmobiliAffitti());
							ArrayList results = seia.find();
							
							if (getWiztype() != PERMESSI){
								results = ProfilerHelper.getInstance().filterAffitti(results, false);
							}								
							
							if (getWiztype() != this.RICERCACLOUD){
								listaRisultatiImmobili.setRisultati(results);
								returnValue = listaRisultatiImmobili;
							}
						}
					}
				}
			}			
			
		}		
		if (page instanceof ListaCriteriAnagrafiche){
			if (ricerca.getCriteriAnagrafiche() != null){
				if (ricerca.getCriteriAnagrafiche().size() > 0){
					if (ricerca.getCriteriAnagrafiche().size() == 1){
						if (((ColloquiCriteriRicercaVO)ricerca.getCriteriAnagrafiche()
																.get(0)).getGetterMethodName()
																		.equalsIgnoreCase("(") ||
							((ColloquiCriteriRicercaVO)ricerca.getCriteriAnagrafiche()
																.get(0)).getGetterMethodName()
																		.equalsIgnoreCase(")") ||
							((ColloquiCriteriRicercaVO)ricerca.getCriteriAnagrafiche()
																.get(0)).getGetterMethodName()
																		.equalsIgnoreCase("")																		
						   ){
							
						}else{
							if (checkCriteriSyntax(false,WinkhouseUtils.ANAGRAFICHE)){
								SearchEngineAnagrafiche sea = new SearchEngineAnagrafiche(ricerca.getCriteriAnagrafiche());
								ArrayList results = sea.find();
								
								if (getWiztype() != PERMESSI){
									results = ProfilerHelper.getInstance().filterAnagrafiche(results, false);
								}								
								if (getWiztype() != this.RICERCACLOUD){
									listaRisultatiAnagrafiche.setRisultati(results);
									returnValue = listaRisultatiAnagrafiche;
								}
																
							}							
						} 
					}else{
						if (checkCriteriSyntax(false,WinkhouseUtils.ANAGRAFICHE)){
							SearchEngineAnagrafiche sea = new SearchEngineAnagrafiche(ricerca.getCriteriAnagrafiche());
							ArrayList results = sea.find();
							if (getWiztype() != PERMESSI){
								results = ProfilerHelper.getInstance().filterAnagrafiche(results, false);
							}								
							if (getWiztype() != this.RICERCACLOUD){
								listaRisultatiAnagrafiche.setRisultati(results);
								returnValue = listaRisultatiAnagrafiche;
							}
						}
					}						
				}
			}						
		}
		if (page instanceof ListaCriteriColloqui){
			if (ricerca.getCriteriColloqui() != null){
				if (ricerca.getCriteriColloqui().size() > 0){
					if (ricerca.getCriteriColloqui().size() == 1){
						if (((ColloquiCriteriRicercaVO)ricerca.getCriteriColloqui()
																.get(0)).getGetterMethodName()
																		.equalsIgnoreCase("(") ||
							((ColloquiCriteriRicercaVO)ricerca.getCriteriColloqui()
																.get(0)).getGetterMethodName()
																		.equalsIgnoreCase(")") ||
							((ColloquiCriteriRicercaVO)ricerca.getCriteriColloqui()
																.get(0)).getGetterMethodName()
																		.equalsIgnoreCase("")																		
						   ){
							
						}else{
							if (checkCriteriSyntax(false,WinkhouseUtils.COLLOQUI)){
								SearchEngineColloqui sei = new SearchEngineColloqui(ricerca.getCriteriColloqui());
								ArrayList results = sei.find();
								if (getWiztype() != PERMESSI){
									results = ProfilerHelper.getInstance().filterColloqui(results, false);
								}
								if (getWiztype() != this.RICERCACLOUD){
									listaRisultatiColloqui.setRisultati(results);								
									returnValue = listaRisultatiColloqui;
								}
							}							
						} 
					}else{
						if (checkCriteriSyntax(false,WinkhouseUtils.COLLOQUI)){
							SearchEngineColloqui sei = new SearchEngineColloqui(ricerca.getCriteriColloqui());
							ArrayList results = sei.find();
							if (getWiztype() != PERMESSI){
								results = ProfilerHelper.getInstance().filterColloqui(results, false);
							}
							if (getWiztype() != this.RICERCACLOUD){
								listaRisultatiColloqui.setRisultati(results);								
								returnValue = listaRisultatiColloqui;
							}
						}
					}						
				}
			}			

		}

		return returnValue;
	}
	
	public boolean checkCriteriSyntax(boolean showpopup, String type){
		boolean returnValue = true;
		int numOpen = 0;
		int numClose = 0;
		if (
				(ricerca != null) && 
			    (
			    		(type.equalsIgnoreCase(WinkhouseUtils.IMMOBILI) && ricerca.getCriteriImmobili() != null) ||
			    		(type.equalsIgnoreCase(WinkhouseUtils.AFFITTI) && ricerca.getCriteriImmobiliAffitti() != null) ||
			    		(type.equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE) && ricerca.getCriteriAnagrafiche() != null)
			    )
			){
			
			Iterator it = (type.equalsIgnoreCase(WinkhouseUtils.IMMOBILI))
					 	      ? ricerca.getCriteriImmobili().iterator()
						      : (type.equalsIgnoreCase(WinkhouseUtils.AFFITTI))
								   ? ricerca.getCriteriImmobiliAffitti().iterator()
							       : ricerca.getCriteriAnagrafiche().iterator();
			String prev = "";
			while (it.hasNext()){
				ColloquiCriteriRicercaVO ccrVO = (ColloquiCriteriRicercaVO)it.next();
				if (ccrVO.getGetterMethodName().equalsIgnoreCase("(")){
					if (!prev.equalsIgnoreCase(")")){
						numOpen++;					 
					}else{
						returnValue = false;
						break;
					}					
				}
				if (ccrVO.getGetterMethodName().equalsIgnoreCase(")")){
					if (!prev.equalsIgnoreCase("(")){
						numClose++;						 
					}else{
						returnValue = false;
						break;
					}															
				}	
				prev = ccrVO.getGetterMethodName();
			}
		}
		if (returnValue){
			returnValue = (numOpen == numClose)?true:false;
			if (!returnValue){
				if (showpopup){
					MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
					mb.setText("Errore sintassi");
					mb.setMessage("Numero parentesi aperte diverse da numero parentesi chiuse");			
					mb.open();							
				}
			}else{
				boolean verifycheck = true;
				if (type.equalsIgnoreCase(WinkhouseUtils.IMMOBILI)){
					SearchEngineImmobili sei = new SearchEngineImmobili(ricerca.getCriteriImmobili());
					verifycheck = sei.verifyQuery();
				}else if (type.equalsIgnoreCase(WinkhouseUtils.AFFITTI)){
					SearchEngineImmobiliAffitti seia = new SearchEngineImmobiliAffitti(ricerca.getCriteriImmobiliAffitti());
					verifycheck = seia.verifyQuery();					
				}else if (type.equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE)){
					SearchEngineAnagrafiche sea = new SearchEngineAnagrafiche(ricerca.getCriteriAnagrafiche());
					verifycheck = sea.verifyQuery();					
				}else{
					SearchEngineColloqui sec = new SearchEngineColloqui(ricerca.getCriteriColloqui());
					verifycheck = sec.verifyQuery();
				}
					
				if (verifycheck){
					if (showpopup){
						MessageBox mb = new MessageBox(this.getShell(),SWT.ICON_INFORMATION);
						mb.setText("Sintassi corretta");
						mb.setMessage("La sintassi delle condizioni inserite risulta corretta");			
						mb.open();
					}
				}else{
					if (showpopup){
						MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
						mb.setText("Errore sintassi");
						mb.setMessage("Richiesta generata non corretta, \n controllare i criteri inseriti");			
						mb.open();
					}
				}
								
			}			
		}else{
			if (showpopup){
				MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
				mb.setText("Errore sequenza parentesi");
				mb.setMessage("Sequenza parentesi non corretta");			
				mb.open();							
			}			
		}
		return returnValue;
	}

	@Override
	public IWizardPage getPreviousPage(IWizardPage page) {
		return super.getPreviousPage(page);
	}

	@Override
	public boolean canFinish() {
		return true;
	}

	public int getWiztype() {
		return wiztype;
	}

	public void setWiztype(int wiztype) {
		this.wiztype = wiztype;
	}

	public AgentiVO getAgente() {
		return agente;
	}

	public void setAgente(AgentiVO agente) {
		this.agente = agente;
	}

	
	public Object getReturnObject() {
		return returnObject;
	}

	
	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
	}

	
	public String getReturnObjectMethodName() {
		return returnObjectMethodName;
	}

	
	public void setReturnObjectMethodName(String returnObjectMethodName) {
		this.returnObjectMethodName = returnObjectMethodName;
	}

	public Class getReturnType() {
		return returnType;
	}

	public void setReturnType(Class returnType) {
		this.returnType = returnType;
	}


}
