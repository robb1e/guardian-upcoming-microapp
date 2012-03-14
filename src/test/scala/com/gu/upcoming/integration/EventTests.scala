package com.gu.upcoming.integration

import com.gu.integration.IntegrationHttpClient._
import com.gu.upcoming.models.Event
import org.apache.commons.lang.RandomStringUtils
import org.joda.time.DateTime

class EventTests extends RequiresRunningApplication {

  val baseUri = "http://localhost:8080"
  val eventResourceUri = "%s/event" format baseUri

  def createDescription = RandomStringUtils.randomAlphabetic(8)

  feature("Creating events") {

    scenario("with default options") {
      createAndAssertEvent((description: String) => {
        when("We hit the HTTP inteface to create an event")
        post(eventResourceUri, Map("description" -> description))
      })
    }

    scenario("with until date only") {
      val event = createAndAssertEvent((description: String) => {
        when("We hit the HTTP inteface to create an event")
        post(eventResourceUri, Map("description" -> description, "displayUntil" -> "Mon, 12 Mar 2012 14:00:00 GMT"))
      })
      event.displayUntil.dayOfMonth().getAsText should be("12")
    }

    scenario("with from and until date") {
      val event = createAndAssertEvent((description: String) => {
        when("We hit the HTTP inteface to create an event")
        post(eventResourceUri, Map("description" -> description,
          "displayUntil" -> "Mon, 12 Mar 2012 14:00:00 GMT",
          "displayFrom" -> "Sun, 11 Mar 2012 14:00:00 GMT"))
      })
      event.displayFrom.dayOfMonth().getAsText should be("11")
    }

    scenario("with event type of promotion") {
      val event = createAndAssertEvent((description: String) => {
        when("We hit the HTTP inteface to create an event")
        post(eventResourceUri, Map("description" -> description, "eventType" -> "promotion"))
      })
      event.eventType should be("promotion")
    }

    def getEventByDescription(description: String) = {
      val events = Event.all().filter(event => description == event.description)
      events.size should be(1)
      events.head
    }

    def createAndAssertEvent(create: String => Unit) = {
      val description = createDescription
      create(description)
      then("it should be in the database")
      val event = getEventByDescription(description)
      and("we should retrieve that event through the id")
      get(eventResourceUri + "/" + event.id) should include(description)
      and("we should see the event on the events page")
      get(eventResourceUri) should include(description)
      event
    }

  }

  feature("Retrieving events") {

    scenario("should only show promotion events") {
      val promoEvent = createEvent("promotion", new DateTime)
      val nonPromoEvent = createEvent("other", new DateTime)

      val response = get(uri = eventResourceUri, params = Map("eventType" -> "promotion"))
      response should include(promoEvent.description)
      response should not include (nonPromoEvent.description)
    }

    def createEvent(eventType: String, displayUntil: DateTime) = {
      val description = createDescription
      val event = Event(description = description, eventType = eventType, displayUntil = displayUntil)
      Event.upsert(event)
      event
    }

  }

}