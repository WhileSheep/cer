package com.example.cer.domain.dto;

import com.example.cer.domain.entity.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * 复学申批表
 */
@Data
@Component
public class CBSApprovalFormDto implements Dto {
    private int i;
    private String pdfName;
    private CBSBasicInformationEntity basicInformation;
    private applyEntity apply;
    private parentOpinionEntity parentOpinion;
    private instructorOpinionEntity instructorOpinion;
    private collegeOpinionEntity collegeOpinion;
    private schoolOpinionEntity schoolOpinion;

    @Override
    public CBSApprovalFormDto run() {
        this.i = 1;
        return this;
    }
}
