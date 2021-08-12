package cn.idu.plugin.internal;

import org.gradle.BuildListener;
import org.gradle.BuildResult;
import org.gradle.api.Task;
import org.gradle.api.execution.TaskExecutionListener;
import org.gradle.api.initialization.Settings;
import org.gradle.api.invocation.Gradle;
import org.gradle.api.tasks.TaskState;

public class TimeTaskListener implements TaskExecutionListener, BuildListener {
    private void llog(String s) {
//        System.out.println("TimeTaskListener: " + s);
    }

    @Override
    public void settingsEvaluated(Settings settings) {
        llog("settingsEvaluated");
    }

    @Override
    public void projectsLoaded(Gradle gradle) {
        llog("projectsLoaded");
    }

    @Override
    public void projectsEvaluated(Gradle gradle) {
        llog("projectsEvaluated");
    }

    @Override
    public void buildFinished(BuildResult buildResult) {
        llog("buildFinished ");
    }

    @Override
    public void beforeExecute(Task task) {
        llog("beforeExecute");
    }

    @Override
    public void afterExecute(Task task, TaskState taskState) {
        llog("afterExecute");
    }
}
