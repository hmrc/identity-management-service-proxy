package uk.gov.hmrc.identitymanagementserviceproxy.service

import play.api.Logging
import play.api.http.HeaderNames
import play.api.libs.ws.WSRequest

import javax.inject.Singleton

@Singleton
class AuthorizationDecorator extends Logging {

  def decorate(wsRequest: WSRequest, maybeAuthHeader: Option[String]) : WSRequest = {
    if (!wsRequest.headers.contains(HeaderNames.AUTHORIZATION) && maybeAuthHeader.isDefined) {
      logger.info("Outbound request has no auth header, setting explicitly from inbound.")
      wsRequest.withHttpHeaders((HeaderNames.AUTHORIZATION, maybeAuthHeader.get))
    } else {
      wsRequest
    }
  }
}
