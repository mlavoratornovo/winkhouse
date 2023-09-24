package winkhouse.wizard.colloqui;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.PojoProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
//import org.eclipse.core.databinding.beans.PojoObservables;
//import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import winkhouse.wizard.ColloquiWizard;


public class VarieColloqui extends WizardPage {

	private Composite container = null;
	private Text tVarieAgenzia = null;
	private Text tVarieCliente = null;
	
	public VarieColloqui(String pageName) {
		super(pageName);
	}

	public VarieColloqui(String pageName, String title,
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
		
		GridData gd = new GridData();		
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		
		Label labelVarieAgenzia = new Label(container,SWT.NONE);
		labelVarieAgenzia.setText("Varie agenzia");
		labelVarieAgenzia.setLayoutData(gd);
		
		GridData gdVarieAgenzia = new GridData();
		gdVarieAgenzia.grabExcessHorizontalSpace = true;
		gdVarieAgenzia.grabExcessVerticalSpace = true;
		gdVarieAgenzia.verticalAlignment = SWT.FILL;
		gdVarieAgenzia.horizontalAlignment = SWT.FILL;
		gdVarieAgenzia.minimumHeight = 50;
		
		tVarieAgenzia = new Text(container,SWT.MULTI);
		tVarieAgenzia.setLayoutData(gdVarieAgenzia);

		Label labelVarieCliente = new Label(container,SWT.NONE);
		labelVarieCliente.setText("Varie cliente");
		labelVarieCliente.setLayoutData(gd);
		
		GridData gdVarieCliente = new GridData();
		gdVarieCliente.grabExcessHorizontalSpace = true;
		gdVarieCliente.grabExcessVerticalSpace = true;
		gdVarieCliente.verticalAlignment = SWT.FILL;
		gdVarieCliente.horizontalAlignment = SWT.FILL;
		gdVarieCliente.minimumHeight = 50;
		
		tVarieCliente = new Text(container,SWT.MULTI);
		tVarieCliente.setLayoutData(gdVarieCliente);

		DataBindingContext bindingContext = new DataBindingContext();
		bindCommenti(bindingContext);
		setControl(container);
	}
	
	private void bindCommenti(DataBindingContext bindingContext){
		
		if (((ColloquiWizard)getWizard()).getColloquio() != null){
			
			bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tVarieAgenzia), 
									 PojoProperties.value("commentoAgenzia").observe(((ColloquiWizard)getWizard()).getColloquio()),
									 null, 
									 null);

			bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tVarieCliente), 
									 PojoProperties.value("commentoCliente").observe(((ColloquiWizard)getWizard()).getColloquio()),
									 null, 
									 null);
			
		}
	}	
}
