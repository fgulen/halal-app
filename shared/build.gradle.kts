import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// Ortak iş mantığı (analyzer, model, veri katmanı). Hedefler:
// - android: uygulama ve Room (P2d) için
// - jvm: birim testleri Windows'ta çalışsın diye
// - iOS: Windows'ta derlenmez (ignoreDisabledTargets); gerçek derleme CI'da (P4).
plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.kotlin.multiplatform.library)
}

kotlin {
  androidLibrary {
    namespace = "com.example.shared"
    // Uygulamayla aynı SDK (app: compileSdk 36.1, minSdk 24). Template'in 37'si kurulu değil.
    compileSdk { version = release(36) { minorApiLevel = 1 } }
    minSdk = 24
    compilerOptions { jvmTarget.set(JvmTarget.JVM_11) }
  }
  jvm {
    compilerOptions { jvmTarget.set(JvmTarget.JVM_11) }
  }
  iosArm64()
  iosSimulatorArm64()

  // Hedefler tanımlandıktan SONRA source set'lere dokunulmalı (jvmTest yapılandırma çakışması).
  sourceSets {
    commonMain.dependencies {
      implementation(libs.androidx.datastore.preferences.core)
      implementation(libs.kotlinx.coroutines.core)
    }
    jvmTest.dependencies {
      implementation(kotlin("test"))
    }
  }
}
