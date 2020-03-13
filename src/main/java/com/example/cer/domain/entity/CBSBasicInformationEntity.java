package com.example.cer.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;


@Data
@Component
public class CBSBasicInformationEntity {
    private String studentName;
    private String sex;
    private String nation;
    private String birthday;
    private String studentNumber;
    private String college;
    private String major;
    private String enterSchoolTime;
    private String suspendSchoolTime;
    private String comeBackSchoolTime;
    private String comeBackSchoolGrade;
    private String provinceAndCity;
}
