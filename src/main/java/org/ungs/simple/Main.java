package org.ungs.simple;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * By simple I mean that I just discover a class within this package
 * that isn't an implementation of an interface.
 *
 * To compile the .java file:
 *  `javac Example.java`
 */
public class Main {

    public static Set<Object> findClasses(String path)
        throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Set<Object> result = new HashSet<>();
        for (File f : new File(path).listFiles()) {
            if (!f.getName().endsWith(".class")) continue;

            String className = f.getName().substring(0, f.getName().lastIndexOf(".class"));
            String packageName = "org.ungs.simple"; // Replace with your actual package name
            String fullyQualifiedClassName = packageName + "." + className;

            Class<?> cls = Class.forName(fullyQualifiedClassName);

            if (!cls.isAssignableFrom(cls)) {
                throw new RuntimeException();
            }

            Example ex = (Example) cls.newInstance();
            ex.sayHello();

            result.add(cls.newInstance());
        }

        return result;
    }

    public static void main(String[] args)
        throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Set<Object> classes = findClasses("./src/main/java/org/ungs/simple");

        for (Object cls : classes) {
            System.out.println(cls.getClass().getName());
        }

    }

}