//package com.main;
//
//import java.io.IOException;
//
//import com.sun.tools.attach.AgentInitializationException;
//import com.sun.tools.attach.AgentLoadException;
//import com.sun.tools.attach.AttachNotSupportedException;
//import com.sun.tools.attach.VirtualMachine;
//
//public class Attach {
//
//    /**
//     * 使用此类必须引入 ${JAVA_HOME}/lib/tools.jar到classpath
//     * 打包运行agent时候,必须注释此类
//     */
//    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
//
//        // 通过 jps -l 查询需要attach的进程的端口
//        VirtualMachine vm = VirtualMachine.attach("29308");
//
//        // 替换自己的agent jar包地址
//        vm.loadAgent("E:\\xieleiagent\\target\\xielei-agent-1.0-SNAPSHOT-jar-with-dependencies.jar");
//    }
//}
