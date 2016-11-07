//
// Created by pkwsh on 2016/07/26.
//
class AliPayParam{
private:
    char* PARTNER;
    char* SELLER;
    char* RSA_PRIVATE;
    char* RSA_PUBLIC;
public:
    char* getPartner();
    char* getSeller();
    char* getRsa_private();
    char* getRsa_public();
};

