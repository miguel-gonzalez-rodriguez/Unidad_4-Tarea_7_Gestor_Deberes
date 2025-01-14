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

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
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

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     * <p>
     * <em>Important:</em> You should NOT modify an existing migration step from version X to X+1
     * once a build has been released containing that migration step.  If a migration step has an
     * error and it runs on a device, the step will NOT re-run itself in the future if a fix is made
     * to the migration step.</p>
     * <p>For example, suppose a migration step renames a database column from {@code foo} to
     * {@code bar} when the name should have been {@code baz}.  If that migration step is released
     * in a build and runs on a user's device, the column will be renamed to {@code bar}.  If the
     * developer subsequently edits this same migration step to change the name to {@code baz} as
     * intended, the user devices which have already run this step will still have the name
     * {@code bar}.  Instead, a NEW migration step should be created to correct the error and rename
     * {@code bar} to {@code baz}, ensuring the error is corrected on devices which have already run
     * the migration step with the error.</p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
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
