package com.rallyhealth.datadog

import datadog.trace.api.interceptor.MutableSpan
import datadog.trace.api.sampling.PrioritySampling
import io.opentracing.util.GlobalTracer
import org.asynchttpclient.{AsyncHttpClient, DefaultAsyncHttpClient}

object DatadogTestApp {

  def main(args: Array[String]): Unit = {
    require(System.getProperty("dd.integration.netty.enabled") == "true")
    require(System.getProperty("dd.priority.sampling") == "true")
    require(GlobalTracer.isRegistered)

    val asyncHttpClient: AsyncHttpClient = new DefaultAsyncHttpClient

    GlobalTracer.get().activeSpan()
    val spanBuilder = GlobalTracer.get().buildSpan("parent")
    val span = spanBuilder.start()
    span.asInstanceOf[MutableSpan].setSamplingPriority(PrioritySampling.USER_KEEP)

    // HttpClientRequestTracingHandler should produce a child span, but doesn't!
    val response = asyncHttpClient.prepareGet("http://www.example.com/").execute().get()
    span.finish()

    // prove that the request was executed for sanity.
    println(response.getResponseBody)

    asyncHttpClient.close()

    Thread.sleep(15000) // wait for flush
  }
}
