package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class HomeworkListWorkModel extends ViewModel {
    // Importante inicializar el LiveData con una lista vacía
    private MutableLiveData<List<Homework>> homeworkList = new MutableLiveData<>(new ArrayList<Homework>());

    public void llenarLista() {
        // Método para tener datos de ejemplo
        List<Homework> homeworkList = new ArrayList<>();
        homeworkList.add(new Homework("PMDM","Deberes 1","20/05/2023",false));
        homeworkList.add(new Homework("AD","Deberes 2","20/05/2023",false));
        homeworkList.add(new Homework("DI","Deberes 3","20/05/2023",false));
        this.homeworkList.setValue(homeworkList);
    }

    public MutableLiveData<List<Homework>> getHomeworkList() {
        return this.homeworkList;
    }

    public void setHomeworkList(List<Homework> homeworkList) {
        this.homeworkList.setValue(homeworkList);
    }

    public void addHomework(Homework homework) {
        List<Homework> currentList = homeworkList.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        currentList.add(homework);
        homeworkList.setValue(currentList);
    }

    public void removeHomework(Homework homework) {
        List<Homework> currentList = homeworkList.getValue();
        if (currentList != null) {
            currentList.remove(homework);
            homeworkList.setValue(currentList);
        }
    }

    public void set(int index, Homework homework) {
        List<Homework> currentList = homeworkList.getValue();
        if (currentList != null && index >= 0 && index < currentList.size()) {
            currentList.set(index, homework);
            homeworkList.setValue(currentList);
        }
    }

    public int indexOf(Homework homework) {
        List<Homework> currentList = homeworkList.getValue();
        if (currentList != null) {
            return currentList.indexOf(homework);
        }
        return -1;
    }

    public void setCompleted(Homework homework) {
        List<Homework> currentList = homeworkList.getValue();
        homework.setCompleted(true);
        if (currentList != null) {
            currentList.set(indexOf(homework), homework);
            homeworkList.setValue(currentList);
        }
    }
}
