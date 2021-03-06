package com.github.blemale.scaffeine.example

import org.scalatest.{ FlatSpec, Matchers }
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.Future

class AsyncLoadingCacheExample
    extends FlatSpec
    with Matchers
    with ScalaFutures {

  "AsyncLoadingCache" should "be created from Scaffeine builder with synchronous loader" in {
    import com.github.blemale.scaffeine.{ AsyncLoadingCache, Scaffeine }
    import scala.concurrent.duration._

    val cache: AsyncLoadingCache[Int, String] =
      Scaffeine()
        .recordStats()
        .expireAfterWrite(1.hour)
        .maximumSize(500)
        .buildAsync((i: Int) => s"foo$i")

    whenReady(cache.get(1)) { value =>
      value should be("foo1")
    }
  }

  "AsyncLoadingCache" should "be created from Scaffeine builder with asynchronous loader" in {
    import com.github.blemale.scaffeine.{ AsyncLoadingCache, Scaffeine }
    import scala.concurrent.duration._
    import scala.concurrent.ExecutionContext.Implicits.global

    val cache: AsyncLoadingCache[Int, String] =
      Scaffeine()
        .recordStats()
        .expireAfterWrite(1.hour)
        .maximumSize(500)
        .buildAsyncFuture((i: Int) => Future.successful(s"foo$i"))

    whenReady(cache.get(1)) { value =>
      value should be("foo1")
    }
  }
}
