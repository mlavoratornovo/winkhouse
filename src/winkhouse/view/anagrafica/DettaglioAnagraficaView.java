package winkhouse.view.anagrafica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.PojoProperties;
//import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
//import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.CellEditor.LayoutData;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.action.anagrafiche.CancellaAnagrafica;
import winkhouse.action.anagrafiche.OpenPopupComuniAction;
import winkhouse.action.anagrafiche.RefreshDettaglioAnagrafica;
import winkhouse.action.anagrafiche.SalvaAnagrafica;
import winkhouse.action.recapiti.ApriDettaglioRecapitiAction;
import winkhouse.action.stampa.StampaAnagraficheAction;
import winkhouse.dao.AttributeDAO;
import winkhouse.dao.EntityDAO;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.EntityModel;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Classicliente;
import winkhouse.orm.Agenti;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.affitti.ListaAffittiView;
import winkhouse.view.common.AbbinamentiView;
import winkhouse.view.common.ColloquiView;
import winkhouse.view.common.EAVView;
import winkhouse.view.common.MapView;
import winkhouse.view.common.RecapitiView;
import winkhouse.view.immobili.ImmaginiImmobiliView;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ClassiClientiVO;
import winkhouse.vo.ComuniVO;



public class DettaglioAnagraficaView extends ViewPart {

	public final static String ID = "winkhouse.dettaglioanagraficaview";
	
	private Anagrafiche anagrafica = null;
	//Anagrafica Immobile
	Text tcognome = null;
	Text tnome = null;
	Text tcittaangrafica = null;
	Text tprovinciaanagrafica = null;
	Text tcapanagrafica = null;
	Text tindirizzoanagrafica = null;
	Text tragionesociale = null;
	ComboViewer cvclassiclienti = null;
	ComboViewer cvagenteinseritoreanagrafica = null;
	Text tcommento = null;
	ScrolledForm f = null;
	Image anagraficaImg = Activator.getImageDescriptor("/icons/anagrafica.png").createImage();
	Image anagraficaPropietaImg = Activator.getImageDescriptor("/icons/anagraficaImmobile.png").createImage();
	Text tcodicefiscale = null;
	Text tpiva = null;
	Text tncivico = null;
	boolean isInCompareMode = false;
	
	private Comparator<Agenti> comparatorAgenti = new Comparator<Agenti>(){

		@Override
		public int compare(Agenti arg0,Agenti arg1) {
			return Integer.valueOf(arg0.getCodAgente()).compareTo(arg1.getCodAgente());
		}
		
	};

	
	private Comparator<Classicliente> comparatorClassiCliente = new Comparator<Classicliente>(){

		@Override
		public int compare(Classicliente arg0,Classicliente arg1) {
			return Integer.valueOf(arg0.getCodClasseCliente()).compareTo(Integer.valueOf(arg1.getCodClasseCliente())); 
		}
		
	};
	
	public DettaglioAnagraficaView() {
	}

