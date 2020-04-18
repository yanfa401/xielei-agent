package com.xielei;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class SimpleAgent {

    public static final String BASE_PACKAGE = "com.test";

    /**
     * jvm 参数形式启动，运行此方法
     * @param agentArgs
     * @param inst
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("正在通过JVM参数方式启动agent...");
        customLogic(inst);
    }

    /**
     * 动态 attach 方式启动，运行此方法
     * @param agentArgs
     * @param inst
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("正在通过动态attach方式启动agent...");
        doDynomicAttch(inst);
    }


    /**
     * 动态 attach
     *
     * @param inst
     */
    private static void doDynomicAttch(Instrumentation inst) {
        // 1.获取所有jvm中已经加载的类
        Class[] allClasses = inst.getAllLoadedClasses();
        for (Class clazz : allClasses) {
            if (clazz.getName().startsWith(BASE_PACKAGE)) {
                // 2.过滤我们感兴趣的类
                System.out.println("侦测到" + clazz.getName() + ",准备重新加载该类...");
                try {
                    // 3.先添加探针
                    inst.addTransformer(new CostTimeTransformer(), true);
                    // 4.对我们增强后的类重新加载到jvm
                    inst.retransformClasses(clazz);
                } catch (UnmodifiableClassException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 统计方法耗时--JVM启动时带该方法
     *
     * @param inst
     */
    private static void customLogic(Instrumentation inst) {
        inst.addTransformer(new CostTimeTransformer(), true);
    }
}
