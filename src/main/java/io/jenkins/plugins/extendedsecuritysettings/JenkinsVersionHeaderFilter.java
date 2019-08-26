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

import hudson.init.Initializer;
import hudson.util.PluginServletFilter;
import jenkins.model.Jenkins;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Restricted(NoExternalUse.class)
public class JenkinsVersionHeaderFilter implements Filter {

    @Initializer
    public static void initialize() throws ServletException {
        PluginServletFilter.addFilter(new JenkinsVersionHeaderFilter());
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletResponse rsp = isEnabled() ? new HeaderRemovingResponseWrapper((HttpServletResponse) response) : response;
        chain.doFilter(request, rsp);
    }

    private boolean isEnabled() {
        final Jenkins jenkins = Jenkins.getInstanceOrNull();
        return jenkins != null && ExtendedSecuritySettings.get().isDisableXJenkinsHeaderUnlessAuthorized() &&
                !jenkins.hasPermission(Jenkins.READ);
    }

    private static class HeaderRemovingResponseWrapper extends HttpServletResponseWrapper {

        private static final String HEADER_NAME = "X-Jenkins";

        private HeaderRemovingResponseWrapper(HttpServletResponse response) {
            super(response);
            response.setHeader(HEADER_NAME, null);
        }

        @Override
        public boolean containsHeader(String name) {
            return !HEADER_NAME.equalsIgnoreCase(name) && super.containsHeader(name);
        }

        @Override
        public void setHeader(String name, String value) {
            if (!HEADER_NAME.equalsIgnoreCase(name)) {
                super.setHeader(name, value);
            }
        }

        @Override
        public void addHeader(String name, String value) {
            if (!HEADER_NAME.equalsIgnoreCase(name)) {
                super.addHeader(name, value);
            }
        }

        @Override
        public String getHeader(String name) {
            return HEADER_NAME.equalsIgnoreCase(name) ? null : super.getHeader(name);
        }

        @Override
        public Collection<String> getHeaders(String name) {
            return HEADER_NAME.equalsIgnoreCase(name) ? Collections.emptySet() : super.getHeaders(name);
        }

        @Override
        public Collection<String> getHeaderNames() {
            return super.getHeaderNames().stream()
                    .filter(headerName -> !HEADER_NAME.equalsIgnoreCase(headerName))
                    .collect(Collectors.toSet());
        }
    }
}
