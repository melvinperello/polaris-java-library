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
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Jhon Melvin
 */
public class PolarisAnnotatedClass {

    private final Class javaClass;
    private final List<Annotation> classAnnotations;
    private final List<Constructor> annotatedConstructors;
    private final List<Field> annotatedFields;
    private final List<Method> annotatedMethods;

    public PolarisAnnotatedClass(Class javaClass) {
        this.javaClass = javaClass;
        this.classAnnotations = new ArrayList<>();
        this.annotatedConstructors = new ArrayList<>();
        this.annotatedFields = new ArrayList<>();
        this.annotatedMethods = new ArrayList<>();
        // Start-up read.
        this.readAnnotations();
    }

    //--------------------------------------------------------------------------
    // Remove // Only the readAnnotation method can write to the internal lists.
    //--------------------------------------------------------------------------
    public void removeClassAnnotation(int index) {
        this.classAnnotations.remove(index);
    }

    public void removeConstructor(int index) {
        this.annotatedConstructors.remove(index);
    }

    public void removeField(int index) {
        this.annotatedFields.remove(index);
    }

    public void removeMethod(int index) {
        this.annotatedMethods.remove(index);
    }

    //--------------------------------------------------------------------------
    // Read-Only Access
    //--------------------------------------------------------------------------
    /**
     * Makes a copy of the internal list. any modification made to the returned
     * list will not be reflected to the internal list.
     *
     * @return
     */
    public List<Annotation> getClassAnnotations() {
        return new ArrayList<>(this.classAnnotations);

    }

    /**
     * Makes a copy of the internal list. any modification made to the returned
     * list will not be reflected to the internal list.
     *
     * @return
     */
    public List<Constructor> getAnnotatedConstructors() {
        return new ArrayList<>(this.annotatedConstructors);
    }

    /**
     * Makes a copy of the internal list. any modification made to the returned
     * list will not be reflected to the internal list.
     *
     * @return
     */
    public List<Field> getAnnotatedFields() {
        return new ArrayList<>(this.annotatedFields);
    }

    /**
     * Makes a copy of the internal list. any modification made to the returned
     * list will not be reflected to the internal list.
     *
     * @return
     */
    public List<Method> getAnnotatedMethods() {
        return new ArrayList<>(this.annotatedMethods);
    }

    //--------------------------------------------------------------------------
    // Methods
    //--------------------------------------------------------------------------
    private void readAnnotations() {
        // Clear Lists.
        this.classAnnotations.clear();
        this.annotatedConstructors.clear();
        this.annotatedFields.clear();
        this.annotatedMethods.clear();
        // Read Annotations.
        this.classAnnotations.addAll(Arrays.asList(PolarisAnnotationReader
                .getClassAnnotations(this.javaClass)));
        this.annotatedConstructors.addAll(Arrays.asList(PolarisAnnotationReader
                .getAnnotatedConstructors(this.javaClass)));
        this.annotatedFields.addAll(Arrays.asList(PolarisAnnotationReader
                .getAnnotatedFields(this.javaClass)));
        this.annotatedMethods.addAll(Arrays.asList(PolarisAnnotationReader
                .getAnnotatedMethods(this.javaClass)));
    }
}
