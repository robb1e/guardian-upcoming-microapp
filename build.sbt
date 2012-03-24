import com.typesafe.sbtscalariform._
import com.typesafe.startscript.StartScriptPlugin

organization := "com.gu"

name := "upcoming"

version := "0.1"

scalaVersion := "2.9.1"

resolvers ++= Seq(
  "Guardian Github Releases" at "http://guardian.github.com/maven/repo-releases",
  "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
  "Scala-tools Releases" at "http://scala-tools.org/repo-releases/",
  "Guice Maven" at "http://guice-maven.googlecode.com/svn/trunk",
  "repo.novus rels" at "http://repo.novus.com/releases/",
  "repo.novus snaps" at "http://repo.novus.com/snapshots/",
  "robb1e Github Releases" at "http://robb1e.github.com/maven/repo-releases",
  "robb1e Github Snapshots" at "http://robb1e.github.com/maven/repo-snapshots"
)

seq(webSettings :_*)

seq(StartScriptPlugin.startScriptForClassesSettings: _*)

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % "2.0.3",
  "org.scalatra" %% "scalatra-scalate" % "2.0.3",
  "org.fusesource.scalate" % "scalate-core" % "1.5.3",
  "org.eclipse.jetty" % "jetty-webapp" % "7.4.5.v20110725" % "container, compile",
  "javax.servlet" % "servlet-api" % "2.5" % "provided",
  "net.liftweb" %% "lift-json" % "2.4-M4",
  "com.novus" %% "salat-core" % "0.0.8-SNAPSHOT",
  "com.gu" %% "management-logback" % "5.6.4",
  "com.gu" %% "management-mongo" % "5.6.4",
  "com.gu" %% "scalatra-openid-consumer" % "0.1.5",
  "com.gu" %% "configuration" % "3.7-SNAPSHOT",
  "com.gu" %% "integration-tools" % "1.4" % "test",
  "net.databinder" %% "dispatch-http" % "0.8.8" % "test",
  "org.scalatest" %% "scalatest" % "1.7.1" % "test",
  "commons-lang" % "commons-lang" % "2.2" % "test"
)

seq(ScalariformPlugin.settings: _*)
