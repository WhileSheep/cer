package com.example.cer.domain.service;

import com.example.cer.domain.dto.OSApprovalFormDto;
import com.example.cer.utils.FileUtil;
import com.example.cer.utils.ImageUtil;
import com.example.cer.utils.PdfUtil;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : liji
 * @date : 2020-03-28 10:28
 */
@Slf4j
@Service
public class txspService {
    private final static float fontSize = 10.6f;
    private final static float xiaoWu = 9f;
    private redisService redisService;

    @Autowired
    public txspService(redisService redisService) {
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
        /**
         * 1代表电子签名,0代表身份证
         */
        private int isSignature;
        private String key;
        private String value;
        item(String key, String value){
            setKey(key);
            setValue(value);
        }
        item(String key, String value,int isSignature){
            setKey(key);
            setValue(value);
            setIsSignature(isSignature);
        }
    }

    txspService txspInfo(Map<String, PdfFormField> formFieldMap, OSApprovalFormDto osApprovalFormDto){
        List<item> listInfo = new ArrayList<>();
        List<item> date = new ArrayList<>();
        List<item> course = new ArrayList<>();
        listInfo.add(new item("name",osApprovalFormDto.getBasicInformation().getStudentName()));
        listInfo.add(new item("xb",osApprovalFormDto.getBasicInformation().getSex()));
        listInfo.add(new item("mz",osApprovalFormDto.getBasicInformation().getNation()));
        listInfo.add(new item("csrq",osApprovalFormDto.getBasicInformation().getBirthday()));
        listInfo.add(new item("number",osApprovalFormDto.getBasicInformation().getStudentNumber()));
        listInfo.add(new item("college",osApprovalFormDto.getBasicInformation().getCollege()));
        listInfo.add(new item("major",osApprovalFormDto.getBasicInformation().getMajor()));
        listInfo.add(new item("lyss",osApprovalFormDto.getBasicInformation().getProvinceAndCity()));
        listInfo.add(new item("rxsj",osApprovalFormDto.getBasicInformation().getEnterSchoolTime()));
        listInfo.add(new item("txsj",osApprovalFormDto.getBasicInformation().getOutSchoolTime()));
        listInfo.add(new item("nj",osApprovalFormDto.getBasicInformation().getGrade()));
        listInfo.add(new item("txsq",osApprovalFormDto.getApply().getApplyInformation()));
        listInfo.add(new item("lxdh",osApprovalFormDto.getApply().getApplicantTelephone()));
        listInfo.add(new item("gx",osApprovalFormDto.getParentOpinion().getStudentRelation()));
        listInfo.add(new item("jzlxdh",osApprovalFormDto.getParentOpinion().getParentTelephone()));
        date.add(new item("1",osApprovalFormDto.getApply().getApplyDate()));
        date.add(new item("2",osApprovalFormDto.getParentOpinion().getParentDate()));
        date.add(new item("3",osApprovalFormDto.getInstructorOpinion().getInstructorDate()));
        date.add(new item("4",osApprovalFormDto.getCollegeOpinion().getCollegeDate()));
        date.add(new item("5",osApprovalFormDto.getSchoolOpinion().getSchoolDate()));
        course.add(new item("yxnj",osApprovalFormDto.getBasicInformation().getCourse().getCgrade()));
        course.add(new item("yxxq",osApprovalFormDto.getBasicInformation().getCourse().getSemester()));
        for (item it : listInfo){
            if (check(it.getValue()))
                formFieldMap.get(it.getKey()).setFont(setFont()).setFontSize(fontSize).setValue(it.getValue());
        }
        for (item it : course){
            if (check(it.getValue()))
                formFieldMap.get(it.getKey()).setFont(setFont()).setFontSize(xiaoWu).setValue(it.getValue());
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

    void txspImage(Map<String, PdfFormField> formFieldMap, OSApprovalFormDto osApprovalFormDto) {
        List<item> listImg = new ArrayList<>();
        listImg.add(new item("sqrqm", osApprovalFormDto.getApply().getApplicantSignature(), 1));
        listImg.add(new item("jzqm", osApprovalFormDto.getParentOpinion().getParentSignature(), 1));
        listImg.add(new item("fdyqm", osApprovalFormDto.getInstructorOpinion().getInstructorSignature(), 1));
        listImg.add(new item("xyqm", osApprovalFormDto.getCollegeOpinion().getCollegeSignature(), 1));
        listImg.add(new item("xxqm", osApprovalFormDto.getSchoolOpinion().getSchoolSignature(), 1));
        listImg.add(new item("sfzzm", osApprovalFormDto.getParentOpinion().getParentIdCard_Zheng(), 0));
        listImg.add(new item("sfzfm", osApprovalFormDto.getParentOpinion().getParentIdCard_Fan(), 0));
        listImg.add(new item("brsfzzm", osApprovalFormDto.getApply().getStudentIdCard_Zheng(), 0));
        listImg.add(new item("brsfzfm", osApprovalFormDto.getApply().getStudentIdCard_Fan(), 0));
        for (item it : listImg) {
            if (check(it.getValue())) {
                if (it.getIsSignature() == 1) {
                    //生成竖版签字
                    ByteArrayOutputStream out = PdfUtil.RotationImage(PdfUtil.SvgToPng(it.getValue()));//生成横版签字
                    String base64 = FileUtil.byteToBase64(out.toByteArray());
                    redisService.set("cer:txsp:" + FileUtil.getFileNameNoEx(osApprovalFormDto.getPdfName()) + ":" + it.getKey(),base64);
                    formFieldMap.get(it.getKey()).setValue(base64);
                } else {
                    InputStream inputStream = ImageUtil.getImageStream(it.getValue());
                    String base64 = null;
                    if (inputStream != null)
                        base64 = FileUtil.getBase64FromInputStream(inputStream);
                    if (base64 != null) {
                        redisService.set("cer:txsp:" + FileUtil.getFileNameNoEx(osApprovalFormDto.getPdfName()) + ":" + it.getKey(), base64);
                        formFieldMap.get(it.getKey()).setValue(base64);
                    }
                }
            }
        }
    }
}
