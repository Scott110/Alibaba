#!/bin/sh
#运行的shell脚本

./gradlew clean

./gradlew assembleRelease

fir publish ./app/build/outputs/apk/app-release.apk -T e0d51b156f441ee1c8f6860179cc8a28 -c $(cat changelogs/changelog.txt)