	@Override
	public void createPartControl(Composite parent) {

		FormToolkit ft = new FormToolkit(getViewSite().getShell().getDisplay());
		f = ft.createScrolledForm(parent);
		
		f.setText("Dettaglio anagrafica");
		f.getBody().setLayout(new GridLayout());
		
		IToolBarManager mgr = f.getToolBarManager();
		
		mgr.add(new OpenPopupComuniAction("Scegli comune", 
										 Activator.getImageDescriptor("icons/cercacomune.png")));
		mgr.add(new RefreshDettaglioAnagrafica());
		mgr.add(new SalvaAnagrafica());
		mgr.add(new CancellaAnagrafica());
		mgr.add(new StampaAnagraficheAction("Report anagrafiche", 
				 						    Action.AS_DROP_DOWN_MENU));		

		f.updateToolBar();
		
			
		GridData gdExpVH = new GridData();
		gdExpVH.grabExcessHorizontalSpace = true;
		gdExpVH.grabExcessVerticalSpace = true;
		gdExpVH.horizontalAlignment = SWT.FILL;
		gdExpVH.verticalAlignment = SWT.FILL;
		
		GridData gdExpVHCS2 = new GridData();
		gdExpVHCS2.grabExcessHorizontalSpace = true;
		gdExpVHCS2.grabExcessVerticalSpace = true;
		gdExpVHCS2.horizontalAlignment = SWT.FILL;
		gdExpVHCS2.verticalAlignment = SWT.FILL;
		gdExpVHCS2.horizontalSpan = 2;		
		
		GridData gdExpH = new GridData();
		gdExpH.grabExcessHorizontalSpace = true;		
		gdExpH.horizontalAlignment = SWT.FILL;
		gdExpH.minimumHeight = 15;

		GridData gdExpH3 = new GridData();
		gdExpH3.grabExcessHorizontalSpace = true;
//			gdExpH3.grabExcessVerticalSpace = true;
		gdExpH3.horizontalAlignment = SWT.FILL;
//			gdExpH3.verticalAlignment = SWT.FILL;
		gdExpH3.minimumHeight = 15;
		gdExpH3.horizontalSpan = 3;
		
		GridData gdExpHV3 = new GridData();
		gdExpHV3.grabExcessHorizontalSpace = true;
		gdExpHV3.grabExcessVerticalSpace = true;
		gdExpHV3.horizontalAlignment = SWT.FILL;
		gdExpHV3.verticalAlignment = SWT.FILL;
		gdExpHV3.minimumHeight = 30;
		gdExpHV3.horizontalSpan = 3;
		

		GridData gdExpH2 = new GridData();
		gdExpH2.grabExcessHorizontalSpace = true;		
		gdExpH2.horizontalAlignment = SWT.FILL;
		gdExpH2.verticalAlignment = SWT.FILL;
		gdExpH2.minimumHeight = 15;
		gdExpH2.horizontalSpan = 2;

		Composite canagraficaimmobile = ft.createComposite(f.getBody());
		canagraficaimmobile.setLayoutData(gdExpVH);
		GridLayout gl = new GridLayout();
		gl.numColumns = 3;
		canagraficaimmobile.setLayout(gl);
		
		Label ragioneSociale = ft.createLabel(canagraficaimmobile, "Ragione sociale",SWT.FLAT);		
		
		tragionesociale = ft.createText(canagraficaimmobile, "",SWT.DOUBLE_BUFFERED);
		tragionesociale.setLayoutData(gdExpH3);
		
		Label lCognome = ft.createLabel(canagraficaimmobile, "Cognome",SWT.FLAT);
//			lCognome.setLayoutData(gdExpH);
		Label lNome = ft.createLabel(canagraficaimmobile, "Nome",SWT.FLAT);
		lNome.setLayoutData(gdExpH2);
		
		tcognome = ft.createText(canagraficaimmobile, "", SWT.DOUBLE_BUFFERED);
		tcognome.setLayoutData(gdExpH);
		tnome = ft.createText(canagraficaimmobile, "", SWT.DOUBLE_BUFFERED);
		tnome.setLayoutData(gdExpH2);
		
		Label lCodiceFiscale = ft.createLabel(canagraficaimmobile, "Codice fiscale", SWT.FLAT);
		Label lPIVA = ft.createLabel(canagraficaimmobile, "Partiva iva", SWT.FLAT);
		lPIVA.setLayoutData(gdExpH2);
		
		tcodicefiscale = ft.createText(canagraficaimmobile, "", SWT.DOUBLE_BUFFERED);
		tcodicefiscale.setLayoutData(gdExpH);
		tpiva = ft.createText(canagraficaimmobile, "", SWT.DOUBLE_BUFFERED);
		tpiva.setLayoutData(gdExpH2);
		
		Label lcitta = ft.createLabel(canagraficaimmobile, "Citt\u00E1");
		Label lprovincia = ft.createLabel(canagraficaimmobile, "Provincia");
		Label lcap = ft.createLabel(canagraficaimmobile, "Cap");
		
		tcittaangrafica = ft.createText(canagraficaimmobile, "", SWT.DOUBLE_BUFFERED);		
		tcittaangrafica.setLayoutData(gdExpH);				
		tprovinciaanagrafica = ft.createText(canagraficaimmobile, "", SWT.DOUBLE_BUFFERED);
		tcapanagrafica = ft.createText(canagraficaimmobile, "", SWT.DOUBLE_BUFFERED);
		
		GridData gdindirizzoNcivico = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gdindirizzoNcivico.horizontalSpan = 3;
		Composite indirizzoNcivico = ft.createComposite(canagraficaimmobile);
		indirizzoNcivico.setLayout(new GridLayout(2, false));
		indirizzoNcivico.setLayoutData(gdindirizzoNcivico);
		
		Label lindirizzo = ft.createLabel(indirizzoNcivico, "Indirizzo");
		lindirizzo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		GridData gdncivico = new GridData(SWT.FILL, SWT.FILL, false, false);
		gdncivico.minimumWidth = 300;
		gdncivico.widthHint = 100;
		Label nCivico = ft.createLabel(indirizzoNcivico, "Numero");
		nCivico.setLayoutData(gdncivico);
		tindirizzoanagrafica = ft.createText(indirizzoNcivico, "", SWT.DOUBLE_BUFFERED);
		tindirizzoanagrafica.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tncivico = ft.createText(indirizzoNcivico, "", SWT.DOUBLE_BUFFERED);
		tncivico.setLayoutData(gdncivico);
		
		Label lclasse = ft.createLabel(canagraficaimmobile, "Categoria");
		lclasse.setLayoutData(gdExpH);
		Label lagente = ft.createLabel(canagraficaimmobile, "Agente inseritore");
		lagente.setLayoutData(gdExpH2);
		 
		cvclassiclienti = new ComboViewer(canagraficaimmobile,SWT.DROP_DOWN | SWT.READ_ONLY | SWT.DOUBLE_BUFFERED);
		cvclassiclienti.getCombo().setLayoutData(gdExpH);
		
		cvagenteinseritoreanagrafica = new ComboViewer(canagraficaimmobile,SWT.DROP_DOWN | SWT.READ_ONLY | SWT.DOUBLE_BUFFERED);
		cvagenteinseritoreanagrafica.getCombo().setLayoutData(gdExpH2);
		
		Label lcommento = ft.createLabel(canagraficaimmobile, "Commento"); 
		lcommento.setLayoutData(gdExpH3);
		
		tcommento = ft.createText(canagraficaimmobile,"", SWT.MULTI|SWT.DOUBLE_BUFFERED|SWT.WRAP|SWT.V_SCROLL|SWT.H_SCROLL);
		tcommento.setLayoutData(gdExpHV3);
		ft.paintBordersFor(canagraficaimmobile);
		f.reflow(true);
	//	section.setClient(canagraficaimmobile);
			
			
	}

