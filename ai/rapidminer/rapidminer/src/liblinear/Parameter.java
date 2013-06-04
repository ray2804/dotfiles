/*
 *  RapidMiner
 *
 *  Copyright (C) 2001-2013 by Rapid-I and the contributors
 *
 *  Complete list of developers available at our web site:
 *
 *       http://rapid-i.com
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package liblinear;

import static liblinear.Linear.copyOf;


/**
 *  C is the cost of constraints violation. (we usually use 1 to 1000)
 *  eps is the stopping criterion. (we usually use 0.01).
 *
 *  nr_weight, weight_label, and weight are used to change the penalty
 *  for some classes (If the weight for a class is not changed, it is
 *  set to 1). This is useful for training classifier using unbalanced
 *  input data or with asymmetric misclassification cost.
 *
 *  nr_weight is the number of elements in the array weight_label and
 *  weight. Each weight[i] corresponds to weight_label[i], meaning that
 *  the penalty of class weight_label[i] is scaled by a factor of weight[i].
 *
 *  If you do not want to change penalty for any of the classes,
 *  just set nr_weight to 0.
 *
 *  *NOTE* To avoid wrong parameters, check_parameter() should be
 *  called before train().
 */
public final class Parameter {

   double     C;

   /** stopping criteria */
   double     eps;

   SolverType solverType;

   double[]   weight      = null;

   int[]      weightLabel = null;

   public Parameter( SolverType solverType, double C, double eps ) {
      setSolverType(solverType);
      setC(C);
      setEps(eps);
   }

   public void setWeights( double[] weights, int[] weightLabels ) {
      if ( weights == null ) throw new IllegalArgumentException("'weight' must not be null");
      if ( weightLabels == null || weightLabels.length != weights.length )
         throw new IllegalArgumentException("'weightLabels' must have same length as 'weight'");
      this.weightLabel = copyOf(weightLabels, weightLabels.length);
      this.weight = copyOf(weights, weights.length);
   }

   public int getNumWeights() {
      if ( weight == null ) return 0;
      return weight.length;
   }

   public void setC( double C ) {
      if ( C <= 0 ) throw new IllegalArgumentException("C must not be <= 0");
      this.C = C;
   }

   public void setEps( double eps ) {
      if ( eps <= 0 ) throw new IllegalArgumentException("eps must not be <= 0");
      this.eps = eps;
   }

   public void setSolverType( SolverType solverType ) {
      if ( solverType == null ) throw new IllegalArgumentException("solver type must not be null");
      this.solverType = solverType;
   }
}
