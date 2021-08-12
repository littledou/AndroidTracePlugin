package cn.idu.apt;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.annotation.processing.FilerException;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import cn.idu.apt.Utils.Utils;
import cn.idu.iroute.anno.Router;


@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)//java版本支持
@SupportedAnnotationTypes({//标注注解处理器支持的注解类型
        "cn.idu.iroute.anno.Router"
})
public class RouterProcessor extends MainProcessor {
    String PACKAGE_NAME = Utils.PackageName;
    String CLASS_NAME = "TRouter";


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        logger.info(">>> RouteProcessor init. <<<");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations != null && annotations.size() > 0) {
            Set<TypeElement> routeElements = ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(Router.class));
            logger.info(">>> Found routes, start... <<<");
            parseAnno(routeElements);

            return true;
        }
        return false;
    }

    private void parseAnno(Set<TypeElement> routeElements) {
        logger.info(">>> Found routes, size is " + routeElements.size() + " <<<");
        TypeSpec.Builder tb = classBuilder(CLASS_NAME).addModifiers(PUBLIC).addJavadoc("@全局路由器 此类由apt自动生成");

        ClassName classNameApplication = ClassName.get("android.app", "Application");
        ClassName classNameActivity = ClassName.get("android.app", "Activity");

        tb.addField(FieldSpec.builder(ParameterizedTypeName.get(Stack.class), "store")
                .addModifiers(STATIC).build());

        MethodSpec.Builder method_init = MethodSpec.methodBuilder("init")
                .addJavadoc("Trouter初始化方法，需在application初始化")
                .addModifiers(PUBLIC, STATIC)
                .addParameter(classNameApplication, "application");
        method_init.addCode("store = new Stack<$T>();", classNameActivity);

        tb.addMethod(method_init.build());

        MethodSpec.Builder go_method = MethodSpec.methodBuilder("go")
                .addJavadoc("activity跳转方法")
                .addModifiers(PUBLIC, STATIC)
                .addParameter(String.class, "activityName");


        List<ClassName> mList = new ArrayList<>();
        for (TypeElement element : routeElements) {
            ClassName currentType = ClassName.get(element);
            if (mList.contains(currentType)) continue;
            mList.add(currentType);

            //定义final字符串常量，标识需要跳转到Activity
            String activitName = currentType.canonicalName();
            activitName = activitName.replace('.', '_').toUpperCase();

            FieldSpec fieldSpec = FieldSpec.builder(String.class, activitName)
                    .addModifiers(PUBLIC, STATIC, FINAL)
                    .initializer("$S", activitName).build();
            tb.addField(fieldSpec);
        }

        tb.addMethod(go_method.build());


        try {
            JavaFile javaFile = JavaFile.builder(Utils.PackageName, tb.build()).build();// 生成源代码
            javaFile.writeTo(mFiler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
