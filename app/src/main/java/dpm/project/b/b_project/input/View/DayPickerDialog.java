package dpm.project.b.b_project.input.View;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Calendar;

import dpm.project.b.b_project.R;

public class DayPickerDialog extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;
    public Calendar cal = Calendar.getInstance();

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    TextView btnConfirm;
    //Button btnCancel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.day_picker, null);

        btnConfirm = dialog.findViewById(R.id.salary_day_picker_ok_btn);
        //btnCancel = dialog.findViewById(R.id.btn_cancel);

        final NumberPicker dayPicker = dialog.findViewById(R.id.picker_day);

        //btnCancel.setOnClickListener(view -> MonthDayPickerDialog.this.getDialog().cancel());

        btnConfirm.setOnClickListener(view -> {
            listener.onDateSet(null, dayPicker.getValue(), 0, 0);
            DayPickerDialog.this.getDialog().cancel();
        });

        //최대, 최소값 설정
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);
        //숫자 클릭시 editText로 변경 제거
        dayPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        dayPicker.setValue(cal.get(Calendar.DAY_OF_MONTH) + 1);

        builder.setView(dialog);

        return builder.create();
    }
}