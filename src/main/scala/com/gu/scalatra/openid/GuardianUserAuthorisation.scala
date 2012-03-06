package com.gu.scalatra.openid

trait GuardianUserAuthorisation extends UserAuthorisation {

  def isUserAuthorised(user: User) = user.email.endsWith("@guardian.co.uk")

}