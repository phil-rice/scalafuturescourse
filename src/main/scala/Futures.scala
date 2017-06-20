import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

object Futures {

  implicit class FuturePimper[T](f: Future[T]) {
    def await(duration: Duration = 5 seconds) = Await.result(f, duration)
  }

}