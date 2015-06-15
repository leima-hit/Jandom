/**
 * Copyright 2015 Gianluca Amato <gamato@unich.it>
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

package it.unich.jandom.fixpoint.finite

import it.unich.jandom.fixpoint._

/**
 * A fixpoint solver based on priority worklists.
 */
object PriorityWorkListSolver extends FixpointSolver {
  /**
   * It solves a finite equation system by using a worklist based method with priorities.
   * @param eqs the equation system to solve.
   * @param start the initial assignment.
   * @param ordering an ordering which specified priorities between unknowns.
   * @param litener the listener whose callbacks are called for debugging and tracing.
   */
  def apply[U, V](eqs: FiniteEquationSystem[U, V], start: U => V, ordering: Ordering[U], listener: FixpointSolverListener[U, V] = FixpointSolverListener.EmptyListener) = {
    val current = (collection.mutable.HashMap.empty[U, V]).withDefault(start)
    listener.initialized(current)
    var workList = scala.collection.mutable.PriorityQueue.empty[U](ordering)
    workList ++= eqs.unknowns
    while (!workList.isEmpty) {
      val x = workList.dequeue()
      val newval = eqs.body(current)(x)
      listener.evaluated(current, x, newval)
      if (newval != current(x)) {
        current(x) = newval
        workList ++= eqs.infl.image(x)
      }
    }
    current
  }
}
