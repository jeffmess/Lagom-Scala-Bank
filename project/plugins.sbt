resolvers += Resolver.url("bintray.scaffolding-plugin.resolver", url("http://dl.bintray.com/fabszn/sbt-plugins"))(Resolver.ivyStylePatterns)

// The Lagom plugin
addSbtPlugin("com.lightbend.lagom" % "lagom-sbt-plugin" % "1.2.0")
// The ConductR plugin
addSbtPlugin("com.lightbend.conductr" % "sbt-conductr" % "2.1.16")

addSbtPlugin("com.lightbend.lagom.sbt" %% "scaffolding-plugin-lagom" % "1.0.0-M3")

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.2.0")
