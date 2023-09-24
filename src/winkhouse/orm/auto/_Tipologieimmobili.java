package winkhouse.orm.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

import winkhouse.orm.Agenti;
import winkhouse.orm.Immobili;

/**
 * Class _Tipologieimmobili was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Tipologieimmobili extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String CODTIPOLOGIAIMMOBILE_PK_COLUMN = "CODTIPOLOGIAIMMOBILE";

    public static final Property<LocalDateTime> DATEUPDATE = Property.create("dateupdate", LocalDateTime.class);
    public static final Property<String> DESCRIZIONE = Property.create("descrizione", String.class);
    public static final Property<Agenti> AGENTI = Property.create("agenti", Agenti.class);
    public static final Property<List<Immobili>> IMMOBILIS = Property.create("immobilis", List.class);

    protected LocalDateTime dateupdate;
    protected String descrizione;

    protected Object agenti;
    protected Object immobilis;

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

    public void setAgenti(Agenti agenti) {
        setToOneTarget("agenti", agenti, true);
    }

    public Agenti getAgenti() {
        return (Agenti)readProperty("agenti");
    }

    public void addToImmobilis(Immobili obj) {
        addToManyTarget("immobilis", obj, true);
    }

    public void removeFromImmobilis(Immobili obj) {
        removeToManyTarget("immobilis", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<Immobili> getImmobilis() {
        return (List<Immobili>)readProperty("immobilis");
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
            case "agenti":
                return this.agenti;
            case "immobilis":
                return this.immobilis;
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
            case "agenti":
                this.agenti = val;
                break;
            case "immobilis":
                this.immobilis = val;
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
        out.writeObject(this.agenti);
        out.writeObject(this.immobilis);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.dateupdate = (LocalDateTime)in.readObject();
        this.descrizione = (String)in.readObject();
        this.agenti = in.readObject();
        this.immobilis = in.readObject();
    }

}
