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
package org.afterschoolcreatives.polaris.java.sql.orm;

import java.sql.Connection;
import java.sql.SQLException;
import org.afterschoolcreatives.polaris.java.sql.ConnectionFactory;
import org.afterschoolcreatives.polaris.java.sql.ConnectionManager;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Table;

/**
 *
 * @author Jhon Melvin
 */
@Table("student")
public class Sample {

    public static void main(String[] args) {
        ConnectionFactory cf = new ConnectionFactory();
        cf.setHost("127.0.0.1");
        cf.setPort("5432");
        cf.setConnectionDriver(ConnectionFactory.Driver.PostgreSQL);
        cf.setUsername("postgres");
        cf.setPassword("root");
        cf.setDatabaseName("sample");

        try (ConnectionManager con = cf.createConnectionManager()) {
            for (int x = 0; x < 2; x++) {
                ScholarInformationModel sample = new ScholarInformationModel();
                sample.setFirstName("Melvin");
                sample.insert(con);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

    }
}
