package com.gu.whatsnext.servlets

import com.gu.scalatra._
import com.gu.whatsnext.models._

class WhatsNext extends GuardianScalatraServlet {

  get("/") {
    render("index")
  }

  get("/admin") {
    val upcomingEvents = Upcoming.all
    render("admin")
  }

}