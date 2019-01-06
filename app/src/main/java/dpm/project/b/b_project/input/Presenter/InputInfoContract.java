package dpm.project.b.b_project.input.Presenter;

public interface InputInfoContract {
    interface View{
        void toast(String msg);
        void onSuccess();
    }

    interface Presenter{
        void inputData(String monthlySalary, String enterDate, String salaryDay, String quitTime);
        void attachView(View view);
        void detachView();
    }
}
