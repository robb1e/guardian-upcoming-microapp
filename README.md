# Guardian Scalatra Bootstrap

## Why

It takes too long to set up each new project, so here's my attempt at trying to create a re-usable template to help bootstrap new Scalatra based projects.

## Some Batteries included

### Guardian Management

The Guardian management libraries, including Logback and MongoDB are included and are already wired into the application.

* Properties
* Logback levels
* Manifest
* Status (metrics)
** HTTP 400 series errors
** HTTP 500 series errors
** Uncaught exceptions to the client
** JVM metrics 

## Guardian Configuration

The configuration library is included and wired to read properties from the class path as well as standard Guardian directorys like /etc/gu. 

## Guardian Scalatra OpenID Consumer

The Guardian Scalatra OpenID consumer is wired with some simple defaults set, including:

* Anything under /admin will be protected
* Anyone signing in must have a guardian.co.uk email account

## Guardian Scalatra Servlet

Calling 'render' in a class that extends from GuardianScalatraServlet uses default parameters allows the following to be set in one call:

* Map[String, Any] to be available to the template
* HTTP response status code, defaults to 200
* Cache control headers using the given time, default to 0, or no caching

The render function accepts a file name which is just the ssp file name without the extension.

There is also a rendering pipeline that converts any class that extends from Product (i.e. any case class) to be rendered into JSON using the Lift.net JSON library.

## Scalate

Pretty much the default rendering engine for Scalatra, although there are other options.  The webapp directory is setup:

* static
** images
** js
*** lib, containing Twitter Bootstrap Javascript library
** styles
*** lib, containing Twitter Bootstrap CSS and images for the CSS
* WEB-INF
** scalate
*** layouts, including a default layout which sets a fluid container and jQuery 1.6.4 from googleapis.com
*** templates