package cn.idu.iwan.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;

import cn.idu.iwan.R;
import cn.idu.iwan.app.Constants;

@Route(path = Constants.ACTIVITY_HOME)
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    void log() {
        long time_start = System.currentTimeMillis();

        String idastr = "code line";

        long time_end = System.currentTimeMillis();
        long cost = time_start - time_end;


        Log.d("tagggggg", "IDA_COST: " + cost);
    }
}