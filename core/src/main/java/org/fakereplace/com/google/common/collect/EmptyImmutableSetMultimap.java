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

/**
 * Implementation of {@link ImmutableListMultimap} with no entries.
 *
 * @author Mike Ward
 */
@GwtCompatible(serializable = true)
class EmptyImmutableSetMultimap extends ImmutableSetMultimap<Object, Object> {
    static final EmptyImmutableSetMultimap INSTANCE
            = new EmptyImmutableSetMultimap();

    private EmptyImmutableSetMultimap() {
        super(ImmutableMap.<Object, ImmutableSet<Object>>of(), 0);
    }

    private Object readResolve() {
        return INSTANCE; // preserve singleton property
    }

    private static final long serialVersionUID = 0;
}
