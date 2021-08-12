package cn.idu.plugin.transform;


import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.TransformOutputProvider;

import org.codehaus.groovy.runtime.IOGroovyMethods;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.idu.plugin.internal.BaseTransform;

public class TimeTaskTransform extends BaseTransform {

    private void listFiles(File file, TransformOutputProvider outputProvider) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File fileItem : files) {
                listFiles(fileItem, outputProvider);
            }
        } else {
            String name = file.getName();
            if (filterClass(name)) {
                byte[] bytes = IOGroovyMethods.getBytes(new FileInputStream(file));
                ClassReader classReader = new ClassReader(bytes);
                ClassNode cn = new ClassNode();
                classReader.accept(cn, 0);
                List<MethodNode> methods = cn.methods;

                List<MethodTimeEntity> entities = new ArrayList<>();
                if (methods != null) {
                    for (MethodNode method : methods) {
                        List<AnnotationNode> annotations = method.visibleAnnotations;
                        if (annotations != null) {
                            for (AnnotationNode an : annotations) {
                                if (an.desc.contains("cn/idu/anno/TraceTime")) {
                                    entities.add(new MethodTimeEntity(method.access, method.name, method.desc, an.values.get(1).toString()));
                                }
                            }
                        }
                    }
                }
                if (entities.size() > 0) {
                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
                    ClassVisitor classVisitor = new MethodTimerClassVisitor(classWriter, entities);
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
                    byte[] code = classWriter.toByteArray();
                    FileOutputStream fos = new FileOutputStream(
                            file.getParentFile().getAbsolutePath() + File.separator + name);
                    fos.write(code);
                    fos.close();
                }
            }

        }
    }

    @Override
    protected void handleDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider
            outputProvider) throws IOException {
        listFiles(directoryInput.getFile(), outputProvider);
    }

    @Override
    protected void handleJarInput(JarInput jarInput, TransformOutputProvider outputProvider) {

    }
}
