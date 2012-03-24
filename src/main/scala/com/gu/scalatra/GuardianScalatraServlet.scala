package com.gu.scalatra

import org.scalatra._
import org.scalatra.scalate._
import net.liftweb.json.Extraction
import net.liftweb.json._
import net.liftweb.json.JsonAST.{ JInt, JValue }
import net.liftweb.json.Printer._
import com.gu.management._

trait GuardianScalatraKernel extends ScalatraKernel with ScalateSupport with ApiFormats with Loggable {

  lazy val none = 0
  lazy val oneMinute = 60
  lazy val fifteenMinutes = oneMinute * 15
  lazy val oneHour = oneMinute * 60
  lazy val oneDay = oneHour * 24
  lazy val oneWeek = oneDay * 7

  def render(file: String, params: Map[String, Any] = Map(), responseContentType: String = "text/html", cacheMaxAge: Int = none, statusCode: Int = 200) {
    contentType = responseContentType
    response.setHeader("Cache-Control", "public, max-age=%d" format cacheMaxAge)
    response.setStatus(statusCode)
    renderResponseBody(templateEngine.layout("/WEB-INF/scalate/templates/%s.ssp" format file, params))
  }

  override protected def renderPipeline = ({
    case p: Product => {
      implicit val formats = DefaultFormats
      contentType = "application/json; charset=utf-8"
      val decomposed = Extraction.decompose(p)
      val rendered = JsonAST.render(decomposed)
      net.liftweb.json.compact(rendered).getBytes("UTF-8")
    }
  }: RenderPipeline) orElse super.renderPipeline

}
