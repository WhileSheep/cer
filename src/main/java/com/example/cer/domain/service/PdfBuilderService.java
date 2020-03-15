package com.example.cer.domain.service;

import com.example.cer.qiNiu.qiniuUpload;
import com.example.cer.utils.FileUtil;
import com.example.cer.utils.PdfUtil;
import com.example.cer.domain.dto.CBSApprovalFormDto;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.codec.Base64;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;


@Slf4j
@Service
public class PdfBuilderService {
    private String rootPath;
    private final float fontSize = 10.6f;
    private PdfUtil pdfUtil;
    private qiniuUpload upload;

    @Autowired
    public PdfBuilderService(PdfUtil pdfUtil, qiniuUpload upload){
        this.pdfUtil = pdfUtil;
        this.upload = upload;
        try {
            rootPath = new File("").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 获取PdfAcroForm
     */
    private PdfAcroForm getPdfAcroForm(PdfDocument pdfDoc){
        return PdfAcroForm.getAcroForm(pdfDoc, true);
    }

    /**
     * 设置字体
     */
    private PdfFont setFont(){
        PdfFont font = null;
        try {
            font = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H",true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return font;
    }


    /**
     * 复学审批表 插入信息
     */
    private BiConsumer<Map<String, PdfFormField>,CBSApprovalFormDto> comeBackInfo  = (formFieldMap,cbsApprovalFormDto) -> {
        Runnable basicInfo = () -> {
            formFieldMap.get("name").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getBasicInformation().getStudentName());
            formFieldMap.get("xb").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getBasicInformation().getSex());
            formFieldMap.get("mz").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getBasicInformation().getNation());
            formFieldMap.get("csrq").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getBasicInformation().getBirthday());
            formFieldMap.get("number").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getBasicInformation().getStudentNumber());
            formFieldMap.get("college").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getBasicInformation().getCollege());
            formFieldMap.get("major").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getBasicInformation().getMajor());
            formFieldMap.get("rxsj").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getBasicInformation().getEnterSchoolTime());
            formFieldMap.get("xxsj").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getBasicInformation().getSuspendSchoolTime());
            formFieldMap.get("fxsj").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getBasicInformation().getComeBackSchoolTime());
            formFieldMap.get("fxnj").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getBasicInformation().getComeBackSchoolGrade());
            formFieldMap.get("lyss").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getBasicInformation().getProvinceAndCity());
        };
        Runnable applyDate = () -> {
            if (!"".equals(cbsApprovalFormDto.getApply().getApplyDate())){
                cbsApprovalFormDto.getApply().setApplyDateYear(cbsApprovalFormDto.getApply().getApplyDate().split("-")[0]);
                cbsApprovalFormDto.getApply().setApplyDateMonth(cbsApprovalFormDto.getApply().getApplyDate().split("-")[1]);
                cbsApprovalFormDto.getApply().setApplyDateDay(cbsApprovalFormDto.getApply().getApplyDate().split("-")[2]);
            }else {
                cbsApprovalFormDto.getApply().setApplyDateYear("");
                cbsApprovalFormDto.getApply().setApplyDateMonth("");
                cbsApprovalFormDto.getApply().setApplyDateDay("");
            }
        };
        Runnable applyInfo = () -> {
            formFieldMap.get("fxsq").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getApply().getApplyInformation());
            formFieldMap.get("lxdh").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getApply().getApplicantTelephone());
            applyDate.run();
            formFieldMap.get("year1").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getApply().getApplyDateYear());
            formFieldMap.get("month1").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getApply().getApplyDateMonth());
            formFieldMap.get("day1").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getApply().getApplyDateDay());
        };
        Runnable parentDate = () -> {
            if (!"".equals(cbsApprovalFormDto.getParentOpinion().getParentDate())){
                cbsApprovalFormDto.getParentOpinion().setParentDateYear(cbsApprovalFormDto.getParentOpinion().getParentDate().split("-")[0]);
                cbsApprovalFormDto.getParentOpinion().setParentDateMonth(cbsApprovalFormDto.getParentOpinion().getParentDate().split("-")[1]);
                cbsApprovalFormDto.getParentOpinion().setParentDateDay(cbsApprovalFormDto.getParentOpinion().getParentDate().split("-")[2]);
            }else {
                cbsApprovalFormDto.getParentOpinion().setParentDateYear("");
                cbsApprovalFormDto.getParentOpinion().setParentDateMonth("");
                cbsApprovalFormDto.getParentOpinion().setParentDateDay("");
            }
        };
        Runnable parentInfo = () -> {
            formFieldMap.get("jzlxdh").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getParentOpinion().getParentTelephone());
            parentDate.run();
            formFieldMap.get("year2").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getParentOpinion().getParentDateYear());
            formFieldMap.get("month2").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getParentOpinion().getParentDateMonth());
            formFieldMap.get("day2").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getParentOpinion().getParentDateDay());
        };
        Runnable instructorDate = () -> {
            if (!"".equals(cbsApprovalFormDto.getInstructorOpinion().getInstructorDate())){
                cbsApprovalFormDto.getInstructorOpinion().setInstructorDateYear(cbsApprovalFormDto.getInstructorOpinion().getInstructorDate().split("-")[0]);
                cbsApprovalFormDto.getInstructorOpinion().setInstructorDateMonth(cbsApprovalFormDto.getInstructorOpinion().getInstructorDate().split("-")[1]);
                cbsApprovalFormDto.getInstructorOpinion().setInstructorDateDay(cbsApprovalFormDto.getInstructorOpinion().getInstructorDate().split("-")[2]);
            }else {
                cbsApprovalFormDto.getInstructorOpinion().setInstructorDateYear("");
                cbsApprovalFormDto.getInstructorOpinion().setInstructorDateMonth("");
                cbsApprovalFormDto.getInstructorOpinion().setInstructorDateDay("");
            }
        };
        Runnable instructorInfo = () -> {
            instructorDate.run();
            formFieldMap.get("year3").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getInstructorOpinion().getInstructorDateYear());
            formFieldMap.get("month3").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getInstructorOpinion().getInstructorDateMonth());
            formFieldMap.get("day3").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getInstructorOpinion().getInstructorDateDay());
        };
        Runnable collegeDate = () -> {
            if (!"".equals(cbsApprovalFormDto.getCollegeOpinion().getCollegeDate())){
                cbsApprovalFormDto.getCollegeOpinion().setCollegeDateYear(cbsApprovalFormDto.getCollegeOpinion().getCollegeDate().split("-")[0]);
                cbsApprovalFormDto.getCollegeOpinion().setCollegeDateMonth(cbsApprovalFormDto.getCollegeOpinion().getCollegeDate().split("-")[1]);
                cbsApprovalFormDto.getCollegeOpinion().setCollegeDateDay(cbsApprovalFormDto.getCollegeOpinion().getCollegeDate().split("-")[2]);
            }else {
                cbsApprovalFormDto.getCollegeOpinion().setCollegeDateYear("");
                cbsApprovalFormDto.getCollegeOpinion().setCollegeDateMonth("");
                cbsApprovalFormDto.getCollegeOpinion().setCollegeDateDay("");
            }
        };
        Runnable collegeInfo = () -> {
            collegeDate.run();
            formFieldMap.get("year4").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getCollegeOpinion().getCollegeDateYear());
            formFieldMap.get("month4").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getCollegeOpinion().getCollegeDateMonth());
            formFieldMap.get("day4").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getCollegeOpinion().getCollegeDateDay());
        };
        Runnable schoolDate = () -> {
            if (!"".equals(cbsApprovalFormDto.getSchoolOpinion().getSchoolDate())){
                cbsApprovalFormDto.getSchoolOpinion().setSchoolDateYear(cbsApprovalFormDto.getSchoolOpinion().getSchoolDate().split("-")[0]);
                cbsApprovalFormDto.getSchoolOpinion().setSchoolDateMonth(cbsApprovalFormDto.getSchoolOpinion().getSchoolDate().split("-")[1]);
                cbsApprovalFormDto.getSchoolOpinion().setSchoolDateDay(cbsApprovalFormDto.getSchoolOpinion().getSchoolDate().split("-")[2]);
            }else {
                cbsApprovalFormDto.getSchoolOpinion().setSchoolDateYear("");
                cbsApprovalFormDto.getSchoolOpinion().setSchoolDateMonth("");
                cbsApprovalFormDto.getSchoolOpinion().setSchoolDateDay("");
            }
        };
        Runnable schoolInfo = () -> {
            schoolDate.run();
            formFieldMap.get("year5").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getSchoolOpinion().getSchoolDateYear());
            formFieldMap.get("month5").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getSchoolOpinion().getSchoolDateMonth());
            formFieldMap.get("day5").setFont(setFont()).setFontSize(fontSize).setValue(cbsApprovalFormDto.getSchoolOpinion().getSchoolDateDay());
        };
        basicInfo.run();
        applyInfo.run();
        parentInfo.run();
        instructorInfo.run();
        collegeInfo.run();
        schoolInfo.run();
    };


    /**
     * 复学审批表 插入图片
     */
    private List<String> comeBackImage(PdfDocument pdfDocument, Map<String,PdfFormField> formFieldMap, CBSApprovalFormDto cbsApprovalFormDto){
        List<String> pngPathList = new ArrayList<>();
        Runnable applySignature = () -> {
            if (!"".equals(cbsApprovalFormDto.getApply().getApplicantSignature())){
                String pngPath =  PdfUtil.SvgToPng(cbsApprovalFormDto.getApply().getApplicantSignature());
                File pngFile = PdfUtil.RotationImage(pngPath);
//                String base64 =  FileUtil.encode(pngFile.getAbsolutePath());
//                System.out.println(base64);
//                formFieldMap.get("sqrqm").setValue(base64);
                pdfUtil.Image(pdfDocument,formFieldMap.get("sqrqm"),pngFile.getAbsolutePath());
                pngPathList.add(pngPath);
                pngPathList.add(pngFile.getAbsolutePath());
            }
        };
        Runnable parentSignature = () -> {
            if (!"".equals(cbsApprovalFormDto.getParentOpinion().getParentSignature())){
                String pngPath =  PdfUtil.SvgToPng(cbsApprovalFormDto.getParentOpinion().getParentSignature());
                File pngFile = PdfUtil.RotationImage(pngPath);
//                String base64 =  FileUtil.encode(pngFile.getAbsolutePath());
//                System.out.println(base64);
//                formFieldMap.get("jzqm").setValue(base64);
                pdfUtil.Image(pdfDocument,formFieldMap.get("jzqm"),pngFile.getAbsolutePath());
                pngPathList.add(pngPath);
                pngPathList.add(pngFile.getAbsolutePath());
            }
        };
        Runnable instructorSignature = () -> {
            if (!"".equals(cbsApprovalFormDto.getInstructorOpinion().getInstructorSignature())){
                String pngPath =  PdfUtil.SvgToPng(cbsApprovalFormDto.getInstructorOpinion().getInstructorSignature());
                File pngFile = PdfUtil.RotationImage(pngPath);
//                String base64 =  FileUtil.encode(pngFile.getAbsolutePath());
//                System.out.println(base64);
//                formFieldMap.get("fdyqm").setValue(base64);
                pdfUtil.Image(pdfDocument,formFieldMap.get("fdyqm"),pngFile.getAbsolutePath());
                pngPathList.add(pngPath);
                pngPathList.add(pngFile.getAbsolutePath());
            }
        };
        Runnable collegeSignature = () -> {
            if (!"".equals(cbsApprovalFormDto.getCollegeOpinion().getCollegeSignature())){
                String pngPath =  PdfUtil.SvgToPng(cbsApprovalFormDto.getCollegeOpinion().getCollegeSignature());
                File pngFile = PdfUtil.RotationImage(pngPath);
                pdfUtil.Image(pdfDocument,formFieldMap.get("xyqm"),pngFile.getAbsolutePath());
                pngPathList.add(pngPath);
                pngPathList.add(pngFile.getAbsolutePath());
            }
        };
        Runnable schoolSignature = () -> {
            if (!"".equals(cbsApprovalFormDto.getSchoolOpinion().getSchoolSignature())){
                String pngPath =  PdfUtil.SvgToPng(cbsApprovalFormDto.getSchoolOpinion().getSchoolSignature());
                File pngFile = PdfUtil.RotationImage(pngPath);
                pdfUtil.Image(pdfDocument,formFieldMap.get("xxqm"),pngFile.getAbsolutePath());
                pngPathList.add(pngPath);
                pngPathList.add(pngFile.getAbsolutePath());
            }
        };
        applySignature.run();
        parentSignature.run();
        instructorSignature.run();
        collegeSignature.run();
        schoolSignature.run();
        return pngPathList;
    }


    private void Builder(CBSApprovalFormDto cbsApprovalFormDto){
        String templatePath = rootPath + "/assets/template/comeBackTemplate.pdf";
        String newPdfPath = rootPath + "/pdf/comeBackSchool/" + cbsApprovalFormDto.getPdfName();
        PdfDocument pdfDocument = null;
        try {
             pdfDocument = new PdfDocument(new PdfReader(templatePath), new PdfWriter(newPdfPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PdfAcroForm pdfAcroForm = getPdfAcroForm(pdfDocument);
        Map<String, PdfFormField> formFieldMap = pdfAcroForm.getFormFields();
        comeBackInfo.accept(formFieldMap,cbsApprovalFormDto);
        List<String> pngList = comeBackImage(pdfDocument,formFieldMap,cbsApprovalFormDto);
        flattenFields(pdfAcroForm);
        assert pdfDocument != null;
        close(pdfDocument);
        deletePng(pngList);//关闭文档后,删除图片
//        upload.uploadPdf(newPdfPath);//异步上传七牛云
    }


    @Async
    public void pdfBuilder(CBSApprovalFormDto cbsApprovalFormDto){
        log.info(Thread.currentThread().getName() + " 内异步方法开始" + Thread.currentThread().isAlive());
        Builder(cbsApprovalFormDto);
        log.info(Thread.currentThread().getName() + " 内异步方法结束" + Thread.currentThread().isAlive());
    }


    /**
     *  删除生成的图片
     *  删除图片之前要关闭所有流inputStream
     */
    @Async
    void deletePng(List<String> pngList){
        log.info(Thread.currentThread().getName() + " delete内异步方法开始" + Thread.currentThread().isAlive());
        for (String s : pngList) {
            FileUtil.deleteFolder(s);
        }
        log.info(Thread.currentThread().getName() + " delete内异步方法结束" + Thread.currentThread().isAlive());
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