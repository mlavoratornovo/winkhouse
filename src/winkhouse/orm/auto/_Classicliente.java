package winkhouse.orm.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

import winkhouse.orm.Agenti;
import winkhouse.orm.Anagrafiche;

/**
 * Class _Classicliente was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Classicliente extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String CODCLASSECLIENTE_PK_COLUMN = "CODCLASSECLIENTE";

    public static final Property<LocalDateTime> DATEUPDATE = Property.create("dateupdate", LocalDateTime.class);
    public static final Property<String> DESCRIZIONE = Property.create("descrizione", String.class);
    public static final Property<Integer> ORDINE = Property.create("ordine", Integer.class);
    public static final Property<Agenti> AGENTI = Property.create("agenti", Agenti.class);
    public static final Property<List<Anagrafiche>> ANAGRAFICHES = Property.create("anagrafiches", List.class);

    protected LocalDateTime dateupdate;
    protected String descrizione;
    protected Integer ordine;

    protected Object agenti;
    protected Object anagrafiches;

    public void setDateupdate(LocalDateTime dateupdate) {
        beforePropertyWrite("dateupdate", this.dateupdate, dateupdate);
        this.dateupdate = dateupdate;
    }

    public LocalDateTime getDateupdate() {
        beforePropertyRead("dateupdate");
        return this.dateupdate;
    }

    public void setDescrizione(String descrizione) {
        beforePropertyWrite("descrizione", this.descrizione, descrizione);
        this.descrizione = descrizione;
    }

    public String getDescrizione() {
        beforePropertyRead("descrizione");
        return this.descrizione;
    }

    public void setOrdine(int ordine) {
        beforePropertyWrite("ordine", this.ordine, ordine);
        this.ordine = ordine;
    }

    public int getOrdine() {
        beforePropertyRead("ordine");
        if(this.ordine == null) {
            return 0;
        }
        return this.ordine;
    }

    public void setAgenti(Agenti agenti) {
        setToOneTarget("agenti", agenti, true);
    }

    public Agenti getAgenti() {
        return (Agenti)readProperty("agenti");
    }

    public void addToAnagrafiches(Anagrafiche obj) {
        addToManyTarget("anagrafiches", obj, true);
    }

    public void removeFromAnagrafiches(Anagrafiche obj) {
        removeToManyTarget("anagrafiches", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<Anagrafiche> getAnagrafiches() {
        return (List<Anagrafiche>)readProperty("anagrafiches");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "dateupdate":
                return this.dateupdate;
            case "descrizione":
                return this.descrizione;
            case "ordine":
                return this.ordine;
            case "agenti":
                return this.agenti;
            case "anagrafiches":
                return this.anagrafiches;
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
            case "descrizione":
                this.descrizione = (String)val;
                break;
            case "ordine":
                this.ordine = (Integer)val;
                break;
            case "agenti":
                this.agenti = val;
                break;
            case "anagrafiches":
                this.anagrafiches = val;
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
        out.writeObject(this.descrizione);
        out.writeObject(this.ordine);
        out.writeObject(this.agenti);
        out.writeObject(this.anagrafiches);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.dateupdate = (LocalDateTime)in.readObject();
        this.descrizione = (String)in.readObject();
        this.ordine = (Integer)in.readObject();
        this.agenti = in.readObject();
        this.anagrafiches = in.readObject();
    }

}
