# Paranoia Mode for Jenkins

[![Build Status](https://ci.jenkins.io/buildStatus/icon?job=Plugins/paranoia-plugin/master)](https://ci.jenkins.io/job/Plugins/job/paranoia-plugin/job/master/)

Jenkins plugin to configure Paranoia Mode: a set of extra-paranoid security settings for Jenkins.

## Disable Password Autocomplete

This feature is designed to allow overly paranoid security scanners to certify Jenkins.
This adds an `autocomplete="off"` attribute to password inputs on the signup and login pages.
Note that this feature is generally ignored by modern web browsers due to the inherent insecurity of attempting to prevent password managers from working which encourages weak passwords or bad password management practices (like using sticky notes).
See [Choosing Secure Passwords](https://www.schneier.com/blog/archives/2014/03/choosing_secure_1.html) for more details.

Check out the [wiki page](https://wiki.jenkins.io/display/JENKINS/Paranoia+Plugin) for more information.
