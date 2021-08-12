package cn.idu.plugin;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.Collections;

import cn.idu.plugin.internal.TimeTaskListener;
import cn.idu.plugin.transform.TimeTaskTransform;


public class PluginTransform implements Plugin<Project> {

    @Override
    public void apply(Project project) {

        System.out.println("Transform插件启动！");
        project.getGradle().addBuildListener(new TimeTaskListener());
//        AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);
        AppExtension appExtension = (AppExtension) project.getProperties().get("android");
        appExtension.registerTransform(new TimeTaskTransform(), Collections.EMPTY_LIST);

    }

}