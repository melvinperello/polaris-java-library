/**
 *
 * Polaris Java Library - Afterschool Creatives "Captivating Creativity"
 *
 * Copyright 2018 Jhon Melvin Perello
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package org.afterschoolcreatives.polaris.java.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads all the annotation of a class with runtime retention.
 *
 * @author Jhon Melvin
 */
public class PolarisAnnotationReader {

    public static Annotation[] getClassAnnotations(Class javaClass) {
        Annotation[] classAnnotation = javaClass.getDeclaredAnnotations();
        return classAnnotation;
    }

    public static Constructor[] checkConstructor(Class javaClass) {
        Constructor[] contructors = javaClass.getDeclaredConstructors();
        final List<Constructor> annotatedConstructor = new ArrayList<>();
        for (Constructor contructor : contructors) {
            Annotation[] constructorAnnotation = contructor.getAnnotations();
            if (constructorAnnotation.length != 0) {
                annotatedConstructor.add(contructor);
            }
        }
        return annotatedConstructor.toArray(new Constructor[annotatedConstructor.size()]);
    }

    public static Field[] checkFields(Class javaClass) {
        Field[] fields = javaClass.getDeclaredFields();
        final List<Field> annotatedField = new ArrayList<>();
        for (Field field : fields) {
            Annotation[] fieldAnnoatations = field.getAnnotations();
            if (fieldAnnoatations.length != 0) {
                annotatedField.add(field);
            }
        }
        return annotatedField.toArray(new Field[annotatedField.size()]);
    }

    public static Method[] checkMethods(Class javaClass) {
        Method[] methods = javaClass.getDeclaredMethods();
        final List<Method> annotatedMethod = new ArrayList<>();
        for (Method method : methods) {
            Annotation[] methodAnnoatations = method.getDeclaredAnnotations();
            if (methodAnnoatations.length != 0) {
                annotatedMethod.add(method);
            }
        }
        return annotatedMethod.toArray(new Method[annotatedMethod.size()]);
    }

}