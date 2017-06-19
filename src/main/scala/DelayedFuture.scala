import java.util.{Timer, TimerTask}

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.Try

object DelayedFuture {
  private val timer = new Timer(true)

  private def makeTask[T](body: => T)(schedule: TimerTask => Unit)(implicit ctx: ExecutionContext): Future[T] = {
    val prom = Promise[T]()
    schedule(
      new TimerTask {
        def run() = ctx.execute(() => prom.complete(Try(body)))

      })
    prom.future
  }

  def apply[T](delay: Long)(body: => T)(implicit ctx: ExecutionContext): Future[T] = {
    makeTask(body)(timer.schedule(_, delay))
  }

}