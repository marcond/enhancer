/**
 * Copyright (C) 2009 Todor Boev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package enhancer.examples.generator.aop;

import enhancer.Enhancer;
import enhancer.Namers;

/**
 * Generates primitive logging wrappers over a Java interface.
 * 
 * @author rinsvind@gmail.com (Todor Boev)
 */
public final class Logging {
  /* Static utility */
  private Logging() {
  }

  /**
   * Build an Enhancer on top of the current class space. E.g. the class space
   * of the bundle where this class is packaged. The created proxy classes will
   * have a suffix "$__logging__".
   */
  private static final Enhancer enhancer = new Enhancer(
      Logging.class.getClassLoader(), 
      Namers.suffixNamer("__logging__"), 
      new LoggingGenerator());

  public static <T> T withLogging(Class<T> iface, T target) {
    try {
      Class<T> wrapperClass = enhancer.enhance(iface);
      return (T) wrapperClass.getConstructor(iface).newInstance(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}