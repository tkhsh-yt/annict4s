name := "annict4s"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.14",
  "com.github.xuwei-k" %% "httpz-scalaj" % "0.5.1",
  "org.scalaj" %% "scalaj-http" % "2.3.0",
  "joda-time" % "joda-time" % "2.9.9",
  "org.joda" % "joda-convert" % "1.7"
)

resolvers += Resolver.sonatypeRepo("releases")

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")

scalacOptions in ThisBuild ++= Seq(
  "-feature",
  "-deprecation",
  "-language:implicitConversions",
  "-language:higherKinds"
)
