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

import static liblinear.Linear.NL;
import static liblinear.Linear.atof;
import static liblinear.Linear.atoi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class Train {

   public static void main( String[] args ) throws IOException {
      new Train().run(args);
   }

   private double    bias             = 1;
   private boolean   cross_validation = false;
   private String    inputFilename;
   private String    modelFilename;
   private int       nr_fold;
   private Parameter param            = null;
   private Problem   prob             = null;

   private void do_cross_validation() {
      int[] target = new int[prob.l];

      //long start, stop;
      //start = System.currentTimeMillis();
      Linear.crossValidation(prob, param, nr_fold, target);
      //stop = System.currentTimeMillis();
      //System.out.println("time: " + (stop - start) + " ms");

      int total_correct = 0;
      for ( int i = 0; i < prob.l; i++ )
         if ( target[i] == prob.y[i] ) ++total_correct;

      //System.out.printf("correct: %d" + NL, total_correct);
      //System.out.printf("Cross Validation Accuracy = %g%%\n", 100.0 * total_correct / prob.l);
   }

   private void exit_with_help() {
      System.out.println("Usage: train [options] training_set_file [model_file]" + NL //
         + "options:" + NL//
         + "-s type : set type of solver (default 1)" + NL//
         + "   0 -- L2-regularized logistic regression" + NL//
         + "   1 -- L2-loss support vector machines (dual)" + NL//
         + "   2 -- L2-loss support vector machines (primal)" + NL//
         + "   3 -- L1-loss support vector machines (dual)" + NL//
         + "   4 -- multi-class support vector machines by Crammer and Singer" + NL//
         + "-c cost : set the parameter C (default 1)" + NL//
         + "-e epsilon : set tolerance of termination criterion" + NL//
         + "   -s 0 and 2" + NL//
         + "       |f'(w)|_2 <= eps*min(pos,neg)/l*|f'(w0)|_2," + NL//
         + "       where f is the primal function, (default 0.01)" + NL//
         + "   -s 1, 3, and 4" + NL//
         + "       Dual maximal violation <= eps; similar to libsvm (default 0.1)" + NL//
         + "-B bias : if bias >= 0, instance x becomes [x; bias]; if < 0, no bias term added (default 1)" + NL//
         + "-wi weight: weights adjust the parameter C of different classes (see README for details)" + NL//
         + "-v n: n-fold cross validation mode" + NL//
      );
      System.exit(1);
   }


   Problem getProblem() {
      return prob;
   }

   double getBias() {
      return bias;
   }

   Parameter getParameter() {
      return param;
   }

   void parse_command_line( String argv[] ) {
      int i;

      // eps: see setting below
      param = new Parameter(SolverType.L2LOSS_SVM_DUAL, 1, Double.POSITIVE_INFINITY);
      // default values
      bias = 1;
      cross_validation = false;

      int nr_weight = 0;

      // parse options
      for ( i = 0; i < argv.length; i++ ) {
         if ( argv[i].charAt(0) != '-' ) break;
         if ( ++i >= argv.length ) exit_with_help();
         switch ( argv[i - 1].charAt(1) ) {
         case 's':
            param.solverType = SolverType.values()[atoi(argv[i])];
            break;
         case 'c':
            param.setC(atof(argv[i]));
            break;
         case 'e':
            param.setEps(atof(argv[i]));
            break;
         case 'B':
            bias = atof(argv[i]);
            break;
         case 'w':
            ++nr_weight;
            {
               int[] old = param.weightLabel;
               param.weightLabel = new int[nr_weight];
               System.arraycopy(old, 0, param.weightLabel, 0, nr_weight - 1);
            }

            {
               double[] old = param.weight;
               param.weight = new double[nr_weight];
               System.arraycopy(old, 0, param.weight, 0, nr_weight - 1);
            }

            param.weightLabel[nr_weight - 1] = atoi(argv[i - 1].substring(2));
            param.weight[nr_weight - 1] = atof(argv[i]);
            break;
         case 'v':
            cross_validation = true;
            nr_fold = atoi(argv[i]);
            if ( nr_fold < 2 ) {
               System.err.print("n-fold cross validation: n must >= 2\n");
               exit_with_help();
            }
            break;
         default:
            System.err.println("unknown option");
            exit_with_help();
         }
      }

      // determine filenames

      if ( i >= argv.length ) exit_with_help();

      inputFilename = argv[i];

      if ( i < argv.length - 1 )
         modelFilename = argv[i + 1];
      else {
         int p = argv[i].lastIndexOf('/');
         ++p; // whew...
         modelFilename = argv[i].substring(p) + ".model";
      }

      if ( param.eps == Double.POSITIVE_INFINITY ) {
         if ( param.solverType == SolverType.L2_LR || param.solverType == SolverType.L2LOSS_SVM ) {
            param.setEps(0.01);
         } else if ( param.solverType == SolverType.L2LOSS_SVM_DUAL || param.solverType == SolverType.L1LOSS_SVM_DUAL
            || param.solverType == SolverType.MCSVM_CS ) {
            param.setEps(0.1);
         }
      }
   }

   // read in a problem (in libsvm format)
   void readProblem( String filename ) throws IOException {
      BufferedReader fp = new BufferedReader(new FileReader(filename));
      List<Integer> vy = new ArrayList<Integer>();
      List<FeatureNode[]> vx = new ArrayList<FeatureNode[]>();
      int max_index = 0;

      try {
         while ( true ) {
            String line = fp.readLine();
            if ( line == null ) break;

            StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");

            String token = st.nextToken();
            vy.add(atoi(token));
            int m = st.countTokens() / 2;
            FeatureNode[] x;
            if ( bias >= 0 ) {
               x = new FeatureNode[m + 1];
            } else {
               x = new FeatureNode[m];
            }
            for ( int j = 0; j < m; j++ ) {
               int index = atoi(st.nextToken());
               double value = atof(st.nextToken());
               x[j] = new FeatureNode(index, value);
            }
            if ( m > 0 ) {
               max_index = Math.max(max_index, x[m - 1].index);
            }

            vx.add(x);
         }

         prob = new Problem();
         prob.bias = bias;
         prob.l = vy.size();
         prob.n = max_index;
         if ( bias >= 0 ) {
            prob.n++;
         }
         prob.x = new FeatureNode[prob.l][];
         for ( int i = 0; i < prob.l; i++ ) {
            prob.x[i] = vx.get(i);

            if ( bias >= 0 ) {
               assert prob.x[i][prob.x[i].length - 1] == null;
               prob.x[i][prob.x[i].length - 1] = new FeatureNode(max_index + 1, bias);
            } else {
               assert prob.x[i][prob.x[i].length - 1] != null;
            }
         }

         prob.y = new int[prob.l];
         for ( int i = 0; i < prob.l; i++ )
            prob.y[i] = vy.get(i);
      }
      finally {
         fp.close();
      }
   }


   private void run( String[] args ) throws IOException {
      parse_command_line(args);
      readProblem(inputFilename);
      if ( cross_validation )
         do_cross_validation();
      else {
         Model model = Linear.train(prob, param);
         Linear.saveModel(new File(modelFilename), model);
      }
   }
}
