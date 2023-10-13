import com.android.build.gradle.internal.api.DefaultAndroidSourceDirectorySet
import java.io.FileInputStream
import java.net.URI
import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    id("maven-publish")
    id("signing")
}

android {
    namespace = "com.xuie0000.scribble_indicator"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.material3)
}

val projectName = "ScribbleIndicator"
val projectDesc = "scribble indicator, a compose library sample"

val githubUrl = "https://github.com/xuie0000/ScribbleIndicator"

val mavenGroup = "com.xuie0000"
val mavenArtifactId = "scribble-indicator"
val mavenVersion = "1.0.0"

val developerId = "xuie0000"
val developerName = "Xu Jie"
val developerEmail = "xuie0000@163.com"

apply(plugin = "maven-publish")
apply(plugin = "signing")

val androidSourcesJar = tasks.register("androidSourcesJar", Jar::class.java) {
    archiveClassifier.set("sources")
    from(android.sourceSets["main"].java.srcDirs)
    from((android.sourceSets["main"].kotlin as DefaultAndroidSourceDirectorySet).srcDirs)
}

ext["signing.keyId"] = ""
ext["signing.password"] = ""
ext["signing.secretKeyRingFile"] = ""
ext["ossrhUsername"] = ""
ext["ossrhPassword"] = ""

val secretPropsFile = project.rootProject.file("local.properties")
if (secretPropsFile.exists()) {
    println("Found secret props file, loading props")
    val p = Properties()
    p.load(FileInputStream(secretPropsFile))
    p.entries.forEach {
        println("key:${it.key} value:${it.value}")
        ext[it.key as String] = it.value
    }
} else {
    println("No props file, loading env vars")
}

val prop = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "local.properties")))
}

// Because the components are created only during the afterEvaluate phase, you must
// configure your publications using the afterEvaluate() lifecycle method.
afterEvaluate {
    publishing {
        publications {
            // Publish the release aar artifact
            create<MavenPublication>("release") {
                // Applies the component for the release build variant.
                from(components["release"])

                // You can then customize attributes of the publication as shown below.
                groupId = mavenGroup
                artifactId = mavenArtifactId
                version = mavenVersion

                artifact(androidSourcesJar)

                // Self-explanatory metadata for the most part
                pom {
                    name = artifactId
                    description = projectDesc
                    // If your project has a dedicated site, use its URL here
                    url = githubUrl
                    licenses {
                        license {
                            // 协议类型，一般默认Apache License2.0的话不用改：
                            name = "The Apache License, Version 2.0"
                            url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                        }
                    }
                    developers {
                        developer {
                            id = developerId
                            name = developerName
                            email = developerEmail
                        }
                    }
                    // Version control info, if you're using GitHub, follow the format as seen here
                    scm {
                        connection = githubUrl
                        developerConnection = githubUrl
                        url = "$githubUrl/tree/master"
                    }
                }
            }
        }
        repositories {
            // The repository to publish to, Sonatype/MavenCentral
            maven {
                // This is an arbitrary name, you may also use "mavencentral" or
                // any other name that's descriptive for you
                name = projectName

                val releasesRepoUrl =
                    URI("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                val snapshotsRepoUrl =
                    URI("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                // You only need this if you want to publish snapshots, otherwise just set the URL
                // to the release repo directly
                url = if (version.toString().endsWith("SNAPSHOT")) {
                    snapshotsRepoUrl
                } else {
                    releasesRepoUrl
                }

                // The username and password we've fetched earlier
                credentials {
                    username = prop.getProperty("ossrhUsername")
                    password = prop.getProperty("ossrhPassword")
                }
            }
        }
    }
}

// Configure the signing plugin.
signing {
    // Use the external gpg binary instead of the built-in PGP library.
    // This lets us use gpg-agent and avoid having to hard-code our PGP key
    // password somewhere.
    //
    // Note that you will need to add this in your ~/.gradle/gradle.properties:
    // signing.gnupg.keyName=<last 8 characters of your PGP key>
    //
    // Additionally, for users who have gpg instead of gpg2:
    // signing.gnupg.useLegacyGpg=true
    useGpgCmd()

    // Since the publication itself was created in `afterEvaluate`, we must
    // do the same here.
    afterEvaluate {
        // This adds a signing stage to the publish task in-place (so we keep
        // using the same task name; it just also performs signing now).
        sign(publishing.publications["release"])
    }
}
