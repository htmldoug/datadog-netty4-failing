package controllers

import io.opentracing.util.GlobalTracer
import javax.inject._
import org.slf4j.LoggerFactory
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(implicit ec: ExecutionContext) extends Controller {

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
    Future {
      spanResult
    }
  }

  private def spanResult = {
    val span = Option(GlobalTracer.get().activeSpan())
    logger.warn(s"Active span in index: $span")

    Ok(span.map(_.toString).getOrElse("Span is missing in controller!"))
  }
}
