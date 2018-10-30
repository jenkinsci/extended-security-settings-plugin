/*
 * The MIT License
 *
 * Copyright (c) 2018, CloudBees, Inc.
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

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import hudson.security.HudsonPrivateSecurityRealm;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class DisableLoginAutocompleteDecoratorTest {
    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void loginPageIsCorrectlyModified_whenEnabled() throws Exception {
        j.jenkins.setSecurityRealm(new HudsonPrivateSecurityRealm(false, false, null));
        JenkinsRule.WebClient wc = j.createWebClient();
        HtmlPage loginPage = wc.goTo("login");
        HtmlPasswordInput passwordInput = loginPage.getDocumentElement().getOneHtmlElementByAttribute("input", "name", "j_password");
        assertNotNull(passwordInput);
        
        assertThat(passwordInput.getAttribute("autocomplete"), equalTo("off"));
    }

    @Test
    public void loginPageIsNotModified_whenDisabled() throws Exception {
        ParanoiaConfiguration.get().setDisableLoginAutocomplete(false);
        
        j.jenkins.setSecurityRealm(new HudsonPrivateSecurityRealm(false, false, null));
        JenkinsRule.WebClient wc = j.createWebClient();
        HtmlPage loginPage = wc.goTo("login");
        HtmlPasswordInput passwordInput = loginPage.getDocumentElement().getOneHtmlElementByAttribute("input", "name", "j_password");
        assertNotNull(passwordInput);
        
        assertThat(passwordInput.getAttribute("autocomplete"), not(equalTo("off")));
    }
}
