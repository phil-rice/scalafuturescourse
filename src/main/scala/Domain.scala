case class ArticleUrl(id: String)

case class Article(content: String)

case class ImageUrl(id: String)

case class ArticleAndImageUrls(article: Article, imageUrls: Seq[ImageUrl])

case class ArticleAndCompressedImages(article: Article, images: Seq[CompressedImage])

case class Image(data: String)

case class CompressedImage(x: String)
