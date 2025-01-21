package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Homework.class}, version = 1)
public abstract class BaseDeDatos extends RoomDatabase {
    public abstract HomeworkDao homeworkDao();
}
