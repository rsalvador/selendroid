/*
 * Copyright 2012-2013 eBay Software Foundation and selendroid committers.
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

import static io.selendroid.android.AndroidSdk.platformExecutableSuffixExe;
import io.selendroid.exceptions.SelendroidException;

import java.io.File;

public class JavaSdk {
  public static String javaHome() {
    String javaHome = System.getenv("JAVA_HOME");

    if (javaHome == null) {
      throw new SelendroidException("Environment variable 'JAVA_HOME' was not found!");
    }
    return javaHome;
  }

  public static File jarsigner() {
    StringBuffer jarsigner = new StringBuffer();
    jarsigner.append(javaHome());
    jarsigner.append(File.separator);
    jarsigner.append("bin");
    jarsigner.append(File.separator);

    return new File(jarsigner.toString(), "jarsigner" + platformExecutableSuffixExe());
  }

  public static File keytool() {
    StringBuffer keytool = new StringBuffer();
    keytool.append(javaHome());
    keytool.append(File.separator);
    keytool.append("bin");
    keytool.append(File.separator);

    return new File(keytool.toString(), "keytool" + platformExecutableSuffixExe());
  }
}
