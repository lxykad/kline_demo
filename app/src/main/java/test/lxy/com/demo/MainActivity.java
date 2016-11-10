package test.lxy.com.demo;


import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseActivity {

    private String TAG = getClass().getSimpleName().toString();
    private View mDecorView;


    @Override
    void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    void initView() {
        System.out.println("555555=======" + TAG);

        mDecorView = getWindow().getDecorView();
        //设置全屏
        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        //getActionBar().hide();
        //清除掉所有flag
        //mDecorView.setSystemUiVisibility(0);

        Button fs = (Button) findViewById(R.id.bt_fs);
        Button kx = (Button) findViewById(R.id.bt_kx);

        fs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,KXActivity.class));
            }
        });

        kx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,FSActivity.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //按home键等 再尽量falg会被清除掉
       // mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

}
