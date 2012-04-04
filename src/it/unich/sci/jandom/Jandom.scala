/**
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
 *
 * (c) 2011 Gianluca Amato
 */

package it.unich.sci.jandom;
import parma_polyhedra_library.{Parma_Polyhedra_Library => PPL}

object Jandom extends App {

  System.load("/usr/local/lib/ppl/libppl_java.so")
  PPL.initialize_library()
 
  {
    val params = new targets.Parameters(domains.PPLCPolyhedron)
    params.narrowing = new narrowings.FixedStepsNarrowing(params.narrowing,2)  
    val source = scala.io.Source.fromFile("examples/Random/octagon-3.R").getLines.mkString("\n")
    val parsed = parsers.RandomParser.parseProgram(source)  
    if (parsed.successful) {
	  val program = parsed.get 
 	  val bb = new annotations.BlackBoard(program)
//      program.analyze(domains.PPLCPolyhedron, params, ann)
      println(program)  
      println(bb)  
    } else {
      println(parsed)
    }    
  }
  
  {
    val params = new targets.Parameters(domains.PPLCPolyhedron)
    params.widening = new widenings.DelayedWidening(params.widening,2)
    val source = scala.io.Source.fromFile("examples/LPinv/berkeley.in").getLines.mkString("\n")
    val parsed = parsers.LPInvParser.parseProgram(source)  
    if (parsed.successful) {
	  val program = parsed.get 
 	  val bb = new annotations.BlackBoard(program)
      program.analyze(domains.PPLCPolyhedron, params, bb)
      println(bb)  
    } else {
      println(parsed)
    }      
  }
  
  PPL.finalize_library()
}
