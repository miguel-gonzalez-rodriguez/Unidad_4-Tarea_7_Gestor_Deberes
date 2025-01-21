package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabla_deberes")
public class Homework implements Parcelable {

    @ColumnInfo(name = "asignatura")
    private String subject; // Asignatura (PMDM, AD, etc.)
    @ColumnInfo(name = "descripcion")
    private String description; // Descripción del deber
    @ColumnInfo(name = "fecha_entrega")
    private String dueDate; // Fecha de entrega en formato dd/MM/yyyy
    @ColumnInfo(name = "completado")
    private boolean isCompleted; // Estado del deber

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Añadimos una propiedad para la clave primaria de la tabla
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    // Constructor
    public Homework(String subject, String description, String dueDate, boolean isCompleted) {
        this.subject = subject;
        this.description = description;
        this.dueDate = dueDate;
        this.isCompleted = isCompleted;
    }

    // Getters y Setters
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    // Implementación de Parcelable
    protected Homework(Parcel in) {
        // Añadimos línea para el id
        id = in.readInt();

        subject = in.readString();
        description = in.readString();
        dueDate = in.readString();
        isCompleted = in.readByte() != 0;
    }

    public static final Creator<Homework> CREATOR = new Creator<Homework>() {
        @Override
        public Homework createFromParcel(Parcel in) {
            return new Homework(in);
        }

        @Override
        public Homework[] newArray(int size) {
            return new Homework[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subject);
        dest.writeString(description);
        dest.writeString(dueDate);
        dest.writeByte((byte) (isCompleted ? 1 : 0));

        // Añadimos línea para el id
        dest.writeInt(id);
    }
}

