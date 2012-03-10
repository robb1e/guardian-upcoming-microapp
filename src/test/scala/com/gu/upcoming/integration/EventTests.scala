package com.gu.upcoming.integration

import com.gu.integration.IntegrationHttpClient._
import com.gu.upcoming.models.Event

class EventTests extends RequiresRunningApplication {

  val baseUri = "http://localhost:8080"
  val eventResourceUri = "%s/event" format baseUri

  feature("Creating events") {

    scenario("create event") {
      val description = "some new event"
      when("We hit the HTTP inteface to create an event")
      post(eventResourceUri, Map("description" -> description))
      then("it should be in the database")
      val event = getEventByDescription(description)
      and("we should retrieve that event through the id")

      {
        val response = get(eventResourceUri + "/" + event.id)
        response should include(description)
      }
      and("we should see the event on the events page")

      {
        val response = get(eventResourceUri)
        response should include(description)
      }
    }

  }

  def getEventByDescription(description: String) = {
    val events = Event.all.filter(event => description == event.description)
    events.size should be(1)
    events.head
  }

}