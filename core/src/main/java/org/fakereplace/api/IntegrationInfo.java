/*
 * Copyright 2011, Stuart Douglas
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.fakereplace.api;

import org.fakereplace.transformation.FakereplaceTransformer;

import java.util.Set;

/**
 * Integrations need to implement this service to tell the transformer
 * about what they need.
 * <p/>
 * Note: all class names should be returned in java (not JVM) format
 *
 * @author stuart
 */
public interface IntegrationInfo {

    /**
     * Integrations have a change to transform classes
     * They get to see the class before any manipulation is
     * done to it.
     * They do not get to transform reloaded classes.
     *
     * @return
     */
    public FakereplaceTransformer getTransformer();

    /**
     * returns the name of the ClassChangeAware object
     * provided by this integration.
     * <p/>
     * This object is installed into the same ClassLoader
     * that the integrations classes are in
     * <p/>
     * Note that the ClassChangeAware object should register
     * itself with the ClassChangeNotifier
     *
     * @return
     */
    public String getClassChangeAwareName();

    /**
     * If a classloader loads one of these classes it enables
     * this integration module.
     * <p/>
     * This also means that the classloader that loaded
     * the class will be intrumented to load classes from the integration.
     */
    public Set<String> getIntegrationTriggerClassNames();

    /**
     * If a classloader is registered as an instrumentation
     * classloader it will attempt to load classes from here
     * first.
     */
    public byte[] loadClass(String className);

    /**
     * get a list of classes that should be turned into tracked instances.
     *
     * @return
     */
    public Set<String> getTrackedInstanceClassNames();
}
