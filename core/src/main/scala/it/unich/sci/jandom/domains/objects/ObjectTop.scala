/**
 * Copyright 2013 Gianluca Amato <gamato@unich.it>
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

package it.unich.sci.jandom.domains.objects

import it.unich.sci.jandom.domains._

/**
 * @author Gianluca Amato <gamato@unich.it>
 *
 */
class ObjectTop extends TopLike[ObjectTop] with ObjectProperty[ObjectTop]

/**
 * The most abstract domain for object analysis.
 * @author Gianluca Amato <gamato@unich.it>
 */
object ObjectTopDomain extends ObjectDomain with WithTop with WithBottom {
  type Property = ObjectTop
  val top = new ObjectTop()
  val bottom = top
}