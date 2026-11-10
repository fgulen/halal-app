import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// Ortak iş mantığı (analyzer, model, veri katmanı). Hedefler:
// - android: uygulama ve Room (P2d) için
// - jvm: birim testleri Windows'ta çalışsın diye
// - iOS: Windows'ta derlenmez (ignoreDisabledTargets); gerçek derleme CI'da (P4).
plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.kotlin.multiplatform.library)
  alias(libs.plugins.google.devtools.ksp)
  alias(libs.plugins.androidx.room)
  alias(libs.plugins.kotlinx.serialization)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.compose.multiplatform)
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
      implementation(libs.androidx.room.runtime)
      implementation(libs.ktor.client.core)
      implementation(libs.kotlinx.serialization.json)
      implementation(libs.kotlinx.coroutines.core)
      implementation(compose.runtime)
      implementation(compose.foundation)
      implementation(compose.material3)
      implementation(compose.ui)
      implementation(compose.materialIconsExtended)
      implementation(libs.kmp.lifecycle.viewmodel)
    }
    jvmTest.dependencies {
      implementation(kotlin("test"))
    }
    androidMain.dependencies {
      implementation(libs.ktor.client.okhttp)
      implementation(libs.coil.compose)
    }
    jvmMain.dependencies {
      implementation(libs.ktor.client.okhttp)
    }
    iosMain.dependencies {
      implementation(libs.ktor.client.darwin)
    }
  }
}

// Room KMP derleyicisi: her hedef için ayrı KSP yapılandırması (iOS yalnız CI'da üretilir).
dependencies {
  add("kspAndroid", libs.androidx.room.compiler)
  add("kspJvm", libs.androidx.room.compiler)
  add("kspIosArm64", libs.androidx.room.compiler)
  add("kspIosSimulatorArm64", libs.androidx.room.compiler)
}

room {
  schemaDirectory("$projectDir/schemas")
}
