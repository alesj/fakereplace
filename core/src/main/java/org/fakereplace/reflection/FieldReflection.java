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

package org.fakereplace.reflection;

import javassist.bytecode.AccessFlag;
import javassist.bytecode.Descriptor;
import org.fakereplace.boot.Constants;
import org.fakereplace.data.ClassData;
import org.fakereplace.data.ClassDataStore;
import org.fakereplace.data.FieldData;
import org.fakereplace.data.MemberType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class that handles access to re-written fields.
 *
 * @author stuart
 */
public class FieldReflection {

    public static Class<?> getDeclaringClass(Field f) {
        Class<?> c = f.getDeclaringClass();
        if (c.getName().startsWith(Constants.GENERATED_CLASS_PACKAGE)) {
            return ClassDataStore.getRealClassFromProxyName(c.getName());
        }
        return c;
    }

    public static Field[] getDeclaredFields(Class<?> clazz) {
        if (!ClassDataStore.isClassReplaced(clazz)) {
            return clazz.getDeclaredFields();
        }
        try {
            ClassData cd = ClassDataStore.getModifiedClassData(clazz.getClassLoader(), Descriptor.toJvmName(clazz.getName()));
            Field[] meth = clazz.getDeclaredFields();

            Collection<FieldData> fieldData = cd.getFields();
            List<Field> visible = new ArrayList<Field>(meth.length);
            for (int i = 0; i < meth.length; ++i) {
                for (FieldData f : fieldData) {
                    if (f.getAccessFlags() == meth[i].getModifiers() && f.getName().equals(meth[i].getName())) {
                        if (f.getMemberType() == MemberType.NORMAL) {
                            visible.add(meth[i]);
                            break;
                        }
                    }
                }
            }

            for (FieldData i : cd.getFields()) {
                if (i.getMemberType() == MemberType.FAKE) {
                    Class<?> c = clazz.getClassLoader().loadClass(i.getClassName());
                    visible.add(i.getField(c));
                }
            }

            Field[] ret = new Field[visible.size()];
            for (int i = 0; i < visible.size(); ++i) {
                ret[i] = visible.get(i);
            }

            return ret;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Field[] getFields(Class<?> clazz) {
        if (!ClassDataStore.isClassReplaced(clazz)) {
            return clazz.getFields();
        }
        try {
            ClassData cd = ClassDataStore.getModifiedClassData(clazz.getClassLoader(), Descriptor.toJvmName(clazz.getName()));

            if (cd == null) {
                return clazz.getDeclaredFields();
            }

            Field[] meth = clazz.getFields();
            Collection<FieldData> fieldData = cd.getFields();
            List<Field> visible = new ArrayList<Field>(meth.length);
            for (int i = 0; i < meth.length; ++i) {
                for (FieldData f : fieldData) {
                    if (f.getAccessFlags() == meth[i].getModifiers() && f.getName().equals(meth[i].getName())) {
                        if (f.getMemberType() == MemberType.NORMAL) {
                            visible.add(meth[i]);
                            break;
                        }
                    }
                }
            }

            ClassData cta = cd;
            while (cta != null) {
                for (FieldData i : cta.getFields()) {
                    if (i.getMemberType() == MemberType.FAKE && AccessFlag.isPublic(i.getAccessFlags())) {
                        Class<?> c = clazz.getClassLoader().loadClass(i.getClassName());
                        visible.add(i.getField(c));
                    }
                }
                cta = cta.getSuperClassInformation();
            }

            Field[] ret = new Field[visible.size()];
            for (int i = 0; i < visible.size(); ++i) {
                ret[i] = visible.get(i);
            }
            return ret;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
        if (!ClassDataStore.isClassReplaced(clazz)) {
            return clazz.getField(name);
        }
        ClassData cd = ClassDataStore.getModifiedClassData(clazz.getClassLoader(), Descriptor.toJvmName(clazz.getName()));

        if (cd == null) {
            return clazz.getField(name);
        }
        FieldData fd = cd.getField(name);
        if (fd == null) {
            return clazz.getField(name);
        }
        if (!AccessFlag.isPublic(fd.getAccessFlags())) {
            throw new NoSuchFieldException(clazz.getName() + "." + name);
        }
        switch (fd.getMemberType()) {

            case NORMAL:
                return clazz.getField(name);
            case FAKE:
                try {
                    Class<?> c = clazz.getClassLoader().loadClass(fd.getClassName());
                    return c.getField(name);

                } catch (NoSuchFieldException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
        }
        throw new NoSuchFieldException();
    }

    public static Field getDeclaredField(Class<?> clazz, String name) throws NoSuchFieldException {
        if (!ClassDataStore.isClassReplaced(clazz)) {
            return clazz.getDeclaredField(name);
        }

        ClassData cd = ClassDataStore.getModifiedClassData(clazz.getClassLoader(), Descriptor.toJvmName(clazz.getName()));

        if (cd == null) {
            return clazz.getDeclaredField(name);
        }
        FieldData fd = cd.getField(name);
        if (fd == null) {
            return clazz.getDeclaredField(name);
        }

        switch (fd.getMemberType()) {
            case NORMAL:
                return clazz.getDeclaredField(name);
            case FAKE:
                try {
                    Class<?> c = clazz.getClassLoader().loadClass(fd.getClassName());
                    return c.getDeclaredField(name);
                } catch (NoSuchFieldException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
        }
        throw new NoSuchFieldException();
    }

    public static void set(Field f, Object object, Object val) throws IllegalAccessException {
        setFakeField(f, object, val);
    }

    public static void setBoolean(Field f, Object object, boolean val) throws IllegalAccessException {
        setFakeField(f, object, val);
    }

    public static void setByte(Field f, Object object, byte val) throws IllegalAccessException {
        setFakeField(f, object, val);
    }

    public static void setChar(Field f, Object object, char val) throws IllegalAccessException {
        setFakeField(f, object, val);
    }

    public static void setDouble(Field f, Object object, double val) throws IllegalAccessException {
        setFakeField(f, object, val);
    }

    public static void setFloat(Field f, Object object, float val) throws IllegalAccessException {
        setFakeField(f, object, val);
    }

    public static void setInt(Field f, Object object, int val) throws IllegalAccessException {
        setFakeField(f, object, val);
    }

    public static void setLong(Field f, Object object, long val) throws IllegalAccessException {
        setFakeField(f, object, val);
    }

    public static void setShort(Field f, Object object, short val) throws IllegalAccessException {
        setFakeField(f, object, val);
    }

    public static Object get(Field f, Object object) throws IllegalAccessException {
        FieldAccessor accessor = ClassDataStore.getFieldAccessor(f.getDeclaringClass().getName());
        return accessor.get(object);
    }

    public static boolean getBoolean(Field f, Object object) throws IllegalAccessException {
        FieldAccessor accessor = ClassDataStore.getFieldAccessor(f.getDeclaringClass().getName());
        return (Boolean) accessor.get(object);
    }

    public static byte getByte(Field f, Object object) throws IllegalAccessException {
        FieldAccessor accessor = ClassDataStore.getFieldAccessor(f.getDeclaringClass().getName());
        return (Byte) accessor.get(object);

    }

    public static char getChar(Field f, Object object) throws IllegalAccessException {
        FieldAccessor accessor = ClassDataStore.getFieldAccessor(f.getDeclaringClass().getName());
        return (Character) accessor.get(object);

    }

    public static Double getDouble(Field f, Object object) throws IllegalAccessException {
        FieldAccessor accessor = ClassDataStore.getFieldAccessor(f.getDeclaringClass().getName());
        return (Double) accessor.get(object);
    }

    public static float getFloat(Field f, Object object) throws IllegalAccessException {
        FieldAccessor accessor = ClassDataStore.getFieldAccessor(f.getDeclaringClass().getName());
        return (Float) accessor.get(object);
    }

    public static int getInt(Field f, Object object) throws IllegalAccessException {
        FieldAccessor accessor = ClassDataStore.getFieldAccessor(f.getDeclaringClass().getName());
        return (Integer) accessor.get(object);
    }

    public static long getLong(Field f, Object object) throws IllegalAccessException {
        FieldAccessor accessor = ClassDataStore.getFieldAccessor(f.getDeclaringClass().getName());
        return (Long) accessor.get(object);
    }

    public static Object getShort(Field f, Object object) throws IllegalAccessException {
        FieldAccessor accessor = ClassDataStore.getFieldAccessor(f.getDeclaringClass().getName());
        return (Short) accessor.get(object);
    }

    /**
     * set fake field instance field values
     *
     * @param f
     * @return false if not a fake field
     */
    public static boolean setFakeField(Field f, Object object, Object val) {
        if ((f.getModifiers() & Modifier.STATIC) == 0 && f.getDeclaringClass().getName().startsWith(org.fakereplace.boot.Constants.GENERATED_CLASS_PACKAGE)) {
            FieldAccessor accessor = ClassDataStore.getFieldAccessor(f.getDeclaringClass().getName());
            accessor.set(object, val);
            return true;
        }
        return false;
    }

    public static boolean isFakeField(Field f) {
        if ((f.getModifiers() & Modifier.STATIC) == 0 && f.getDeclaringClass().getName().startsWith(org.fakereplace.boot.Constants.GENERATED_CLASS_PACKAGE)) {
            return true;
        }
        return false;
    }

}
