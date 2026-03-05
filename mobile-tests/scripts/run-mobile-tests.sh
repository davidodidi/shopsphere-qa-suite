#!/bin/bash
set -e

echo "Waiting for emulator boot to complete..."
adb wait-for-device

until adb shell getprop sys.boot_completed 2>/dev/null | grep -q '1'; do
  echo "  still booting..."; sleep 3
done

echo "Boot completed. Waiting for package manager to be ready..."
until adb shell pm path android 2>/dev/null | grep -q 'package:'; do
  echo "  waiting for package manager..."; sleep 2
done

echo "Settling UI layer..."
sleep 5

echo "Emulator fully ready. Starting tests."
mvn test \
  -pl mobile-tests \
  -Dplatform=android \
  -Dappium.server=http://127.0.0.1:4723 \
  -Dapp.path=$GITHUB_WORKSPACE/mobile-tests/src/test/resources/apps/SauceLabs.apk \
  -Ddevice.name=Pixel_6_API_35 \
  -Dplatform.version=15 \
  -Dcucumber.filter.tags=@mobile \
  --no-transfer-progress
