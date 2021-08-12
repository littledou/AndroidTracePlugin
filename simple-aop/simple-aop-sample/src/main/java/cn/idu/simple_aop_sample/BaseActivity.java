package cn.idu.simple_aop_sample;

import android.app.Activity;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.stream.Stream;

import cn.idu.simple_aop_sample.anno.ActivityCheck;
import cn.idu.simple_aop_sample.anno.Check;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void startActivity(Intent intent) {
        boolean loginState = true;
        try {
            Class<? extends Activity> targetActivityClass = (Class<? extends Activity>) Class.forName(intent.getComponent().getClassName());
            Check anno = targetActivityClass.getAnnotation(Check.class);
            if (anno != null) {
                Class<? extends ActivityCheck>[] annoClasses = anno.value();

                loginState = Stream.of(annoClasses).anyMatch(c -> {
                    try {
                        ActivityCheck activityChecker = c.newInstance();
                        return activityChecker.intercept(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (loginState)
            super.startActivity(intent);
    }
}

