case class ArticleUrl(id: String)

case class Article(content: String) {
  def withImages(images: Seq[CompressedImage]) = ArticleAndCompressedImages(this, images)
}

case class ImageUrl(id: String)

case class ArticleAndImageUrls(article: Article, imageUrls: Seq[ImageUrl]){
  def withImages(images: Seq[CompressedImage]) = ArticleAndCompressedImages(article, images)
}

case class ArticleAndCompressedImages(article: Article, images: Seq[CompressedImage])

case class Image(data: String)

case class CompressedImage(x: String)
