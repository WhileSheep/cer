package com.example.cer.domain.service;

import com.example.cer.config.PdfConfig;
import com.example.cer.domain.dto.*;
import com.example.cer.qiNiu.qiniuUpload;
import com.example.cer.utils.FileUtil;
import com.example.cer.utils.ImageUtil;
import com.example.cer.utils.PdfUtil;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
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
    private final float fontSize = 10.6f;
    private PdfUtil pdfUtil;
    private qiniuUpload upload;
    private PdfConfig pdfConfig;
    private FromDto fromDto;

    @Autowired
    public PdfBuilderService(PdfUtil pdfUtil, qiniuUpload upload){
        this.pdfUtil = pdfUtil;
        this.upload = upload;
        this.pdfConfig = new PdfConfig();
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
     * 复学 插入信息
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
     * 复学 插入图片
     */
    private List<String> comeBackImage(PdfDocument pdfDocument, Map<String,PdfFormField> formFieldMap, CBSApprovalFormDto cbsApprovalFormDto){
        List<String> pngPathList = new ArrayList<>();
        Runnable applySignature = () -> {
            if (!"".equals(cbsApprovalFormDto.getApply().getApplicantSignature())){
                String pngPath =  PdfUtil.SvgToPng(cbsApprovalFormDto.getApply().getApplicantSignature(),cbsApprovalFormDto.getPdfName(),"comeBackSchool");//生成竖版签字
                File pngFile = PdfUtil.RotationImage(pngPath,cbsApprovalFormDto.getPdfName(),"comeBackSchool");//生成横版签字
                String base64 =  FileUtil.encode(pngFile.getAbsolutePath());
//                System.out.println(base64);
                formFieldMap.get("sqrqm").setValue(base64);
//                pdfUtil.Image(pdfDocument,formFieldMap.get("sqrqm"),pngFile.getAbsolutePath());
//                pngPathList.add(pngPath);
//                pngPathList.add(pngFile.getAbsolutePath());
            }
        };
        Runnable parentSignature = () -> {
            if (!"".equals(cbsApprovalFormDto.getParentOpinion().getParentSignature())){
                String pngPath =  PdfUtil.SvgToPng(cbsApprovalFormDto.getParentOpinion().getParentSignature(),cbsApprovalFormDto.getPdfName(),"comeBackSchool");
                File pngFile = PdfUtil.RotationImage(pngPath,cbsApprovalFormDto.getPdfName(),"comeBackSchool");
                String base64 =  FileUtil.encode(pngFile.getAbsolutePath());
//                System.out.println(base64);
                formFieldMap.get("jzqm").setValue(base64);
//                pdfUtil.Image(pdfDocument,formFieldMap.get("jzqm"),pngFile.getAbsolutePath());
//                pngPathList.add(pngPath);
//                pngPathList.add(pngFile.getAbsolutePath());
            }
        };
        Runnable instructorSignature = () -> {
            if (!"".equals(cbsApprovalFormDto.getInstructorOpinion().getInstructorSignature())){
                String pngPath =  PdfUtil.SvgToPng(cbsApprovalFormDto.getInstructorOpinion().getInstructorSignature(),cbsApprovalFormDto.getPdfName(),"comeBackSchool");
                File pngFile = PdfUtil.RotationImage(pngPath,cbsApprovalFormDto.getPdfName(),"comeBackSchool");
                String base64 =  FileUtil.encode(pngFile.getAbsolutePath());
//                System.out.println(base64);
                formFieldMap.get("fdyqm").setValue(base64);
//                pdfUtil.Image(pdfDocument,formFieldMap.get("fdyqm"),pngFile.getAbsolutePath());
//                pngPathList.add(pngPath);
//                pngPathList.add(pngFile.getAbsolutePath());
            }
        };
        Runnable collegeSignature = () -> {
            if (!"".equals(cbsApprovalFormDto.getCollegeOpinion().getCollegeSignature())){
                String pngPath =  PdfUtil.SvgToPng(cbsApprovalFormDto.getCollegeOpinion().getCollegeSignature(),cbsApprovalFormDto.getPdfName(),"comeBackSchool");
                File pngFile = PdfUtil.RotationImage(pngPath,cbsApprovalFormDto.getPdfName(),"comeBackSchool");
                String base64 =  FileUtil.encode(pngFile.getAbsolutePath());
//                System.out.println(base64);
                formFieldMap.get("xyqm").setValue(base64);
//                pdfUtil.Image(pdfDocument,formFieldMap.get("xyqm"),pngFile.getAbsolutePath());
//                pngPathList.add(pngPath);
//                pngPathList.add(pngFile.getAbsolutePath());
            }
        };
        Runnable schoolSignature = () -> {
            if (!"".equals(cbsApprovalFormDto.getSchoolOpinion().getSchoolSignature())){
                String pngPath =  PdfUtil.SvgToPng(cbsApprovalFormDto.getSchoolOpinion().getSchoolSignature(),cbsApprovalFormDto.getPdfName(),"comeBackSchool");
                File pngFile = PdfUtil.RotationImage(pngPath,cbsApprovalFormDto.getPdfName(),"comeBackSchool");
                String base64 =  FileUtil.encode(pngFile.getAbsolutePath());
//                System.out.println(base64);
                formFieldMap.get("xxqm").setValue(base64);
//                pdfUtil.Image(pdfDocument,formFieldMap.get("xxqm"),pngFile.getAbsolutePath());
//                pngPathList.add(pngPath);
//                pngPathList.add(pngFile.getAbsolutePath());
            }
        };
        applySignature.run();
        parentSignature.run();
        instructorSignature.run();
        collegeSignature.run();
        schoolSignature.run();
        return pngPathList;
    }


    /**
     * 休学 插入信息
     */
    private BiConsumer<Map<String, PdfFormField>, SSApprovalFormDto> suspendInfo = ((formFieldMap, ssApprovalFormDto) -> {
        Runnable basicInfo = () -> {
            formFieldMap.get("name").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getBasicInformation().getStudentName());
            formFieldMap.get("xb").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getBasicInformation().getSex());
            formFieldMap.get("mz").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getBasicInformation().getNation());
            formFieldMap.get("csrq").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getBasicInformation().getBirthday());
            formFieldMap.get("number").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getBasicInformation().getStudentNumber());
            formFieldMap.get("college").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getBasicInformation().getCollege());
            formFieldMap.get("major").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getBasicInformation().getMajor());
            formFieldMap.get("rxsj").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getBasicInformation().getEnterSchoolTime());
            formFieldMap.get("xxsj").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getBasicInformation().getXxTime());
            formFieldMap.get("fxsj").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getBasicInformation().getComeBackSchoolTime());
            formFieldMap.get("xxqx").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getBasicInformation().getLimitTime());
            formFieldMap.get("lyss").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getBasicInformation().getProvinceAndCity());
        };
        Runnable applyDate = () -> {
            if (!"".equals(ssApprovalFormDto.getApply().getApplyDate())){
                ssApprovalFormDto.getApply().setApplyDateYear(ssApprovalFormDto.getApply().getApplyDate().split("-")[0]);
                ssApprovalFormDto.getApply().setApplyDateMonth(ssApprovalFormDto.getApply().getApplyDate().split("-")[1]);
                ssApprovalFormDto.getApply().setApplyDateDay(ssApprovalFormDto.getApply().getApplyDate().split("-")[2]);
            }else {
                ssApprovalFormDto.getApply().setApplyDateYear("");
                ssApprovalFormDto.getApply().setApplyDateMonth("");
                ssApprovalFormDto.getApply().setApplyDateDay("");
            }
        };
        Runnable applyInfo = () -> {
            formFieldMap.get("fxsq").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getApply().getApplyInformation());
            formFieldMap.get("lxdh").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getApply().getApplicantTelephone());
            applyDate.run();
            formFieldMap.get("year1").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getApply().getApplyDateYear());
            formFieldMap.get("month1").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getApply().getApplyDateMonth());
            formFieldMap.get("day1").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getApply().getApplyDateDay());
        };
        Runnable parentDate = () -> {
            if (!"".equals(ssApprovalFormDto.getParentOpinion().getParentDate())){
                ssApprovalFormDto.getParentOpinion().setParentDateYear(ssApprovalFormDto.getParentOpinion().getParentDate().split("-")[0]);
                ssApprovalFormDto.getParentOpinion().setParentDateMonth(ssApprovalFormDto.getParentOpinion().getParentDate().split("-")[1]);
                ssApprovalFormDto.getParentOpinion().setParentDateDay(ssApprovalFormDto.getParentOpinion().getParentDate().split("-")[2]);
            }else {
                ssApprovalFormDto.getParentOpinion().setParentDateYear("");
                ssApprovalFormDto.getParentOpinion().setParentDateMonth("");
                ssApprovalFormDto.getParentOpinion().setParentDateDay("");
            }
        };
        Runnable parentInfo = () -> {
            formFieldMap.get("gx").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getParentOpinion().getStudentRelation());
            formFieldMap.get("jzlxdh").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getParentOpinion().getParentTelephone());
            parentDate.run();
            formFieldMap.get("year2").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getParentOpinion().getParentDateYear());
            formFieldMap.get("month2").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getParentOpinion().getParentDateMonth());
            formFieldMap.get("day2").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getParentOpinion().getParentDateDay());
        };
        Runnable instructorDate = () -> {
            if (!"".equals(ssApprovalFormDto.getInstructorOpinion().getInstructorDate())){
                ssApprovalFormDto.getInstructorOpinion().setInstructorDateYear(ssApprovalFormDto.getInstructorOpinion().getInstructorDate().split("-")[0]);
                ssApprovalFormDto.getInstructorOpinion().setInstructorDateMonth(ssApprovalFormDto.getInstructorOpinion().getInstructorDate().split("-")[1]);
                ssApprovalFormDto.getInstructorOpinion().setInstructorDateDay(ssApprovalFormDto.getInstructorOpinion().getInstructorDate().split("-")[2]);
            }else {
                ssApprovalFormDto.getInstructorOpinion().setInstructorDateYear("");
                ssApprovalFormDto.getInstructorOpinion().setInstructorDateMonth("");
                ssApprovalFormDto.getInstructorOpinion().setInstructorDateDay("");
            }
        };
        Runnable instructorInfo = () -> {
            instructorDate.run();
            formFieldMap.get("year3").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getInstructorOpinion().getInstructorDateYear());
            formFieldMap.get("month3").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getInstructorOpinion().getInstructorDateMonth());
            formFieldMap.get("day3").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getInstructorOpinion().getInstructorDateDay());
        };
        Runnable collegeDate = () -> {
            if (!"".equals(ssApprovalFormDto.getCollegeOpinion().getCollegeDate())){
                ssApprovalFormDto.getCollegeOpinion().setCollegeDateYear(ssApprovalFormDto.getCollegeOpinion().getCollegeDate().split("-")[0]);
                ssApprovalFormDto.getCollegeOpinion().setCollegeDateMonth(ssApprovalFormDto.getCollegeOpinion().getCollegeDate().split("-")[1]);
                ssApprovalFormDto.getCollegeOpinion().setCollegeDateDay(ssApprovalFormDto.getCollegeOpinion().getCollegeDate().split("-")[2]);
            }else {
                ssApprovalFormDto.getCollegeOpinion().setCollegeDateYear("");
                ssApprovalFormDto.getCollegeOpinion().setCollegeDateMonth("");
                ssApprovalFormDto.getCollegeOpinion().setCollegeDateDay("");
            }
        };
        Runnable collegeInfo = () -> {
            collegeDate.run();
            formFieldMap.get("year4").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getCollegeOpinion().getCollegeDateYear());
            formFieldMap.get("month4").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getCollegeOpinion().getCollegeDateMonth());
            formFieldMap.get("day4").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getCollegeOpinion().getCollegeDateDay());
        };
        Runnable schoolDate = () -> {
            if (!"".equals(ssApprovalFormDto.getSchoolOpinion().getSchoolDate())){
                ssApprovalFormDto.getSchoolOpinion().setSchoolDateYear(ssApprovalFormDto.getSchoolOpinion().getSchoolDate().split("-")[0]);
                ssApprovalFormDto.getSchoolOpinion().setSchoolDateMonth(ssApprovalFormDto.getSchoolOpinion().getSchoolDate().split("-")[1]);
                ssApprovalFormDto.getSchoolOpinion().setSchoolDateDay(ssApprovalFormDto.getSchoolOpinion().getSchoolDate().split("-")[2]);
            }else {
                ssApprovalFormDto.getSchoolOpinion().setSchoolDateYear("");
                ssApprovalFormDto.getSchoolOpinion().setSchoolDateMonth("");
                ssApprovalFormDto.getSchoolOpinion().setSchoolDateDay("");
            }
        };
        Runnable schoolInfo = () -> {
            schoolDate.run();
            formFieldMap.get("year5").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getSchoolOpinion().getSchoolDateYear());
            formFieldMap.get("month5").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getSchoolOpinion().getSchoolDateMonth());
            formFieldMap.get("day5").setFont(setFont()).setFontSize(fontSize).setValue(ssApprovalFormDto.getSchoolOpinion().getSchoolDateDay());
        };
        basicInfo.run();
        applyInfo.run();
        parentInfo.run();
        instructorInfo.run();
        collegeInfo.run();
        schoolInfo.run();
    });

    /**
     * 休学 插入图片
     */
    private List<String> suspendImage(PdfDocument pdfDocument, Map<String,PdfFormField> formFieldMap, SSApprovalFormDto ssApprovalFormDto){
        List<String> pngPathList = new ArrayList<>();
        Runnable applySignature = () -> {
            if (!"".equals(ssApprovalFormDto.getApply().getApplicantSignature())){
                String pngPath =  PdfUtil.SvgToPng(ssApprovalFormDto.getApply().getApplicantSignature(),ssApprovalFormDto.getPdfName(),"suspendSchool");//生成竖版签字
                File pngFile = PdfUtil.RotationImage(pngPath,ssApprovalFormDto.getPdfName(),"suspendSchool");//生成横版签字
                String base64 =  FileUtil.encode(pngFile.getAbsolutePath());
                formFieldMap.get("sqrqm").setValue(base64);
                formFieldMap.get("xsqm").setValue(base64);
            }
        };
        Runnable parentSignature = () -> {
            if (!"".equals(ssApprovalFormDto.getParentOpinion().getParentSignature())){
                String pngPath =  PdfUtil.SvgToPng(ssApprovalFormDto.getParentOpinion().getParentSignature(),ssApprovalFormDto.getPdfName(),"suspendSchool");
                File pngFile = PdfUtil.RotationImage(pngPath,ssApprovalFormDto.getPdfName(),"suspendSchool");
                String base64 =  FileUtil.encode(pngFile.getAbsolutePath());
                formFieldMap.get("jzqm").setValue(base64);
            }
        };
        Runnable instructorSignature = () -> {
            if (!"".equals(ssApprovalFormDto.getInstructorOpinion().getInstructorSignature())){
                String pngPath =  PdfUtil.SvgToPng(ssApprovalFormDto.getInstructorOpinion().getInstructorSignature(),ssApprovalFormDto.getPdfName(),"suspendSchool");
                File pngFile = PdfUtil.RotationImage(pngPath,ssApprovalFormDto.getPdfName(),"suspendSchool");
                String base64 =  FileUtil.encode(pngFile.getAbsolutePath());
                formFieldMap.get("fdyqm").setValue(base64);
            }
        };
        Runnable collegeSignature = () -> {
            if (!"".equals(ssApprovalFormDto.getCollegeOpinion().getCollegeSignature())){
                String pngPath =  PdfUtil.SvgToPng(ssApprovalFormDto.getCollegeOpinion().getCollegeSignature(),ssApprovalFormDto.getPdfName(),"suspendSchool");
                File pngFile = PdfUtil.RotationImage(pngPath,ssApprovalFormDto.getPdfName(),"suspendSchool");
                String base64 =  FileUtil.encode(pngFile.getAbsolutePath());
                formFieldMap.get("xyqm").setValue(base64);
            }
        };
        Runnable schoolSignature = () -> {
            if (!"".equals(ssApprovalFormDto.getSchoolOpinion().getSchoolSignature())){
                String pngPath =  PdfUtil.SvgToPng(ssApprovalFormDto.getSchoolOpinion().getSchoolSignature(),ssApprovalFormDto.getPdfName(),"suspendSchool");
                File pngFile = PdfUtil.RotationImage(pngPath,ssApprovalFormDto.getPdfName(),"suspendSchool");
                String base64 =  FileUtil.encode(pngFile.getAbsolutePath());
                formFieldMap.get("xxqm").setValue(base64);
            }
        };
        Runnable sfz = () -> {
            if (!"".equals(ssApprovalFormDto.getParentOpinion().getParentIdCard_Zheng())){
                InputStream inputStream = ImageUtil.getImageStream(ssApprovalFormDto.getParentOpinion().getParentIdCard_Zheng());
                String base64 =  FileUtil.getBase64FromInputStream(inputStream);
                formFieldMap.get("sfzzm").setValue(base64);
            }
            if (!"".equals(ssApprovalFormDto.getParentOpinion().getParentIdCard_Fan())){
                InputStream inputStream = ImageUtil.getImageStream(ssApprovalFormDto.getParentOpinion().getParentIdCard_Fan());
                String base64 =  FileUtil.getBase64FromInputStream(inputStream);
                formFieldMap.get("sfzfm").setValue(base64);
            }
        };
        applySignature.run();
        parentSignature.run();
        instructorSignature.run();
        collegeSignature.run();
        schoolSignature.run();
        sfz.run();
        return pngPathList;
    }


    /**
     * 审批表 核心
     */
    private void Builder(Dto dto){
        int i = -1;
        String pdfName = null;
        CBSApprovalFormDto cbsApprovalFormDto = null;
        SSApprovalFormDto ssApprovalFormDto = null;
        List<String> pngList = null;
        fromDto = new FromDto(dto);
        if (fromDto.getI() == 0){
            i = 0;
            cbsApprovalFormDto = fromDto.getCbsApprovalFormDto();
            pdfName = cbsApprovalFormDto.getPdfName();
        }else if (fromDto.getI() == 1){
            i = 1;
            ssApprovalFormDto = fromDto.getSsApprovalFormDto();
            pdfName = ssApprovalFormDto.getPdfName();
        }
        String templatePath = pdfConfig.getDto(i).getTemplatePath();
        String newPdfPath = pdfConfig.getDto(i).getPdfPath() + pdfName;
        PdfDocument pdfDocument = null;
        try {
             pdfDocument = new PdfDocument(new PdfReader(templatePath), new PdfWriter(newPdfPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PdfAcroForm pdfAcroForm = getPdfAcroForm(pdfDocument);
        Map<String, PdfFormField> formFieldMap = pdfAcroForm.getFormFields();

        if (i == 0){
            comeBackInfo.accept(formFieldMap,cbsApprovalFormDto);
            pngList = comeBackImage(pdfDocument,formFieldMap,cbsApprovalFormDto);
        }else if (i ==1){
            suspendInfo.accept(formFieldMap,ssApprovalFormDto);
            pngList = suspendImage(pdfDocument,formFieldMap,ssApprovalFormDto);
        }

        flattenFields(pdfAcroForm);
        assert pdfDocument != null;
        close(pdfDocument);
//        deletePng(pngList);//关闭文档后,删除图片
        upload.uploadPdf(newPdfPath);//异步上传七牛云
    }



    @Async
    public void pdfBuilderComeBack(CBSApprovalFormDto cbsApprovalFormDto){
        log.info(Thread.currentThread().getName() + " 内异步方法开始" + Thread.currentThread().isAlive());
        Builder(cbsApprovalFormDto);
        log.info(Thread.currentThread().getName() + " 内异步方法结束" + Thread.currentThread().isAlive());
    }

    @Async
    public void pdfBuilderSuspend(SSApprovalFormDto ssApprovalFormDto){
        log.info(Thread.currentThread().getName() + " 内异步方法开始" + Thread.currentThread().isAlive());
        Builder(ssApprovalFormDto);
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