name := "annict4s"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.14",
  "com.github.xuwei-k" %% "httpz-scalaj" % "0.5.1"
)

resolvers += Resolver.sonatypeRepo("releases")

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")

scalacOptions in ThisBuild ++= Seq(
  "-feature",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-deprecation"  
)
