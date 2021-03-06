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

package org.fakereplace.manip.util;

import org.fakereplace.com.google.common.base.Function;
import org.fakereplace.com.google.common.collect.MapMaker;

import java.util.Map;

public class MapFunction<F, K, V> implements Function<F, Map<K, V>> {
    final boolean weakKeys;

    public MapFunction(boolean weakKeys) {
        this.weakKeys = weakKeys;
    }

    public Map<K, V> apply(F from) {
        if (weakKeys) {
            return new MapMaker().weakKeys().makeMap();
        } else {
            return new MapMaker().makeMap();
        }
    }

}
