/**
 * Copyright 2013 Gianluca Amato
 *
 * This file is part of JANDOM: JVM-based Analyzer for Numerical DOMains
 * JANDOM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JANDOM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty ofa
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JANDOM.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.unich.sci.jandom.targets

import org.scalatest.FunSuite
import it.unich.sci.jandom.domains.PPLCPolyhedron
import it.unich.sci.jandom.parsers.NumericalPropertyParser
import it.unich.sci.jandom.targets.jvm._
import soot._
import it.unich.sci.jandom.domains.PPLProperty
import it.unich.sci.jandom.domains.PPLDomain
import it.unich.sci.jandom.domains.PPLBoxDouble
import scala.collection.mutable.ArrayStack

/**
 * Simple test suite for the JVMSoot target.
 * @author Gianluca Amato
 *
 */
class JVMSootSuite extends FunSuite {
  val scene = Scene.v()
  scene.setSootClassPath(scene.defaultClassPath + ":examples/Java/")
  val c = scene.loadClass("SimpleTest", 1)
  c.setApplicationClass()
  val domain = PPLCPolyhedron

  val bafTests = Seq(
    ("sequential", Array(0), "i0 == 10"),
    ("conditional", Array(0), "i0 == 1"),
    ("loop", Array(0), "i0 == 10"),
    ("nested", Array(0, 1, -1), "i0 >= 9 && i1 == 10"),
    // "longassignment" -> "true",  unsupported bytecode
    ("topologicalorder", Array(0), "i0 >= 3 && i0 <= 4"))

  test("Baf analysis with fixed frame environment") {
    val params = new Parameters[BafMethod] {
      val domain = new JVMEnvFixedFrameDomain(JVMSootSuite.this.domain)
      //wideningFactory = MemoizingFactory(method)(DelayedWideningFactory(DefaultWidening, 2))
      //narrowingFactory = MemoizingFactory(method)(DelayedNarrowingFactory(NoNarrowing, 2))
      //debugWriter = new java.io.StringWriter
    }
    for ((methodName, frame, propString) <- bafTests) {
      val method = new BafMethod(c.getMethodByName(methodName))
      val ann = method.analyze(params)
      val env = Environment()
      val parser = new NumericalPropertyParser(env)
      val prop = parser.parseProperty(propString, domain).get
      val jvmenv = new JVMEnvDynFrame(frame, ArrayStack(), prop).toJVMEnvFixedFrame
      assert(ann(method.lastPP.get) === jvmenv , s"In the analysis of ${methodName}")
    }
  }

  test("Jimple analysis") {
    val tests = Seq(
      "sequential" -> "v0 == 0 && v1 == 10 && v2 == 10",
      "conditional" -> "v0 == 0 && v1 == 0 && v2 == 1 && v3==v3",
      "loop" -> "v0 >= 10 && v0 <= 11",
      "nested" -> "v0 >= v1 - 1 && v1 >= 10 && v1 <= 11 && v2==v2",
      // "longassignment" -> "true",  unsupported bytecode
      "topologicalorder" -> "v0==1 && v1-v2 == -1 &&  v2 >= 3 && v2 <= 4")

    val params = new Parameters[JimpleMethod] {
      val domain = JVMSootSuite.this.domain
    }
    for ((methodName, propString) <- tests) {
      val method = new JimpleMethod(c.getMethodByName(methodName))
      val ann = method.analyze(params)
      val env = Environment()
      val parser = new NumericalPropertyParser(env)
      val prop = parser.parseProperty(propString, domain).get
      assert(ann(method.lastPP.get) === prop, s"In the analysis of ${methodName}")
    }
  }
}