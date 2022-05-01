package winkhouse.orm.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

import winkhouse.orm.Agenti;

/**
 * Class _Permessiui was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Permessiui extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String CODPERMESSOUI_PK_COLUMN = "CODPERMESSOUI";

    public static final Property<LocalDateTime> DATEUPDATE = Property.create("dateupdate", LocalDateTime.class);
    public static final Property<String> DIALOGID = Property.create("dialogid", String.class);
    public static final Property<Boolean> ISNOT = Property.create("isnot", Boolean.class);
    public static final Property<String> PERSPECTIVEID = Property.create("perspectiveid", String.class);
    public static final Property<String> VIEWID = Property.create("viewid", String.class);
    public static final Property<Agenti> AGENTI = Property.create("agenti", Agenti.class);
    public static final Property<Agenti> AGENTI1 = Property.create("agenti1", Agenti.class);

    protected LocalDateTime dateupdate;
    protected String dialogid;
    protected Boolean isnot;
    protected String perspectiveid;
    protected String viewid;

    protected Object agenti;
    protected Object agenti1;

    public void setDateupdate(LocalDateTime dateupdate) {
        beforePropertyWrite("dateupdate", this.dateupdate, dateupdate);
        this.dateupdate = dateupdate;
    }

    public LocalDateTime getDateupdate() {
        beforePropertyRead("dateupdate");
        return this.dateupdate;
    }

    public void setDialogid(String dialogid) {
        beforePropertyWrite("dialogid", this.dialogid, dialogid);
        this.dialogid = dialogid;
    }

    public String getDialogid() {
        beforePropertyRead("dialogid");
        return this.dialogid;
    }

    public void setIsnot(boolean isnot) {
        beforePropertyWrite("isnot", this.isnot, isnot);
        this.isnot = isnot;
    }

	public boolean isIsnot() {
        beforePropertyRead("isnot");
        if(this.isnot == null) {
            return false;
        }
        return this.isnot;
    }

    public void setPerspectiveid(String perspectiveid) {
        beforePropertyWrite("perspectiveid", this.perspectiveid, perspectiveid);
        this.perspectiveid = perspectiveid;
    }

    public String getPerspectiveid() {
        beforePropertyRead("perspectiveid");
        return this.perspectiveid;
    }

    public void setViewid(String viewid) {
        beforePropertyWrite("viewid", this.viewid, viewid);
        this.viewid = viewid;
    }

    public String getViewid() {
        beforePropertyRead("viewid");
        return this.viewid;
    }

    public void setAgenti(Agenti agenti) {
        setToOneTarget("agenti", agenti, true);
    }

    public Agenti getAgenti() {
        return (Agenti)readProperty("agenti");
    }

    public void setAgenti1(Agenti agenti1) {
        setToOneTarget("agenti1", agenti1, true);
    }

    public Agenti getAgenti1() {
        return (Agenti)readProperty("agenti1");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "dateupdate":
                return this.dateupdate;
            case "dialogid":
                return this.dialogid;
            case "isnot":
                return this.isnot;
            case "perspectiveid":
                return this.perspectiveid;
            case "viewid":
                return this.viewid;
            case "agenti":
                return this.agenti;
            case "agenti1":
                return this.agenti1;
            default:
                return super.readPropertyDirectly(propName);
        }
    }

    @Override
    public void writePropertyDirectly(String propName, Object val) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch (propName) {
            case "dateupdate":
                this.dateupdate = (LocalDateTime)val;
                break;
            case "dialogid":
                this.dialogid = (String)val;
                break;
            case "isnot":
                this.isnot = (Boolean)val;
                break;
            case "perspectiveid":
                this.perspectiveid = (String)val;
                break;
            case "viewid":
                this.viewid = (String)val;
                break;
            case "agenti":
                this.agenti = val;
                break;
            case "agenti1":
                this.agenti1 = val;
                break;
            default:
                super.writePropertyDirectly(propName, val);
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        writeSerialized(out);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        readSerialized(in);
    }

    @Override
    protected void writeState(ObjectOutputStream out) throws IOException {
        super.writeState(out);
        out.writeObject(this.dateupdate);
        out.writeObject(this.dialogid);
        out.writeObject(this.isnot);
        out.writeObject(this.perspectiveid);
        out.writeObject(this.viewid);
        out.writeObject(this.agenti);
        out.writeObject(this.agenti1);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.dateupdate = (LocalDateTime)in.readObject();
        this.dialogid = (String)in.readObject();
        this.isnot = (Boolean)in.readObject();
        this.perspectiveid = (String)in.readObject();
        this.viewid = (String)in.readObject();
        this.agenti = in.readObject();
        this.agenti1 = in.readObject();
    }

}
