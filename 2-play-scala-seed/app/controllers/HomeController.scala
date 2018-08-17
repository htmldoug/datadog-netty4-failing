package controllers

import io.opentracing.util.GlobalTracer
import javax.inject._
import org.slf4j.LoggerFactory
import play.api.mvc._
import play.api.libs.ws._

import scala.concurrent.{ExecutionContext, Future}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(ws: WSClient)(implicit ec: ExecutionContext) extends Controller {

  private val logger = LoggerFactory.getLogger(getClass)

  def index = Action { implicit request =>
    spanResult
  }

  def futureMap = Action.async {
    Future(())
      .map { _ =>
        // Will the span be available across a Future/ExecutionContext
        spanResult
      }
  }

  def post = Action.async(parse.anyContent) { request =>
    val request: WSRequest = ws.url("http://localhost:9000/futureMap")
    request.get().map { resp =>
      Ok(resp.body)
    }
  }

  private def spanResult = {
    val span = Option(GlobalTracer.get().activeSpan())
    logger.warn(s"Active span in index: $span")

    Ok(span.map(_.toString).getOrElse("Span is missing in controller!"))
  }
}
