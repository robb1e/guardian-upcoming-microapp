package com.gu.mongodb

import com.novus.salat._
import com.mongodb.DBObject
import com.mongodb.casbah.commons.conversions.scala.{ RegisterJodaTimeConversionHelpers, RegisterConversionHelpers }
import org.bson.types.ObjectId

object SalatTypeConversions {
  implicit def caseClass2DBObject[A <: CaseClass](a: A)(implicit ctx: com.novus.salat.Context, m: scala.Predef.Manifest[A]): DBObject = {
    grater[A].asDBObject(a)
  }
}

trait CasbahConverstionHelpers {
  RegisterConversionHelpers()
  RegisterJodaTimeConversionHelpers()
}