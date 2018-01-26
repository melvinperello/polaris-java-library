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
package org.afterschoolcreatives.polaris.java.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * This class acts as a normal HashMap but has extended features to read
 * contents from a property file with a key value pair and also has the ability
 * to modify its contents.
 *
 * @author Jhon Melvin
 */
public class PolarisProperties extends Properties {

    /**
     * Serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Writes the property file into a binary data in UTF-8 Encoding. This
     * method uses buffered writer.
     *
     * Note: FileWriter doesn't let you specify the encoding, which is extremely
     * annoying. It always uses the system default encoding. Just suck it up and
     * use OutputStreamWriter wrapping a FileOutputStream. You can still wrap
     * the OutputStreamWriter in a BufferedWriter of course.
     *
     * @param file
     * @param comments
     * @throws IOException
     */
    public void write(File file, String comments) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            this.store(writer, comments);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * Writes the property file into a binary data in UTF-8 Encoding. This
     * method uses buffered writer.
     *
     * Note: FileWriter doesn't let you specify the encoding, which is extremely
     * annoying. It always uses the system default encoding. Just suck it up and
     * use OutputStreamWriter wrapping a FileOutputStream. You can still wrap
     * the OutputStreamWriter in a BufferedWriter of course.
     *
     * @param file
     * @throws IOException
     */
    public void write(File file) throws IOException {
        this.write(file, null);
    }

    /**
     * Reads a file and loads it to the properties. Using UTF-8 Default
     * Encoding.
     *
     * @param file
     * @throws IOException
     */
    public void read(File file) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            this.load(reader);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * Returns always the String Class of the property.
     *
     * @param key
     * @return
     */
    @Override
    public synchronized String get(Object key) {
        return String.valueOf(super.get(key)); //To change body of generated methods, choose Tools | Templates.
    }

}
