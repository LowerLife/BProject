package dpm.project.b.b_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import dpm.project.b.b_project.base.BaseActivity;
import dpm.project.b.b_project.story.StoryActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        utils.getWorkTime();
        startActivity(new Intent(this,StoryActivity.class));
    }
}
