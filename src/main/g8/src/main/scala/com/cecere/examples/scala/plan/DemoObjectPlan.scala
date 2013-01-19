package com.cecere.examples.scala.plan

import unfiltered.filter.Plan
import javax.servlet.ServletRequest
import org.clapper.avsl.Logger
import unfiltered.request._
import unfiltered.response._
import net.liftweb.json._
import com.cecere.examples.scala.domain.DemoObject
import com.cecere.examples.scala.service.DemoObjectService
import com.cecere.examples.scala.service.DbDemoObjectService

//A Plan that has a dependency on DemoObjectService
class DemoObjectPlan(implicit val demoObjectService:DemoObjectService,implicit val formats:Formats) extends Plan {
  val logger = Logger(classOf[DemoObjectPlan])
 
  /*
   * We first match the request by path using match{case} expressions.
   * Then we match again by HttpMethod to figure out the action. These can be combined but it results
   * in lots of repeated Path() matches
   */
  def intent ={
     // match /demoObjects with nothing after it.
     case req @ Path(Seg("demoObjects" :: Nil)) => req match {
       case req @ GET(_) =>
       	 logger.debug("GET /demoObjects/")
       	 val demoObjects = demoObjectService.findAll
       	 JsonContent ~> Ok ~> ResponseString(Serialization.write(demoObjects))
       // req @ binds whatever matched to the req variable so it can be used later
       case req @ POST(_) => 
         logger.debug("POST /demoObjects/")
         // you can't get the request body from the match because it gets consumed and would only match once.
         // we use liftweb to read the body and deserialize the json now that we know we need it
         val demoObject = Serialization.read[DemoObject](Body.reader(req))
         // we build the response saying it will contain JsonContent, 201 http code (Created), and 
         // then the serialized object using liftweb again
         JsonContent ~> Created ~> ResponseString(Serialization.write(demoObjectService.create(demoObject)))
       case _ => Pass //not interested. let a different plan deal with this
    }
    // match /demoObjects/<id> with nothing after it. also binds the <id> value to the id variable for use
    case req @ Path(Seg("demoObjects" :: id :: Nil)) => req match {
       case req @ GET(_) =>
       	 logger.debug("GET /demoObjects/"+id)
       	 // Options allow you to get Some value, or No value, without dealing with null or exceptions
       	 val demoObjectOption:Option[DemoObject] = demoObjectService.find(id toLong)
       	 demoObjectOption match {
       	   case None => NotFound //no content, just result in the status code
       	   // both matches the Some() case AND extracts the actual demoObject to a variable for use.
       	   case Some(dObj) => JsonContent ~> Ok ~> ResponseString(Serialization.write(dObj))
       	 }
       case req @ PUT(_) => 
         logger.debug("PUT /demoObjects/"+id)
         val demoObject = Serialization.read[DemoObject](Body.reader(req))
         JsonContent ~> Ok ~> ResponseString(Serialization.write(demoObjectService.update((id toLong),demoObject)))
       case req @ DELETE(_) => 
         logger.debug("DELETE /demoObjects/"+id)
         demoObjectService.delete(id toLong)
         NoContent
       case _ => Pass //not interested. let a different plan deal with this
    }
  }
}

