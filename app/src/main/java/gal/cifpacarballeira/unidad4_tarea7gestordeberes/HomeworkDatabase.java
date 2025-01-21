package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HomeworkDatabase extends SQLiteOpenHelper {
    // Información de la base de datos
    private static final String DATABASE_NAME = "homework_database";
    private static final int DATABASE_VERSION = 1;

    // Información de la tabla
    private static final String TABLE_HOMEWORK = "homework";
    private static final String KEY_ID = "id";
    private static final String KEY_SUBJECT = "subject";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DUE_DATE = "due_date";
    private static final String KEY_ISCOMPLETED = "isCompleted";

    public HomeworkDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla de deberes
        String CREATE_HOMEWORK_TABLE = "CREATE TABLE " + TABLE_HOMEWORK + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_SUBJECT + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DUE_DATE + " TEXT,"
                + KEY_ISCOMPLETED + " INTEGER" + ")";
        db.execSQL(CREATE_HOMEWORK_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Actualizar la base de datos (si cambia la versión)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOMEWORK);
        onCreate(db);
    }

    // Operaciones CRUD

    // Crear una nueva tarea
    public long addHomework(Homework homework) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SUBJECT, homework.getSubject());
        values.put(KEY_DESCRIPTION, homework.getDescription());
        values.put(KEY_DUE_DATE, homework.getDueDate());
        values.put(KEY_ISCOMPLETED, homework.isCompleted() ? 1 : 0); // 1 para true, 0 para false

        long id = db.insert(TABLE_HOMEWORK, null, values);
        db.close();
        return id;
    }

    // Leer una tarea por ID
    public Homework getHomework(int id) {
        // Abrir la base de datos en modo de lectura
        SQLiteDatabase db = this.getReadableDatabase();
        // Crea un cursor para recorrer los resultados de la consulta
        Cursor cursor = db.query(TABLE_HOMEWORK, new String[]{KEY_ID, KEY_SUBJECT, KEY_DESCRIPTION, KEY_DUE_DATE, KEY_ISCOMPLETED},
                KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        // Si hay resultados, mueve el cursor al primer resultado
        // Aquí deberíamos gestionar el null pointer exception
        if (cursor != null) {
            cursor.moveToFirst();
        }

        // Crea un objeto Homework con los datos del cursor
        Homework homework = new Homework();
        if (cursor != null && cursor.getCount() > 0) {
            homework.setId(Integer.parseInt(cursor.getString(0)));
            homework.setSubject(cursor.getString(1));
            homework.setDescription(cursor.getString(2));
            homework.setDueDate(cursor.getString(3));
            // Recordar que SQLite no tiene un tipo booleano, por lo que tenemos que convertirlo
            homework.setCompleted(cursor.getInt(4) == 1);
            cursor.close();
        }
        // Cierra la base de datos
        db.close();

        // Devuelve el objeto Homework
        return homework;
    }

    // Leer todas las tareas
    public List<Homework> getAllHomework() {
        List<Homework> homeworkList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_HOMEWORK;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Homework homework = new Homework();
                homework.setId(Integer.parseInt(cursor.getString(0)));
                homework.setSubject(cursor.getString(1));
                homework.setDescription(cursor.getString(2));
                homework.setDueDate(cursor.getString(3));
                homework.setCompleted(cursor.getInt(4));
                homeworkList.add(homework);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return homeworkList;
    }

    // Actualizar una tarea
    public int updateHomework(Homework homework) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SUBJECT, homework.getSubject());
        values.put(KEY_DESCRIPTION, homework.getDescription());
        values.put(KEY_DUE_DATE, homework.getDueDate());
        values.put(KEY_ISCOMPLETED, homework.isCompleted() ? 1 : 0);

        int rowsAffected = db.update(TABLE_HOMEWORK, values, KEY_ID + "=?", new String[]{String.valueOf(homework.getId())});
        db.close();
        return rowsAffected;
    }

    // Eliminar una tarea
    public void deleteHomework(Homework homework) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HOMEWORK, KEY_ID + "=?", new String[]{String.valueOf(homework.getId())});
        db.close();
    }

    // Eliminar todas las tareas
    public void deleteAllHomework() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HOMEWORK, null, null);
        db.close();
    }


}
