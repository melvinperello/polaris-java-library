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

import java.sql.SQLException;
import org.afterschoolcreatives.polaris.java.sql.ConnectionFactory;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Column;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.FetchOnly;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Nullable;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.PrimaryKey;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Table;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Limit;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Unsigned;

/**
 *
 * @author Jhon Melvin
 */
@Table("student")
public class Sample extends PolarisEntity {

    @Column("name")
    @PrimaryKey
    @FetchOnly
    @Unsigned
    @Nullable(Nullable.Mode.NULL_IS_BLANK)
    @Limit(length = 150, apprehension = Limit.Apprehension.CHOP)
    public String studentName;

    public static void main(String[] args) {

        Sample sample = new Sample();
        try {

            sample.insert(null);
            sample.update(null);
        } catch (SQLException ex) {

//            Logger.getLogger(Sample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
