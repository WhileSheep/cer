package com.example.cer.domain.service;

import com.example.cer.domain.dto.CBSApprovalFormDto;
import com.example.cer.domain.dto.KSApprovalFormDto;
import com.example.cer.domain.dto.OSApprovalFormDto;
import com.example.cer.domain.dto.SSApprovalFormDto;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class ApprovalFormService {
    private PdfBuilderService pdfBuilderService;

    @Autowired
    public ApprovalFormService(PdfBuilderService pdfBuilderService) {
        this.pdfBuilderService = pdfBuilderService;
    }


    /**
     * 复学审批表
     */
    @Async
    public void cBSDataProcessing(List<CBSApprovalFormDto> cBSApprovalFormDtos){
        log.info(Thread.currentThread().getName() + " 外异步方法开始: " + "生成复学审批表" + Thread.currentThread().isAlive());
        log.info(String.valueOf(cBSApprovalFormDtos));
        for (CBSApprovalFormDto cBSApprovalFormDto : cBSApprovalFormDtos) {
            pdfBuilderService.pdfBuilder(cBSApprovalFormDto);   //内异步开始  //插入信息
        }
        log.info(Thread.currentThread().getName() + " 外异步方法结束: " + "生成复学审批表" + Thread.currentThread().isAlive());
    }


    /**
     * 休学审批表
     */
    public void sSDataProcessing(List<SSApprovalFormDto> sSApprovalFormDtos){
        log.info(String.valueOf(sSApprovalFormDtos));
    }

    /**
     * 退学审批表
     */
    public void outDataProcessing(List<OSApprovalFormDto> osApprovalFormDtos){
        log.info(String.valueOf(osApprovalFormDtos));
    }

    /**
     * 保留学籍审批表
     */
    public void keepDataProcessing(List<KSApprovalFormDto> ksApprovalFormDtos){
        log.info(String.valueOf(ksApprovalFormDtos));
    }
}
