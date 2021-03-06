package com.sksamuel.elastic4s.requests.indexes

import com.sksamuel.elastic4s.testkit.DockerTests
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.util.Try

class ClearCacheRequestTest extends AnyWordSpec with Matchers with DockerTests {

  Try {
    client.execute {
      deleteIndex("clearcache1")
    }.await
  }

  Try {
    client.execute {
      deleteIndex("clearcache2")
    }.await
  }

  client.execute {
    createIndex("clearcache1").mapping(
      properties(
        textField("name")
      )
    )
  }.await

  client.execute {
    createIndex("clearcache2").mapping(
      properties(
        textField("name")
      )
    )
  }.await

  "ClearCache" should {
    "support single index" in {
      val resp = client.execute {
        clearCache("clearcache1")
      }.await
      resp.result.shards.successful should be > 0
    }

    "support multiple types" in {
      val resp = client.execute {
        clearCache("clearcache1", "clearcache2")
      }.await
      resp.result.shards.successful should be > 0
    }
  }
}
