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

package org.fakereplace.integration.metawidget;

import org.fakereplace.api.IntegrationInfo;
import org.fakereplace.transformation.FakereplaceTransformer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MetawidgetIntegrationInfo implements IntegrationInfo {

    public final static String BASE_PROPERTY_STYLE = "org.metawidget.inspector.impl.propertystyle.BasePropertyStyle";
    public final static String BASE_ACTION_STYLE = "org.metawidget.inspector.impl.actionstyle.BaseActionStyle";


    private static final Set<String> classNames;

    static {
        Set<String> ret = new HashSet<String>();
        ret.add(BASE_ACTION_STYLE);
        ret.add(BASE_PROPERTY_STYLE);
        classNames = Collections.unmodifiableSet(ret);
    }

    public String getClassChangeAwareName() {
        return "org.fakereplace.integration.metawidget.ClassRedefinitionPlugin";
    }

    public Set<String> getIntegrationTriggerClassNames() {
        return classNames;
    }

    public Set<String> getTrackedInstanceClassNames() {
        return classNames;
    }

    public FakereplaceTransformer getTransformer() {
        return null;
    }

    public byte[] loadClass(String className) {
        return null;
    }

}
