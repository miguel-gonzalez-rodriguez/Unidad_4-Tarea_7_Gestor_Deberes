package gal.cifpacarballeira.unidad4_tarea7gestordeberes;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HomeworkAdapter adapter;
    private List<Homework> homeworkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fab = findViewById(R.id.fab);

        homeworkList = new ArrayList<>();
        adapter = new HomeworkAdapter(homeworkList, this::showBottomSheet);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> showAddHomeworkDialog(null));
    }

    private void showAddHomeworkDialog(Homework homeworkToEdit) {

        NewHomeworkDialogFragment dialog = new NewHomeworkDialogFragment();
        // Pasarle el objeto Homework al diálogo si se está editando
        if (homeworkToEdit != null) {
            Bundle args = new Bundle();
            args.putParcelable("homework", homeworkToEdit);
            dialog.setArguments(args);
        }
        dialog.setOnHomeworkSavedListener(homework -> {
                    if (homeworkToEdit == null) {
                        homeworkList.add(homework);
                    } else {
                        homeworkList.set(homeworkList.indexOf(homeworkToEdit), homework);
                    }
            adapter.notifyDataSetChanged();
                });
        dialog.show(getSupportFragmentManager(), "AddHomeworkDialog");
//
//        AddHomeworkDialogFragment dialog = AddHomeworkDialogFragment.newInstance(homeworkToEdit);
//        dialog.setOnHomeworkSavedListener(homework -> {
//            if (homeworkToEdit == null) {
//                homeworkList.add(homework);
//            } else {
//                homeworkList.set(homeworkList.indexOf(homeworkToEdit), homework);
//            }
//            adapter.notifyDataSetChanged();
//        });
//        dialog.show(getSupportFragmentManager(), "AddHomeworkDialog");
    }

    private void showBottomSheet(Homework homework) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_homework_options, null);

        view.findViewById(R.id.editOption).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            showAddHomeworkDialog(homework);
        });

        view.findViewById(R.id.deleteOption).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            showDeleteConfirmation(homework);
        });

        view.findViewById(R.id.completeOption).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            homework.setCompleted(true);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Tarea marcada como completada", Toast.LENGTH_SHORT).show();
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void showDeleteConfirmation(Homework homework) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar este deber?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    homeworkList.remove(homework);
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
