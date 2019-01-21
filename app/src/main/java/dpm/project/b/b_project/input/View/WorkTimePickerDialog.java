package dpm.project.b.b_project.input.View;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dpm.project.b.b_project.R;

public class WorkTimePickerDialog extends DialogFragment {

    private PickerListener listener;
    private long now = System.currentTimeMillis();
    private Date date = new Date(now);
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
    private String getTime = sdf.format(date);
    private boolean isStartTimeWritten = false;

    public void setListener(PickerListener listener) {
        this.listener = listener;
    }

    TextView btnConfirm;
    TextView timeOptionView;

    private int workStartHour = 0;
    private int workStartMinute = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.time_picker, null);

        btnConfirm = dialog.findViewById(R.id.time_picker_ok_btn);
        timeOptionView = dialog.findViewById(R.id.picker_time_option_view);

        final NumberPicker hourPicker = dialog.findViewById(R.id.picker_hour);
        final NumberPicker minutePicker = dialog.findViewById(R.id.picker_minute);

        btnConfirm.setOnClickListener(view -> {
            if(!isStartTimeWritten){
                workStartHour  = hourPicker.getValue();
                workStartMinute = minutePicker.getValue();
                isStartTimeWritten = true;
                btnConfirm.setText("OK");
                timeOptionView.setText("퇴근시간");
            } else{
                listener.onDataSet(
                        (workStartHour < 10 ? "0" + String.valueOf(workStartHour) : String.valueOf(workStartHour)),
                        (workStartMinute < 10 ? "0" + String.valueOf(workStartMinute) : String.valueOf(workStartMinute)),
                        String.valueOf(hourPicker.getValue()<10? "0" + hourPicker.getValue() : hourPicker.getValue()),
                        String.valueOf(minutePicker.getValue()<10? "0" + minutePicker.getValue() : minutePicker.getValue())
                );
                WorkTimePickerDialog.this.getDialog().cancel();
            }
        });

        //Hour Picker
        hourPicker.setMinValue(1);
        hourPicker.setMaxValue(12);
        hourPicker.setFormatter(i -> {
            String num = String.valueOf(i);
            if(i<10){
                return "0"+num;
            }
            return num;
        });
        //숫자 클릭시 editText 로 변경 제거
        hourPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        int hour = Integer.valueOf(getTime.split(":")[0]);
        hourPicker.setValue(hour);

        //Minute Picker
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setFormatter(i -> {
            String num = String.valueOf(i);
            if(i<10){
                return "0"+num;
            }
            return num;
        });
        minutePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        int minute = Integer.valueOf(getTime.split(":")[1]);
        minutePicker.setValue(minute);

        builder.setView(dialog);

        return builder.create();
    }
}