package winkhouse.orm.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

import winkhouse.orm.Affitti;
import winkhouse.orm.Affittispese;
import winkhouse.orm.Agenti;
import winkhouse.orm.Anagrafiche;

/**
 * Class _Affittispese was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Affittispese extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String CODAFFITTISPESE_PK_COLUMN = "CODAFFITTISPESE";

    public static final Property<LocalDateTime> DATAFINE = Property.create("datafine", LocalDateTime.class);
    public static final Property<LocalDateTime> DATAINIZIO = Property.create("datainizio", LocalDateTime.class);
    public static final Property<LocalDateTime> DATAPAGATO = Property.create("datapagato", LocalDateTime.class);
    public static final Property<LocalDateTime> DATEUPDATE = Property.create("dateupdate", LocalDateTime.class);
    public static final Property<String> DESCRIZIONE = Property.create("descrizione", String.class);
    public static final Property<Double> IMPORTO = Property.create("importo", Double.class);
    public static final Property<LocalDateTime> SCADENZA = Property.create("scadenza", LocalDateTime.class);
    public static final Property<Double> VERSATO = Property.create("versato", Double.class);
    public static final Property<Affitti> AFFITTI = Property.create("affitti", Affitti.class);
    public static final Property<Affittispese> AFFITTISPESE = Property.create("affittispese", Affittispese.class);
    public static final Property<List<Affittispese>> AFFITTISPESES = Property.create("affittispeses", List.class);
    public static final Property<Agenti> AGENTI = Property.create("agenti", Agenti.class);
    public static final Property<Anagrafiche> ANAGRAFICHE = Property.create("anagrafiche", Anagrafiche.class);

    protected LocalDateTime datafine;
    protected LocalDateTime datainizio;
    protected LocalDateTime datapagato;
    protected LocalDateTime dateupdate;
    protected String descrizione;
    protected Double importo;
    protected LocalDateTime scadenza;
    protected Double versato;

    protected Object affitti;
    protected Object affittispese;
    protected Object affittispeses;
    protected Object agenti;
    protected Object anagrafiche;

    public void setDatafine(LocalDateTime datafine) {
        beforePropertyWrite("datafine", this.datafine, datafine);
        this.datafine = datafine;
    }

    public LocalDateTime getDatafine() {
        beforePropertyRead("datafine");
        return this.datafine;
    }

    public void setDatainizio(LocalDateTime datainizio) {
        beforePropertyWrite("datainizio", this.datainizio, datainizio);
        this.datainizio = datainizio;
    }

    public LocalDateTime getDatainizio() {
        beforePropertyRead("datainizio");
        return this.datainizio;
    }

    public void setDatapagato(LocalDateTime datapagato) {
        beforePropertyWrite("datapagato", this.datapagato, datapagato);
        this.datapagato = datapagato;
    }

    public LocalDateTime getDatapagato() {
        beforePropertyRead("datapagato");
        return this.datapagato;
    }

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

    public void setImporto(double importo) {
        beforePropertyWrite("importo", this.importo, importo);
        this.importo = importo;
    }

    public double getImporto() {
        beforePropertyRead("importo");
        if(this.importo == null) {
            return 0;
        }
        return this.importo;
    }

    public void setScadenza(LocalDateTime scadenza) {
        beforePropertyWrite("scadenza", this.scadenza, scadenza);
        this.scadenza = scadenza;
    }

    public LocalDateTime getScadenza() {
        beforePropertyRead("scadenza");
        return this.scadenza;
    }

    public void setVersato(double versato) {
        beforePropertyWrite("versato", this.versato, versato);
        this.versato = versato;
    }

    public double getVersato() {
        beforePropertyRead("versato");
        if(this.versato == null) {
            return 0;
        }
        return this.versato;
    }

    public void setAffitti(Affitti affitti) {
        setToOneTarget("affitti", affitti, true);
    }

    public Affitti getAffitti() {
        return (Affitti)readProperty("affitti");
    }

    public void setAffittispese(Affittispese affittispese) {
        setToOneTarget("affittispese", affittispese, true);
    }

    public Affittispese getAffittispese() {
        return (Affittispese)readProperty("affittispese");
    }

    public void addToAffittispeses(Affittispese obj) {
        addToManyTarget("affittispeses", obj, true);
    }

    public void removeFromAffittispeses(Affittispese obj) {
        removeToManyTarget("affittispeses", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<Affittispese> getAffittispeses() {
        return (List<Affittispese>)readProperty("affittispeses");
    }

    public void setAgenti(Agenti agenti) {
        setToOneTarget("agenti", agenti, true);
    }

    public Agenti getAgenti() {
        return (Agenti)readProperty("agenti");
    }

    public void setAnagrafiche(Anagrafiche anagrafiche) {
        setToOneTarget("anagrafiche", anagrafiche, true);
    }

    public Anagrafiche getAnagrafiche() {
        return (Anagrafiche)readProperty("anagrafiche");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "datafine":
                return this.datafine;
            case "datainizio":
                return this.datainizio;
            case "datapagato":
                return this.datapagato;
            case "dateupdate":
                return this.dateupdate;
            case "descrizione":
                return this.descrizione;
            case "importo":
                return this.importo;
            case "scadenza":
                return this.scadenza;
            case "versato":
                return this.versato;
            case "affitti":
                return this.affitti;
            case "affittispese":
                return this.affittispese;
            case "affittispeses":
                return this.affittispeses;
            case "agenti":
                return this.agenti;
            case "anagrafiche":
                return this.anagrafiche;
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
            case "datafine":
                this.datafine = (LocalDateTime)val;
                break;
            case "datainizio":
                this.datainizio = (LocalDateTime)val;
                break;
            case "datapagato":
                this.datapagato = (LocalDateTime)val;
                break;
            case "dateupdate":
                this.dateupdate = (LocalDateTime)val;
                break;
            case "descrizione":
                this.descrizione = (String)val;
                break;
            case "importo":
                this.importo = (Double)val;
                break;
            case "scadenza":
                this.scadenza = (LocalDateTime)val;
                break;
            case "versato":
                this.versato = (Double)val;
                break;
            case "affitti":
                this.affitti = val;
                break;
            case "affittispese":
                this.affittispese = val;
                break;
            case "affittispeses":
                this.affittispeses = val;
                break;
            case "agenti":
                this.agenti = val;
                break;
            case "anagrafiche":
                this.anagrafiche = val;
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
        out.writeObject(this.datafine);
        out.writeObject(this.datainizio);
        out.writeObject(this.datapagato);
        out.writeObject(this.dateupdate);
        out.writeObject(this.descrizione);
        out.writeObject(this.importo);
        out.writeObject(this.scadenza);
        out.writeObject(this.versato);
        out.writeObject(this.affitti);
        out.writeObject(this.affittispese);
        out.writeObject(this.affittispeses);
        out.writeObject(this.agenti);
        out.writeObject(this.anagrafiche);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.datafine = (LocalDateTime)in.readObject();
        this.datainizio = (LocalDateTime)in.readObject();
        this.datapagato = (LocalDateTime)in.readObject();
        this.dateupdate = (LocalDateTime)in.readObject();
        this.descrizione = (String)in.readObject();
        this.importo = (Double)in.readObject();
        this.scadenza = (LocalDateTime)in.readObject();
        this.versato = (Double)in.readObject();
        this.affitti = in.readObject();
        this.affittispese = in.readObject();
        this.affittispeses = in.readObject();
        this.agenti = in.readObject();
        this.anagrafiche = in.readObject();
    }

}