	@Override
	public void setFocus() {
		DataBindingContext bindingContext = new DataBindingContext();
		bindAnagrafica(bindingContext);
		updateViews();
		if ((anagrafica != null) && 
			(anagrafica.getImmobilis() != null) && 
			(anagrafica.getImmobilis().size() > 0)){
			f.setImage(anagraficaPropietaImg);
			setTitleImage(anagraficaPropietaImg);
		}else{
			f.setImage(anagraficaImg);
			setTitleImage(anagraficaImg);			
		}
	}

	public Anagrafiche getAnagrafica() {
		return anagrafica;
	}

	public void setAnagrafica(Anagrafiche anagrafica) {
		isInCompareMode = false;
		DataBindingContext bindingContext = new DataBindingContext();
		this.anagrafica = anagrafica;		
		if ((anagrafica.getCognome() == null) && 
			(anagrafica.getNome()== null)){
			setPartName("Nuova anagrafica");
		}else{
			setPartName(anagrafica.getCognome() + " - " + anagrafica.getNome());
		}
		if (
			(anagrafica.getImmobilis() == null) ||
			(anagrafica.getImmobilis().size() == 0)
			){				
			f.setImage(anagraficaImg);
			setTitleImage(anagraficaImg);
		}else{
			f.setImage(anagraficaPropietaImg);
			setTitleImage(anagraficaPropietaImg);
		}
		bindAnagrafica(bindingContext);
		updateViews();
	}
	
