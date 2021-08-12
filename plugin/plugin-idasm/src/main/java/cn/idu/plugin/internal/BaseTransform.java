package cn.idu.plugin.internal;

import static com.android.build.gradle.internal.pipeline.TransformManager.CONTENT_CLASS;
import static com.android.build.gradle.internal.pipeline.TransformManager.SCOPE_FULL_PROJECT;

import com.android.build.api.transform.Context;
import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public abstract class BaseTransform extends Transform {

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public boolean isCacheable() {
        return true;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        //OutputProvider管理输出路径，如果消费型输入为空，你会发现OutputProvider == null
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        boolean incremental = transformInvocation.isIncremental();
        if (!incremental) outputProvider.deleteAll();///如果不是增量更新，删除所有
        //消费型输入，可以从中获取jar包和class文件夹路径。必须输出给下一个任务
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        //引用型输入，无需输出。
        Collection<TransformInput> referencedInputs = transformInvocation.getReferencedInputs();

        inputs.forEach(input -> {

            input.getDirectoryInputs().forEach(item -> {

                String path = item.getFile().getAbsolutePath();
                try {
                    handleDirectoryInput(item, outputProvider);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                File dest = outputProvider.getContentLocation(
                        item.getName(),
                        item.getContentTypes(),
                        item.getScopes(),
                        Format.DIRECTORY);
                //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
                try {
                    FileUtils.copyDirectory(item.getFile(), dest);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });

            input.getJarInputs().forEach(item -> {
                handleJarInput(item, outputProvider);
                File dest = outputProvider.getContentLocation(
                        item.getFile().getAbsolutePath(),
                        item.getContentTypes(),
                        item.getScopes(),
                        Format.JAR);
                try {
                    FileUtils.copyFile(item.getFile(), dest);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        });

    }

    /**
     * 检查class文件是否需要处理
     *
     * @param name
     * @return
     */
    protected boolean filterClass(String name) {
        return (name.endsWith(".class")
                && !name.startsWith("R\\$")
                && !"R.class".equals(name)
                && !"BuildConfig.class".equals(name));
    }

    protected abstract void handleDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider outputProvider) throws IOException;

    protected abstract void handleJarInput(JarInput jarInput, TransformOutputProvider outputProvider);
}
