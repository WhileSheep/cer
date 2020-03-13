package com.example.cer.filter;


import com.alibaba.dubbo.common.utils.IOUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.cer.domain.dto.CBSApprovalFormDto;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.Date;
import java.util.List;

@Slf4j
@WebFilter(filterName = "dataFilter",urlPatterns = "/apply/comeBackSchool")
public class dataFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("initialize filter");
    }
    /**
     *  正常向下执行即访问/apply/comeBackSchool
     *  异常则将请求跳转到/Exception/NullPointer
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        BufferedReader bufferedReader = request.getReader();
        String bodyStr = IOUtils.read(bufferedReader);
        JSONArray jsonArray = JSONArray.parseArray(bodyStr);
        List<CBSApprovalFormDto> cbsApprovalFormDtos = JSONObject.parseArray(jsonArray.toJSONString(),CBSApprovalFormDto.class);
        if (cbsApprovalFormDtos.size() == 0){
            servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
        }
        boolean flag = false;
        int i = 0;
        for (CBSApprovalFormDto cbsApprovalFormDto : cbsApprovalFormDtos) {
            if (cbsApprovalFormDto.getPdfName() == null){
                flag = true;
                request.setAttribute("data","pdfName[" + i + "]");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - pdfName[\"" + i + "\"] - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getBasicInformation() == null){
                flag = true;
                request.setAttribute("data","basicInformation[" + i + "]");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - basicInformation[\" + i + \"] - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getApply() == null){
                flag = true;
                request.setAttribute("data","apply[" + i + "]");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - apply[\" + i + \"] - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getParentOpinion() == null){
                flag = true;
                request.setAttribute("data","parentOpinion[" + i + "]");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - parentOpinion[\" + i + \"] - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getInstructorOpinion() == null){
                flag = true;
                request.setAttribute("data","instructorOpinion[" + i + "]");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - instructorOpinion[\" + i + \"] - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getCollegeOpinion() == null){
                flag = true;
                request.setAttribute("data","collegeOpinion[" + i + "]");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - collegeOpinion[\" + i + \"] - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getSchoolOpinion() == null){
                flag = true;
                request.setAttribute("data","schoolOpinion[" + i + "]");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - schoolOpinion[\" + i + \"] - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getBasicInformation().getStudentName() == null){
                flag = true;
                request.setAttribute("data","basicInformation[" + i + "].studentName");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - basicInformation[\" + i + \"].studentName - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getBasicInformation().getSex() == null){
                flag = true;
                request.setAttribute("data","basicInformation[" + i + "].sex");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - basicInformation[\" + i + \"].sex - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getBasicInformation().getNation() == null){
                flag = true;
                request.setAttribute("data","basicInformation[" + i + "].nation");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - basicInformation[\" + i + \"].nation - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getBasicInformation().getBirthday() == null){
                flag = true;
                request.setAttribute("data","basicInformation[" + i + "].birthday");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - basicInformation[\" + i + \"].birthday - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getBasicInformation().getStudentNumber() == null){
                flag = true;
                request.setAttribute("data","basicInformation[" + i + "].studentNumber");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - basicInformation[\" + i + \"].studentNumber - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getBasicInformation().getCollege() == null){
                flag = true;
                request.setAttribute("data","basicInformation[" + i + "].college");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - basicInformation[\" + i + \"].college - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getBasicInformation().getMajor() == null){
                flag = true;
                request.setAttribute("data","basicInformation[" + i + "].major");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - basicInformation[\" + i + \"].major - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getBasicInformation().getEnterSchoolTime() == null){
                flag = true;
                request.setAttribute("data","basicInformation[" + i + "].enterSchoolTime");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - basicInformation[\" + i + \"].enterSchoolTime - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getBasicInformation().getSuspendSchoolTime() == null){
                flag = true;
                request.setAttribute("data","basicInformation[" + i + "].suspendSchoolTime");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - basicInformation[\" + i + \"].suspendSchoolTime - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getBasicInformation().getComeBackSchoolTime() == null){
                flag = true;
                request.setAttribute("data","basicInformation[" + i + "].comeBackSchoolTime");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - basicInformation[\" + i + \"].comeBackSchoolTime - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getBasicInformation().getComeBackSchoolGrade() == null){
                flag = true;
                request.setAttribute("data","basicInformation[" + i + "].comeBackSchoolGrade");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - basicInformation[\" + i + \"].comeBackSchoolGrade - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getBasicInformation().getProvinceAndCity() == null){
                flag = true;
                request.setAttribute("data","basicInformation[" + i + "].provinceAndCity");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - basicInformation[\" + i + \"].provinceAndCity - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getApply().getApplyInformation() == null){
                flag = true;
                request.setAttribute("data","apply[" + i + "].applyInformation");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - apply[\" + i + \"].applyInformation - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getApply().getApplicantTelephone() == null){
                flag = true;
                request.setAttribute("data","apply[" + i + "].applicantTelephone");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - apply[\" + i + \"].applicantTelephone - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getApply().getApplicantSignature() == null){
                flag = true;
                request.setAttribute("data","apply[" + i + "].applicantSignature");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - apply[\" + i + \"].applicantSignature - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getApply().getApplyDate() == null){
                flag = true;
                request.setAttribute("data","apply[" + i + "].applyDate");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - apply[\" + i + \"].applyDate - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getParentOpinion().getParentTelephone() == null){
                flag = true;
                request.setAttribute("data","parentOpinion[" + i + "].parentTelephone");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - parentOpinion[\" + i + \"].parentTelephone - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getParentOpinion().getParentSignature() == null){
                flag = true;
                request.setAttribute("data","parentOpinion[" + i + "].parentSignature");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - parentOpinion[\" + i + \"].parentSignature - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getParentOpinion().getParentDate() == null){
                flag = true;
                request.setAttribute("data","parentOpinion[" + i + "].parentDate");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - parentOpinion[\" + i + \"].parentDate - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getInstructorOpinion().getInstructorSignature() == null){
                flag = true;
                request.setAttribute("data","instructorOpinion[" + i + "].instructorSignature");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - instructorOpinion[\" + i + \"].instructorSignature - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getInstructorOpinion().getInstructorDate() == null){
                flag = true;
                request.setAttribute("data","instructorOpinion[" + i + "].instructorDate");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - instructorOpinion[\" + i + \"].instructorDate - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getCollegeOpinion().getCollegeSignature() == null){
                flag = true;
                request.setAttribute("data","collegeOpinion[" + i + "].collegeSignature");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - collegeOpinion[\" + i + \"].collegeSignature - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getCollegeOpinion().getCollegeDate() == null){
                flag = true;
                request.setAttribute("data","collegeOpinion[" + i + "].collegeDate");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - collegeOpinion[\" + i + \"].collegeDate - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getSchoolOpinion().getSchoolSignature() == null){
                flag = true;
                request.setAttribute("data","schoolOpinion[" + i + "].schoolSignature");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - schoolOpinion[\" + i + \"].schoolSignature - 这个请求已经被拦截");
                break;
            }else if (cbsApprovalFormDto.getSchoolOpinion().getSchoolDate() == null){
                flag = true;
                request.setAttribute("data","schoolOpinion[" + i + "].schoolDate");
                servletRequest.getRequestDispatcher("/Exception/NullPointer").forward(servletRequest, servletResponse);
                log.warn(new Date(System.currentTimeMillis()) + " - " +  request.getRequestURI() +  " - schoolOpinion[\" + i + \"].schoolDate - 这个请求已经被拦截");
                break;
            }
            i++;
        }
        if (!flag){
            filterChain.doFilter(servletRequest, servletResponse);//放行
        }
    }

    /**
     * 一个流不能读两次异常，这种异常一般出现在框架或者拦截器中读取了request中的流的数据
     * 我们在业务代码中再次读取（如@RequestBody），由于流中的数据已经没了，所以第二次读取的时候就会抛出异常。
     * 解决方案：定义一个过滤器将流中的数据读取到一个数组中，并重写getInputStream()和getRead()方法
     * 后续获取流中的数据的时候，直接去数组中读取，代码实现如下：
     */
    static class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private final byte[] body;

        BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            body = toByteArray(request.getInputStream());
        }

        private byte[] toByteArray(InputStream in) throws IOException {

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int n = 0;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }


        @Override
        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream bais = new ByteArrayInputStream(body);
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                }

                @Override
                public int read() throws IOException {
                    return bais.read();
                }
            };
        }
    }

    @Override
    public void destroy() {
        log.info("destroy filter");
    }
}
