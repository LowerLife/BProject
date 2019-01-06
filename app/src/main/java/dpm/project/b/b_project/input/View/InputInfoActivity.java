package dpm.project.b.b_project.input.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dpm.project.b.b_project.R;
import dpm.project.b.b_project.input.Presenter.InputInfoContract;
import dpm.project.b.b_project.input.Presenter.InputInfoPresenter;

public class InputInfoActivity extends AppCompatActivity
        implements InputInfoContract.View{

    @BindView(R.id.info_input_monthly_salary)
    EditText monthlySalary;
    @BindView(R.id.info_input_enter_date)
    EditText enterDate;
    @BindView(R.id.info_input_salary_date)
    EditText salaryDay;
    @BindView(R.id.info_input_quit_time)
    EditText quitTime;

    //옵저버 사용해서 모두 입력되었을 시 버튼 활성화하기?-?
    private Boolean isComplete = Boolean.FALSE;

    private InputInfoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_info);
        ButterKnife.bind(this);

        presenter = new InputInfoPresenter();
        presenter.attachView(this);
    }

    @OnClick(R.id.input_info_complete_btn)
    public void onCompleteClick(){
        String salary = monthlySalary.getText().toString();
        String date = enterDate.getText().toString();
        String day = salaryDay.getText().toString();
        String time = quitTime.getText().toString().split(" ")[0];

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
            toast("너의 퇴근은?");
            return;
        }

        presenter.inputData(salary, date, day, time);
    }

    //숫자 키패드
    /*@OnClick(R.id.info_input_monthly_salary)
    public void onMonthlySalaryClick(){

    }*/

    @OnClick(R.id.info_input_enter_date)
    public void onEnterDateClick(){
        startActivityForResult(
                new Intent(InputInfoActivity.this, DatePickerPopup.class),
                200);
    }

    //1~31까지 입력가능
    /*@OnClick(R.id.info_input_salary_date)
    public void onSalaryDateClick(){

    }*/

    @OnClick(R.id.info_input_quit_time)
    public void onQuitTimeClick(){
        startActivityForResult(
                new Intent(InputInfoActivity.this, TimePickerPopup.class),
                201);
    }

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
