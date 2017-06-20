import scala.concurrent.{ExecutionContext, Future}

trait Arrow {


  implicit class FutureSeqPimper[T](seq: Seq[Future[T]])(implicit executionContext: ExecutionContext) {
    def >>>[T1](fn: Seq[T] => T1): Future[T1] = Future.sequence(seq).map(fn)

    def >~>[T1](fn: T => Future[T1]): Seq[Future[T1]] = seq.map(_.flatMap(fn))

    def ~>[T1](fn: T => T1): Seq[Future[T1]] = seq.map(_.map(fn))
  }

  implicit class FuturePimper[T](f: Future[T])(implicit executionContext: ExecutionContext) {
    def ~>[T1](fn: T => T1) = f.map(fn)

    def >~>[T1](fn: T => Future[T1]) = f.flatMap(fn)
  }

  implicit class SeqPimper[T](f: Seq[T]) {
    def ~>[T1](fn: T => T1) = f.map(fn)
  }

  implicit class ValuePimper[T](f: T) {
    def ~>[T1](fn: T => T1) = fn(f)
  }

}

object Arrow extends Arrow