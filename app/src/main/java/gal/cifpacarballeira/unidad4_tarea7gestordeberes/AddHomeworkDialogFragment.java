package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

public class AddHomeworkDialogFragment extends DialogFragment {

    private EditText descriptionEditText;
    private EditText dueDateEditText;
    private Spinner subjectSpinner;
    private OnHomeworkSavedListener listener;
    private Homework homeworkToEdit;

    public static AddHomeworkDialogFragment newInstance(Homework homework) {
        AddHomeworkDialogFragment fragment = new AddHomeworkDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("homework", homework);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_homework, container, false);

        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        dueDateEditText = view.findViewById(R.id.dueDateEditText);
        subjectSpinner = view.findViewById(R.id.subjectSpinner);
        Button saveButton = view.findViewById(R.id.saveButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        if (getArguments() != null) {
            homeworkToEdit = getArguments().getParcelable("homework");
            if (homeworkToEdit != null) {
                descriptionEditText.setText(homeworkToEdit.getDescription());
                dueDateEditText.setText(homeworkToEdit.getDueDate());
                // Configura el spinner según la asignatura actual (se asume un método auxiliar para esto).
            }
        }

        dueDateEditText.setOnClickListener(v -> showDatePickerDialog());

        saveButton.setOnClickListener(v -> {
            if (validateInputs()) {
                Homework homework = new Homework(
                        subjectSpinner.getSelectedItem().toString(),
                        descriptionEditText.getText().toString(),
                        dueDateEditText.getText().toString(),
                        false
                );
                if (listener != null) {
                    listener.onHomeworkSaved(homework);
                }
                dismiss();
            }
        });

        cancelButton.setOnClickListener(v -> dismiss());

        return view;
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(
                getContext(),
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                    dueDateEditText.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(descriptionEditText.getText())) {
            descriptionEditText.setError("La descripción es obligatoria");
            return false;
        }
        if (TextUtils.isEmpty(dueDateEditText.getText())) {
            dueDateEditText.setError("La fecha de entrega es obligatoria");
            return false;
        }
        return true;
    }

    public void setOnHomeworkSavedListener(OnHomeworkSavedListener listener) {
        this.listener = listener;
    }

    public interface OnHomeworkSavedListener {
        void onHomeworkSaved(Homework homework);
    }
}