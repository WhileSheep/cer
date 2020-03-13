package com.example.cer.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class OSBasicInformationEntity {
    private String studentName;
    private String sex;
    private String nation;
    private String birthday;
    private String studentNumber;
    private String college;
    private String major;
    private String provinceAndCity;
    private String enterSchoolTime;
    private String outSchoolTime;
    private String grade;
    private courseEntity course;
}
