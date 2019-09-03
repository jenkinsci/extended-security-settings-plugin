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

import com.gargoylesoftware.htmlunit.WebResponse;
import jenkins.model.Jenkins;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.MockAuthorizationStrategy;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UnauthorizedUserHttpHeaderRestrictionFilterTest {

    public @Rule JenkinsRule j = new JenkinsRule();
    private Set<HttpHeaderName> previousHeaderNames;

    @Before
    public void setUp() {
        j.jenkins.setSecurityRealm(j.createDummySecurityRealm());
        j.jenkins.setAuthorizationStrategy(new MockAuthorizationStrategy().grant(Jenkins.READ).everywhere().to("admin"));
        previousHeaderNames = ExtendedSecuritySettings.get().getHttpHeaderNames();
    }

    @After
    public void tearDown() {
        ExtendedSecuritySettings.get().setHttpHeaderNames(previousHeaderNames);
        previousHeaderNames = null;
    }

    @Test
    public void shouldFilterHeadersWhenUnauthorized() throws Exception {
        setHttpHeaderNamesToFilter("x-jenkins", "x-hudson");
        WebResponse response = j.createWebClient().withThrowExceptionOnFailingStatusCode(false).goTo("").getWebResponse();
        assertNull(response.getResponseHeaderValue("X-Jenkins"));
        assertNull(response.getResponseHeaderValue("X-Hudson"));

        response = j.createWebClient().login("admin").goTo("").getWebResponse();
        assertEquals(Jenkins.VERSION, response.getResponseHeaderValue("x-jenkins"));
    }

    private static void setHttpHeaderNamesToFilter(String... headerNames) {
        ExtendedSecuritySettings.get().setHttpHeaderNames(
                Arrays.stream(headerNames).map(HttpHeaderName::new).collect(Collectors.toSet()));
    }

}