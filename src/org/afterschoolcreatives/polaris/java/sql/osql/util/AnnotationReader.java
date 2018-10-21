/**
 *
 * Objective SQL - Afterschool Creatives "Captivating Creativity"
 *
 *
 * Copyright 2018 Jhon Melvin Nieto Perello
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
 * Contact Us:
 * Facebook: www.facebook.com/afterschoolcreatives
 * Google Mail: afterschoolcreatives@gmail.com
 *
 */
package org.afterschoolcreatives.polaris.java.sql.osql.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jhon Melvin
 */
public class AnnotationReader {

    /**
     * Get all annotations in this class.
     *
     * @param javaClass
     * @return
     */
    public static Annotation[] getClassAnnotations(Class javaClass) {
        Annotation[] classAnnotation = javaClass.getDeclaredAnnotations();
        return classAnnotation;
    }

    /**
     * Get all annotated constructors of this class.
     *
     * @param javaClass
     * @return
     */
    public static Constructor[] getAnnotatedConstructors(Class javaClass) {
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

    /**
     * Get all annotated fields.
     *
     * @param javaClass
     * @return
     */
    public static Field[] getAnnotatedFields(Class javaClass) {
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

    /**
     * Get all annotated methods.
     *
     * @param javaClass
     * @return
     */
    public static Method[] getAnnotatedMethods(Class javaClass) {
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
