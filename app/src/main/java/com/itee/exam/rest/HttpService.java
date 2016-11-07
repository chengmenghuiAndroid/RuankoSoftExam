package com.itee.exam.rest;

import com.itee.exam.app.entity.AnswerSheet;
import com.itee.exam.app.entity.Courses;
import com.itee.exam.app.entity.Exam;
import com.itee.exam.app.entity.ExamHistory;
import com.itee.exam.app.entity.ExamHistoryVO;
import com.itee.exam.app.entity.ExamPaperVO;
import com.itee.exam.app.entity.ExamSimplePaper;
import com.itee.exam.app.entity.QuestionAnswer;
import com.itee.exam.app.entity.User;
import com.itee.exam.app.ui.vo.Apply;
import com.itee.exam.app.ui.vo.ApplyPlace;
import com.itee.exam.app.ui.vo.Order;
import com.itee.exam.app.ui.vo.Subject;
import com.itee.exam.core.utils.Page;
import com.itee.exam.vo.ExecuteResult;
import com.itee.exam.vo.HttpMessage;
import com.itee.exam.vo.UserAppToken;
import com.itee.exam.vo.meta.CodeType;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by xin on 2015-06-08.
 */
public interface HttpService {

    /**
     * 注册
     *
     * @param userName
     * @param phoneNum
     * @param password
     * @param email
     * @return
     */
    @POST("/mobile/registUser")
    HttpMessage<Object> registUser(@Query("userName") String userName, @Query("password") String password, @Query("phoneNum") String phoneNum, @Query("validateCode") String validateCode, @Query("email") String email);

    @POST("/mobile/login")
    HttpMessage<UserAppToken> login(@Query("userName") String userName, @Query("password") String password);


    @FormUrlEncoded
    @POST("/mobile/getUserInfo")
    HttpMessage<User> getUserInfo(@Field("userName") String userName, @Field("tokenContent") String tokenContent);

    /**
     * 获取令牌
     *
     * @param appKey
     * @return
     */
    @GET("/mobile/getUserAppToken")
    HttpMessage<UserAppToken> getUserAppToken(@Query("userName") String userName, @Query("appKey") String appKey);


    @GET("/mobile/logout.do")
    void logout(Callback<ExecuteResult> callback);

    /**
     * 获取手机验证码
     *
     * @param phoneNum
     * @param codeType
     * @return
     */
    @GET("/mobile/getSecurityCode")
    HttpMessage getSecurityCode(@Query("phoneNum") String phoneNum, @Query("codeType") CodeType codeType);

    /**
     * 通过手机找回密码
     *
     * @param phoneNum
     * @return
     */
    @GET("/mobile/findPasswordByMobile")
    HttpMessage findPasswordByMobile(@Query("phoneNum") String phoneNum);

    /**
     * 修改密码
     *
     * @param userName
     * @param validateCode
     * @param newPassword
     * @return
     */
    @POST("/mobile/updateCustomerPasswordByMobile")
    HttpMessage updateCustomerPasswordByMobile(@Query("userName") String userName, @Query("phoneNum") String phoneNum,
                                               @Query("validateCode") String validateCode, @Query("newPassword") String newPassword);

    @POST("/mobile/findExamPaper")
    HttpMessage<List<ExamSimplePaper>> findExamPaper(@Query("userName") String userName, @Query("tokenContent") String tokenContent,
                                                     @Query("keyWord") String keyWord);

    /*
     * 获取所有试卷类型
     * 返回值：List<Field> 试卷类型列表
     * */
    @POST("/mobile/getAllExamPaperType")
    HttpMessage<List<com.itee.exam.app.entity.Field>> getAllExamPaperType(@Query("userName") String userName, @Query("tokenContent") String tokenContent);

    /*
     * 获取所有试卷类型
     * 返回值：List<Field> 试卷类型列表
     * */
    @POST("/mobile/getAllExamPaperType")
    HttpMessage<List<com.itee.exam.app.entity.Field>> getAllExamPaperType();

    /**
     * 获取所有的考试列表，根据试卷类型
     *
     * @param userName
     * @param tokenContent
     * @param examPaperType FieldId
     * @return
     */
    @POST("/mobile/getAllExamByPaperField")
    HttpMessage<List<Exam>> getAllExamByPaperField(@Query("userName") String userName, @Query("tokenContent") String tokenContent, @Query("examPaperType") int examPaperType);

    /**
     * 获取所有的考试列表，根据试卷类型
     *
     * @param examPaperType FieldId
     * @return
     */
    @POST("/mobile/getAllExamByPaperField")
    HttpMessage<List<Exam>> getAllExamByPaperField(@Query("examPaperType") int examPaperType);

    /**
     * 开始模拟考试
     *
     * @param userName     用户
     * @param tokenContent 令牌
     * @param examId       试卷id
     * @return
     */
    @POST("/mobile/startMockExam")
    HttpMessage<ExamPaperVO> startMockExam(@Query("userName") String userName, @Query("tokenContent") String tokenContent, @Query("examId") int examId);

