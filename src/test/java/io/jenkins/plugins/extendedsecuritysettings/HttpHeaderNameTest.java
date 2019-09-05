/*
 * The MIT License
 *
 * Copyright (c) 2019 CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.jenkins.plugins.extendedsecuritysettings;

import org.junit.Test;

import static org.junit.Assert.*;

public class HttpHeaderNameTest {

    @Test
    public void httpHeaderNameShouldBeCaseInsensitive() {
        assertEquals(new HttpHeaderName("X-Cache-Expiration"), new HttpHeaderName("x-cache-ExPiRaTiOn"));
        assertEquals(new HttpHeaderName("X-Cache-Expiration").hashCode(), new HttpHeaderName("x-cache-ExPiRaTiOn").hashCode());

        assertNotEquals(new HttpHeaderName("Length"), new HttpHeaderName("X-Cache-Expiration"));
        // contract for hashCode: a.equals(b) => a.hashCode() == b.hashCode()
        // testing that two hashCodes are different is interesting for testing purpose but not 100% true in practice for any value
        // fine with "fixed" value like this to avoid method returning a hardcoded value.
        assertNotEquals(new HttpHeaderName("Length").hashCode(), new HttpHeaderName("X-Cache-Expiration").hashCode());
    }

    @Test
    public void httpHeaderNameShouldAcceptNull() {
        assertEquals(new HttpHeaderName(), new HttpHeaderName());
        assertEquals(new HttpHeaderName().hashCode(), new HttpHeaderName().hashCode());

        assertNotEquals(new HttpHeaderName(), new HttpHeaderName("Length"));
        assertNotEquals(new HttpHeaderName().hashCode(), new HttpHeaderName("Length").hashCode());
        assertNotEquals(new HttpHeaderName("Length"), new HttpHeaderName());
        assertNotEquals(new HttpHeaderName("Length").hashCode(), new HttpHeaderName().hashCode());
    }

    @Test
    public void httpHeaderNameToStringShouldMaintainInputCase() {
        assertEquals("HttpHeaderFilter(Length)", new HttpHeaderName("Length").toString());
        assertEquals("HttpHeaderFilter(length)", new HttpHeaderName("length").toString());
    }
}