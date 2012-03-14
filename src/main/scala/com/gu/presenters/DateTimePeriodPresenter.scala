package com.gu.presenters

import org.joda.time._
import format._

case class DateTimePeriodPresenter(period: Period) {

  val formatter = PeriodFormat.getDefault

  override def toString = formatter.print(period)

}