package cn.idu.plugin.transform;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.List;

public class MethodTimerClassVisitor extends ClassVisitor {
    String methodOwner;
    List<MethodTimeEntity> entities;

    MethodTimeEntity getEntity(int access, String name, String descriptor) {
        for (MethodTimeEntity entity : entities) {
            if (entity.isSame(access, name, descriptor)) return entity;
        }
        return null;
    }

    MethodTimerClassVisitor(ClassVisitor classVisitor, List<MethodTimeEntity> entities) {
        super(Opcodes.ASM7, classVisitor);
        this.entities = entities;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        methodOwner = name;
    }

    /**
     * 扫描类的方法进行调用
     *
     * @param access     修饰符
     * @param name       方法名字
     * @param descriptor 方法签名
     * @param signature  泛型信息
     * @param exceptions 抛出的异常
     * @return
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);

        MethodTimeEntity entity = getEntity(access, name, descriptor);
        if (entity != null) {
            methodVisitor = new AdviceAdapter(
                    api, methodVisitor, access, name, descriptor
            ) {
                int slotIndex;

                @Override
                protected void onMethodEnter() {
                    super.onMethodEnter();
                    slotIndex = newLocal(Type.LONG_TYPE);
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
                    mv.visitVarInsn(LSTORE, slotIndex);
                }

                @Override
                protected void onMethodExit(int opcode) {
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
                    mv.visitVarInsn(LSTORE, slotIndex);
                    mv.visitLdcInsn("TraceTime:"+entity.tag);
                    mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
                    mv.visitInsn(DUP);
                    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
                    mv.visitLdcInsn(methodOwner + "/" + name + " exec cost: (");
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                    mv.visitVarInsn(LLOAD, slotIndex);
                    mv.visitVarInsn(LLOAD, slotIndex);
                    mv.visitInsn(LSUB);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
                    mv.visitLdcInsn("ms)");
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
                    mv.visitMethodInsn(INVOKESTATIC, "android/util/Log", "d", "(Ljava/lang/String;Ljava/lang/String;)I", false);
                    mv.visitInsn(POP);
                    super.onMethodExit(opcode);
                }
            };
        }
        return methodVisitor;
    }

}
