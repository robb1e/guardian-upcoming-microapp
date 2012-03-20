resolvers ++= Seq(
  "Web plugin repo" at "http://siasia.github.com/maven2",
  "zentrope" at "http://zentrope.com/maven",
  "sbt-idea-repo" at "http://mpeltonen.github.com/maven/",
  Resolver.url("Typesafe repository", new java.net.URL("http://typesafe.artifactoryonline.com/typesafe/ivy-releases/"))(Resolver.defaultIvyPatterns)
)

resolvers += Classpaths.typesafeResolver

libraryDependencies <+= sbtVersion(v => "com.github.siasia" %% "xsbt-web-plugin" % (v+"-0.2.7"))

resolvers += {
    val typesafeRepoUrl = new java.net.URL("http://repo.typesafe.com/typesafe/ivy-snapshots")
    val pattern = Patterns(false, "[organisation]/[module]/[sbtversion]/[revision]/[type]s/[module](-[classifier])-[revision].[ext]")
    Resolver.url("Typesafe Ivy Snapshot Repository", typesafeRepoUrl)(pattern)
}

resolvers += Classpaths.typesafeResolver

addSbtPlugin("com.typesafe.sbtscalariform" % "sbt-scalariform" % "0.1.4")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "0.11.0")

addSbtPlugin("com.zentrope" %% "xsbt-scalate-precompile-plugin" % "1.6")

addSbtPlugin("com.typesafe.startscript" % "xsbt-start-script-plugin" % "0.3.0")