	private void updateViews(){

		if (anagrafica != null){
			
			
			IViewPart ivp = PlatformUI.getWorkbench()
					 				 .getActiveWorkbenchWindow()
					 				 .getActivePage()
					 				 .findView(ColloquiView.ID);
			
			if (ivp != null){
				// ((ColloquiView)ivp).setAnagrafica(anagrafica);
				((ColloquiView)ivp).setCompareView(isInCompareMode);
			}

			ivp = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			   			   .getActivePage()
			   			   .findView(RecapitiView.ID);
			
			if (ivp != null){
				ArrayList<Anagrafiche> anagrafiche = new ArrayList<Anagrafiche>();
				anagrafiche.add(anagrafica);			

				ApriDettaglioRecapitiAction adra = new ApriDettaglioRecapitiAction(anagrafiche,false);
				adra.run();
				((RecapitiView)ivp).setCompareView(isInCompareMode);
			}

			ivp = PlatformUI.getWorkbench()
			   			   .getActiveWorkbenchWindow()
			   			   .getActivePage()
			   			   .findView(ImmaginiImmobiliView.ID);

			if (ivp != null){
				((ImmaginiImmobiliView)ivp).setImmobile(null);
				((ImmaginiImmobiliView)ivp).setCompareView(isInCompareMode);
			}
			
			
//			ivp = PlatformUI.getWorkbench()
//			   			   .getActiveWorkbenchWindow()
//			   			   .getActivePage()
//			   			   .findView(ListaAffittiView.ID);
//
//			if (ivp != null){
//				((ListaAffittiView)ivp).setImmobile(null);				
//			}

			ivp = PlatformUI.getWorkbench()
						   .getActiveWorkbenchWindow()
						   .getActivePage()
						   .findView(ImmobiliPropietaView.ID);

			if (ivp != null){
				((ImmobiliPropietaView)ivp).setAnagrafica(anagrafica);
			}
			
			IViewPart eavv = PlatformUI.getWorkbench()
	   				  .getActiveWorkbenchWindow()
	   				  .getActivePage()
	   				  .findView(EAVView.ID);

			if (eavv != null){
				EntityDAO eDAO = new EntityDAO();
				EntityModel e = eDAO.getEntityByClassName(AnagraficheVO.class.getName());
				
				if ((e != null) && (e.getAttributes()!= null)){
					((EAVView)eavv).setAttributes(e.getAttributes(), anagrafica.getCodAnagrafica());
					((EAVView)eavv).setCompareView(isInCompareMode);
				}
			}
			
			IViewPart mapv = PlatformUI
		  				  .getWorkbench()
		  				  .getActiveWorkbenchWindow()
		  				  .getActivePage()
		  				  .findView(MapView.ID);

			if (mapv != null){
		
				((MapView)mapv).searchGeoData(anagrafica.getIndirizzo(), anagrafica.getNcivico(), anagrafica.getCitta());

			}
			
			
			WinkhouseUtils.getInstance()
							.setLastCodAnagraficaSelected(anagrafica.getCodAnagrafica());
			WinkhouseUtils.getInstance()
							.setLastEntityTypeFocused(AnagraficheModel.class.getName());
			
			ivp = PlatformUI.getWorkbench()
		   			   .getActiveWorkbenchWindow()
		   			   .getActivePage()
		   			   .findView(AbbinamentiView.ID);
		
			if (ivp != null){
				((AbbinamentiView)ivp).setAnagrafica(anagrafica);
				((AbbinamentiView)ivp).setCompareView(isInCompareMode);
			}
		
		}
		
	}
	
	private void bindAnagrafica(DataBindingContext bindingContext){
		if (anagrafica != null){
			this.setPartName(((anagrafica.getRagsoc() != null)
							  ? anagrafica.getRagsoc() + " - "
							  :"") + 
							  ((anagrafica.getCognome() != null)
							  ? anagrafica.getCognome() + " - "
							  :"") + 
							  ((anagrafica.getNome() != null)
							  ? anagrafica.getNome() + " - "
							  :""));
			
			  bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tragionesociale),
					  				   PojoProperties.value("ragsoc").observe(anagrafica), null, null);
			  
