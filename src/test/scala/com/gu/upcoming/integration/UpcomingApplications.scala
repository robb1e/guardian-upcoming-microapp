package com.gu.upcoming.integration

import com.gu.integration._
import com.gu.management.Loggable
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import com.gu.upcoming.models.Event

object UpcomingApplications extends AppServer with LazyStop {

  override val port = 8080

  lazy val apps = List(UpcomingApp)

  object UpcomingApp extends ClasspathWebApp {
    lazy val srcPath = "."
    override lazy val contextPath = "/"
  }

}

trait RequiresRunningApplication extends Suite with BeforeAndAfterAll with FeatureSpec with GivenWhenThen with ShouldMatchers with Loggable {

  override protected def beforeAll() {
    UpcomingApplications.start
    Event.all().map(event => Event.delete(event))
  }

  override protected def afterAll() {
    UpcomingApplications.stopUnlessSomeoneCallsStartAgainSoon
    Event.all().map(event => Event.delete(event))
  }
}
