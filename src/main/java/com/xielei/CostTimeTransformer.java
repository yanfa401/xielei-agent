package com.xielei;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.Modifier;

/**
 *
 */
public class CostTimeTransformer implements ClassFileTransformer{

    public static final String BASE_PACKAGE = "com/test/";

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        // 只针对 com.xielei 开头的包进行耗时打印
        if (!className.startsWith(BASE_PACKAGE)) {
            return classfileBuffer;
        }

        byte[] transformed = null;
        System.out.println("Transforming:" + className);
        ClassPool pool = ClassPool.getDefault();
        CtClass cl = null;
        try {
            cl = pool.makeClass(new ByteArrayInputStream(classfileBuffer));
            // 如果不是接口
            if (cl.isInterface() == false) {
                // 获取声明的所有方法
                CtBehavior[] methods = cl.getDeclaredBehaviors();
                // 循环遍历所有方法
                for (CtBehavior method : methods) {
                    if (method.isEmpty() == false && !Modifier.isNative(method.getModifiers())) {
                        /**
                         * 核心逻辑: 修改每个方法的字节码
                         */
                        doWithMethod(className, method);
                    }
                }
                // 获取修改后的字节码字节数组
                transformed = cl.toBytecode();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (cl != null) {
                cl.detach();
            }
        }
        return transformed;
    }


    /**
     * 修改传入方法的字节码
     */
    private void doWithMethod(String className, CtBehavior method) throws CannotCompileException {
        //增加本地变量
        method.addLocalVariable("startTime", CtClass.longType);
        // 在方法头前插入代码
        method.insertBefore("startTime = System.currentTimeMillis();");
        // 在方法尾部插入代码
        String afterWord = String.format("System.out.println(\"探针追踪 %s 方法,总共耗时(毫秒):\" + (System.currentTimeMillis() - startTime));", className + "#" + method.getName());
        method.insertAfter(afterWord);

    }
}
