enablePlugins(JavaAgent)

javaAgents += "com.datadoghq" % "dd-java-agent" % "0.11.0"

libraryDependencies ++= Seq(
  "org.asynchttpclient" % "async-http-client" % "2.0.0",
  "io.netty" % "netty-all" % "4.0.0.Final",
  "io.opentracing" % "opentracing-api" % "0.31.0",
  "io.opentracing" % "opentracing-util" % "0.31.0",
  "com.datadoghq" % "dd-trace-ot" % "0.11.0"
)

run / javaOptions := {
  Seq(
    "-Ddd.service.name=datadog_test_app",
    "-Ddd.trace.span.tags=env:dev",
    "-Ddd.integration.netty.enabled=true",
    "-Ddd.priority.sampling=true"
  ) ++ resolvedJavaAgents.value.map("-javaagent:" + _.artifact)
}

fork := true

