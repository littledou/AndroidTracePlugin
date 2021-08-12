package cn.idu.plugin

import cn.idu.plugin.internal.BaseTransform
import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.TransformOutputProvider

class MethodTimeTransform extends BaseTransform {

    @Override
    protected void handleDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        if (directoryInput.file.isDirectory()) {
            directoryInput.file.eachFileRecurse { file ->
                def name = file.name
                file.bytes
                println("list source: $name")
                if (filterClass(name)) {
                }
            }
        }
    }

    @Override
    protected void handleJarInput(JarInput jarInput, TransformOutputProvider outputProvider) {

    }
}