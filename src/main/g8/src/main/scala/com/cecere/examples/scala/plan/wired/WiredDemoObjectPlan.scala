package com.cecere.examples.scala.plan.wired

import javax.servlet.ServletRequest
import com.cecere.examples.scala.plan.DemoObjectPlan

//special import that gives all dependencies for injection
import com.cecere.examples.scala.conf.DatabaseConfiguration._

/**
 * Special class that comes pre-wired with implicits from imports. This allows instantiation without implicits in scope.
 * you can think of this as the spring context that wires everything together
 **/
class WiredDemoObjectPlan extends DemoObjectPlan {

}