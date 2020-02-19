# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#GLIDE
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-dontnote org.apache.http.**
-dontnote android.net.http.**
-dontnote com.google.protobuf.**
-dontnote com.google.android.gms.common.internal.safeparcel.SafeParcelable
-dontnote com.google.android.gms.internal.zzfiq
-dontnote com.bumptech.glide.GeneratedAppGlideModuleImpl
-dontnote com.google.android.gms.common.api.internal.LifecycleCallback
-dontnote com.google.android.youtube.player.internal.aa
