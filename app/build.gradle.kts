plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

// Los workflows de GitHub pasan estos valores por variables de entorno.
// En una compilación local quedan los valores por defecto.
val versionNombre = providers.gradleProperty("yunta.versionName").get()
val versionCodigo = providers.environmentVariable("VERSION_CODE").orNull?.toInt() ?: 1
val ramaCompilacion = providers.environmentVariable("BUILD_RAMA").getOrElse("local")
val commitCompilacion = providers.environmentVariable("BUILD_COMMIT").getOrElse("local")
val archivoClaveRelease = providers.environmentVariable("KEYSTORE_FILE").orNull

android {
    namespace = "com.yunta.app"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.yunta.app"
        minSdk = 26
        targetSdk = 37
        versionCode = versionCodigo
        versionName = versionNombre

        buildConfigField("String", "BUILD_RAMA", "\"$ramaCompilacion\"")
        buildConfigField("String", "BUILD_COMMIT", "\"$commitCompilacion\"")
    }

    signingConfigs {
        // Clave de pruebas compartida: no es secreta y permite instalar "Yunta Dev"
        // tanto desde Android Studio como desde el instalador que genera GitHub.
        getByName("debug") {
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        // La clave real solo existe en GitHub (secretos) y en el equipo de quien la creó.
        if (archivoClaveRelease != null) {
            create("release") {
                storeFile = file(archivoClaveRelease)
                storePassword = providers.environmentVariable("KEYSTORE_PASSWORD").get()
                keyAlias = providers.environmentVariable("KEY_ALIAS").get()
                keyPassword = providers.environmentVariable("KEY_PASSWORD").get()
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }
        release {
            optimization {
                enable = false
            }
            signingConfig = signingConfigs.findByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    lint {
        // Estas comprobaciones de seguridad detienen la compilación en lugar de solo avisar.
        error += setOf(
            "AllowBackup",
            "HardcodedDebugMode",
            "ExportedContentProvider",
            "ExportedReceiver",
            "ExportedService",
            "SetJavaScriptEnabled",
            "TrustAllX509TrustManager",
            "BadHostnameVerifier",
            "WorldReadableFiles",
            "WorldWriteableFiles",
            "PackagedPrivateKey",
            "UnsafeDynamicallyLoadedCode",
        )
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
