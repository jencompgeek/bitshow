package bitshow

import unfiltered.request._
import unfiltered.response._

import net.liftweb.json.JsonDSL._

import org.clapper.avsl.Logger

/** This filter should handle conversions requested
 * by the client page, serve listings of available
 * images and converters, and serve the images themselves */
object API extends unfiltered.filter.Plan {
  import LazyJson._
  val logger = Logger(getClass)

  def intent = {
    case POST(Path("/convert")) =>
      logger.debug("Convert request")
      Json(("hello" -> "world") ~ ("id" -> "something") )
      
    // TODO list images
    
    // TODO list converters
    case GET(Path("/converters")) => Json(Converters.names.toJson)
    
    case GET(Path(Seg("images" :: id :: Nil))) =>
      DefaultStore.get(id).map(i => ContentType(i.contentType) ~> ResponseBytes(i.bytes)).getOrElse(NotFound)
      
    case GET(Path("/test")) =>
      val bytes = org.apache.commons.io.IOUtils.toByteArray(
        getClass.getResource("/www/img/ny-scala.png").openStream
      )
      val logo = Item("image/png", bytes)
      val item = GraySquare(logo)
      ContentType(item.contentType) ~> ResponseBytes(item.bytes)
  }
}
