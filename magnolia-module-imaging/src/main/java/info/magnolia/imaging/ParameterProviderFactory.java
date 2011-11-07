/**
 * This file Copyright (c) 2009-2011 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This file is dual-licensed under both the Magnolia
 * Network Agreement and the GNU General Public License.
 * You may elect to use one or the other of these licenses.
 *
 * This file is distributed in the hope that it will be
 * useful, but AS-IS and WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, or NONINFRINGEMENT.
 * Redistribution, except as permitted by whichever of the GPL
 * or MNA you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or
 * modify this file under the terms of the GNU General
 * Public License, Version 3, as published by the Free Software
 * Foundation.  You should have received a copy of the GNU
 * General Public License, Version 3 along with this program;
 * if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * 2. For the Magnolia Network Agreement (MNA), this file
 * and the accompanying materials are made available under the
 * terms of the MNA which accompanies this distribution, and
 * is available at http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package info.magnolia.imaging;

import info.magnolia.imaging.caching.CachingStrategy;

/**
 * A ParameterProviderFactory is responsible for instanciating a ParameterProvider
 * for a given environment. Implementations know what concrete environment they expect and what
 * concrete type of ParameterProvider they generate. The "environment" is likely to be
 * an HttpServletRequest, but could also be a WorkItemContext, for instance.
 *
 * The ParameterProviderFactory must also ensure the parameter has appropriate
 * equals() and hashCode() methods: the parameter is used as part of a key of a
 * blocking map to avoid generating the same image multiple times.
 *
 * TODO -- verify, validate, fix the following :
 * TODO -- The type of context basically depends on the caller. In most cases, the context (C)
 * TODO -- will be an HttpServletRequest or a Magnolia Context instance.
 * TODO -- Since the ParameterProviderFactory is provided by
 * TODO -- the ImageGenerator, this might means that a particular ImageGenerator is tied
 * TODO -- to a specific context.
 *
 * @see info.magnolia.imaging.caching.ImageGenerationJob
 *
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public interface ParameterProviderFactory<E, PT> {

    /**
     * Instanciates a new ParameterProvider for the given environment.
     */
    ParameterProvider<PT> newParameterProviderFor(E environment);

    CachingStrategy<PT> getCachingStrategy();

}
