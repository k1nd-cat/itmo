import 'dart:io';
import 'dart:math';
import 'methods/nonlinear_equations/nonlinear_equation_solver.dart';
import 'methods/nonlinear_equations/roots_intervals.dart';
import 'methods/nonlinear_equations/simple_iteration_method.dart';
import 'task/mathematical_problem.dart';
import 'task/nonlinear_equation.dart';
import 'task/nonlinear_equations_system.dart';

void main() {
  final List<MathematicalProblem> equations = [
    /// Equation 1
    NonlinearEquation("x^3 + 4.81x^2 - 17.37x + 5.38")
      ..f = (double x) {
        return pow(x, 3).toDouble() + 4.81 * pow(x, 2).toDouble() - 17.37 * x + 5.83;
      }
      ..firstDerivative = (double x) {
        return 3 * pow(x, 2).toDouble() + 9.62 * x - 17.37;
      }
      ..secondDerivative = (double x) {
        return 6 * x + 9.62;
      }
      ..iterationFunction = (double x) {
        return x - (pow(x, 3).toDouble() + 4.81 * pow(x, 2).toDouble() - 17.37 * x + 5.38) / (3 * pow(x, 2).toDouble() + 9.62 * x - 17.37);
      },

    /// Equation 2
    NonlinearEquation("x^3 - 10x + 4")
      ..f = (double x) {
        return pow(x, 3).toDouble() - 10 * x + 4;
      }
      ..firstDerivative = (double x) {
        return 3 * pow(x, 2).toDouble() - 10;
      }
      ..secondDerivative = (double x) {
        return 6 * x;
      }
      ..iterationFunction = (double x) {
        return x - (pow(x, 3).toDouble() - 10 * x + 4) / (3 * pow(x, 2).toDouble() - 10);
      },

    /// Equation 3
    NonlinearEquation("e^x - 8x^2 + 5")
      ..f = (double x) {
        return exp(x) - 8 * pow(x, 2).toDouble() + 5;
      }
      ..firstDerivative = (double x) {
        return exp(x) - 16 * x;
      }
      ..secondDerivative = (double x) {
        return exp(x) - 16;
      }
      ..iterationFunction = (double x) {
        return x - (exp(x) - 8 * pow(x, 2).toDouble() + 5) / (exp(x) - 16 * x);
      },

    /// Equation 4
    NonlinearEquation("e^x - 2")
      ..f = (double x) {
        return exp(x) - 2;
      }
      ..firstDerivative = (double x) {
        return exp(x);
      }
      ..secondDerivative = (double x) {
        return exp(x);
      }
      ..iterationFunction = (double x) {
        return ln2;
      },


    /// Equation 5
    NonlinearEquationsSystem("y - cos(x) = 2\nx + cos(y - 1) = 0.8"),

    /// Equation 6
    NonlinearEquationsSystem("x^2 + y^2 = 4\ny = 3x^2"),
  ];

  for (int i = 0; i < equations.length; ++i) {
    print("---------${i + 1}---------\n${equations[i].equation}");
  }

  var number = int.parse(stdin.readLineSync()!);
  number--;
  var equation = equations[number];

  if (equation is NonlinearEquation) {
    var intervals = RootsIntervals(Interval(0, 4), equation);
    intervals.findRootsIntervals();
    if (intervals.roots.isNotEmpty) {
      print("Количество корней на заданном интервале: ${intervals.roots.length}");
      for (var i in intervals.roots) {
        var method = SimpleIterationMethod(equation, i, 0.00001);
        var x = method.solve();
        print("x = ${x[x.length - 1].x}");
      }
    }

    print("Выберите, каким методом надо решить нелинейное уравнение\n${equation.equation}");
  } else {
    print("Выберите, каким методом надо решить систему нелинейных уравнений\n${equation.equation}");
  }
}