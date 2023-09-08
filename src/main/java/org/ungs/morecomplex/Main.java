package org.ungs.morecomplex;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Here I discover a class within this package
 * that is an implementation of an interface.
 *
 * That's why it's more complex than the simple version.
 *
 * To compile the .java files:
 * `javac Example.java ExampleImpl.java`
 */
public class Main {

    public static Set<Example> findClasses(String path)
        throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Set<Example> result = new HashSet<>();
        for (File f : new File(path).listFiles()) {
            if (!f.getName().endsWith(".class")) continue;

            String className = f.getName().substring(0, f.getName().lastIndexOf(".class"));
            String packageName = "org.ungs.morecomplex"; // Replace with your actual package name
            String fullyQualifiedClassName = packageName + "." + className;

            Class<?> cls = Class.forName(fullyQualifiedClassName);

            if (!cls.isAssignableFrom(cls)) {
                throw new RuntimeException();
            }

            Example ex;
            try {
                ex = (ExampleImpl) cls.newInstance();
                result.add(ex);
            } catch (InstantiationException e) {
                System.out.println(cls.getName() + " is not an implementation of Example");
            }

        }

        return result;
    }

    public static void main(String[] args)
        throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Set<Example> classes = findClasses("./src/main/java/org/ungs/morecomplex");

        for (Example cls : classes) {
           cls.sayHello();
        }

    }

}