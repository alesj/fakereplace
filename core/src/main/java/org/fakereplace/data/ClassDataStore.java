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

package org.fakereplace.data;

import org.fakereplace.BuiltinClassData;
import org.fakereplace.com.google.common.collect.MapMaker;
import org.fakereplace.manip.util.MapFunction;
import org.fakereplace.reflection.FieldAccessor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassDataStore {

    static final Map<String, Class<?>> proxyNameToReplacedClass = new ConcurrentHashMap<String, Class<?>>();
    private static final Map<String, FieldAccessor> proxyNameToFieldAccessor = new ConcurrentHashMap<String, FieldAccessor>();

    private static final Map<ClassLoader, Map<String, ClassData>> classData = new MapMaker().weakKeys().makeComputingMap(new MapFunction<ClassLoader, String, ClassData>(false));

    private static final Map<ClassLoader, Map<String, BaseClassData>> baseClassData = new MapMaker().weakKeys().makeComputingMap(new MapFunction<ClassLoader, String, BaseClassData>(false));

    private static final Map<String, MethodData> proxyNameToMethodData = new ConcurrentHashMap<String, MethodData>();

    private static final Map<Class<?>, Object> replacedClasses = new MapMaker().weakKeys().makeMap();

    /**
     * takes the place of the null key on ConcurrentHashMap
     */
    private static ClassLoader nullLoader = new ClassLoader() {
    };

    public static void markClassReplaced(Class<?> clazz) {
        replacedClasses.put(clazz, nullLoader);
    }

    public static boolean isClassReplaced(Class<?> clazz) {
        return replacedClasses.containsKey(clazz);
    }

    public static void saveClassData(ClassLoader loader, String className, ClassDataBuilder data) {
        className = className.replace('/', '.');
        if (loader == null) {
            loader = nullLoader;
        }
        Map<String, ClassData> map = classData.get(loader);
        map.put(className, data.buildClassData());
    }

    public static void saveClassData(ClassLoader loader, String className, BaseClassData data) {
        className = className.replace('/', '.');
        if (loader == null) {
            loader = nullLoader;
        }
        Map<String, BaseClassData> map = baseClassData.get(loader);
        map.put(className, data);
    }

    public static ClassData getModifiedClassData(ClassLoader loader, String className) {
        className = className.replace('/', '.');
        if (loader == null) {
            loader = nullLoader;
        }
        Map<String, ClassData> map = classData.get(loader);
        ClassData cd = map.get(className);
        if (cd == null) {
            BaseClassData dd = getBaseClassData(loader, className);
            if (dd == null) {
                return null;
            }
            ClassDataBuilder builder = new ClassDataBuilder(dd);
            ClassData d = builder.buildClassData();
            map.put(className, d);
            return d;
        }

        return cd;
    }

    public static BaseClassData getBaseClassData(ClassLoader loader, String className) {
        className = className.replace('/', '.');
        if (loader == null) {
            loader = nullLoader;
        }
        Map<String, BaseClassData> map = baseClassData.get(loader);
        if (!map.containsKey(className)) {
            // if this is a class that is not being instrumented it is safe to
            // load the class and get the data
            if (BuiltinClassData.skipInstrumentation(className)) {
                try {
                    if (loader != nullLoader) {
                        Class<?> cls = loader.loadClass(className);
                        saveClassData(loader, className, new BaseClassData(cls));
                    } else {
                        Class<?> cls = Class.forName(className);
                        saveClassData(loader, className, new BaseClassData(cls));
                    }
                } catch (ClassNotFoundException e) {
                    return null;
                }
            } else {
                return null;
            }
        }

        BaseClassData cd = map.get(className);
        return cd;
    }

    public static Class<?> getRealClassFromProxyName(String proxyName) {
        return proxyNameToReplacedClass.get(proxyName);
    }

    public static void registerProxyName(Class<?> c, String proxyName) {
        proxyNameToReplacedClass.put(proxyName, c);
    }

    public static void registerFieldAccessor(String proxyName, FieldAccessor accessor) {
        proxyNameToFieldAccessor.put(proxyName, accessor);
    }

    public static void registerReplacedMethod(String proxyName, MethodData methodData) {
        proxyNameToMethodData.put(proxyName, methodData);
    }

    public static MethodData getMethodInformation(String proxyName) {
        return proxyNameToMethodData.get(proxyName);
    }

    public static FieldAccessor getFieldAccessor(String proxyName) {
        return proxyNameToFieldAccessor.get(proxyName);
    }

}
