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

import java.text.SimpleDateFormat;
import java.util.Date;
import dpm.project.b.b_project.R;

public class TimePickerDialogg extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener listener;
    private long now = System.currentTimeMillis();
    private Date date = new Date(now);
    private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
    private String getTime = sdf.format(date);
    private boolean isComplete = false;

    public void setListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }

    TextView btnConfirm;
    TextView timeOptionView;

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
            if(isComplete != true){
                listener.onTimeSet(null, hourPicker.getValue(), minutePicker.getValue());
                isComplete = true;
                btnConfirm.setText("OK");
                timeOptionView.setText("퇴근시간");
            } else{
                listener.onTimeSet(null, hourPicker.getValue(), minutePicker.getValue());
                TimePickerDialogg.this.getDialog().cancel();
            }
        });

        //최대, 최소값 설정
        hourPicker.setMinValue(1);
        hourPicker.setMaxValue(12);
        //숫자 클릭시 editText 로 변경 제거
        hourPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        int hour = Integer.valueOf(getTime.split(":")[0]);
        hourPicker.setValue(hour);

        minutePicker.setMinValue(00);
        minutePicker.setMaxValue(59);
        minutePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        int minute = Integer.valueOf(getTime.split(":")[1]);
        minutePicker.setValue(minute);

        builder.setView(dialog);

        return builder.create();
    }
}
