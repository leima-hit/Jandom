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

package it.unich.sci.jandom
package targets.linearcondition

import domains.NumericalProperty

/**
 * An atomic condition for a non-deterministic choice.
 * @author Gianluca Amato <amato@sci.unich.it>
 */
object BRandomCond extends LinearCond {
  def opposite = BRandomCond
  override def analyze[Property <: NumericalProperty[Property]] (input: Property) = input
  override def mkString(vars: Seq[String]) = "brandom()"
}

/**
 * The valid condition.
 * @author Gianluca Amato <amato@sci.unich.it>
 */
object TrueCond extends LinearCond {
  def opposite = FalseCond
  override def analyze[Property <: NumericalProperty[Property]] (input: Property) = input  
  override def mkString(vars: Seq[String]) = "TRUE"
}

/**
 * The false condition.
 * @author Gianluca Amato <amato@sci.unich.it>
 */
object FalseCond extends LinearCond {
  def opposite = TrueCond
  override def analyze[Property <: NumericalProperty[Property]] (input: Property): Property = input.empty
  override def mkString(vars: Seq[String]) = "FALSE"
}
