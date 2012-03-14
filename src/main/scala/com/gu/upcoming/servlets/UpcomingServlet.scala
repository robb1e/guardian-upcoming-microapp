package com.gu.upcoming.servlets

import com.gu.scalatra._
import com.gu.upcoming.models._
import org.joda.time.format.DateTimeFormat
import java.util.Locale
import org.joda.time.{ DateTimeZone, DateTime }

class UpcomingServlet extends GuardianScalatraServlet {

  get("/") {
    render("index")
  }

  get("/admin") {
    val upcomingEvents = Event.all()
    val from = rfc1123DateFormat.print(new DateTime())
    val to = rfc1123DateFormat.print(new DateTime().plusDays(1))
    render("admin", Map("events" -> upcomingEvents, "from" -> from, "to" -> to))
  }

  post("/event") {
    val description = params.get("description") getOrElse halt(status = 400, reason = "no description given")
    val displayFrom = params.get("displayFrom") map (rfc1123DateFormat.parseDateTime(_)) getOrElse new DateTime()
    val displayUntil = params.get("displayUntil") map (rfc1123DateFormat.parseDateTime(_)) getOrElse new DateTime().plusDays(1)
    val eventType = params.get("eventType") getOrElse "promotion"
    val event = Event(description = description, displayFrom = displayFrom, displayUntil = displayUntil, eventType = eventType)
    Event.upsert(event)
    redirect(request.getHeader("Referer"))
  }

  var whiteList = List("eventType")

  get("/event") {

    var query = whiteList.filter(params.contains(_)).map(key => (key, params(key))).toList
    val events = Event.all(query)
    render("events", Map("events" -> events))
  }

  get("/event/:id") {
    val event = Event.find(params("id")) getOrElse halt(status = 404, reason = "Event not found")
    render("event", Map("event" -> event))
  }

  lazy val rfc1123DateFormat = DateTimeFormat
    .forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'")
    .withLocale(Locale.US)
    .withZone(DateTimeZone.UTC);

}