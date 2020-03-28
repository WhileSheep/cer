package com.example.cer.domain.service;

import com.example.cer.domain.dto.*;
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
            pdfBuilderService.fxsp(cBSApprovalFormDto);   //内异步开始  //插入信息
        }
        log.info(Thread.currentThread().getName() + " 外异步方法结束: " + "生成复学审批表" + Thread.currentThread().isAlive());
    }


    /**
     * 休学审批表
     */
    @Async
    public void sSDataProcessing(List<SSApprovalFormDto> sSApprovalFormDtos){
        log.info(Thread.currentThread().getName() + " 外异步方法开始: " + "生成复学审批表" + Thread.currentThread().isAlive());
        log.info(String.valueOf(sSApprovalFormDtos));
        for (SSApprovalFormDto ssApprovalFormDto : sSApprovalFormDtos) {
            pdfBuilderService.xxsp(ssApprovalFormDto);   //内异步开始  //插入信息
        }
        log.info(Thread.currentThread().getName() + " 外异步方法结束: " + "生成复学审批表" + Thread.currentThread().isAlive());
    }


    /**
     * 退学审批表
     */
    @Async
    public void outDataProcessing(List<OSApprovalFormDto> osApprovalFormDtos){
        log.info(Thread.currentThread().getName() + " 外异步方法开始: " + "生成复学审批表" + Thread.currentThread().isAlive());
        log.info(String.valueOf(osApprovalFormDtos));
        for (OSApprovalFormDto osApprovalFormDto : osApprovalFormDtos) {
            pdfBuilderService.txsp(osApprovalFormDto);   //内异步开始  //插入信息
        }
        log.info(Thread.currentThread().getName() + " 外异步方法结束: " + "生成复学审批表" + Thread.currentThread().isAlive());
    }


    /**
     * 保留学籍审批表
     */
    @Async
    public void keepDataProcessing(List<KSApprovalFormDto> ksApprovalFormDtos){
        log.info(String.valueOf(ksApprovalFormDtos));
    }
}
