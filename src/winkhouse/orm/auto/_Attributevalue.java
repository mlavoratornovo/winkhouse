package winkhouse.orm.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

import winkhouse.orm.Agenti;
import winkhouse.orm.Attribute;

/**
 * Class _Attributevalue was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Attributevalue extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String IDVALUE_PK_COLUMN = "IDVALUE";

    public static final Property<LocalDateTime> DATEUPDATE = Property.create("dateupdate", LocalDateTime.class);
    public static final Property<String> FIELDVALUE = Property.create("fieldvalue", String.class);
    public static final Property<Integer> IDOBJECT = Property.create("idobject", Integer.class);
    public static final Property<Agenti> AGENTI = Property.create("agenti", Agenti.class);
    public static final Property<Attribute> ATTRIBUTE = Property.create("attribute", Attribute.class);

    protected LocalDateTime dateupdate;
    protected String fieldvalue;
    protected Integer idobject;

    protected Object agenti;
    protected Object attribute;

    public void setDateupdate(LocalDateTime dateupdate) {
        beforePropertyWrite("dateupdate", this.dateupdate, dateupdate);
        this.dateupdate = dateupdate;
    }

    public LocalDateTime getDateupdate() {
        beforePropertyRead("dateupdate");
        return this.dateupdate;
    }

    public void setFieldvalue(String fieldvalue) {
        beforePropertyWrite("fieldvalue", this.fieldvalue, fieldvalue);
        this.fieldvalue = fieldvalue;
    }

    public String getFieldvalue() {
        beforePropertyRead("fieldvalue");
        return this.fieldvalue;
    }

    public void setIdobject(int idobject) {
        beforePropertyWrite("idobject", this.idobject, idobject);
        this.idobject = idobject;
    }

    public int getIdobject() {
        beforePropertyRead("idobject");
        if(this.idobject == null) {
            return 0;
        }
        return this.idobject;
    }

    public void setAgenti(Agenti agenti) {
        setToOneTarget("agenti", agenti, true);
    }

    public Agenti getAgenti() {
        return (Agenti)readProperty("agenti");
    }

    public void setAttribute(Attribute attribute) {
        setToOneTarget("attribute", attribute, true);
    }

    public Attribute getAttribute() {
        return (Attribute)readProperty("attribute");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "dateupdate":
                return this.dateupdate;
            case "fieldvalue":
                return this.fieldvalue;
            case "idobject":
                return this.idobject;
            case "agenti":
                return this.agenti;
            case "attribute":
                return this.attribute;
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
            case "fieldvalue":
                this.fieldvalue = (String)val;
                break;
            case "idobject":
                this.idobject = (Integer)val;
                break;
            case "agenti":
                this.agenti = val;
                break;
            case "attribute":
                this.attribute = val;
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
        out.writeObject(this.fieldvalue);
        out.writeObject(this.idobject);
        out.writeObject(this.agenti);
        out.writeObject(this.attribute);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.dateupdate = (LocalDateTime)in.readObject();
        this.fieldvalue = (String)in.readObject();
        this.idobject = (Integer)in.readObject();
        this.agenti = in.readObject();
        this.attribute = in.readObject();
    }

}