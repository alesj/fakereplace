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

package a.org.fakereplace.test.replacement.repeat;

import a.org.fakereplace.test.util.ClassReplacer;
import org.testng.annotations.Test;

public class RepeatReplacementTest {
    @Test
    public void firstReplacementTest() throws SecurityException, NoSuchFieldException, NoSuchMethodException {
        ClassReplacer r = new ClassReplacer();
        r.queueClassForReplacement(Replace.class, Replace1.class);
        r.replaceQueuedClasses();

        Replace.class.getDeclaredField("field1");
        Replace.class.getDeclaredField("sfield1");
        Replace.class.getDeclaredMethod("method1");
        Replace.class.getDeclaredMethod("smethod1");

        try {
            Replace.class.getDeclaredField("field");
            assert false;
        } catch (NoSuchFieldException e) {
        }
        try {
            Replace.class.getDeclaredField("sfield");
            assert false;
        } catch (NoSuchFieldException e) {
        }
        try {
            Replace.class.getDeclaredMethod("method");
            assert false;
        } catch (NoSuchMethodException e) {
        }
        try {
            Replace.class.getDeclaredMethod("smethod");
            assert false;
        } catch (NoSuchMethodException e) {
        }

    }

    @Test(dependsOnMethods = "firstReplacementTest")
    public void secondReplacementTest() throws SecurityException, NoSuchFieldException, NoSuchMethodException {
        ClassReplacer r = new ClassReplacer();
        r.queueClassForReplacement(Replace.class, Replace2.class);
        r.replaceQueuedClasses();

        Replace.class.getDeclaredField("field2");
        Replace.class.getDeclaredField("sfield2");
        Replace.class.getDeclaredMethod("method2");
        Replace.class.getDeclaredMethod("smethod2");

        try {
            Replace.class.getDeclaredField("field1");
            assert false;
        } catch (NoSuchFieldException e) {
        }
        try {
            Replace.class.getDeclaredField("sfield1");
            assert false;
        } catch (NoSuchFieldException e) {
        }
        try {
            Replace.class.getDeclaredMethod("method1");
            assert false;
        } catch (NoSuchMethodException e) {
        }
        try {
            Replace.class.getDeclaredMethod("smethod1");
            assert false;
        } catch (NoSuchMethodException e) {
        }
    }

    @Test(dependsOnMethods = "secondReplacementTest")
    public void thirdReplacementTest() throws SecurityException, NoSuchFieldException, NoSuchMethodException {
        ClassReplacer r = new ClassReplacer();
        r.queueClassForReplacement(Replace.class, Replace3.class);
        r.replaceQueuedClasses();

        Replace.class.getDeclaredField("field3");
        Replace.class.getDeclaredField("sfield3");
        Replace.class.getDeclaredMethod("method3");
        Replace.class.getDeclaredMethod("smethod3");

        try {
            Replace.class.getDeclaredField("field2");
            assert false;
        } catch (NoSuchFieldException e) {
        }
        try {
            Replace.class.getDeclaredField("sfield2");
            assert false;
        } catch (NoSuchFieldException e) {
        }
        try {
            Replace.class.getDeclaredMethod("method2");
            assert false;
        } catch (NoSuchMethodException e) {
        }
        try {
            Replace.class.getDeclaredMethod("smethod2");
            assert false;
        } catch (NoSuchMethodException e) {
        }
    }

    @Test(dependsOnMethods = "thirdReplacementTest")
    public void fourthReplacementTest() throws SecurityException, NoSuchFieldException, NoSuchMethodException {
        ClassReplacer r = new ClassReplacer();
        r.queueClassForReplacement(Replace.class, Replace4.class);
        r.replaceQueuedClasses();

        Replace.class.getDeclaredField("field4");
        Replace.class.getDeclaredField("sfield4");
        Replace.class.getDeclaredMethod("method4");
        Replace.class.getDeclaredMethod("smethod4");

        try {
            Replace.class.getDeclaredField("field3");
            assert false;
        } catch (NoSuchFieldException e) {
        }
        try {
            Replace.class.getDeclaredField("sfield3");
            assert false;
        } catch (NoSuchFieldException e) {
        }
        try {
            Replace.class.getDeclaredMethod("method3");
            assert false;
        } catch (NoSuchMethodException e) {
        }
        try {
            Replace.class.getDeclaredMethod("smethod3");
            assert false;
        } catch (NoSuchMethodException e) {
        }
    }

}
