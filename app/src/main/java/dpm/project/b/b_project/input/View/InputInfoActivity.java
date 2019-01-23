package dpm.project.b.b_project.input.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dpm.project.b.b_project.R;
import dpm.project.b.b_project.input.Presenter.InputInfoContract;
import dpm.project.b.b_project.input.Presenter.InputInfoPresenter;
import dpm.project.b.b_project.main.MainActivity;

public class InputInfoActivity extends AppCompatActivity
        implements InputInfoContract.View{//, ObserverCallback{

    @BindView(R.id.info_input_text2)
    TextView middleLineTextView;
    @BindView(R.id.info_input_monthly_salary)
    EditText monthlySalary;
    @BindView(R.id.info_input_enter_date)
    TextView enterDate;
    @BindView(R.id.info_input_salary_date)
    TextView salaryDay;
    @BindView(R.id.info_input_quit_time)
    TextView workStartAndEndTime;
    @BindView(R.id.input_info_bottom_bg)
    View inputInfoBottomView;
    @BindView(R.id.input_info_complete_btn)
    TextView completeBtn;

    //옵저버 사용해서 모두 입력되었을 시 버튼 활성화하기?-?
    private Boolean isComplete = Boolean.FALSE;

    private ViewObserver viewObserver;
    private InputInfoPresenter presenter;

    private String time1, time2;

    private AtomicBoolean isMonthlySalary;
    private AtomicBoolean isEnterDate;
    private AtomicBoolean isSalaryDay;
    private AtomicBoolean isQuitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_info);
        ButterKnife.bind(this);

        //취소선ㅠㅠ
        //middleLineTextView.setPaintFlags(middleLineTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        /*isMonthlySalary = new AtomicBoolean(Boolean.FALSE);
        isEnterDate = new AtomicBoolean(Boolean.FALSE);
        isSalaryDay = new AtomicBoolean(Boolean.FALSE);
        isQuitTime = new AtomicBoolean(Boolean.FALSE);*/

        /*viewObserver = new ViewObserver(this);
        viewObserver.add(isMonthlySalary);
        viewObserver.add(isEnterDate);
        viewObserver.add(isSalaryDay);
        viewObserver.add(isQuitTime);*/

        presenter = new InputInfoPresenter();
        presenter.attachView(this);
    }

    /*@Override
    public void update(boolean b) {
        if (b) {
            inputInfoBottomView.setVisibility(View.VISIBLE);
            completeBtn.setVisibility(View.VISIBLE);
            isComplete = Boolean.TRUE;
        } else {
            inputInfoBottomView.setVisibility(View.INVISIBLE);
            completeBtn.setVisibility(View.INVISIBLE);
            isComplete = Boolean.FALSE;
        }
    }*/

    @OnClick(R.id.input_info_complete_btn)
    public void onCompleteClick(){
        String salary = monthlySalary.getText().toString();
        String date = enterDate.getText().toString();
        String day = salaryDay.getText().toString();
        String time = workStartAndEndTime.getText().toString();

        if(salary.equals("")){
            toast("너의 월급은?");
            return;
        }

        if(date.equals("")){
            toast("너의 입사일은?");
            return;
        }

        if(day.equals("") || day.compareTo("31") == 1){
            toast("너의 월급날은?");
            return;
        }

        if(time.equals("")){
            toast("너의 출퇴근시간은?");
            return;
        }

        date = date.replace("월 ","").replace("일","");
        day = day.replace("일","");
        Log.d("test",salary + " " + date + " " + day + " " + time);
        presenter.inputData(getApplicationContext(), Integer.parseInt(salary), date, day, time);
        onSuccess();
    }

    /*@OnFocusChange(R.id.info_input_monthly_salary)
    public void monthlySalaryFocusChange() {
        if (!isSalaryDay.get()) {
            viewObserver.modifyValue(isMonthlySalary, Boolean.FALSE);
        }
        if (!monthlySalary.equals("")) {
            //presenter.validateExistEmail(email);
            viewObserver.modifyValue(isMonthlySalary, Boolean.TRUE);
        }
    }*/

    @OnClick(R.id.info_input_enter_date)
    public void onEnterDateClick(){

        PickerListener d = ((month, dayOfMonth, nullValue1, nullValue2) -> {
            enterDate.setText(month + "월 " + dayOfMonth +"일");
            Log.d("MonthDayPickerTest", "month = " + month + ", day = " + dayOfMonth);
        });

        MonthDayPickerDialog pd = new MonthDayPickerDialog();
        pd.setListener(d);
        pd.show(getSupportFragmentManager(), "MonthDayPickerTest");
    }

    /*@OnFocusChange(R.id.info_input_enter_date)
    public void enterDateFocusChange() {
        if (!isEnterDate.get()) {
            viewObserver.modifyValue(isEnterDate, Boolean.FALSE);
        }
        if (!enterDate.equals("")) {
            //presenter.validateExistEmail(email);
            viewObserver.modifyValue(isEnterDate, Boolean.TRUE);
        }
    }*/

    @OnClick(R.id.info_input_salary_date)
    public void onSalaryDateClick(){
        PickerListener d = ((dayOfMonth, nullValue1, nullValue2, nullValue3) -> {
            salaryDay.setText(dayOfMonth +"일");
            Log.d("DayPickerTest", "day = " + dayOfMonth);
        });

        DayPickerDialog pd = new DayPickerDialog();
        pd.setListener(d);
        pd.show(getSupportFragmentManager(), "DayPickerTest");
    }

    /*@OnFocusChange(R.id.info_input_salary_date)
    public void salaryDayFocusChange() {
        if (!isSalaryDay.get()) {
            viewObserver.modifyValue(isSalaryDay, Boolean.FALSE);
        }
        if (!salaryDay.equals("")) {
            //presenter.validateExistEmail(email);
            viewObserver.modifyValue(isSalaryDay, Boolean.TRUE);
        }
    }*/

    @OnClick(R.id.info_input_quit_time)
    public void onQuitTimeClick(){
        PickerListener d = ((hour1, minute1, hour2, minute2) -> {
                time1 = hour1 + ":" + minute1;
                time2 = hour2 + ":" + minute2;
                workStartAndEndTime.setText(time1 + " / " + time2);
                inputInfoBottomView.setVisibility(View.VISIBLE);
                completeBtn.setVisibility(View.VISIBLE);

            Log.d("TimePickerTest", "Work Start Hour = " + hour1 +
                    ", Work Start minute = " + minute1 +
                    ", Work End Hour = " + hour2 +
                    ", Work End minute = " + minute2);
        });

        WorkTimePickerDialog pd = new WorkTimePickerDialog();
        pd.setListener(d);
        pd.show(getSupportFragmentManager(), "TimePickerTest");
    }

    /*@OnFocusChange(R.id.info_input_quit_time)
    public void quitTimeFocusChange() {
        if (!isQuitTime.get()) {
            viewObserver.modifyValue(isQuitTime, Boolean.FALSE);
        }
        if (!workStartAndEndTime.equals("")) {
            //presenter.validateExistEmail(email);
            viewObserver.modifyValue(isQuitTime, Boolean.TRUE);
        }
    }*/

    @Override
    public void toast(String msg) {
        Runnable r = () -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        runOnUiThread(r);
    }

    @Override
    public void onSuccess() {
        toast("님의 정보 저장완료!");
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
