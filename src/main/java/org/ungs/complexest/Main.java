package org.ungs.complexest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.ungs.evenmorecomplex.anotherpackage.Example;
import org.ungs.evenmorecomplex.anotherpackage.ExampleImpl;
import org.ungs.utils.FullyQualifiedNameFinder;

/**
 * Here we load a jar file from src/test/resources
 * and load a specific class that is an interface
 * implementation from an interface defined in here
 *
 * FINAL BOSS
 */
public class Main {

    public static void extractClassesFromJar(String jarFilePath) throws IOException {
        try {
            File jarFile = new File(jarFilePath);

            // Check if the JAR file exists
            if (!jarFile.exists() || !jarFile.isFile()) {
                System.err.println("Jar file does not exist.");
                return;
            }

            // Create a directory for extracted classes in the same location as the JAR file
            File outputDir = new File(jarFile.getParent(), "extracted_classes");
            if (!outputDir.exists()) {
                outputDir.mkdir();
            }

            // Open the JAR file
            JarFile jar = new JarFile(jarFile);

            // Iterate through JAR entries and extract class files
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                // Check if the entry is a class file
                if (entry.getName().endsWith(".class")) {
                    // Create a file for the extracted class in the output directory
                    File outputFile = new File(outputDir, entry.getName());

                    // Ensure the parent directories exist
                    File parentDir = outputFile.getParentFile();
                    if (!parentDir.exists()) {
                        parentDir.mkdirs();
                    }

                    // Extract the class file content
                    InputStream inputStream = jar.getInputStream(entry);
                    FileOutputStream outputStream = new FileOutputStream(outputFile);

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    // Close streams
                    outputStream.close();
                    inputStream.close();
                }
            }

            // Close the JAR file
            jar.close();

            System.out.println("Classes extracted to: " + outputDir.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        //extractClassesFromJar("src/test/resources/example-ext.jar");
        Set<Example> classes = findClasses("src/test/resources/extracted_classes");

        for (Example cls : classes) {
            cls.sayHello();
        }
    }


}
