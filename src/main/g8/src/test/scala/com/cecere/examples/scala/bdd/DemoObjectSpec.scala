package com.example

import org.specs._
import dispatch._
import com.cecere.examples.scala.plan.DemoObjectPlan
import com.cecere.examples.scala.domain.DemoObject
import net.liftweb.json.Serialization
import net.liftweb.json.ShortTypeHints
import java.io.InputStreamReader
import org.apache.http.client.methods.HttpPost
import unfiltered.request.QParams.Fail
import com.cecere.examples.scala.plan.wired.WiredDemoObjectPlan

//special import that allows usage of any implicit objects within the database configuration module (ie. demoObjectService)
import com.cecere.examples.scala.conf.DatabaseConfiguration._

//<< = post
//<<< = put
object DemoObjectPlanSpec extends Specification 
	with unfiltered.spec.jetty.Served {
  
  import dispatch._
  
  implicit val formats=Serialization.formats(ShortTypeHints(List(classOf[DemoObject])))
  
  def setup = { _.filter(new WiredDemoObjectPlan) } //use wired object for test since that is the plan that is really used in web.xml
  
  val http = new Http
  
  "demo object rest api" should {
    "serve list of demo objects" in {
      val status = http x (host / "demoObjects" as_str) {
        case (code, _, _, _) => code
        case _ => Fail
      }
      status must_== 200
      demoObjectService.deleteAll
    }
  }
  
  "demo object rest api" should {
    "get single demoObject" in {
      val testObj = demoObjectService.create(new DemoObject(1,"test"))
      http x (host / "demoObjects" / "1" as_str) {
        case (code, _, Some(json), _) => {
        	code must_== 200 
        	val retObj:DemoObject = Serialization.read(new InputStreamReader(json.getContent()))
        	retObj.name must_== "test"
        }
        case _ => Fail
      }
      demoObjectService.deleteAll
    }
  }
  
  "demo object rest api" should {
    "create single demoObject" in {
      val newObj:DemoObject = new DemoObject(1,"test")
      http x (((host / "demoObjects") << Serialization.write(newObj))as_str) {
        case (code, _, Some(json), _) => {
        	code must_== 201 
        	val retObj:DemoObject = Serialization.read(new InputStreamReader(json.getContent()))
        	retObj.name must_== "test"
        }
        case _ => Fail
      }
      demoObjectService.deleteAll
    }
  }
  
  "demo object rest api" should {
    "update single demoObject" in {
      val testObjExists = demoObjectService.create(new DemoObject(1,"testexists"))
      val newObj:DemoObject = new DemoObject(1,"testupdated")
      http x (((host / "demoObjects" / "1") <<< Serialization.write(newObj))as_str) {
        case (code, _, Some(json), _) => {
        	code must_== 200 
        	val retObj:DemoObject = Serialization.read(new InputStreamReader(json.getContent()))
        	retObj.name must_== "testupdated"
        }
        case _ => Fail
      }
      demoObjectService.deleteAll
    }
  }
  
  "demo object rest api" should {
    "delete single demoObject" in {
      val testObjExists = demoObjectService.create(new DemoObject(1,"testexists"))
      http x (host.DELETE / "demoObjects" / "1" as_str) {
        case (code, _, _, _) => {
        	code must_== 204
        	demoObjectService.find(1) must_== None
        }
        case _ => Fail
      }
      demoObjectService.deleteAll
    }
  }
  
}
