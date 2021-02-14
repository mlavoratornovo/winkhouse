package winkhouse.xmldeser.helpers;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.ui.PlatformUI;

import winkhouse.xmldeser.utils.CheckExists;
import winkhouse.xmldeser.utils.DataWriter;
import winkhouse.xmldeser.utils.ElementFileLoader;
import winkhouse.xmldeser.wizard.importer.ImporterWizard;
import winkhouse.xmldeser.wizard.importer.vo.ImporterVO;

public class ImporterHelper {

	public ImporterHelper() {
		// TODO Auto-generated constructor stub
	}
	
	public void importDataNoShell(ImporterVO importerVO){
		ElementFileLoader efl = new ElementFileLoader(importerVO);
		try {
			efl.run(null);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void importData(ImporterVO importerVO){
		
		ProgressMonitorDialog pmd = new ProgressMonitorDialog(PlatformUI.getWorkbench()
																		.getActiveWorkbenchWindow()
																		.getShell());
		try {
			pmd.run(false, true, new ElementFileLoader(importerVO));
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	public void checkExists(ImporterVO importerVO){
		
		ProgressMonitorDialog pmd = new ProgressMonitorDialog(PlatformUI.getWorkbench()
																		.getActiveWorkbenchWindow()
																		.getShell());
		try {
			pmd.run(false, true, new CheckExists(importerVO));
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public void writeData(ImporterVO importerVO){
		
		ProgressMonitorDialog pmd = new ProgressMonitorDialog(PlatformUI.getWorkbench()
																		.getActiveWorkbenchWindow()
																		.getShell());
		try {
			pmd.run(false, true, new DataWriter(importerVO));
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
}
