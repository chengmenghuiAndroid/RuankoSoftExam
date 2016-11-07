//
// Created by pkwsh on 2016/07/26.
//
#include "tools.h"
#include "alipay.h"
#include "weixin.h"

JNIEXPORT jstring JNICALL Java_com_itee_exam_tools_PayTools_getAlipayParam(JNIEnv *env, jobject obj, jobject pay) {
    AliPayParam *alipay = new AliPayParam();
    const char *partner = alipay->getPartner();
    const char *seller = alipay->getSeller();
    const char *rsa_private = alipay->getRsa_private();
    const char *rsa_public = alipay->getRsa_public();
    jclass clazz = env->GetObjectClass(pay);;
    jfieldID partnerId = env->GetFieldID(clazz, "PARTNER", "Ljava/lang/String;");
    jfieldID sellerId = env->GetFieldID(clazz, "SELLER", "Ljava/lang/String;");
    jfieldID rsa_pvId = env->GetFieldID(clazz, "RSA_PRIVATE", "Ljava/lang/String;");
    jfieldID rsa_pbId = env->GetFieldID(clazz, "RSA_PUBLIC", "Ljava/lang/String;");
    env->SetObjectField(pay, partnerId, env->NewStringUTF(partner));
    env->SetObjectField(pay, sellerId, env->NewStringUTF(seller));
    env->SetObjectField(pay, rsa_pvId, env->NewStringUTF(rsa_private));
    env->SetObjectField(pay, rsa_pbId, env->NewStringUTF(rsa_public));
    return env->NewStringUTF("SUCCESS");
}

JNIEXPORT jstring JNICALL Java_com_itee_exam_tools_PayTools_getWeixinParam(JNIEnv *env, jobject obj, jobject pay) {
    WXPayParam *weixin = new WXPayParam();
    const char *appId = weixin->getWxAppId();
    const char *mchId = weixin->getWxMchId();
    const char *apiKey = weixin->getWxApiKey();
    jclass clazz = env->GetObjectClass(pay);
    jfieldID wxAppId = env->GetFieldID(clazz, "WEIXIN_APP_ID", "Ljava/lang/String;");
    jfieldID wxMchId = env->GetFieldID(clazz, "WEIXIN_MCH_ID", "Ljava/lang/String;");
    jfieldID wxApiKey = env->GetFieldID(clazz, "WEIXIN_API_KEY", "Ljava/lang/String;");
    env->SetObjectField(pay, wxAppId, env->NewStringUTF(appId));
    env->SetObjectField(pay, wxMchId, env->NewStringUTF(mchId));
    env->SetObjectField(pay, wxApiKey, env->NewStringUTF(apiKey));
    return env->NewStringUTF("SUCCESS");
}
