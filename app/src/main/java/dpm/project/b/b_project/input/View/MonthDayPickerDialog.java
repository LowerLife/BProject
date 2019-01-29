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

public class MonthDayPickerDialog extends DialogFragment {

    private PickerListener listener;
    public Calendar cal = Calendar.getInstance();

    public void setListener(PickerListener listener) {
        this.listener = listener;
    }

    private TextView btnConfirm;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.month_day_picker, null);

        btnConfirm = dialog.findViewById(R.id.enter_date_picker_ok_btn);

        final NumberPicker monthPicker = dialog.findViewById(R.id.picker_month);
        final NumberPicker dayPicker = dialog.findViewById(R.id.picker_day);

        btnConfirm.setOnClickListener(view -> {
            int month = monthPicker.getValue();
            listener.onDataSet(
                    (month < 10 ? "0" + String.valueOf(month) : String.valueOf(month)),
                    String.valueOf(dayPicker.getValue()),
                    "",
                    "");
            MonthDayPickerDialog.this.getDialog().cancel();
        });

        //최대, 최소값 설정
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        //숫자 클릭시 editText로 변경 제거
        monthPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        monthPicker.setValue(cal.get(Calendar.MONTH) + 1);

        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);
        dayPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        dayPicker.setValue(cal.get(Calendar.DAY_OF_MONTH));

        builder.setView(dialog);

        return builder.create();
    }
}
