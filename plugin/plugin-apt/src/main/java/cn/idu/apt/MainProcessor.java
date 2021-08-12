package cn.idu.apt;

import com.google.auto.service.AutoService;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import cn.idu.apt.Utils.Logger;


public abstract class MainProcessor extends AbstractProcessor {

    Filer mFiler; //文件相关的辅助类
    Elements elementUtils; //元素相关的辅助类
    Logger logger;//日志相关的辅助类


    Types types;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        types = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        logger = new Logger(processingEnv.getMessager());
    }

}
