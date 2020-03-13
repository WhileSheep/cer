package com.example.cer.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class KSBasicInformationEntity {
    private String studentName;
    private String sex;
    private String nation;
    private String birthday;
    private String studentNumber;
    private String college;
    private String major;
    private String enterSchoolTime;
    private String keepSchoolTime;
    private String limitTime;
    private String grade;
    private String provinceAndCity;
    private String address;
    private String applicantTelephone;
}
