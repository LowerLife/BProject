package dpm.project.b.b_project.base;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import dpm.project.b.b_project.util.Utils;

public class BaseActivity extends AppCompatActivity {

    protected SharedPreferences sharedPreferences;
    protected Utils utils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("bproject",MODE_PRIVATE);
        utils = new Utils(this);
    }
}
