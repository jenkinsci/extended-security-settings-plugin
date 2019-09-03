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

import javax.annotation.Nonnull;
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
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Restricted(NoExternalUse.class)
public class UnauthorizedUserHttpHeaderRestrictionFilter implements Filter {

    @Initializer
    public static void initialize() throws ServletException {
        PluginServletFilter.addFilter(new UnauthorizedUserHttpHeaderRestrictionFilter());
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletResponse wrappedResponse = response;
        if (isFilterableResponse(response) && !hasOverallRead()) {
            Set<String> headerNames = getFilteredHeaderNames();
            if (!headerNames.isEmpty()) {
                wrappedResponse = new HeaderRemovingResponseWrapper((HttpServletResponse) response, headerNames);
            }
        }
        chain.doFilter(request, wrappedResponse);
    }

    private static boolean isFilterableResponse(@Nonnull ServletResponse response) {
        return response instanceof HttpServletResponse && !response.isCommitted();
    }

    private static boolean hasOverallRead() {
        Jenkins jenkins = Jenkins.getInstanceOrNull();
        return jenkins != null && jenkins.hasPermission(Jenkins.READ);
    }

    private static @Nonnull Set<String> getFilteredHeaderNames() {
        Set<HttpHeaderName> headerNames = ExtendedSecuritySettings.get().getHttpHeaderNames();
        if (headerNames.isEmpty()) {
            return Collections.emptySet();
        }
        return headerNames.stream()
                .map(HttpHeaderName::getHeaderName)
                .filter(headerName -> headerName != null && !headerName.isEmpty())
                .map(headerName -> headerName.toLowerCase(Locale.ENGLISH))
                .collect(Collectors.toSet());
    }

    private static class HeaderRemovingResponseWrapper extends HttpServletResponseWrapper {

        private final Set<String> headerNames;

        private HeaderRemovingResponseWrapper(@Nonnull HttpServletResponse response, @Nonnull Set<String> headerNames) {
            super(response);
            this.headerNames = headerNames;
            headerNames.forEach(headerName -> response.setHeader(headerName, null));
        }

        private boolean isHeaderAllowed(@Nonnull String headerName) {
            return !headerNames.contains(headerName.toLowerCase(Locale.ENGLISH));
        }

        @Override
        public boolean containsHeader(String name) {
            return isHeaderAllowed(name) && super.containsHeader(name);
        }

        @Override
        public void setHeader(String name, String value) {
            if (isHeaderAllowed(name)) {
                super.setHeader(name, value);
            }
        }

        @Override
        public void addHeader(String name, String value) {
            if (isHeaderAllowed(name)) {
                super.addHeader(name, value);
            }
        }

        @Override
        public String getHeader(String name) {
            return isHeaderAllowed(name) ? super.getHeader(name) : null;
        }

        @Override
        public Collection<String> getHeaders(String name) {
            return isHeaderAllowed(name) ? super.getHeaders(name) : Collections.emptySet();
        }

        @Override
        public Collection<String> getHeaderNames() {
            return super.getHeaderNames().stream().filter(this::isHeaderAllowed).collect(Collectors.toSet());
        }
    }
}
