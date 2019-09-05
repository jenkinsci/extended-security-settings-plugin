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

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Objects;

@Restricted(NoExternalUse.class)
public class HttpHeaderName extends AbstractDescribableImpl<HttpHeaderName> {

    private String headerName;

    @DataBoundConstructor
    public HttpHeaderName() {
    }

    HttpHeaderName(@Nonnull String headerName) {
        this.headerName = headerName;
    }

    @DataBoundSetter
    public void setHeaderName(@CheckForNull String headerName) {
        this.headerName = headerName;
    }

    public @CheckForNull String getHeaderName() {
        return headerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpHeaderName that = (HttpHeaderName) o;
        return StringUtils.equalsIgnoreCase(headerName, that.headerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(StringUtils.lowerCase(headerName, Locale.ENGLISH));
    }

    @Override
    public String toString() {
        return "HttpHeaderFilter(" + headerName + ')';
    }

    @Extension
    @Restricted(NoExternalUse.class)
    public static class DescriptorImpl extends Descriptor<HttpHeaderName> {
    }
}
