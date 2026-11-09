import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// Ortak iş mantığı (analyzer, model). P1: yalnız JVM hedefi; Android uygulaması
// bunu jar gibi kullanır. iOS hedefleri P4'te eklenir.
plugins {
  alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
  jvm {
    // app modülü Java 11 hedefliyor; uyumsuzluk derleme hatası vermesin diye aynı.
    compilerOptions { jvmTarget.set(JvmTarget.JVM_11) }
  }
}
