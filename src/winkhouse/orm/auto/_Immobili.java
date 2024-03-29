package winkhouse.orm.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

import winkhouse.orm.Abbinamenti;
import winkhouse.orm.Affitti;
import winkhouse.orm.Agenti;
import winkhouse.orm.Allegatiimmobili;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Classienergetiche;
import winkhouse.orm.Colloqui;
import winkhouse.orm.Daticatastali;
import winkhouse.orm.Immagini;
import winkhouse.orm.Immobilipropietari;
import winkhouse.orm.Riscaldamenti;
import winkhouse.orm.Stanzeimmobili;
import winkhouse.orm.Statoconservativo;
import winkhouse.orm.Tipologieimmobili;

/**
 * Class _Immobili was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Immobili extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String CODIMMOBILE_PK_COLUMN = "CODIMMOBILE";

    public static final Property<Boolean> AFFITTO = Property.create("affitto", Boolean.class);
    public static final Property<Integer> ANNOCOSTRUZIONE = Property.create("annocostruzione", Integer.class);
    public static final Property<String> CAP = Property.create("cap", String.class);
    public static final Property<String> CITTA = Property.create("citta", String.class);
    public static final Property<LocalDate> DATAINSERIMENTO = Property.create("datainserimento", LocalDate.class);
    public static final Property<LocalDate> DATALIBERO = Property.create("datalibero", LocalDate.class);
    public static final Property<LocalDateTime> DATEUPDATE = Property.create("dateupdate", LocalDateTime.class);
    public static final Property<String> DESCRIZIONE = Property.create("descrizione", String.class);
    public static final Property<String> INDIRIZZO = Property.create("indirizzo", String.class);
    public static final Property<Integer> MQ = Property.create("mq", Integer.class);
    public static final Property<Double> MUTUO = Property.create("mutuo", Double.class);
    public static final Property<String> MUTUODESCRIZIONE = Property.create("mutuodescrizione", String.class);
    public static final Property<String> NCIVICO = Property.create("ncivico", String.class);
    public static final Property<Double> PREZZO = Property.create("prezzo", Double.class);
    public static final Property<String> PROVINCIA = Property.create("provincia", String.class);
    public static final Property<String> RIF = Property.create("rif", String.class);
    public static final Property<Double> SPESE = Property.create("spese", Double.class);
    public static final Property<Boolean> STORICO = Property.create("storico", Boolean.class);
    public static final Property<String> VARIE = Property.create("varie", String.class);
    public static final Property<Boolean> VISIONE = Property.create("visione", Boolean.class);
    public static final Property<String> ZONA = Property.create("zona", String.class);
    public static final Property<List<Abbinamenti>> ABBINAMENTIS = Property.create("abbinamentis", List.class);
    public static final Property<List<Affitti>> AFFITTIS = Property.create("affittis", List.class);
    public static final Property<Agenti> AGENTI = Property.create("agenti", Agenti.class);
    public static final Property<Agenti> AGENTI1 = Property.create("agenti1", Agenti.class);
    public static final Property<List<Allegatiimmobili>> ALLEGATIIMMOBILIS = Property.create("allegatiimmobilis", List.class);
    public static final Property<Anagrafiche> ANAGRAFICHE = Property.create("anagrafiche", Anagrafiche.class);
    public static final Property<Classienergetiche> CLASSIENERGETICHE = Property.create("classienergetiche", Classienergetiche.class);
    public static final Property<List<Colloqui>> COLLOQUIS = Property.create("colloquis", List.class);
    public static final Property<List<Daticatastali>> DATICATASTALIS = Property.create("daticatastalis", List.class);
    public static final Property<List<Immagini>> IMMAGINIS = Property.create("immaginis", List.class);
    public static final Property<List<Immobilipropietari>> IMMOBILIPROPIETARIS = Property.create("immobilipropietaris", List.class);
    public static final Property<Riscaldamenti> RISCALDAMENTI = Property.create("riscaldamenti", Riscaldamenti.class);
    public static final Property<List<Stanzeimmobili>> STANZEIMMOBILIS = Property.create("stanzeimmobilis", List.class);
    public static final Property<Statoconservativo> STATOCONSERVATIVO = Property.create("statoconservativo", Statoconservativo.class);
    public static final Property<Tipologieimmobili> TIPOLOGIEIMMOBILI = Property.create("tipologieimmobili", Tipologieimmobili.class);

    protected Boolean affitto;
    protected Integer annocostruzione;
    protected String cap;
    protected String citta;
    protected LocalDate datainserimento;
    protected LocalDate datalibero;
    protected LocalDateTime dateupdate;
    protected String descrizione;
    protected String indirizzo;
    protected Integer mq;
    protected Double mutuo;
    protected String mutuodescrizione;
    protected String ncivico;
    protected Double prezzo;
    protected String provincia;
    protected String rif;
    protected Double spese;
    protected Boolean storico;
    protected String varie;
    protected Boolean visione;
    protected String zona;

    protected Object abbinamentis;
    protected Object affittis;
    protected Object agenti;
    protected Object agenti1;
    protected Object allegatiimmobilis;
    protected Object anagrafiche;
    protected Object classienergetiche;
    protected Object colloquis;
    protected Object daticatastalis;
    protected Object immaginis;
    protected Object immobilipropietaris;
    protected Object riscaldamenti;
    protected Object stanzeimmobilis;
    protected Object statoconservativo;
    protected Object tipologieimmobili;

    public void setAffitto(boolean affitto) {
        beforePropertyWrite("affitto", this.affitto, affitto);
        this.affitto = affitto;
    }

	public boolean isAffitto() {
        beforePropertyRead("affitto");
        if(this.affitto == null) {
            return false;
        }
        return this.affitto;
    }

    public void setAnnocostruzione(int annocostruzione) {
        beforePropertyWrite("annocostruzione", this.annocostruzione, annocostruzione);
        this.annocostruzione = annocostruzione;
    }

    public int getAnnocostruzione() {
        beforePropertyRead("annocostruzione");
        if(this.annocostruzione == null) {
            return 0;
        }
        return this.annocostruzione;
    }

    public void setCap(String cap) {
        beforePropertyWrite("cap", this.cap, cap);
        this.cap = cap;
    }

    public String getCap() {
        beforePropertyRead("cap");
        return this.cap;
    }

    public void setCitta(String citta) {
        beforePropertyWrite("citta", this.citta, citta);
        this.citta = citta;
    }

    public String getCitta() {
        beforePropertyRead("citta");
        return this.citta;
    }

    public void setDatainserimento(LocalDate datainserimento) {
        beforePropertyWrite("datainserimento", this.datainserimento, datainserimento);
        this.datainserimento = datainserimento;
    }

    public LocalDate getDatainserimento() {
        beforePropertyRead("datainserimento");
        return this.datainserimento;
    }

    public void setDatalibero(LocalDate datalibero) {
        beforePropertyWrite("datalibero", this.datalibero, datalibero);
        this.datalibero = datalibero;
    }

    public LocalDate getDatalibero() {
        beforePropertyRead("datalibero");
        return this.datalibero;
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

    public void setIndirizzo(String indirizzo) {
        beforePropertyWrite("indirizzo", this.indirizzo, indirizzo);
        this.indirizzo = indirizzo;
    }

    public String getIndirizzo() {
        beforePropertyRead("indirizzo");
        return this.indirizzo;
    }

    public void setMq(int mq) {
        beforePropertyWrite("mq", this.mq, mq);
        this.mq = mq;
    }

    public int getMq() {
        beforePropertyRead("mq");
        if(this.mq == null) {
            return 0;
        }
        return this.mq;
    }

    public void setMutuo(double mutuo) {
        beforePropertyWrite("mutuo", this.mutuo, mutuo);
        this.mutuo = mutuo;
    }

    public double getMutuo() {
        beforePropertyRead("mutuo");
        if(this.mutuo == null) {
            return 0;
        }
        return this.mutuo;
    }

    public void setMutuodescrizione(String mutuodescrizione) {
        beforePropertyWrite("mutuodescrizione", this.mutuodescrizione, mutuodescrizione);
        this.mutuodescrizione = mutuodescrizione;
    }

    public String getMutuodescrizione() {
        beforePropertyRead("mutuodescrizione");
        return this.mutuodescrizione;
    }

    public void setNcivico(String ncivico) {
        beforePropertyWrite("ncivico", this.ncivico, ncivico);
        this.ncivico = ncivico;
    }

    public String getNcivico() {
        beforePropertyRead("ncivico");
        return this.ncivico;
    }

    public void setPrezzo(double prezzo) {
        beforePropertyWrite("prezzo", this.prezzo, prezzo);
        this.prezzo = prezzo;
    }

    public double getPrezzo() {
        beforePropertyRead("prezzo");
        if(this.prezzo == null) {
            return 0;
        }
        return this.prezzo;
    }

    public void setProvincia(String provincia) {
        beforePropertyWrite("provincia", this.provincia, provincia);
        this.provincia = provincia;
    }

    public String getProvincia() {
        beforePropertyRead("provincia");
        return this.provincia;
    }

    public void setRif(String rif) {
        beforePropertyWrite("rif", this.rif, rif);
        this.rif = rif;
    }

    public String getRif() {
        beforePropertyRead("rif");
        return this.rif;
    }

    public void setSpese(double spese) {
        beforePropertyWrite("spese", this.spese, spese);
        this.spese = spese;
    }

    public double getSpese() {
        beforePropertyRead("spese");
        if(this.spese == null) {
            return 0;
        }
        return this.spese;
    }

    public void setStorico(boolean storico) {
        beforePropertyWrite("storico", this.storico, storico);
        this.storico = storico;
    }

	public boolean isStorico() {
        beforePropertyRead("storico");
        if(this.storico == null) {
            return false;
        }
        return this.storico;
    }

    public void setVarie(String varie) {
        beforePropertyWrite("varie", this.varie, varie);
        this.varie = varie;
    }

    public String getVarie() {
        beforePropertyRead("varie");
        return this.varie;
    }

    public void setVisione(boolean visione) {
        beforePropertyWrite("visione", this.visione, visione);
        this.visione = visione;
    }

	public boolean isVisione() {
        beforePropertyRead("visione");
        if(this.visione == null) {
            return false;
        }
        return this.visione;
    }

    public void setZona(String zona) {
        beforePropertyWrite("zona", this.zona, zona);
        this.zona = zona;
    }

    public String getZona() {
        beforePropertyRead("zona");
        return this.zona;
    }

    public void addToAbbinamentis(Abbinamenti obj) {
        addToManyTarget("abbinamentis", obj, true);
    }

    public void removeFromAbbinamentis(Abbinamenti obj) {
        removeToManyTarget("abbinamentis", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<Abbinamenti> getAbbinamentis() {
        return (List<Abbinamenti>)readProperty("abbinamentis");
    }

    public void addToAffittis(Affitti obj) {
        addToManyTarget("affittis", obj, true);
    }

    public void removeFromAffittis(Affitti obj) {
        removeToManyTarget("affittis", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<Affitti> getAffittis() {
        return (List<Affitti>)readProperty("affittis");
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

    public void addToAllegatiimmobilis(Allegatiimmobili obj) {
        addToManyTarget("allegatiimmobilis", obj, true);
    }

    public void removeFromAllegatiimmobilis(Allegatiimmobili obj) {
        removeToManyTarget("allegatiimmobilis", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<Allegatiimmobili> getAllegatiimmobilis() {
        return (List<Allegatiimmobili>)readProperty("allegatiimmobilis");
    }

    public void setAnagrafiche(Anagrafiche anagrafiche) {
        setToOneTarget("anagrafiche", anagrafiche, true);
    }

    public Anagrafiche getAnagrafiche() {
        return (Anagrafiche)readProperty("anagrafiche");
    }

    public void setClassienergetiche(Classienergetiche classienergetiche) {
        setToOneTarget("classienergetiche", classienergetiche, true);
    }

    public Classienergetiche getClassienergetiche() {
        return (Classienergetiche)readProperty("classienergetiche");
    }

    public void addToColloquis(Colloqui obj) {
        addToManyTarget("colloquis", obj, true);
    }

    public void removeFromColloquis(Colloqui obj) {
        removeToManyTarget("colloquis", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<Colloqui> getColloquis() {
        return (List<Colloqui>)readProperty("colloquis");
    }

    public void addToDaticatastalis(Daticatastali obj) {
        addToManyTarget("daticatastalis", obj, true);
    }

    public void removeFromDaticatastalis(Daticatastali obj) {
        removeToManyTarget("daticatastalis", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<Daticatastali> getDaticatastalis() {
        return (List<Daticatastali>)readProperty("daticatastalis");
    }

    public void addToImmaginis(Immagini obj) {
        addToManyTarget("immaginis", obj, true);
    }

    public void removeFromImmaginis(Immagini obj) {
        removeToManyTarget("immaginis", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<Immagini> getImmaginis() {
        return (List<Immagini>)readProperty("immaginis");
    }

    public void addToImmobilipropietaris(Immobilipropietari obj) {
        addToManyTarget("immobilipropietaris", obj, true);
    }

    public void removeFromImmobilipropietaris(Immobilipropietari obj) {
        removeToManyTarget("immobilipropietaris", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<Immobilipropietari> getImmobilipropietaris() {
        return (List<Immobilipropietari>)readProperty("immobilipropietaris");
    }

    public void setRiscaldamenti(Riscaldamenti riscaldamenti) {
        setToOneTarget("riscaldamenti", riscaldamenti, true);
    }

    public Riscaldamenti getRiscaldamenti() {
        return (Riscaldamenti)readProperty("riscaldamenti");
    }

    public void addToStanzeimmobilis(Stanzeimmobili obj) {
        addToManyTarget("stanzeimmobilis", obj, true);
    }

    public void removeFromStanzeimmobilis(Stanzeimmobili obj) {
        removeToManyTarget("stanzeimmobilis", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<Stanzeimmobili> getStanzeimmobilis() {
        return (List<Stanzeimmobili>)readProperty("stanzeimmobilis");
    }

    public void setStatoconservativo(Statoconservativo statoconservativo) {
        setToOneTarget("statoconservativo", statoconservativo, true);
    }

    public Statoconservativo getStatoconservativo() {
        return (Statoconservativo)readProperty("statoconservativo");
    }

    public void setTipologieimmobili(Tipologieimmobili tipologieimmobili) {
        setToOneTarget("tipologieimmobili", tipologieimmobili, true);
    }

    public Tipologieimmobili getTipologieimmobili() {
        return (Tipologieimmobili)readProperty("tipologieimmobili");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "affitto":
                return this.affitto;
            case "annocostruzione":
                return this.annocostruzione;
            case "cap":
                return this.cap;
            case "citta":
                return this.citta;
            case "datainserimento":
                return this.datainserimento;
            case "datalibero":
                return this.datalibero;
            case "dateupdate":
                return this.dateupdate;
            case "descrizione":
                return this.descrizione;
            case "indirizzo":
                return this.indirizzo;
            case "mq":
                return this.mq;
            case "mutuo":
                return this.mutuo;
            case "mutuodescrizione":
                return this.mutuodescrizione;
            case "ncivico":
                return this.ncivico;
            case "prezzo":
                return this.prezzo;
            case "provincia":
                return this.provincia;
            case "rif":
                return this.rif;
            case "spese":
                return this.spese;
            case "storico":
                return this.storico;
            case "varie":
                return this.varie;
            case "visione":
                return this.visione;
            case "zona":
                return this.zona;
            case "abbinamentis":
                return this.abbinamentis;
            case "affittis":
                return this.affittis;
            case "agenti":
                return this.agenti;
            case "agenti1":
                return this.agenti1;
            case "allegatiimmobilis":
                return this.allegatiimmobilis;
            case "anagrafiche":
                return this.anagrafiche;
            case "classienergetiche":
                return this.classienergetiche;
            case "colloquis":
                return this.colloquis;
            case "daticatastalis":
                return this.daticatastalis;
            case "immaginis":
                return this.immaginis;
            case "immobilipropietaris":
                return this.immobilipropietaris;
            case "riscaldamenti":
                return this.riscaldamenti;
            case "stanzeimmobilis":
                return this.stanzeimmobilis;
            case "statoconservativo":
                return this.statoconservativo;
            case "tipologieimmobili":
                return this.tipologieimmobili;
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
            case "affitto":
                this.affitto = (Boolean)val;
                break;
            case "annocostruzione":
                this.annocostruzione = (Integer)val;
                break;
            case "cap":
                this.cap = (String)val;
                break;
            case "citta":
                this.citta = (String)val;
                break;
            case "datainserimento":
                this.datainserimento = (LocalDate)val;
                break;
            case "datalibero":
                this.datalibero = (LocalDate)val;
                break;
            case "dateupdate":
                this.dateupdate = (LocalDateTime)val;
                break;
            case "descrizione":
                this.descrizione = (String)val;
                break;
            case "indirizzo":
                this.indirizzo = (String)val;
                break;
            case "mq":
                this.mq = (Integer)val;
                break;
            case "mutuo":
                this.mutuo = (Double)val;
                break;
            case "mutuodescrizione":
                this.mutuodescrizione = (String)val;
                break;
            case "ncivico":
                this.ncivico = (String)val;
                break;
            case "prezzo":
                this.prezzo = (Double)val;
                break;
            case "provincia":
                this.provincia = (String)val;
                break;
            case "rif":
                this.rif = (String)val;
                break;
            case "spese":
                this.spese = (Double)val;
                break;
            case "storico":
                this.storico = (Boolean)val;
                break;
            case "varie":
                this.varie = (String)val;
                break;
            case "visione":
                this.visione = (Boolean)val;
                break;
            case "zona":
                this.zona = (String)val;
                break;
            case "abbinamentis":
                this.abbinamentis = val;
                break;
            case "affittis":
                this.affittis = val;
                break;
            case "agenti":
                this.agenti = val;
                break;
            case "agenti1":
                this.agenti1 = val;
                break;
            case "allegatiimmobilis":
                this.allegatiimmobilis = val;
                break;
            case "anagrafiche":
                this.anagrafiche = val;
                break;
            case "classienergetiche":
                this.classienergetiche = val;
                break;
            case "colloquis":
                this.colloquis = val;
                break;
            case "daticatastalis":
                this.daticatastalis = val;
                break;
            case "immaginis":
                this.immaginis = val;
                break;
            case "immobilipropietaris":
                this.immobilipropietaris = val;
                break;
            case "riscaldamenti":
                this.riscaldamenti = val;
                break;
            case "stanzeimmobilis":
                this.stanzeimmobilis = val;
                break;
            case "statoconservativo":
                this.statoconservativo = val;
                break;
            case "tipologieimmobili":
                this.tipologieimmobili = val;
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
        out.writeObject(this.affitto);
        out.writeObject(this.annocostruzione);
        out.writeObject(this.cap);
        out.writeObject(this.citta);
        out.writeObject(this.datainserimento);
        out.writeObject(this.datalibero);
        out.writeObject(this.dateupdate);
        out.writeObject(this.descrizione);
        out.writeObject(this.indirizzo);
        out.writeObject(this.mq);
        out.writeObject(this.mutuo);
        out.writeObject(this.mutuodescrizione);
        out.writeObject(this.ncivico);
        out.writeObject(this.prezzo);
        out.writeObject(this.provincia);
        out.writeObject(this.rif);
        out.writeObject(this.spese);
        out.writeObject(this.storico);
        out.writeObject(this.varie);
        out.writeObject(this.visione);
        out.writeObject(this.zona);
        out.writeObject(this.abbinamentis);
        out.writeObject(this.affittis);
        out.writeObject(this.agenti);
        out.writeObject(this.agenti1);
        out.writeObject(this.allegatiimmobilis);
        out.writeObject(this.anagrafiche);
        out.writeObject(this.classienergetiche);
        out.writeObject(this.colloquis);
        out.writeObject(this.daticatastalis);
        out.writeObject(this.immaginis);
        out.writeObject(this.immobilipropietaris);
        out.writeObject(this.riscaldamenti);
        out.writeObject(this.stanzeimmobilis);
        out.writeObject(this.statoconservativo);
        out.writeObject(this.tipologieimmobili);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.affitto = (Boolean)in.readObject();
        this.annocostruzione = (Integer)in.readObject();
        this.cap = (String)in.readObject();
        this.citta = (String)in.readObject();
        this.datainserimento = (LocalDate)in.readObject();
        this.datalibero = (LocalDate)in.readObject();
        this.dateupdate = (LocalDateTime)in.readObject();
        this.descrizione = (String)in.readObject();
        this.indirizzo = (String)in.readObject();
        this.mq = (Integer)in.readObject();
        this.mutuo = (Double)in.readObject();
        this.mutuodescrizione = (String)in.readObject();
        this.ncivico = (String)in.readObject();
        this.prezzo = (Double)in.readObject();
        this.provincia = (String)in.readObject();
        this.rif = (String)in.readObject();
        this.spese = (Double)in.readObject();
        this.storico = (Boolean)in.readObject();
        this.varie = (String)in.readObject();
        this.visione = (Boolean)in.readObject();
        this.zona = (String)in.readObject();
        this.abbinamentis = in.readObject();
        this.affittis = in.readObject();
        this.agenti = in.readObject();
        this.agenti1 = in.readObject();
        this.allegatiimmobilis = in.readObject();
        this.anagrafiche = in.readObject();
        this.classienergetiche = in.readObject();
        this.colloquis = in.readObject();
        this.daticatastalis = in.readObject();
        this.immaginis = in.readObject();
        this.immobilipropietaris = in.readObject();
        this.riscaldamenti = in.readObject();
        this.stanzeimmobilis = in.readObject();
        this.statoconservativo = in.readObject();
        this.tipologieimmobili = in.readObject();
    }

}
