package com.cecere.examples.scala.conf

import com.cecere.examples.scala.service.DbDemoObjectService
import com.cecere.examples.scala.service.DemoObjectService
import com.cecere.examples.scala.plan.DemoObjectPlan
import org.squeryl.Schema
import org.squeryl.Table
import com.cecere.examples.scala.domain.DemoObject
import net.liftweb.json.Serialization
import net.liftweb.json.ShortTypeHints
/**
 * Scope object that defines all implicit dependencies to be used for a database driven implementation
 */
object DatabaseConfiguration extends Schema{
    //define formats we can serialize/deserialize
    implicit val formats=Serialization.formats(ShortTypeHints(List(classOf[DemoObject])))
	//define implicit squeryl schema objects
	implicit val demoObjects:Table[DemoObject] = table[DemoObject]
	//create services
	implicit val demoObjectService:DemoObjectService = new DbDemoObjectService()
}
