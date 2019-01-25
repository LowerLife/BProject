package dpm.project.b.b_project.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dpm.project.b.b_project.R;
import dpm.project.b.b_project.main.MainActivity;

import static dpm.project.b.b_project.util.Const.ENTER_DATE;
import static dpm.project.b.b_project.util.Const.MONTHLY_PAY;
import static dpm.project.b.b_project.util.Const.SALARY_DAY;
import static dpm.project.b.b_project.util.Const.WORK_START_AND_END_AT;

public class MyPageActivity extends AppCompatActivity {

    @BindView(R.id.my_page_yearly_salary_view)
    TextView myYearlySalary;
    @BindView(R.id.edit_monthly_salary)
    ConstraintLayout editSalaryBtn;
    @BindView(R.id.edit_enter_date)
    ConstraintLayout editEnterDateBtn;
    @BindView(R.id.edit_salary_day)
    ConstraintLayout editSalaryDayBtn;
    @BindView(R.id.edit_start_and_quit_time)
    ConstraintLayout editWorkTimeBtn;
    @BindView(R.id.my_page_back_btn)
    LinearLayout myPageBackBtn;

    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("bproject", Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myYearlySalary.setText(String.valueOf(sharedPreferences.getInt(MONTHLY_PAY, 0)*12));
    }

    @OnClick(R.id.edit_monthly_salary)
    public void onClickEditSalary(){
        Intent intent = new Intent(this, EditInfoActivity.class);
        intent.putExtra("edit_key", 0);
        startActivity(intent);
    }

    @OnClick(R.id.edit_enter_date)
    public void onClickEditEnterDate(){
        Intent intent = new Intent(this, EditInfoActivity.class);
        intent.putExtra("edit_key", 1);
        startActivity(intent);
    }

    @OnClick(R.id.edit_salary_day)
    public void onClickEditSalaryDay(){
        Intent intent = new Intent(this, EditInfoActivity.class);
        intent.putExtra("edit_key", 2);
        startActivity(intent);
    }

    @OnClick(R.id.edit_start_and_quit_time)
    public void onClickEditWorkTime(){
        Intent intent = new Intent(this, EditInfoActivity.class);
        intent.putExtra("edit_key", 3);
        startActivity(intent);
    }

    @OnClick(R.id.my_page_back_btn)
    public void onClickBackBtn(){
        finish();
    }
}