			  bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tcognome),
					  				   PojoProperties.value("cognome").observe(anagrafica), null, null);

			  bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tnome),
	  				   				   PojoProperties.value("nome").observe(anagrafica), null, null);

			  bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tcodicefiscale),
					  				   PojoProperties.value("codicefiscale").observe(anagrafica), null, null);
			  
			  bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tpiva),
	  				   				   PojoProperties.value("partitaiva").observe(anagrafica), null, null);
			  
			  bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tcapanagrafica),
					  				   PojoProperties.value("cap").observe(anagrafica), null, null);
			  
			  bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tcittaangrafica),
	  				   				   PojoProperties.value("citta").observe(anagrafica), null, null);
			  
			  bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tprovinciaanagrafica),
					  				   PojoProperties.value("provincia").observe(anagrafica), null, null);
			  
			  bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tindirizzoanagrafica),
	  				   				   PojoProperties.value("indirizzo").observe(anagrafica), null, null);
			  
			  bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tncivico),
	  				   				   PojoProperties.value("ncivico").observe(anagrafica), null, null);

			  bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tcommento),
	  				   				   PojoProperties.value("commento").observe(anagrafica), null, null);
			 			
			cvagenteinseritoreanagrafica.setContentProvider(new IStructuredContentProvider(){
	
				@Override
				public Object[] getElements(Object inputElement) {				
					return MobiliaDatiBaseCache.getInstance().getAgenti().toArray();
				}
	
				@Override
				public void dispose() {
	
					
				}
	
				@Override
				public void inputChanged(Viewer viewer, Object oldInput,
						Object newInput) {
	
					
				}
				
			});
			
			cvagenteinseritoreanagrafica.setLabelProvider(new LabelProvider(){
	
				@Override
				public String getText(Object element) {
					return ((Agenti)element).getCognome() + " " + ((Agenti)element).getNome();
				}
				
			});
			
			cvagenteinseritoreanagrafica.setInput(new Object());
			
			if ((anagrafica != null) && 
				(anagrafica.getAgenti() != null)){
				
				int index = Collections.binarySearch(MobiliaDatiBaseCache.getInstance().getAgenti(), 
													 anagrafica.getAgenti(), 
													 comparatorAgenti);
				
				if (index > -1){
					Object[] sel = new Object[1];
					sel[0] = MobiliaDatiBaseCache.getInstance().getAgenti().get(index);
					StructuredSelection ss = new StructuredSelection(sel);
					cvagenteinseritoreanagrafica.setSelection(ss);
				}
			}
			
			cvagenteinseritoreanagrafica.addSelectionChangedListener(new ISelectionChangedListener(){
	
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					anagrafica.setAgenti(((Agenti)((StructuredSelection)event.getSelection()).getFirstElement()));				
				}
				
			});
	
			cvclassiclienti.setContentProvider(new IStructuredContentProvider(){
	
				@Override
				public Object[] getElements(Object inputElement) {
					return MobiliaDatiBaseCache.getInstance().getClassiClienti().toArray();
				}
	
				@Override
				public void dispose() {
				}
	
				@Override
				public void inputChanged(Viewer viewer, Object oldInput,
						Object newInput) {
					
				}
				
			});
			
			cvclassiclienti.setLabelProvider(new LabelProvider(){
	
				@Override
				public String getText(Object element) {				
					return ((Classicliente)element).getDescrizione();
				}
				
			});
			
			cvclassiclienti.setInput(new Object());
			
			if ((anagrafica != null) && 
				(anagrafica.getClassicliente() != null)){
				ArrayList<Classicliente> classi = MobiliaDatiBaseCache.getInstance().getClassiClienti();

				int index = Collections.binarySearch(classi, 
													 anagrafica.getClassicliente(), 
													 comparatorClassiCliente);

				if (index > -1){
					Object[] sel = new Object[1];
					sel[0] = MobiliaDatiBaseCache.getInstance().getClassiClienti().get(index);
					StructuredSelection ss = new StructuredSelection(sel);
					cvclassiclienti.setSelection(ss);
				}
			}
			
			cvclassiclienti.addSelectionChangedListener(new ISelectionChangedListener(){
	
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					anagrafica.setClassicliente(((Classicliente)((StructuredSelection)event.getSelection()).getFirstElement())); 
				}
				
			});
			
		}
			
			
	}
	
	public void setCompareView(boolean enabled){
			
		tcognome.setEditable(!enabled);
		tnome.setEditable(!enabled);
		tcittaangrafica.setEditable(!enabled);		
		tprovinciaanagrafica.setEditable(!enabled);
		tcapanagrafica.setEditable(!enabled);
		tindirizzoanagrafica.setEditable(!enabled);
		
		cvclassiclienti.getCombo().setEnabled(!enabled);
		cvagenteinseritoreanagrafica.getCombo().setEnabled(!enabled);
		
		tcommento.setEditable(!enabled);
		
		tragionesociale.setEditable(!enabled);
		tcodicefiscale.setEditable(!enabled);
		tpiva.setEditable(!enabled);

		isInCompareMode = true;
		for (int i = 0; i < f.getToolBarManager().getItems().length; i++) {
			f.getToolBarManager().getItems()[i].setVisible(!enabled);
			f.getToolBarManager().getItems()[i].update();
		} 
		f.getToolBarManager().update(true);
		f.updateToolBar();
	}

	public void setComune(ComuniVO cVO){
	
		tcittaangrafica.setText(cVO.getComune());
		tprovinciaanagrafica.setText(cVO.getProvincia());
		tcapanagrafica.setText(cVO.getCap());

		
	}
	
	public String getComune(){
		return tcittaangrafica.getText();
	}
}