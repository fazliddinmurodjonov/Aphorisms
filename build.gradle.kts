// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false // If you need this, make sure it’s not a duplicate of 'com.android.application'
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
   id("com.google.dagger.hilt.android") version "2.48" apply false
      id("com.android.library") version "8.3.1" apply false
}

