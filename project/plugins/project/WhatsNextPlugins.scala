import sbt._

object WhatsNextPlugins extends Build {
   lazy val plugins = Project("whats-next-plugins", file("."))
    .dependsOn(
      uri("git://github.com/guardian/sbt-teamcity-test-reporting-plugin.git#1.1"),
      uri("git://github.com/guardian/sbt-version-info-plugin.git#1.0"),
      uri("git://github.com/guardian/sbt-dist-plugin.git#1.6")
    )
}