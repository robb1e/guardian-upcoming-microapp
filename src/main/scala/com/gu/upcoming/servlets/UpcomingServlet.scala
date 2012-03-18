package com.gu.upcoming.servlets

import com.gu._
import scalatra._
import presenters._
import upcoming.models._

import org.joda.time.format.DateTimeFormat
import java.util.Locale
import org.joda.time.{ DateTimeZone, DateTime, Period }

class UpcomingServlet extends GuardianScalatraServlet {

  get("/") {
    render("index")
  }

  get("/admin") {
    val now = new DateTime().getMillis
    val allEvents = Event.all()
    val priorEvents = allEvents.filter { _.displayUntil.getMillis < now }
    val currentEvents = allEvents.filter { event => event.displayUntil.getMillis > now && event.displayFrom.getMillis < now }
    val upcomingEvents = allEvents.filter { event => event.displayUntil.getMillis > now && event.displayFrom.getMillis > now }

    val from = rfc1123DateFormat.print(new DateTime())
    val to = rfc1123DateFormat.print(new DateTime().plusDays(1))
    render("admin", Map("priorEvents" -> priorEvents, "currentEvents" -> currentEvents, "upcomingEvents" -> upcomingEvents, "from" -> from, "to" -> to))
  }

  post("/event") {
    val description = params.get("description") getOrElse halt(status = 400, reason = "no description given")
    val title = params.get("title") getOrElse halt(status = 400, reason = "no title given")
    val displayFrom = params.get("displayFrom") map (rfc1123DateFormat.parseDateTime(_)) getOrElse new DateTime()
    val displayUntil = params.get("displayUntil") map (rfc1123DateFormat.parseDateTime(_)) getOrElse new DateTime().plusDays(1)
    val eventType = params.get("eventType") getOrElse "promotion"
    val event = Event(description = description, title = title, displayFrom = displayFrom, displayUntil = displayUntil, eventType = eventType.toLowerCase)
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
    val event = Event.find(params("id")) getOrElse halt(status = 404, reason = "Event not found.")
    if (event.displayUntil.getMillis < new DateTime().getMillis) halt(status = 410, reason = "That event has expired.")

    val diff = DateTimePeriodPresenter(new Period(new DateTime(), event.displayUntil))

    render("event", Map("event" -> event, "diff" -> diff))
  }

  get("/microapp/component/event/:id") {
    val event = Event.find(params("id")) getOrElse halt(status = 404, reason = "Event not found.")
    if (event.displayUntil.getMillis < new DateTime().getMillis) halt(status = 410, reason = "That event has expired.")
    val period = DateTimePeriodPresenter(new Period(new DateTime(), event.displayUntil))
    render("microapp", Map("event" -> event, "period" -> period))
  }

  put("/event/:id") {
    val event = Event.find(params("id")) getOrElse halt(status = 404, reason = "Event not found.")
    var query = whiteList.filter(params.contains(_)).map(key => (key, params(key))).toList
    val description = params.get("description") getOrElse event.description
    val displayFrom = params.get("displayFrom") map (rfc1123DateFormat.parseDateTime(_)) getOrElse event.displayFrom
    val displayUntil = params.get("displayUntil") map (rfc1123DateFormat.parseDateTime(_)) getOrElse event.displayUntil
    val eventType = params.get("eventType") getOrElse event.eventType
    val updatedEvent = event.copy(description = description, displayFrom = displayFrom, displayUntil = displayUntil, eventType = eventType)
    Event.upsert(updatedEvent)
    redirect(request.getHeader("Referer"))
  }

  lazy val rfc1123DateFormat = DateTimeFormat
    .forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'")
    .withLocale(Locale.US)
    .withZone(DateTimeZone.UTC);

}