package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class NewHomeworkDialogFragment extends DialogFragment {

    private EditText descriptionEditText;
    private EditText dueDateEditText;
    private Spinner subjectSpinner;
    private NewHomeworkDialogFragment.OnHomeworkSavedListener listener;
    private Homework homeworkToEdit;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Construir el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflar el layout del diálogo con el xml que hemos creado antes
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_add_homework, null);

        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        dueDateEditText = view.findViewById(R.id.dueDateEditText);
        subjectSpinner = view.findViewById(R.id.subjectSpinner);

        dueDateEditText.setOnClickListener(v -> showDatePickerDialog());

        if (getArguments() != null) {
            homeworkToEdit = getArguments().getParcelable("homework");
            if (homeworkToEdit != null) {
                descriptionEditText.setText(homeworkToEdit.getDescription());
                dueDateEditText.setText(homeworkToEdit.getDueDate());
                // Configurar el spinner según la asignatura actual (se asume un método auxiliar para esto).
                subjectSpinner.setSelection(getIndex(subjectSpinner, homeworkToEdit.getSubject()));
            }
        }

        Button saveButton = view.findViewById(R.id.saveButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);
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
        builder.setView(view);
        return builder.create();

    }

    private int getIndex(Spinner subjectSpinner, String subject) {
        for (int i = 0; i < subjectSpinner.getCount(); i++) {
            if (subjectSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(subject)) {
                return i;
            }
        }
        return 0;
    }


    public interface OnHomeworkSavedListener {
        void onHomeworkSaved(Homework homework);
    }

    public void setOnHomeworkSavedListener(NewHomeworkDialogFragment.OnHomeworkSavedListener listener) {
        this.listener = listener;
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

}
