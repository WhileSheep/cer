package com.example.cer.domain.service;

import com.example.cer.config.PdfConfig;
import com.example.cer.domain.dto.*;
import com.example.cer.qiNiu.qiniuUpload;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;


/**
 * 未优化版本，一张pdf生成到上传七牛云 耗时7s ，其中大量为io耗时
 * 取出io操作改为ByteArrayOutputStream，本地生成一张pdf,加上传七牛云 总耗时 3s
 */
@Slf4j
@Service
public class PdfBuilderService {
    private qiniuUpload upload;
    private PdfConfig pdfConfig;
    private txspService txspService;
    private fxspService fxspService;
    private xxspService xxspService;
    private redisService redisService;

    @Autowired
    public PdfBuilderService(qiniuUpload upload, txspService txspService, fxspService fxspService,xxspService xxspService, redisService redisService){
        this.upload = upload;
        this.txspService = txspService;
        this.fxspService = fxspService;
        this.xxspService = xxspService;
        this.pdfConfig = new PdfConfig();
        this.redisService = redisService;
    }

    /**
     * 获取PdfAcroForm
     */
    private PdfAcroForm getPdfAcroForm(PdfDocument pdfDoc){
        return PdfAcroForm.getAcroForm(pdfDoc, true);
    }


    @Async
    public void fxsp(CBSApprovalFormDto cbsApprovalFormDto){
        String pdfPath = pdfConfig.getDto(0).getPdfPath() + cbsApprovalFormDto.getPdfName();
        PdfDocument pdfDocument = null;
        try {
            pdfDocument = new PdfDocument(new PdfReader(pdfConfig.getDto(0).getTemplatePath()), new PdfWriter(pdfPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PdfAcroForm pdfAcroForm = getPdfAcroForm(pdfDocument);
        Map<String, PdfFormField> formFieldMap = pdfAcroForm.getFormFields();
        fxspService.fxspInfo(formFieldMap,cbsApprovalFormDto)
                .fxspImage(formFieldMap,cbsApprovalFormDto);
        flattenFields(pdfAcroForm);
        assert pdfDocument != null;
        close(pdfDocument);
        upload.uploadPdf(pdfPath);
    }


    @Async
    public void xxsp(SSApprovalFormDto ssApprovalFormDto){
        String pdfPath = pdfConfig.getDto(1).getPdfPath() + ssApprovalFormDto.getPdfName();
        PdfDocument pdfDocument = null;
        try {
            pdfDocument = new PdfDocument(new PdfReader(pdfConfig.getDto(1).getTemplatePath()), new PdfWriter(pdfPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PdfAcroForm pdfAcroForm = getPdfAcroForm(pdfDocument);
        Map<String, PdfFormField> formFieldMap = pdfAcroForm.getFormFields();
        xxspService.xxspInfo(formFieldMap,ssApprovalFormDto)
                .xxspImage(formFieldMap,ssApprovalFormDto);
        flattenFields(pdfAcroForm);
        assert pdfDocument != null;
        close(pdfDocument);
        upload.uploadPdf(pdfPath);
    }


    @Async
    public void txsp(OSApprovalFormDto osApprovalFormDto){
        log.info("pdf开始");
        String pdfPath = pdfConfig.getDto(2).getPdfPath() + osApprovalFormDto.getPdfName();
        PdfDocument pdfDocument = null;
        try {
            pdfDocument = new PdfDocument(new PdfReader(pdfConfig.getDto(2).getTemplatePath()), new PdfWriter(pdfPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PdfAcroForm pdfAcroForm = getPdfAcroForm(pdfDocument);
        Map<String, PdfFormField> formFieldMap = pdfAcroForm.getFormFields();
        txspService.txspInfo(formFieldMap,osApprovalFormDto)
                .txspImage(formFieldMap,osApprovalFormDto);
        flattenFields(pdfAcroForm);
        assert pdfDocument != null;
        close(pdfDocument);
        log.info("pdf完成");
        upload.uploadPdf(pdfPath);
    }



    /**
     *  设置生成表单不可编辑
     */
    private void flattenFields(PdfAcroForm pdfAcroForm){
        pdfAcroForm.flattenFields();
    }

    /**
     *  关闭pdfDocument
     */
    private void close(PdfDocument pdfDocument){
        pdfDocument.close();
    }
}