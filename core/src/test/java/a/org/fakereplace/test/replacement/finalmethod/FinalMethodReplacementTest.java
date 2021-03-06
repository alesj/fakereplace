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

package a.org.fakereplace.test.replacement.finalmethod;

import a.org.fakereplace.test.util.ClassReplacer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class FinalMethodReplacementTest {

    @BeforeClass
    public void setup() {
        ClassReplacer cr = new ClassReplacer();
        cr.queueClassForReplacement(FinalMethodClass.class, FinalMethodClass1.class);
        cr.replaceQueuedClasses();
    }

    @Test(enabled = false)
    public void testNonFinalMethodIsNonFinal() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        FinalMethodClass cl = new FinalMethodClass();
        Method method = cl.getClass().getMethod("finalMethod-replaced");
        assert Modifier.isFinal(method.getModifiers());
        assert method.invoke(cl).equals("finalMethod-replaced");
    }

    @Test(enabled = false)
    public void testFinalMethodIsFinal() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        FinalMethodClass cl = new FinalMethodClass();
        Method method = cl.getClass().getMethod("nonFinalMethod-replaced");
        assert !Modifier.isFinal(method.getModifiers());
        assert method.invoke(cl).equals("nonFinalMethod-replaced");
    }
}
