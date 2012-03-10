package com.gu.upcoming.filters

import com.gu.management._
import logback.LogbackLevelPage
import com.gu.upcoming.management._

class Management extends ManagementFilter {
  lazy val pages =
    new HealthcheckManagementPage() ::
      new Switchboard(Switches.all) ::
      new PropertiesPage(ConfigurationManager.config.toString) ::
      new LogbackLevelPage() ::
      new ManifestPage() ::
      new StatusPage("Whats Next", Metrics.all) ::
      Nil
}

