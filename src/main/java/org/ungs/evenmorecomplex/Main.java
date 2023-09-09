package org.ungs.evenmorecomplex;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import org.ungs.evenmorecomplex.anotherpackage.Example;
import org.ungs.evenmorecomplex.anotherpackage.ExampleImpl;
import org.ungs.utils.FullyQualifiedNameFinder;

/**
 * Here we search classes that are not in the same
 * package as the main class.
 *
 * To compile the .java files, checkout to anotherpackage and run:
 * `javac Example.java ExampleImpl.java`
 */
public class Main {

    public static Set<Example> findClasses(String path)
        throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Set<Example> result = new HashSet<>();
        for (File f : new File(path).listFiles()) {
            if(f.isDirectory()) {
                return findClasses(f.getPath());
            }
            if (!f.getName().endsWith(".class")) continue;

            String className = f.getName().substring(0, f.getName().lastIndexOf(".class"));
            String fullyQualifiedClassName = FullyQualifiedNameFinder.find(className, path);

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
        Set<Example> classes = findClasses("src/main/java/org/ungs/evenmorecomplex");

        for (Example cls : classes) {
            cls.sayHello();
        }
    }

}
