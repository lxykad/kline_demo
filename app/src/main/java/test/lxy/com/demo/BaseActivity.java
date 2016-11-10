package test.lxy.com.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public abstract class BaseActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName().toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView();
        initView();

    }

    abstract void setContentView();
    abstract void initView();



}
