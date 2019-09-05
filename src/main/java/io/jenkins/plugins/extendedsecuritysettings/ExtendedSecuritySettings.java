/*
 * The MIT License
 *
 * Copyright (c) 2018-2019 CloudBees, Inc.
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
import jenkins.model.GlobalConfiguration;
import jenkins.model.GlobalConfigurationCategory;
import net.sf.json.JSONObject;
import org.jenkinsci.Symbol;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.Set;

@Restricted(NoExternalUse.class)
@Extension
@Symbol("extendedSecuritySettings")
public class ExtendedSecuritySettings extends GlobalConfiguration {

    public static @Nonnull ExtendedSecuritySettings get() {
        return GlobalConfiguration.all().getInstance(ExtendedSecuritySettings.class);
    }

    private boolean disableLoginAutocomplete = true;

    private boolean enableXssProtectionHeader = true;

    private Set<HttpHeaderName> httpHeaderNames;

    public ExtendedSecuritySettings() {
        load();
    }

    public boolean isDisableLoginAutocomplete() {
        return disableLoginAutocomplete;
    }

    @DataBoundSetter
    public void setDisableLoginAutocomplete(boolean disableLoginAutocomplete) {
        this.disableLoginAutocomplete = disableLoginAutocomplete;
        save();
    }

    public boolean isEnableXssProtectionHeader() {
        return enableXssProtectionHeader;
    }

    @DataBoundSetter
    public void setEnableXssProtectionHeader(boolean enableXssProtectionHeader) {
        this.enableXssProtectionHeader = enableXssProtectionHeader;
        save();
    }

    public @CheckForNull Set<HttpHeaderName> getHttpHeaderNames() {
        return httpHeaderNames;
    }

    @DataBoundSetter
    public void setHttpHeaderNames(@CheckForNull Set<HttpHeaderName> httpHeaderNames) {
        this.httpHeaderNames = httpHeaderNames;
        save();
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        // reset state in case httpHeaderNames were all removed
        this.httpHeaderNames = null;
        return super.configure(req, json);
    }

    @Override
    public @Nonnull GlobalConfigurationCategory getCategory() {
        return GlobalConfigurationCategory.get(GlobalConfigurationCategory.Security.class);
    }
}
