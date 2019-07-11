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

package io.jenkins.plugins.paranoia;

import com.gargoylesoftware.htmlunit.WebResponse;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XSSProtectionHeaderPageDecoratorTest {
    public @Rule JenkinsRule j = new JenkinsRule();

    @Test
    public void shouldHaveXSSProtectionHeaderByDefault() throws IOException, SAXException {
        final WebResponse response = j.createWebClient().goTo("").getWebResponse();
        assertEquals("1; mode=block", response.getResponseHeaderValue("X-XSS-Protection"));
    }

    @Test
    public void shouldNotHaveXSSProtectionHeaderWhenDisabled() throws IOException, SAXException {
        ParanoiaConfiguration.get().setEnableXssProtectionHeader(false);
        try {
            final WebResponse response = j.createWebClient().goTo("").getWebResponse();
            assertNull(response.getResponseHeaderValue("X-XSS-Protection"));
        } finally {
            ParanoiaConfiguration.get().setEnableXssProtectionHeader(true);
        }
    }
}
