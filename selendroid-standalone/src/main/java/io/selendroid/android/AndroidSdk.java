/*
 * Copyright 2013 selendroid committers.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.selendroid.android;

import io.selendroid.exceptions.AndroidSdkException;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.selendroid.exceptions.SelendroidException;

public class AndroidSdk {
  public static final String ANDROID_FOLDER_PREFIX = "android-";
  public static final String ANDROID_HOME = "ANDROID_HOME";

  public static String adb() {
    StringBuffer adbCommand = new StringBuffer();
    adbCommand.append(platformToolsHome());
    adbCommand.append("adb");
    adbCommand.append(platformExecutableSuffix());
    return adbCommand.toString();
  }

  public static String aapt() throws AndroidSdkException {
    StringBuffer command = new StringBuffer();
    command.append("aapt");
    command.append(platformExecutableSuffix());
    File platformToolsAapt = new File(platformToolsHome() + command.toString());
    if (platformToolsAapt.exists()) {
      return platformToolsAapt.getAbsolutePath();
    }
    File buildToolsAapt = new File(buildToolsHome() + command.toString());
    if (buildToolsAapt.exists()) {
      return buildToolsAapt.getAbsolutePath();
    }

    throw new AndroidSdkException(
        "Command 'aapt' was not found inside the Android SDK. Please update to the latest development tools and try again.");
  }

  public static String android() {
    StringBuffer command = new StringBuffer();
    command.append(toolsHome());
    command.append("android");
    command.append(platformExecutableSuffix());
    return command.toString();
  }

  public static String emulator() {
    StringBuffer command = new StringBuffer();
    command.append(toolsHome());
    command.append("emulator");
    command.append(platformExecutableSuffix());
    return command.toString();
  }

  private static String toolsHome() {
    StringBuffer command = new StringBuffer();
    command.append(androidHome());
    command.append(File.separator);
    command.append("tools");
    command.append(File.separator);
    return command.toString();
  }

  private static String buildToolsHome() {
    StringBuffer command = new StringBuffer();
    command.append(androidHome());
    command.append(File.separator);
    command.append("build-tools");
    command.append(File.separator);
    command.append("17.0.0");
    command.append(File.separator);

    return command.toString();
  }

  private static String platformToolsHome() {
    StringBuffer command = new StringBuffer();
    command.append(androidHome());
    command.append(File.separator);
    command.append("platform-tools");
    command.append(File.separator);
    return command.toString();
  }

  public static String androidHome() {
    String androidHome = System.getenv(ANDROID_HOME);

    if (androidHome == null) {
      throw new SelendroidException("Environment variable '" + ANDROID_HOME + "' was not found!");
    }
    return androidHome;
  }

  /* package */static String platformExecutableSuffix() {
    boolean isWindows = System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
    return isWindows ? ".exe" : "";
  }

  /**
   * @return path to android.jar of latest android api.
   */
  public static String androidJar() {
    String platformsRootFolder = androidHome() + File.separator + "platforms";
    File platformsFolder = new File(platformsRootFolder);

    File[] androidApis = platformsFolder.listFiles(new AndroidFileFilter());
    if (androidApis == null || androidApis.length == 0) {
      throw new SelendroidException("No installed Android APIs have been found.");
    }
    List<File> folders = Arrays.asList(androidApis);
    Collections.sort(folders, new AndroidVersionComoparator());

    String apiLevel = folders.get(folders.size() - 1).getAbsolutePath() + "/android.jar";

    return apiLevel;
  }

  public static class AndroidVersionComoparator implements Comparator<File> {
    public int compare(File object1, File object2) {
      if (isAndroidFolder(object1, object2)) {
        return versionNumber(object1).compareTo(versionNumber(object2));
      }
      return object1.compareTo(object2);
    }

    private Integer versionNumber(File file) {
      return Integer.parseInt(file.getName().split("-")[1]);
    }

    private boolean isAndroidFolder(File object1, File object2) {
      return AndroidFileFilter.isAndroidSdkFolder(object1)
          && AndroidFileFilter.isAndroidSdkFolder(object2);
    }
  }
  public static class AndroidFileFilter implements FileFilter {
    @Override
    public boolean accept(File pathname) {
      return isAndroidSdkFolder(pathname);
    }

    public static boolean isAndroidSdkFolder(File file) {
      return file.getName().startsWith(ANDROID_FOLDER_PREFIX);
    }
  }
}