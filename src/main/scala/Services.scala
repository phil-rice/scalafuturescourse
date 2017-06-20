import scala.concurrent.{ExecutionContext, Future}

trait Service[Req, Res] extends (Req => Future[Res])

trait ArticleService extends Service[ArticleUrl, ArticleAndImageUrls]

trait ImageService extends Service[ImageUrl, Image]

trait CompressionService extends Service[Image, CompressedImage]

trait FullArticleService extends Service[ArticleUrl, ArticleAndCompressedImages]

class FullArticleServiceImpl(implicit articleService: ArticleService, imageService: ImageService, compressionService: CompressionService, ec: ExecutionContext) extends FullArticleService {
  override def apply(articleUrl: ArticleUrl): Future[ArticleAndCompressedImages] = {
    articleService(articleUrl).flatMap {
      case (ArticleAndImageUrls(article, imageIds)) =>
        Future.sequence(imageIds.map(imageId => imageService(imageId).flatMap(compressionService))).
          map(article.withImages)
    }
  }
}

import Arrow._

class FullArticleServiceImplWithArrows(implicit articleService: ArticleService, imageService: ImageService, compressionService: CompressionService, ec: ExecutionContext) extends FullArticleService {
  override def apply(articleUrl: ArticleUrl) =
    for {ArticleAndImageUrls(article, imageUrls) <- articleUrl ~> articleService
         articleAndCompressedImages <- imageUrls ~> imageService >~> compressionService >>> article.withImages}
      yield articleAndCompressedImages
}

class FullArticleServiceImplWithArrows2(implicit articleService: ArticleService, imageService: ImageService, compressionService: CompressionService, ec: ExecutionContext) extends FullArticleService {
  override def apply(articleUrl: ArticleUrl) =
    articleUrl ~> articleService >~> {
      case ArticleAndImageUrls(article, imageUrls) => imageUrls ~> imageService >~> compressionService >>> article.withImages
    }
}

class FullArticleServiceImplWithArrows3(implicit articleService: ArticleService, imageService: ImageService, compressionService: CompressionService, ec: ExecutionContext) extends FullArticleService {
  override def apply(articleUrl: ArticleUrl) =
    articleUrl ~> articleService >~> { articleAndImageIds => articleAndImageIds.imageUrls ~> imageService >~> compressionService >>> articleAndImageIds.withImages}
}
