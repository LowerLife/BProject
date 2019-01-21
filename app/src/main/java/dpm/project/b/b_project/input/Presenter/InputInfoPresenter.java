package dpm.project.b.b_project.input.Presenter;

import android.content.Context;
import android.content.SharedPreferences;

import dpm.project.b.b_project.util.Utils;

import static dpm.project.b.b_project.util.Const.ENTER_DATE;
import static dpm.project.b.b_project.util.Const.MONTHLY_PAY;
import static dpm.project.b.b_project.util.Const.SALARY_DAY;
import static dpm.project.b.b_project.util.Const.WORK_START_AND_END_AT;

public class InputInfoPresenter
        implements InputInfoContract.Presenter{

    private InputInfoContract.View view;

    @Override
    public void inputData(Context context, int monthlySalary, String enterDate, String salaryDay, String workStartAndQuitTime) {
        if (Utils.sharedPreferences == null) {
            Utils.sharedPreferences = context.getSharedPreferences("bproject", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = Utils.sharedPreferences.edit();
        editor.putInt(MONTHLY_PAY, monthlySalary);
        editor.putString(ENTER_DATE, enterDate);
        editor.putString(SALARY_DAY, salaryDay);
        editor.putString(WORK_START_AND_END_AT, workStartAndQuitTime);
        editor.commit();
    }

    @Override
    public void attachView(InputInfoContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}
