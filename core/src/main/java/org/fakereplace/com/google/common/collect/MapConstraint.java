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

package org.fakereplace.com.google.common.collect;

import org.fakereplace.com.google.common.annotations.GwtCompatible;


@GwtCompatible
interface MapConstraint<K, V> {
    /**
     * Throws a suitable {@code RuntimeException} if the specified key or value is
     * illegal. Typically this is either a {@link NullPointerException}, an
     * {@link IllegalArgumentException}, or a {@link ClassCastException}, though
     * an application-specific exception class may be used if appropriate.
     */
    void checkKeyValue(K key, V value);

    /**
     * Returns a brief human readable description of this constraint, such as
     * "Not null".
     */
    String toString();
}
