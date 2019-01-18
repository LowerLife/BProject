package dpm.project.b.b_project.input.View;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v4.content.ContextCompat;
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
import butterknife.OnFocusChange;
import dpm.project.b.b_project.R;
import dpm.project.b.b_project.input.Presenter.InputInfoContract;
import dpm.project.b.b_project.input.Presenter.InputInfoPresenter;

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
    TextView quitTime;
    @BindView(R.id.input_info_bottom_bg)
    View inputInfoBottomView;
    @BindView(R.id.input_info_complete_btn)
    TextView completeBtn;

    //옵저버 사용해서 모두 입력되었을 시 버튼 활성화하기?-?
    private Boolean isComplete = Boolean.FALSE;

    private ViewObserver viewObserver;
    private InputInfoPresenter presenter;

    private String time1, time2;
    private boolean isTimeInputComplete = false;

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
        String time = quitTime.getText().toString();

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

        presenter.inputData(salary, date, day, time);
        onSuccess();
    }

    //숫자 키패드
    /*@OnClick(R.id.info_input_monthly_salary)
    public void onMonthlySalaryClick(){

    }*/

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

        DatePickerDialog.OnDateSetListener d = ((view, month, dayOfMonth, i2) -> {
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
        DatePickerDialog.OnDateSetListener d = ((view, dayOfMonth, i1, i2) -> {
            salaryDay.setText(dayOfMonth +"일");
            Log.d("DayPickerTest", "month = " + dayOfMonth + ", day = " + dayOfMonth);
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
        TimePickerDialog.OnTimeSetListener d = ((NULL, hour, minute) -> {
            if(isTimeInputComplete){
                time2 = hour + ":" + minute;
                quitTime.setText(time1 + " / " + time2);
                inputInfoBottomView.setVisibility(View.VISIBLE);
                completeBtn.setVisibility(View.VISIBLE);
            } else{
                time1 = hour + ":" + minute;
                isTimeInputComplete = true;
            }
            //Log.d("TimePickerTest", "hour = " + hour + ", minute = " + minute);
        });

        TimePickerDialogg pd = new TimePickerDialogg();
        pd.setListener(d);
        pd.show(getSupportFragmentManager(), "TimePickerTest");
    }

    /*@OnFocusChange(R.id.info_input_quit_time)
    public void quitTimeFocusChange() {
        if (!isQuitTime.get()) {
            viewObserver.modifyValue(isQuitTime, Boolean.FALSE);
        }
        if (!quitTime.equals("")) {
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
        finish();
    }
}
