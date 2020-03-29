package com.example.cer.domain.service;

import com.example.cer.domain.dto.CBSApprovalFormDto;
import com.example.cer.utils.FileUtil;
import com.example.cer.utils.PdfUtil;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : liji
 * @date : 2020-03-28 13:11
 */
@Slf4j
@Service
public class fxspService {
    private final static float fontSize = 10.6f;
    private redisService redisService;

    public fxspService(redisService redisService) {
        this.redisService = redisService;
    }

    private static boolean check(String item){
        return !"".equals(item) && item != null;
    }
    private static PdfFont setFont(){
        PdfFont font = null;
        try {
            font = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H",true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return font;
    }

    @Data
    static class item{
        private String key;
        private String value;
        item(String key, String value){
            setKey(key);
            setValue(value);
        }
    }

    fxspService fxspInfo(Map<String, PdfFormField> formFieldMap, CBSApprovalFormDto cbsApprovalFormDto){
        List<item> listInfo = new ArrayList<>();
        List<item> date = new ArrayList<>();
        listInfo.add(new item("name",cbsApprovalFormDto.getBasicInformation().getStudentName()));
        listInfo.add(new item("xb",cbsApprovalFormDto.getBasicInformation().getSex()));
        listInfo.add(new item("mz",cbsApprovalFormDto.getBasicInformation().getNation()));
        listInfo.add(new item("csrq",cbsApprovalFormDto.getBasicInformation().getBirthday()));
        listInfo.add(new item("number",cbsApprovalFormDto.getBasicInformation().getStudentNumber()));
        listInfo.add(new item("college",cbsApprovalFormDto.getBasicInformation().getCollege()));
        listInfo.add(new item("major",cbsApprovalFormDto.getBasicInformation().getMajor()));
        listInfo.add(new item("rxsj",cbsApprovalFormDto.getBasicInformation().getEnterSchoolTime()));
        listInfo.add(new item("xxsj",cbsApprovalFormDto.getBasicInformation().getSuspendSchoolTime()));
        listInfo.add(new item("fxsj",cbsApprovalFormDto.getBasicInformation().getComeBackSchoolTime()));
        listInfo.add(new item("fxnj",cbsApprovalFormDto.getBasicInformation().getComeBackSchoolGrade()));
        listInfo.add(new item("lyss",cbsApprovalFormDto.getBasicInformation().getProvinceAndCity()));
        listInfo.add(new item("fxsq",cbsApprovalFormDto.getApply().getApplyInformation()));
        listInfo.add(new item("lxdh",cbsApprovalFormDto.getApply().getApplicantTelephone()));
        listInfo.add(new item("jzlxdh",cbsApprovalFormDto.getParentOpinion().getParentTelephone()));
        date.add(new item("1",cbsApprovalFormDto.getApply().getApplyDate()));
        date.add(new item("2",cbsApprovalFormDto.getParentOpinion().getParentDate()));
        date.add(new item("3",cbsApprovalFormDto.getInstructorOpinion().getInstructorDate()));
        date.add(new item("4",cbsApprovalFormDto.getCollegeOpinion().getCollegeDate()));
        date.add(new item("5",cbsApprovalFormDto.getSchoolOpinion().getSchoolDate()));
        for (item it : listInfo){
            if (check(it.getValue()))
                formFieldMap.get(it.getKey()).setFont(setFont()).setFontSize(fontSize).setValue(it.getValue());
        }
        for (item it : date){
            if (check(it.getValue())){
                String[] a = it.getValue().split("-");
                formFieldMap.get("year" + it.getKey()).setFont(setFont()).setFontSize(fontSize).setValue(a[0]);
                formFieldMap.get("month" + it.getKey()).setFont(setFont()).setFontSize(fontSize).setValue(a[1]);
                formFieldMap.get("day" + it.getKey()).setFont(setFont()).setFontSize(fontSize).setValue(a[2]);
            }
        }
        return this;
    }

    void fxspImage(Map<String, PdfFormField> formFieldMap, CBSApprovalFormDto cbsApprovalFormDto) {
        List<item> listImg = new ArrayList<>();
        listImg.add(new item("sqrqm", cbsApprovalFormDto.getApply().getApplicantSignature()));
        listImg.add(new item("jzqm", cbsApprovalFormDto.getParentOpinion().getParentSignature()));
        listImg.add(new item("fdyqm", cbsApprovalFormDto.getInstructorOpinion().getInstructorSignature()));
        listImg.add(new item("xyqm", cbsApprovalFormDto.getCollegeOpinion().getCollegeSignature()));
        listImg.add(new item("xxqm", cbsApprovalFormDto.getSchoolOpinion().getSchoolSignature()));
        for (item it : listImg) {
            if (check(it.getValue())) {
                //生成竖版签字
                ByteArrayOutputStream out = PdfUtil.RotationImage(PdfUtil.SvgToPng(it.getValue()));//生成横版签字
                String base64 = FileUtil.byteToBase64(out.toByteArray());
                redisService.set("cer:fxsp:" + FileUtil.getFileNameNoEx(cbsApprovalFormDto.getPdfName()) + ":" + it.getKey(),base64);
                formFieldMap.get(it.getKey()).setValue(base64);
            }
        }
    }
}
