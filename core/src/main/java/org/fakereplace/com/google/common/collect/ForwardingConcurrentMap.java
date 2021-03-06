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

import java.util.concurrent.ConcurrentMap;

/**
 * A concurrent map which forwards all its method calls to another concurrent
 * map. Subclasses should override one or more methods to modify the behavior of
 * the backing map as desired per the <a
 * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 *
 * @author Charles Fry
 * @see ForwardingObject
 */
@GwtCompatible
public abstract class ForwardingConcurrentMap<K, V> extends ForwardingMap<K, V>
        implements ConcurrentMap<K, V> {

    @Override
    protected abstract ConcurrentMap<K, V> delegate();

    public V putIfAbsent(K key, V value) {
        return delegate().putIfAbsent(key, value);
    }

    public boolean remove(Object key, Object value) {
        return delegate().remove(key, value);
    }

    public V replace(K key, V value) {
        return delegate().replace(key, value);
    }

    public boolean replace(K key, V oldValue, V newValue) {
        return delegate().replace(key, oldValue, newValue);
    }

}
