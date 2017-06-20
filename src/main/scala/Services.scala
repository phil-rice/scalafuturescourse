import scala.concurrent.{ExecutionContext, Future}

trait Service[Req, Res] extends (Req => Future[Res])

trait ArticleService extends Service[ArticleUrl, ArticleAndImageUrls]

trait ImageService extends Service[ImageUrl, Image]

trait CompressionService extends Service[Image, CompressedImage]

trait FullArticleService extends Service[ArticleUrl, ArticleAndCompressedImages]

class FullArticleServiceImpl(implicit articleService: ArticleService, imageService: ImageService, compressionService: CompressionService, ec: ExecutionContext) extends FullArticleService {
  override def apply(articleUrl: ArticleUrl): Future[ArticleAndCompressedImages] = ???
}
