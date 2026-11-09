import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// Ortak iş mantığı (analyzer, model). JVM hedefi: testler ve Android uygulaması.
// iOS hedefleri: Windows'ta derlenmez (ignoreDisabledTargets), commonMain'in
// JVM dışı API kullanmadığını doğrulamak için tanımlı.
plugins {
  alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
  jvm {
    // app modülü Java 11 hedefliyor; uyumsuzluk derleme hatası vermesin diye aynı.
    compilerOptions { jvmTarget.set(JvmTarget.JVM_11) }
  }
  iosArm64()
  iosSimulatorArm64()
  // Windows native derleme kontrolü: commonMain JVM dışı API sızıntısını yakalar.
  mingwX64()
}
