package winkhouse.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import winkhouse.view.agenda.CalendarioView;
import winkhouse.view.agenda.DettaglioAppuntamentoView;
import winkhouse.view.agenda.ListaAppuntamentiView;
import winkhouse.view.colloqui.DettaglioColloquioView;


public class AgendaPerspective implements IPerspectiveFactory {

	public final static String ID = "winkhouse.agenda";
	
	@Override
	public void createInitialLayout(IPageLayout layout) {

		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);		
		
		IFolderLayout fllistaappuntamenti = layout.createFolder("listaappuntamenti", IPageLayout.BOTTOM, 0.10f, editorArea);
		fllistaappuntamenti.addView(ListaAppuntamentiView.ID);
		layout.getViewLayout(ListaAppuntamentiView.ID).setCloseable(false);
		layout.getViewLayout(ListaAppuntamentiView.ID).setMoveable(false);

		fllistaappuntamenti.addView(CalendarioView.ID);
		layout.getViewLayout(CalendarioView.ID).setCloseable(false);
		layout.getViewLayout(CalendarioView.ID).setMoveable(false);		
		
		IFolderLayout fldettaglioappuntamento = layout.createFolder("dettaglioappuntamento", IPageLayout.TOP, 0.40f, ListaAppuntamentiView.ID);
		fldettaglioappuntamento.addPlaceholder(DettaglioAppuntamentoView.ID);
		fldettaglioappuntamento.addPlaceholder(DettaglioAppuntamentoView.ID+":*");	
		fldettaglioappuntamento.addPlaceholder(DettaglioColloquioView.ID);
		fldettaglioappuntamento.addPlaceholder(DettaglioColloquioView.ID+":*");

	}

}
