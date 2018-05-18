/**
 * Information Retrieval Integrated System ( I.R.I.S. )
 * Republic of The Philippines, DOST Regional Office No. III
 * Provincial Science Technology Center, City of Malolos, Bulacan
 *
 * Afterschool Creatives "Captivating Creativity"
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

import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Column;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.PrimaryKey;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Table;

/**
 *
 * @author Jhon Melvin
 */
@Table(ScholarInformationModel.TABLE)
public class ScholarInformationModel extends PolarisEntity {

    //==========================================================================
    // Afterschool Creatives Polaris Record Content Standardization
    //==========================================================================
    // Sections
    // 01. Table Columns
    // 02. Model Fields
    // 03. Constructor (Initialize Default Values)
    // 04-A. Static Inner Classes
    // 04-B. Static Methods (Mostly Database Queries)
    // 04-C. Custom Methods (Non-Static Methods)
    // 05-A. Getters
    // 05-B. Setters
    //
    // To standardized the creation of PolarisRecord classes. a complete child
    // class must contain all of the following sections mentioned above.
    //
    // All Child classes must contain this standardization notice for reference.
    //
    // Standardization Code: 001 - 03/31/2018
    //==========================================================================
    // 01. Table Columns
    //==========================================================================
    public final static String TABLE = "scholar_information";
    public final static String SCHOLAR_ID = "scholar_id";
    public final static String STUDENT_NUMBER = "student_number";
    public final static String LAST_NAME = "last_name";
    public final static String FIRST_NAME = "first_name";
    public final static String MIDDLE_NAME = "middle_name";
    public final static String EXT_NAME = "ext_name";
    public final static String GENDER = "gender";
    public final static String SCHOLAR_TYPE = "scholar_type";
    public final static String MERIT_TYPE = "merit_type";
    public final static String COURSE = "course";
    public final static String YEAR = "year_level";
    public final static String SECTION = "section";
    public final static String UNIVERSITY = "university";
    public final static String MOBILE_NO = "mobile_no";
    public final static String TEL_NO = "tel_no";
    public final static String E_MAIL = "e_mail";
    public final static String STUDENT_ADDRESS = "student_address";
    public final static String STUDENT_CITY_MUNICIPALITY = "student_city_municipality";
    public final static String STUDENT_PROVINCE = "student_province";

    //==========================================================================
    // 02. Model Fields
    //==========================================================================
    @PrimaryKey
    @Column(SCHOLAR_ID)
    private String scholarId;

    @Column(STUDENT_NUMBER)
    private String studentNumber;

    @Column(LAST_NAME)
    private String lastName;

    @Column(FIRST_NAME)
    private String firstName;

    @Column(MIDDLE_NAME)
    private String middleName;

    @Column(EXT_NAME)
    private String extName;

    @Column(GENDER)
    private String gender;

    @Column(SCHOLAR_TYPE)
    private Integer scholarType;

    @Column(MERIT_TYPE)
    private Integer meritType;

    @Column(COURSE)
    private String course;

    @Column(YEAR)
    private Integer year;

    @Column(SECTION)
    private String section;

    @Column(UNIVERSITY)
    private String university;

    @Column(MOBILE_NO)
    private String mobileNo;

    @Column(TEL_NO)
    private String telNo;

    @Column(E_MAIL)
    private String mail;

    @Column(STUDENT_ADDRESS)
    private String studentAddress;

    @Column(STUDENT_CITY_MUNICIPALITY)
    private String studentCityMunicipality;

    @Column(STUDENT_PROVINCE)
    private String studentProvince;

    //==========================================================================
    // 03. Constructor (Initialize Default Values)
    //==========================================================================
    public ScholarInformationModel() {
        this.studentNumber = "";
        this.lastName = "";
        this.firstName = "";
        this.middleName = "";
        this.extName = "";
        this.gender = null;
        this.course = "";
        this.year = null;
        this.section = "";
        this.university = "";
        this.mobileNo = "";
        this.telNo = "";
        this.mail = "";
        this.studentAddress = "";
        this.studentCityMunicipality = "";
        this.studentProvince = "";
    }

    public String getScholarId() {
        return scholarId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getExtName() {
        return extName;
    }

    public String getGender() {
        return gender;
    }

    public Integer getScholarType() {
        return scholarType;
    }

    public Integer getMeritType() {
        return meritType;
    }

    public String getCourse() {
        return course;
    }

    public Integer getYear() {
        return year;
    }

    public String getSection() {
        return section;
    }

    public String getUniversity() {
        return university;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getTelNo() {
        return telNo;
    }

    public String getMail() {
        return mail;
    }

    public String getStudentAddress() {
        return studentAddress;
    }

    public String getStudentCityMunicipality() {
        return studentCityMunicipality;
    }

    public String getStudentProvince() {
        return studentProvince;
    }

    public void setScholarId(String scholarId) {
        this.scholarId = scholarId;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setScholarType(Integer scholarType) {
        this.scholarType = scholarType;
    }

    public void setMeritType(Integer meritType) {
        this.meritType = meritType;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setStudentAddress(String studentAddress) {
        this.studentAddress = studentAddress;
    }

    public void setStudentCityMunicipality(String studentCityMunicipality) {
        this.studentCityMunicipality = studentCityMunicipality;
    }

    public void setStudentProvince(String studentProvince) {
        this.studentProvince = studentProvince;
    }

}
