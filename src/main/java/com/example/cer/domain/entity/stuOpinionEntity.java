package com.example.cer.domain.entity;


import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 学生处意见
 */
@Data
@Component
public class stuOpinionEntity {
    private String opinionInfo;
    private String stuSignature;
    private String stuDate;
}
