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

---

Check out the [wiki page](https://wiki.jenkins.io/display/JENKINS/Extended+Security+Settings+Plugin) for the changelog.
