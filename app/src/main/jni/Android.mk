LOCAL_PATH : = $(call
my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    : = payJni
LOCAL_SRC_FILES : = tools.cpp, alipay.cpp,weixin.cpp

        LOCAL_DEFAULT_CPP_EXTENSION : = cpp

#include $(BUILD_EXECUTABLE)

include $(BUILD_SHARED_LIBRARY)