/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.identitymanagementserviceproxy.controller

import com.github.tomakehurst.wiremock.client.WireMock.{status => _, _}
import org.scalatest.OptionValues
import org.scalatest.freespec.AsyncFreeSpec
import org.scalatest.matchers.must.Matchers
import play.api.http.ContentTypes
import play.api.http.Status.OK
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Helpers._
import play.api.test.{FakeHeaders, FakeRequest}
import play.api.{Application, Configuration}
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.test.{HttpClientV2Support, WireMockSupport}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

class IdmsControllerSpec extends AsyncFreeSpec
  with Matchers
  with WireMockSupport
  with HttpClientV2Support
  with OptionValues {

  "POST request" - {
    "must be forwarded with headers" in {
      val requestBody = """{"cheese":"crackers"}"""
      val responseBody = """{"jam": "scones"}"""

      stubFor(
        post(urlEqualTo("/identity-management-service-stubs/identity"))
          .withHeader(ACCEPT, equalTo(ContentTypes.JSON))
          .withHeader(CONTENT_TYPE, equalTo(ContentTypes.JSON))
          .withHeader(AUTHORIZATION, equalTo("Basic dGVzdC1lbXMtY2xpZW50LWlkOnRlc3QtZW1zLXNlY3JldA=="))
          .withRequestBody(
            equalToJson(requestBody)
          )
          .willReturn(
            aResponse()
              .withBody(responseBody)
          )
      )

      val application = buildApplication()
      running(application) {
        val request = FakeRequest(POST, "/identity-management-service-proxy/identity")
          .withHeaders(FakeHeaders(Seq(
            (AUTHORIZATION, "Basic dGVzdC1lbXMtY2xpZW50LWlkOnRlc3QtZW1zLXNlY3JldA=="),
            (ACCEPT, "application/json"),
            (CONTENT_TYPE, "application/json"),
            ("x-api-key", "cheese")
          )))
          .withBody(requestBody)
        val result = route(application, request).value

        status(result) mustBe OK
        contentAsString(result) mustBe responseBody
      }
    }
  }

  "GET request" - {
    "must be forwarded with headers" in {
      val responseBody = """{"jam": "scones"}"""

      stubFor(
        get(urlEqualTo("/identity-management-service-stubs/identity/12345"))
          .withHeader(ACCEPT, equalTo(ContentTypes.JSON))
          .withHeader(AUTHORIZATION, equalTo("Basic dGVzdC1lbXMtY2xpZW50LWlkOnRlc3QtZW1zLXNlY3JldA=="))
          .willReturn(
            aResponse()
              .withBody(responseBody)
          )
      )

      val application = buildApplication()
      running(application) {
        val request = FakeRequest(GET, "/identity-management-service-proxy/identity/12345")
          .withHeaders(FakeHeaders(Seq(
            (AUTHORIZATION, "Basic dGVzdC1lbXMtY2xpZW50LWlkOnRlc3QtZW1zLXNlY3JldA=="),
            (ACCEPT, "application/json")
          )))
        val result = route(application, request).value

        status(result) mustBe OK
        contentAsString(result) mustBe responseBody
      }
    }
  }

  "PUT request" - {
    "must be forwarded with headers" in {
      val requestBody = """{"cheese":"crackers"}"""
      val responseBody = """{"jam": "scones"}"""

      stubFor(
        put(urlEqualTo("/identity-management-service-stubs/identity/1234"))
          .withHeader(ACCEPT, equalTo(ContentTypes.JSON))
          .withHeader(CONTENT_TYPE, equalTo(ContentTypes.JSON))
          .withHeader(AUTHORIZATION, equalTo("Basic dGVzdC1lbXMtY2xpZW50LWlkOnRlc3QtZW1zLXNlY3JldA=="))
          .withRequestBody(
            equalToJson(requestBody)
          )
          .willReturn(
            aResponse()
              .withBody(responseBody)
          )
      )

      val application = buildApplication()
      running(application) {
        val request = FakeRequest(PUT, "/identity-management-service-proxy/identity/1234")
          .withHeaders(FakeHeaders(Seq(
            (AUTHORIZATION, "Basic dGVzdC1lbXMtY2xpZW50LWlkOnRlc3QtZW1zLXNlY3JldA=="),
            (ACCEPT, "application/json"),
            (CONTENT_TYPE, "application/json"),
          )))
          .withBody(requestBody)
        val result = route(application, request).value

        status(result) mustBe OK
        contentAsString(result) mustBe responseBody
      }
    }
  }

  "DELETE request" - {
    "must be forwarded with headers" in {

      stubFor(
        delete(urlEqualTo("/identity-management-service-stubs/identity/12345"))
          .withHeader(ACCEPT, equalTo(ContentTypes.JSON))
          .withHeader(AUTHORIZATION, equalTo("Basic dGVzdC1lbXMtY2xpZW50LWlkOnRlc3QtZW1zLXNlY3JldA=="))
          .willReturn(
            aResponse()
              .withStatus(200)
          )
      )

      val application = buildApplication()
      running(application) {
        val request = FakeRequest(DELETE, "/identity-management-service-proxy/identity/12345")
          .withHeaders(FakeHeaders(Seq(
            (AUTHORIZATION, "Basic dGVzdC1lbXMtY2xpZW50LWlkOnRlc3QtZW1zLXNlY3JldA=="),
            (ACCEPT, "application/json")
          )))
        val result = route(application, request).value

        status(result) mustBe OK
      }
    }
  }

  private def buildApplication(): Application = {
    val servicesConfig = new ServicesConfig(
      Configuration.from(Map(
        "microservice.services.idms.host" -> wireMockHost,
        "microservice.services.idms.port" -> wireMockPort,
        "microservice.services.idms.path" -> "identity-management-service-stubs"
      ))
    )

    new GuiceApplicationBuilder()
      .overrides(
        bind[ServicesConfig].toInstance(servicesConfig),
        bind[HttpClientV2].toInstance(httpClientV2)
      )
      .build()
  }

}