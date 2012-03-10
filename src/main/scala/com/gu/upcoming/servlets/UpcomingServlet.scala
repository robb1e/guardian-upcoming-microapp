package com.gu.upcoming.servlets

import com.gu.scalatra._
import com.gu.upcoming.models._
import org.joda.time.DateTime

class UpcomingServlet extends GuardianScalatraServlet {

  get("/") {
    render("index")
  }

  get("/admin") {
    val upcomingEvents = Event.all
    render("admin", Map("events" -> upcomingEvents))
  }

  post("/event") {
    val description = params.get("description") getOrElse halt(status = 400, reason = "no description given")
    val displayUntil = new DateTime()
    val displayFrom = params.get("displayFrom") map (new DateTime(_)) getOrElse new DateTime()
    val event = Event(description = description, displayFrom = displayFrom, displayUntil = displayUntil)
    Event.upsert(event)
  }

  get("/event") {
    val events = Event.all
    render("events", Map("events" -> events))
  }

  get("/event/:id") {
    val event = Event.find(params("id")) getOrElse halt(status = 404, reason = "Event not found")
    render("event", Map("event" -> event))
  }

}