organization := "com.example"

name := "demoobject"

version := "0.1.0-SNAPSHOT"

resolvers += "releases"  at "http://oss.sonatype.org/content/repositories/releases"

libraryDependencies ++= Seq(
   "net.databinder" %% "unfiltered-filter" % "0.6.4",
   "javax.servlet" % "servlet-api" % "2.3" % "provided",
   "org.eclipse.jetty" % "jetty-webapp" % "7.4.5.v20110725" % "container",
   "org.clapper" %% "avsl" % "0.4",
   "net.liftweb" %% "lift-json" % "2.5-M3",
   "org.squeryl" %% "squeryl" % "0.9.5",
   "mysql" % "mysql-connector-java" % "5.1.10",
   "net.databinder" %% "unfiltered-spec" % "0.6.4" % "test",
   "junit" % "junit" % "4.8.1" % "test",
   // so that sbt will find plain old junit scala test classes
   "com.novocode" % "junit-interface" % "0.10-M2" % "test",
   "org.easymock" % "easymock" % "3.1" % "test"
)

seq(webSettings :_*)
