package dpm.project.b.b_project.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dpm.project.b.b_project.R;
import dpm.project.b.b_project.base.BaseActivity;
import dpm.project.b.b_project.input.View.DayPickerDialog;
import dpm.project.b.b_project.input.View.MonthDayPickerDialog;
import dpm.project.b.b_project.input.View.PickerListener;
import dpm.project.b.b_project.input.View.WorkTimePickerDialog;
import dpm.project.b.b_project.util.Utils;

import static dpm.project.b.b_project.util.Const.ENTER_DATE;
import static dpm.project.b.b_project.util.Const.MONTHLY_PAY;
import static dpm.project.b.b_project.util.Const.SALARY_DAY;
import static dpm.project.b.b_project.util.Const.WORK_START_AND_END_AT;

public class EditInfoActivity extends BaseActivity {

    @BindView(R.id.edit_info_emoji)
    ImageView editInfoEmoji;
    @BindView(R.id.edit_info_textView)
    TextView editInfoTextView;
    @BindView(R.id.edit_info_editText)
    EditText editInfoEditText;
    @BindView(R.id.edit_info_editText2)
    TextView editInfoEditText2;
    @BindView(R.id.edit_info_back_btn)
    LinearLayout backBtn;
    @BindView(R.id.edit_info_ok_btn)
    TextView okBtn;

    private int editKeyValue;
    private String time1, time2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        editKeyValue = intent.getIntExtra("edit_key", -1);

        switch (editKeyValue){
            case 0:
                editInfoEmoji.setImageResource(R.drawable.invalid_name);
                editInfoTextView.setText(R.string.edit_monthly_salary_view);
                break;
            case 1:
                editInfoEditText.setVisibility(View.GONE);
                editInfoEditText2.setVisibility(View.VISIBLE);
                editInfoEmoji.setImageResource(R.drawable.emoji_eye);
                editInfoTextView.setText(R.string.edit_enter_date_view);
                break;
            case 2:
                editInfoEditText.setVisibility(View.GONE);
                editInfoEditText2.setVisibility(View.VISIBLE);
                editInfoEmoji.setImageResource(R.drawable.emoji_sad);
                editInfoTextView.setText(R.string.edit_salary_day_view);
                break;
            case 3:
                editInfoEditText.setVisibility(View.GONE);
                editInfoEditText2.setVisibility(View.VISIBLE);
                editInfoEmoji.setImageResource(R.drawable.emoji_wink);
                editInfoTextView.setText(R.string.edit_work_time_view);
                editInfoEditText2.setHint(R.string.edit_info_edit_text_view);
                break;
            default:
        }
    }

    @OnClick(R.id.edit_info_editText2)
    public void onClickEditText(){

        switch (editKeyValue){
            case 1:
                PickerListener d = ((month, dayOfMonth, nullValue1, nullValue2)
                        -> editInfoEditText2.setText(month + "월 " + dayOfMonth +"일"));
                MonthDayPickerDialog pd = new MonthDayPickerDialog();
                pd.setListener(d);
                pd.show(getSupportFragmentManager(), "MonthDayPickerTest");
                break;
            case 2:
                PickerListener d2 = ((dayOfMonth, nullValue1, nullValue2, nullValue3)
                        -> editInfoEditText2.setText(dayOfMonth +"일"));
                DayPickerDialog pd2 = new DayPickerDialog();
                pd2.setListener(d2);
                pd2.show(getSupportFragmentManager(), "DayPickerTest");
                break;
            case 3:
                PickerListener d3 = ((hour1, minute1, hour2, minute2) -> {
                    time1 = hour1 + ":" + minute1;
                    time2 = hour2 + ":" + minute2;
                    editInfoEditText2.setText(time1 + " / " + time2);
                });
                WorkTimePickerDialog pd3 = new WorkTimePickerDialog();
                pd3.setListener(d3);
                pd3.show(getSupportFragmentManager(), "TimePickerTest");
                break;
            default:
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick(R.id.edit_info_back_btn)
    public void onClickBackBtn(){
        finish();
    }

    @OnClick(R.id.edit_info_ok_btn)
    public void onClickOkBtn(){
        String salary = "";
        String date = "";
        String day = "";
        String time = "";
        SharedPreferences.Editor editor = Utils.sharedPreferences.edit();

        switch (editKeyValue){
            case 0:
                salary = editInfoEditText.getText().toString();
                //최소금액 설정 -> 설정 금액 이하는 저장되지 않음
                if(Integer.parseInt(salary)<10000){
                    Toast.makeText(this, "최소금액은 10000원입니당", Toast.LENGTH_SHORT).show();
                    break;
                }else {
                    editor.putInt(MONTHLY_PAY, Integer.parseInt(salary));
                    break;
                }
            case 1:
                date = editInfoEditText2.getText().toString();
                date = date.replace("월 ","").replace("일","");
                editor.putString(ENTER_DATE, date);
                break;
            case 2:
                day = editInfoEditText2.getText().toString();
                day = day.replace("일","");
                editor.putString(SALARY_DAY, day);
                break;
            case 3:
                time = editInfoEditText2.getText().toString();
                editor.putString(WORK_START_AND_END_AT, time);
                break;
            default:
        }
        editor.commit();
        finish();
    }
}
