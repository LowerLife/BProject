package dpm.project.b.b_project.input.Presenter;

public class InputInfoPresenter
        implements InputInfoContract.Presenter{

    private InputInfoContract.View view;

    @Override
    public void inputData(String monthlySalary, String enterDate, String salaryDay, String quitTime) {

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
