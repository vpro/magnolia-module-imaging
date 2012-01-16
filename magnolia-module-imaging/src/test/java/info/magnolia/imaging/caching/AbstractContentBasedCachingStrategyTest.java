/**
 * This file Copyright (c) 2010-2011 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This program and the accompanying materials are made
 * available under the terms of the Magnolia Network Agreement
 * which accompanies this distribution, and is available at
 * http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package info.magnolia.imaging.caching;

import junit.framework.TestCase;

import java.util.Calendar;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class AbstractContentBasedCachingStrategyTest extends TestCase {
    public void testSelfSanity() {
        final Calendar cal = Calendar.getInstance();

        // It seems some JVMs throw NPEs on this ?
        assertFalse(cal.before(null));
    }
}
