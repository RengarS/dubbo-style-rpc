package com.aries.dubbo.like.rpc.common.util;

import com.aries.dubbo.like.rpc.common.annotations.RpcProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * Author: aries
 * Date: 2018/8/23
 * Description: 扫描包中的类
 */
@Slf4j
public class ClassUtil {

    private static List<Class<?>> CLASS_LIST = new LinkedList<>();

    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }


    public static void packageScan(String basePath) {
        try {
            Enumeration<URL> urls = getClassLoader().getResources(basePath.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")) {
                        String packagePath = url.getPath().replaceAll("%20", "");
                        addClass(CLASS_LIST, packagePath, basePath);
                    } else if (protocol.equals("jar")) {
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        if (jarURLConnection != null) {
                            JarFile jarFile = jarURLConnection.getJarFile();
                            if (jarFile != null) {
                                Enumeration<JarEntry> jarEntries = jarFile.entries();
                                while (jarEntries.hasMoreElements()) {
                                    JarEntry jarEntry = jarEntries.nextElement();
                                    String jarEntryName = jarEntry.getName();
                                    if (jarEntryName.endsWith(".class")) {
                                        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf("."))
                                                .replaceAll("/", ".");
                                        doAddClass(CLASS_LIST, className);
                                    }
                                }
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void addClass(List<Class<?>> classList, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) ||
                file.isDirectory());
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (StringUtils.isNotEmpty(packageName)) {
                    className = packageName + "." + className;
                }
                doAddClass(classList, className);
            } else {
                String subPackagePath = fileName;
                if (StringUtils.isNotEmpty(packagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (StringUtils.isNotEmpty(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                addClass(classList, subPackagePath, subPackageName);
            }
        }
    }

    private static void doAddClass(List<Class<?>> classList, String className) {
        Class<?> cls = loadClass(className, false);
        classList.add(cls);
    }

    /**
     * 加载类
     *
     * @param className     class的名称
     * @param isInitialized class是否初始化
     * @return
     */
    private static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> cls;
        try {
            cls = Class.forName(className, isInitialized, getClassLoader());

        } catch (ClassNotFoundException e) {

            throw new RuntimeException(e);
        }
        return cls;
    }

    public static List<Class<?>> getClassList() {
        return CLASS_LIST;
    }

    /**
     * 获取被标记为rpc服务提供者的接口
     *
     * @return
     */
    public static List<Class<?>> getAnnotationedClassList() {
        return CLASS_LIST.stream()
                .filter(clz -> clz.isAnnotationPresent(RpcProvider.class))
                .collect(Collectors.toList());
    }


}
