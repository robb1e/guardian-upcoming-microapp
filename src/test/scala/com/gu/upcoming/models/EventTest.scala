package com.gu.upcoming.models

import org.joda.time.DateTime
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers

class EventTest extends Suite with FeatureSpec with ShouldMatchers {

  feature("exit link") {

    scenario("The description is not a link") {
      val event = Event(description = "no link here", title = "title", displayUntil = new DateTime, headline = "headline")
      event.isExit should be(false)
    }

    scenario("The description is a relative link") {
      val event = Event(description = """relative <a href="/foo""", title = "title", displayUntil = new DateTime, headline = "headline")
      event.isExit should be(false)
    }

    scenario("The description is a relative link - single quotes") {
      val event = Event(description = """relative <a href='/foo'""", title = "title", displayUntil = new DateTime, headline = "headline")
      event.isExit should be(false)
    }

    scenario("The description is a Guardian.co.uk link") {
      val event = Event(description = """relative <a href="www.guardian.co.uk""", title = "title", displayUntil = new DateTime, headline = "headline")
      event.isExit should be(false)
    }

    scenario("The description is a Guardian.co.uk link - single quote") {
      val event = Event(description = """relative <a href='www.guardian.co.uk'""", title = "title", displayUntil = new DateTime, headline = "headline")
      event.isExit should be(false)
    }

    scenario("The description is a Guardiannews.com link") {
      val event = Event(description = """relative <a href="www.guardiannews.com""", title = "title", displayUntil = new DateTime, headline = "headline")
      event.isExit should be(false)
    }

    scenario("The description is a Guardiannews.com link - single quote") {
      val event = Event(description = """relative <a href='http://www.guardiannews.com'""", title = "title", displayUntil = new DateTime, headline = "headline")
      event.isExit should be(false)
    }

    scenario("The description is a Gu.com link") {
      val event = Event(description = """relative <a href="gu.com""", title = "title", displayUntil = new DateTime, headline = "headline")
      event.isExit should be(false)
    }

    scenario("The description is an external link") {
      val event = Event(description = """relative <a href="www.externalsite.co.uk""", title = "title", displayUntil = new DateTime, headline = "headline")
      event.isExit should be(true)
    }

    scenario("The description is an external link - single quote") {
      val event = Event(description = """relative <a href='www.externalsite.co.uk'""", title = "title", displayUntil = new DateTime, headline = "headline")
      event.isExit should be(true)
    }

    scenario("The description is an external link - no subdomain") {
      val event = Event(description = """relative <a href='http://externalsite.co.uk'""", title = "title", displayUntil = new DateTime, headline = "headline")
      event.isExit should be(true)
    }

  }

}