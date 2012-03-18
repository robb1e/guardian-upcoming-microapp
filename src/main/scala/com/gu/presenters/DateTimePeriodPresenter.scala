package com.gu.presenters

import org.joda.time._
import format._
import java.util.Locale

case class DateTimePeriodPresenter(period: Period) {

  override def toString = humanFormatted.print(period)

  lazy val humanFormatted = new PeriodFormatterBuilder()
    .appendYears()
    .appendSuffix(" year", " years")
    .appendSeparator(", ")
    .printZeroRarelyLast()
    .appendMonths()
    .appendSuffix(" month", " months")
    .appendSeparator(", ")
    .appendDays()
    .appendSuffix(" day", " days")
    .appendSeparator(", ")
    .appendHours()
    .appendSuffix(" hour", " hours")
    .appendSeparator(" and ")
    .appendMinutes()
    .appendSuffix(" minute", " minutes")
    .toFormatter();

}

object DateTimePresenter {

  def rfc1123(dateTime: DateTime) = rfc1123DateFormat.print(dateTime)

  def iso8601(dateTime: DateTime) = iso8601DateFormat.print(dateTime)

  lazy val iso8601DateFormat = ISODateTimeFormat.dateTimeNoMillis

  lazy val rfc1123DateFormat = DateTimeFormat
    .forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'")
    .withLocale(Locale.US)
    .withZone(DateTimeZone.UTC);

}