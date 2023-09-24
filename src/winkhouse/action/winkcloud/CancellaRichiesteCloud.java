/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.eclipse.jface.action.Action
 *  org.eclipse.jface.dialogs.MessageDialog
 *  org.eclipse.jface.resource.ImageDescriptor
 *  org.eclipse.jface.viewers.StructuredSelection
 *  org.eclipse.jface.viewers.TableViewer
 *  org.eclipse.swt.widgets.Shell
 */
package winkhouse.action.winkcloud;

import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Shell;
import winkhouse.Activator;

public class CancellaRichiesteCloud
extends Action {
    private TableViewer tv = null;

    public CancellaRichiesteCloud() {
    }

    public CancellaRichiesteCloud(String text) {
        super(text);
    }

    public CancellaRichiesteCloud(String text, ImageDescriptor image) {
        super(text, image);
    }

    public CancellaRichiesteCloud(String text, int style) {
        super(text, style);
    }

    public ImageDescriptor getImageDescriptor() {
        return Activator.getImageDescriptor("icons/edittrash.png");
    }

    public void run() {
        if (this.tv != null) {
            Iterator it = ((StructuredSelection)this.tv.getSelection()).iterator();
            while (it.hasNext()) {
                ((ArrayList)this.tv.getInput()).remove(it.next());
            }
            this.tv.refresh();
        } else {
            MessageDialog.openError((Shell)this.tv.getControl().getShell(), (String)"Errore interno", (String)"Impossibile individuare la tabella da cancellare");
        }
    }

    public TableViewer getTv() {
        return this.tv;
    }

    public void setTv(TableViewer tv) {
        this.tv = tv;
    }

    public String getToolTipText() {
        return "Cancella le richieste selezionate";
    }
}

