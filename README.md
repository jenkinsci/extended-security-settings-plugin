# Extended Security Settings for Jenkins

[![Build Status](https://ci.jenkins.io/buildStatus/icon?job=Plugins/extended-security-settings-plugin/master)](https://ci.jenkins.io/job/Plugins/job/extended-security-settings-plugin/job/master/)

Jenkins plugin to configure Extended Security Settings: a set of additional security settings for Jenkins.

## Disable Password Autocomplete

This feature is designed to allow overly paranoid security scanners to certify Jenkins.
This adds an `autocomplete="off"` attribute to password inputs on the signup and login pages.
Note that this feature is generally ignored by modern web browsers due to the inherent insecurity of attempting to prevent password managers from working which encourages weak passwords or bad password management practices (like using sticky notes).
See [Choosing Secure Passwords](https://www.schneier.com/blog/archives/2014/03/choosing_secure_1.html) for more details.

## Enable X-XSS-Protection Header

This feature enables the HTTP header `X-XSS-Protection: 1; mode=block` to be sent on all requests which some web browsers intend as a way to automatically block suspected cross-site scripting attacks.
Several web browsers (e.g., Firefox, Edge, and Chrome) do not support this header.

## Remove HTTP Headers for Unauthorized Users

This feature adds a configurable list of HTTP header names (case-insensitive) that can be automatically removed from HTTP responses sent to users lacking the *Overall/Read* permission.
This is useful for hiding the Jenkins and other software versions present in HTTP headers like `X-Jenkins.`
Various security scanning tools recommend hiding version information advertised this way.
Note that there are numerous other ways to fingerprint Jenkins to deduce which version is running even when this feature is enabled.

This feature is only provided for basic HTTP header removal.
See also the [Jetty header filter](https://www.eclipse.org/jetty/documentation/current/header-filter.html) which comes with Jetty (the default Jenkins web container) for some additional header filter operations.
For more complex header rewrite rules, using a reverse proxy such as [Apache](https://httpd.apache.org/) with [mod_headers](https://httpd.apache.org/docs/current/mod/mod_headers.html) is a more complete solution.
Use of a reverse proxy in front of Jenkins is generally recommended in order to more easily support HTTPS and allow for additional security hardening through the reverse proxy itself.
Many popular reverse proxies and HTTP load balancers support some form of response filtering, so consult the relevant documentation for those products on how to filter HTTP headers.

---

Check out the [wiki page](https://wiki.jenkins.io/display/JENKINS/Extended+Security+Settings+Plugin) for the changelog.
