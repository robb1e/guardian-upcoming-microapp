package com.gu.whatsnext.management

import com.gu.management._
import request._
import mongodb._

object Metrics {
  lazy val all = HttpRequestsTimingMetric ::
    ClientErrorCounter ::
    ServerErrorCounter ::
    ExceptionCountMetric ::
    MongoRequests ::
    JvmMetrics.all
}