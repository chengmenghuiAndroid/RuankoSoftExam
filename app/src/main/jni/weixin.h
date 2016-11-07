//
// Created by pkwsh on 2016-07-28.
//
class WXPayParam{
private:
    char* wxAppId;
    char* wxMchId;
    char* wxApiKey;
    char* wxAppSecret;
public:
    char* getWxAppId();
    char* getWxMchId();
    char* getWxApiKey();
    char* getWxAppSecret();
};
