package com.cecere.examples.scala.service

import org.junit.Test
import org.easymock.EasyMock._
import com.cecere.examples.scala.domain.DemoObject
import org.squeryl.Table
import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.PrimitiveTypeMode._ //used for implicit conversions for squeryl dsl like ===
import scala.Function0

class DbDemoObjectServiceUnitTest {

  //define implicit mocks
  implicit val demoObjects:Table[DemoObject] =createMock(classOf[Table[DemoObject]])
  val demoObjectService:DemoObjectService = new DbDemoObjectService()
  val input:DemoObject = createMock(classOf[DemoObject])
  
  @Test
  def testCreate(){
    expect(demoObjects.insert(input)).andReturn(input) //verify insert is called
    replay(input)
    replay(demoObjects)
    
    val ret:DemoObject = demoObjectService.create(input)
    
    verify(input)
    verify(demoObjects)
  }
}