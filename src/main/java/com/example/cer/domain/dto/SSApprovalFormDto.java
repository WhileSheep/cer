package com.example.cer.domain.dto;

import com.example.cer.domain.entity.*;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 休学审批表
 */
@Data
@Component
public class SSApprovalFormDto implements Dto{
    private int i;
    private String pdfName;
    private SSBasicInformationEntity basicInformation;
    private applyEntity apply;
    private SSparentOpinionEntity parentOpinion;
    private instructorOpinionEntity instructorOpinion;
    private collegeOpinionEntity collegeOpinion;
    private schoolOpinionEntity schoolOpinion;
    //    private String studentSignature;  //使用申请人签字

    @Override
    public SSApprovalFormDto run() {
        this.i = 2;
        return this;
    }

}