    /**
     * 提交答题卡
     *
     * @param userName
     * @param tokenContent
     * @param answerSheet
     * @return
     */
    @POST("/mobile/submitAnswerSheet")
    HttpMessage<Float> submitAnswerSheet(@Query("userName") String userName, @Query("tokenContent") String tokenContent, @Body AnswerSheet answerSheet);

    /**
     * 获取考试历史列表
     *
     * @param userId
     * @param pageModel
     * @param tokenContent
     * @param userName
     * @return
     */
    @POST("/mobile/PageExamHistory")
    HttpMessage<Page<ExamHistory>> PageExamHistory(@Query("userId") int userId, @Body Page<ExamHistory> pageModel, @Query("tokenContent") String tokenContent, @Query("userName") String userName);

    @GET("/mobile/getExamHistoryByHisId")
    HttpMessage<ExamHistoryVO> getExamHistoryByHisId(@Query("hisId") int hisId, @Query("tokenContent") String tokenContent, @Query("userName") String userName);

    @GET("/mobile/getRuankoCourseLearnUrl")
    HttpMessage getRuankoCourseLearnUrl(@Query("userName") String userName, @Query("tokenContent") String tokenContent, @Query("course_id") String course_id);

    @POST("/mobile/updateUser")
    HttpMessage updateUser(@Body User user, @Query("groupId") int groupId, @Query("tokenContent") String tokenContent);

    @POST("/mobile/DeleteExamHistory")
    HttpMessage DeleteExamHistory(@Query("hisId") int hisId, @Query("tokenContent") String tokenContent, @Query("userName") String userName);

    @POST("/mobile/findPasswordByPhoneNum")
    HttpMessage findPasswordByPhoneNum(@Query("phoneNum") String phoneNum, @Query("validateCode") String validateCode, @Query("newPassword") String newPassword);

    //获取考试科目
    @GET("/mobile/examSubjects")
    HttpMessage<List<Subject>> examSubjects();

    //报名接口
    @POST("/mobile/examRegistration")
    HttpMessage<Order> examRegistration(@Body Apply apply, @Query("validateCode") String validateCode);

    //获取报名点
    @GET("/mobile/registrationPlace")
    HttpMessage<List<ApplyPlace>> registrationPlace();

    //报名支付成功
    @POST("/mobile/paySuccess")
    HttpMessage paySuccess(@Query("orderId") int orderId, @Query("payWay") String payWay);

    //完善报名信息
    @POST("/mobile/examSupplement")
    HttpMessage examSupplement(@Body Apply apply);

    //获取报名订单信息
    @POST("/mobile/getOrderinfo")
    HttpMessage<Order> getOrderinfo(@Body User user);

    //检查报名信息是否完善
    @POST("/mobile/CheckReginfo")
    HttpMessage CheckReginfo(@Body User user);

    @Multipart
    @POST("/mobile/uploadPhoto")
    HttpMessage uploadPhoto(@Query("applyId") int applyId, @Part("file") TypedFile file);

    @Multipart
    @POST("/mobile/changeCustomerFace.do")
    ExecuteResult changeCustomerFace(@Query("customerId") int customerId, @Part("file") TypedFile file);

    @POST("/mobile/modifySubject")
    HttpMessage<Order> modifySubject(@Query("orderId") int orderId, @Query("subjectId") int subjectId);

    @GET("/mobile/listExamByFieldAndFlag")
    HttpMessage<List<Exam>> listExamByFieldAndFlag(@Query("fieldId") int fieldId, @Query("paperFlag") String paperFlag);

    @GET("/mobile/startAfternoonExam")
    HttpMessage<ExamPaperVO> startAfternoonExam(@Query("userName") String userName, @Query("tokenContent") String tokenContent, @Query("examId") int examId);

    @POST("/mobile/submitAfternoonPaper")
    HttpMessage submitAfternoonPaper(@Query("userName") String userName, @Query("tokenContent") String tokenContent, @Body AnswerSheet answerSheet);

    @GET("/mobile/getExamHistoryAfternoon")
    HttpMessage<ExamHistoryVO> getExamHistoryAfternoon(@Query("hisId") int hisId, @Query("tokenContent") String tokenContent, @Query("userName") String userName);

    //下午试题答案解析
    @FormUrlEncoded
    @POST("/mobile/getAnswerListByQuestionId")
    HttpMessage<List<QuestionAnswer>> getAnswerListByQuestionId(@Field("questionId") int questionId);

    //软考报名
    @POST("/mobile/getApplyState")
    void  getApplyState(Callback<Response> reponse);

   //学习中心
   @POST("/mobile/getUserCourseListByUserId")
   HttpMessage<List<Courses>> getUserCourseListByUserId(@Query("userId") int userId);

}