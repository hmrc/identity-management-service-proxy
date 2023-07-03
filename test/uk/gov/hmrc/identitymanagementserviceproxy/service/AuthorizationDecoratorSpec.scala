package uk.gov.hmrc.identitymanagementserviceproxy.service

import org.scalatest.freespec.AsyncFreeSpec
import org.scalatest.matchers.must.Matchers
import play.api.test.Helpers.{AUTHORIZATION, POST}
import play.api.test.{FakeHeaders, FakeRequest}

class AuthorizationDecoratorSpec extends AsyncFreeSpec
  with Matchers {

  "Decorator " - {
    "must not supply auth header if already present" in {
      val decorator = new AuthorizationDecorator
      val request = FakeRequest(POST, "/identity-management-service-proxy/identity")
        .withHeaders(FakeHeaders(Seq(
          (AUTHORIZATION, "Cheese"))))

      val decorated = decorator.decorate(request, Some("xyz"))

    }
  }
}
