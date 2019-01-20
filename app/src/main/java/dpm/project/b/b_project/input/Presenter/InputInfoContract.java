package dpm.project.b.b_project.input.Presenter;

import android.content.Context;

public interface InputInfoContract {
    interface View{
        void toast(String msg);
        void onSuccess();
    }

    interface Presenter{
        void inputData(Context context, String monthlySalary, String enterDate, String salaryDay, String quitTime);
        void attachView(View view);
        void detachView();
    }
}
