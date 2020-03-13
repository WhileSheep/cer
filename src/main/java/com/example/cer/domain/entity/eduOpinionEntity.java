package com.example.cer.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 教务处意见
 */
@Data
@Component
public class eduOpinionEntity {
    private String opinionInfo;
    private String eduSignature;
    private String eduDate;
}
