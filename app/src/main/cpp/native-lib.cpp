#include <jni.h>
#include <string>
#include <cstdio>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_musicapp_NativeUtils_formatTimeJNI(
        JNIEnv *env,
        jclass ,
        jint milliseconds) {
    int minutes = (milliseconds / 1000) / 60;
    int seconds = (milliseconds / 1000) % 60;

    char formattedTime[6];
    std::snprintf(formattedTime, sizeof(formattedTime), "%02d:%02d", minutes, seconds);

    return env->NewStringUTF(formattedTime);
}
