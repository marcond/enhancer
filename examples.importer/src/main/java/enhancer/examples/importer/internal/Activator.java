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
package enhancer.examples.importer.internal;

import static enhancer.examples.generator.aop.Logging.withLoggingFor;
import static enhancer.examples.generator.proxy.Services.service;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import enhancer.examples.exporter.goodbye.Goodbye;

public class Activator implements BundleActivator {
  public void start(BundleContext bc) throws Exception {
    /*
     * Build a proxy stack that will track a service and also dump what is going
     * on on the console. Here we have two class load bridges playing together:
     * one per generator. It appears like we stack one bridge on top of the
     * other but in fact we bridge the owner of the Goodbye class twice. The
     * reason we can't stack bridges is that the generators do not support
     * enhancement of classes. For this reason we must pass to the logging
     * generator an interface who's methods we want to log and an object that
     * implements that interface. Likewise we pass to the tracker generator the
     * interface of the service we want to track.
     */

    System.out.println("----- Build proxy stack ------");
    
    /* Wrap BundleContext in the logging aspect */
    BundleContext loggedBc = withLoggingFor(BundleContext.class, bc);
    System.out.println("Generated: " + loggedBc.getClass());
    
    /* Build a service tracker on top */
    Goodbye trackedGoodbye = service(Goodbye.class, loggedBc);
    System.out.println("Generated: " + trackedGoodbye.getClass());
    
    /* Wrap the service tracker in the logging aspect */
    Goodbye loggedTrackedGoodbye = withLoggingFor(Goodbye.class, trackedGoodbye);
    System.out.println("Generated: " + loggedTrackedGoodbye.getClass());

    /* Drive the proxy stack */
    System.out.println("----- Drive proxy stack ------");
    System.out.println(loggedTrackedGoodbye.goodbye("importer"));
  }

  public void stop(BundleContext bc) throws Exception {
  }
}
