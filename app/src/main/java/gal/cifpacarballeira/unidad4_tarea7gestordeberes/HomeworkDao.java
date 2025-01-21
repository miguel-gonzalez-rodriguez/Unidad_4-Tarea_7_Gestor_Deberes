package gal.cifpacarballeira.unidad4_tarea7gestordeberes;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HomeworkDao {
    @Query("SELECT * FROM tabla_deberes")
    List<Homework> getAll();

    @Insert
    void insert(Homework homework);

    @Update
    void update(Homework homework);

    @Delete
    void delete(Homework homework);

    @Query("SELECT * FROM tabla_deberes WHERE id = :id")
    Homework getById(int id);


}
