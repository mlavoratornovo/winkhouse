package winkhouse.orm.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

/**
 * Class _Promemoriaoggetti was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Promemoriaoggetti extends BaseDataObject {

    private static final long serialVersionUID = 1L; 


    public static final Property<Integer> CODOGGETTO = Property.create("codoggetto", Integer.class);
    public static final Property<Integer> CODPROMEMORIA = Property.create("codpromemoria", Integer.class);
    public static final Property<Integer> TIPO = Property.create("tipo", Integer.class);

    protected Integer codoggetto;
    protected Integer codpromemoria;
    protected Integer tipo;


    public void setCodoggetto(int codoggetto) {
        beforePropertyWrite("codoggetto", this.codoggetto, codoggetto);
        this.codoggetto = codoggetto;
    }

    public int getCodoggetto() {
        beforePropertyRead("codoggetto");
        if(this.codoggetto == null) {
            return 0;
        }
        return this.codoggetto;
    }

    public void setCodpromemoria(int codpromemoria) {
        beforePropertyWrite("codpromemoria", this.codpromemoria, codpromemoria);
        this.codpromemoria = codpromemoria;
    }

    public int getCodpromemoria() {
        beforePropertyRead("codpromemoria");
        if(this.codpromemoria == null) {
            return 0;
        }
        return this.codpromemoria;
    }

    public void setTipo(int tipo) {
        beforePropertyWrite("tipo", this.tipo, tipo);
        this.tipo = tipo;
    }

    public int getTipo() {
        beforePropertyRead("tipo");
        if(this.tipo == null) {
            return 0;
        }
        return this.tipo;
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "codoggetto":
                return this.codoggetto;
            case "codpromemoria":
                return this.codpromemoria;
            case "tipo":
                return this.tipo;
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
            case "codoggetto":
                this.codoggetto = (Integer)val;
                break;
            case "codpromemoria":
                this.codpromemoria = (Integer)val;
                break;
            case "tipo":
                this.tipo = (Integer)val;
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
        out.writeObject(this.codoggetto);
        out.writeObject(this.codpromemoria);
        out.writeObject(this.tipo);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.codoggetto = (Integer)in.readObject();
        this.codpromemoria = (Integer)in.readObject();
        this.tipo = (Integer)in.readObject();
    }

}
