enablePlugins(JavaAgent)

val datadogAgentVersion = "0.13.0-SNAPSHOT"

javaAgents += "com.datadoghq" % "dd-java-agent" % datadogAgentVersion

libraryDependencies ++= Seq(
  "org.asynchttpclient" % "async-http-client" % "2.0.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "io.opentracing" % "opentracing-api" % "0.31.0",
  "io.opentracing" % "opentracing-util" % "0.31.0",
  "com.datadoghq" % "dd-trace-ot" % datadogAgentVersion
)

run / javaOptions := {
  Seq(
    "-Ddd.service.name=datadog_test_app",
    "-Ddd.trace.span.tags=env:dev",
    "-Ddd.integration.netty.enabled=true",
    "-Ddd.writer.type=LoggingWriter",
    "-Ddd.priority.sampling=true"
  ) ++ resolvedJavaAgents.value.map("-javaagent:" + _.artifact)
}

fork := true

resolvers += "DD snapshots" at "https://oss.jfrog.org/oss-snapshot-local"
