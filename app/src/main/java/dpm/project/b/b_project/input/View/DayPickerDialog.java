package dpm.project.b.b_project.input.View;

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

    private PickerListener listener;
    //public Calendar cal = Calendar.getInstance();

    public void setListener(PickerListener listener) {
        this.listener = listener;
    }

    TextView btnConfirm;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.day_picker, null);

        btnConfirm = dialog.findViewById(R.id.salary_day_picker_ok_btn);

        final NumberPicker dayPicker = dialog.findViewById(R.id.picker_day);

        btnConfirm.setOnClickListener(view -> {
            listener.onDataSet(
                    String.valueOf(dayPicker.getValue()),
                    "",
                    "",
                    "");
            DayPickerDialog.this.getDialog().cancel();
        });

        //최대, 최소값 설정
        dayPicker.setMinValue(1);
        //30일, 31일이 없는 달
        dayPicker.setMaxValue(28);
        //숫자 클릭시 editText로 변경 제거
        dayPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        dayPicker.setValue(5);

        builder.setView(dialog);

        return builder.create();
    }
}