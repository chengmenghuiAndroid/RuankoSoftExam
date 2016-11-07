package com.itee.exam.app.ui.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by malin on 2016/7/27.
 */
public class Apply implements Serializable {
    private int applyId;
    private int subjectId;
    private String subjectName;
    private String phoneNumber;
    private String applyState;
    private String realName;
    private String applyPlace;
    private Date creatime;
    private String sex;
    private String certificateType;//证件类型
    private String certificateNumber;//证件号码
    private String birthplace;//出生地
    private String currentLocation;//当前所在省（是否湖北省）
    private String currentProvince;//当前所在省
    private String currentCity;//当前所在市/区
    private Date birthday;
    private String highestEducation;
    private String highestDegree;
    private String highestCollegesType;
    private String highestColleges;
    private String entranceTime;//入学时间
    private String professionalOrSubjects;//所学专业是否与报考科目相同/相近/不同
    private String occupationState;//职业状态/在职1/在读2/无业3

    private String companyType;//单位类别
    private String companyName;//单位名称
    private String jobLevel;//已有职称（级别）
    //可选字段
    private Date getTime;//职称获取的时间
    private String administrationRank;//行政级别
    private String businessRank;//业务职级
    private String departmentPosts;//工作部门或岗位
    private Date startWorkingTime;//参加工作的时间
    private String workingLifeTime;//工作年限
    private String archivePosition;//档案存档单位
    private String socialSecurity;//社保缴纳状态

    private String faculties;//院系（全日制在读人员所填）
    private String professional;//专业（全日制在读人员所填）

    private String email;
    private String fixedPhone;
    private String QQ;

    private String addressCategory;//通信地址类别（工作单位/学校地址/家庭地址）
    private String address;
    private String detailAddress;
    private String postcode;//邮政编码
    //可选字段
    private String spareContacts;//备用联系人姓名
    private String contactsRelationship;//通信地址类别（工作单位/学校地址/家庭地址）跟本人关系
    private String spareContactsPhone; //备用联系人手机号码
    private String examHistory;//试考历史（未参加/参加无证书/参加且有证书）
    private int isFristApply;//是否第一次报考本科目

    private String uploadPhoto;//上传证件照
    private String operator;

    public int getApplyId() {
        return applyId;
    }

    public void setApplyId(int applyId) {
        this.applyId = applyId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getApplyState() {
        return applyState;
    }

    public void setApplyState(String applyState) {
        this.applyState = applyState;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getApplyPlace() {
        return applyPlace;
    }

    public void setApplyPlace(String applyPlace) {
        this.applyPlace = applyPlace;
    }

    public Date getCreatime() {
        return creatime;
    }

    public void setCreatime(Date creatime) {
        this.creatime = creatime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getCurrentProvince() {
        return currentProvince;
    }

    public void setCurrentProvince(String currentProvince) {
        this.currentProvince = currentProvince;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getHighestEducation() {
        return highestEducation;
    }

    public void setHighestEducation(String highestEducation) {
        this.highestEducation = highestEducation;
    }

    public String getHighestDegree() {
        return highestDegree;
    }

    public void setHighestDegree(String highestDegree) {
        this.highestDegree = highestDegree;
    }

    public String getHighestCollegesType() {
        return highestCollegesType;
    }

    public void setHighestCollegesType(String highestCollegesType) {
        this.highestCollegesType = highestCollegesType;
    }

    public String getHighestColleges() {
        return highestColleges;
    }

    public void setHighestColleges(String highestColleges) {
        this.highestColleges = highestColleges;
    }

    public String getEntranceTime() {
        return entranceTime;
    }

    public void setEntranceTime(String entranceTime) {
        this.entranceTime = entranceTime;
    }

    public String getProfessionalOrSubjects() {
        return professionalOrSubjects;
    }

    public void setProfessionalOrSubjects(String professionalOrSubjects) {
        this.professionalOrSubjects = professionalOrSubjects;
    }

    public String getOccupationState() {
        return occupationState;
    }

    public void setOccupationState(String occupationState) {
        this.occupationState = occupationState;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobLevel() {
        return jobLevel;
    }

    public void setJobLevel(String jobLevel) {
        this.jobLevel = jobLevel;
    }

    public Date getGetTime() {
        return getTime;
    }

    public void setGetTime(Date getTime) {
        this.getTime = getTime;
    }

    public String getAdministrationRank() {
        return administrationRank;
    }

    public void setAdministrationRank(String administrationRank) {
        this.administrationRank = administrationRank;
    }

    public String getBusinessRank() {
        return businessRank;
    }

    public void setBusinessRank(String businessRank) {
        this.businessRank = businessRank;
    }

    public String getDepartmentPosts() {
        return departmentPosts;
    }

    public void setDepartmentPosts(String departmentPosts) {
        this.departmentPosts = departmentPosts;
    }

    public Date getStartWorkingTime() {
        return startWorkingTime;
    }

    public void setStartWorkingTime(Date startWorkingTime) {
        this.startWorkingTime = startWorkingTime;
    }

    public String getWorkingLifeTime() {
        return workingLifeTime;
    }

    public void setWorkingLifeTime(String workingLifeTime) {
        this.workingLifeTime = workingLifeTime;
    }

    public String getArchivePosition() {
        return archivePosition;
    }

    public void setArchivePosition(String archivePosition) {
        this.archivePosition = archivePosition;
    }

    public String getSocialSecurity() {
        return socialSecurity;
    }

    public void setSocialSecurity(String socialSecurity) {
        this.socialSecurity = socialSecurity;
    }

    public String getFaculties() {
        return faculties;
    }

    public void setFaculties(String faculties) {
        this.faculties = faculties;
    }

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFixedPhone() {
        return fixedPhone;
    }

    public void setFixedPhone(String fixedPhone) {
        this.fixedPhone = fixedPhone;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public String getAddressCategory() {
        return addressCategory;
    }

    public void setAddressCategory(String addressCategory) {
        this.addressCategory = addressCategory;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getSpareContacts() {
        return spareContacts;
    }

    public void setSpareContacts(String spareContacts) {
        this.spareContacts = spareContacts;
    }

    public String getContactsRelationship() {
        return contactsRelationship;
    }

    public void setContactsRelationship(String contactsRelationship) {
        this.contactsRelationship = contactsRelationship;
    }

    public String getSpareContactsPhone() {
        return spareContactsPhone;
    }

    public void setSpareContactsPhone(String spareContactsPhone) {
        this.spareContactsPhone = spareContactsPhone;
    }

    public String getExamHistory() {
        return examHistory;
    }

    public void setExamHistory(String examHistory) {
        this.examHistory = examHistory;
    }

    public int getIsFristApply() {
        return isFristApply;
    }

    public void setIsFristApply(int isFristApply) {
        this.isFristApply = isFristApply;
    }

    public String getUploadPhoto() {
        return uploadPhoto;
    }

    public void setUploadPhoto(String uploadPhoto) {
        this.uploadPhoto = uploadPhoto;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
