import org.scalatest.{FlatSpec, Matchers}
import org.scalamock.scalatest.MockFactory

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._
import Futures._

class ArticleAndCompressedImageServiceSpec extends FlatSpec with Matchers with MockFactory {

  val articleUrl = ArticleUrl("someArticleUrl")
  val imageUrl1 = ImageUrl("one")
  val imageUrl2 = ImageUrl("two")
  val imageUrl3 = ImageUrl("three")
  val image1 = Image("one")
  val image2 = Image("two")
  val image3 = Image("three")
  val compressed1 = CompressedImage("one")
  val compressed2 = CompressedImage("two")
  val compressed3 = CompressedImage("three")
   val article = Article("someArticle")
  val articleWithNoImageUrls = ArticleAndImageUrls(article, List())
  val articleAndImageUrls = ArticleAndImageUrls(article, List(imageUrl1, imageUrl2, imageUrl3))

  def setup(fn: (FullArticleService, ArticleService, ImageService, CompressionService) => Unit) = {
    implicit val articleService = stub[ArticleService]
    implicit val imageService = stub[ImageService]
    implicit val compressionService = stub[CompressionService]

    (imageService.apply _).when(imageUrl1).returns(Future.successful(image1))
    (imageService.apply _).when(imageUrl2).returns(Future.successful(image2))
    (imageService.apply _).when(imageUrl3).returns(Future.successful(image3))

    (compressionService.apply _).when(image1).returns(Future.successful(compressed1))
    (compressionService.apply _).when(image2).returns(Future.successful(compressed2))
    (compressionService.apply _).when(image3).returns(Future.successful(compressed3))

    fn(new FullArticleServiceImpl, articleService, imageService, compressionService)
  }

  behavior of "FullArticleService"


  it should "get an article from the article service which has no images and return it" in {
    setup { (fullArticleService, articleService, imageService, compressionService) =>
      (articleService.apply _).when(articleUrl).returns(Future.successful(articleWithNoImageUrls))
      fullArticleService.apply(articleUrl).await() shouldBe ArticleAndCompressedImages(article,List())
    }
  }
  it should "get an article from the article service, then get the images, compress them and return a future of an article with the compressed images" in {
    setup { (fullArticleService, articleService, imageService, compressionService) =>
      (articleService.apply _).when(articleUrl).returns(Future.successful(articleAndImageUrls))
      fullArticleService.apply(articleUrl).await() shouldBe ArticleAndCompressedImages(article, List(CompressedImage("one"), CompressedImage("two"), CompressedImage("three")))
    }
  }

}
