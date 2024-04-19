import '../../task/nonlinear_equation.dart';
import 'nonlinear_equation_solver.dart';

class SimpleIterationMethod implements NonlinearEquationSolver {
  final NonlinearEquation _equation;
  final Interval _interval;
  final double _epsilon;

  SimpleIterationMethod(this._equation, this._interval, this._epsilon);

  @override
  List<IterationSimpleIterations> solve() {
    List<IterationSimpleIterations> iteration = [];
    double a = _interval.a;
    double b = _interval.b;
    double q = 0;
    double delta = 0;
    double x = a;

    do {
      double xNext = _equation.iterationFunction(x);
      delta = (x - xNext).abs();

      if (iteration.isEmpty) {
        q = (_equation.iterationFunction(a) - _equation.iterationFunction(b)).abs() / (b - a).abs();
        iteration.add(IterationSimpleIterations(x, xNext, _equation.f(x), _equation.iterationFunction(x), delta));
      } else {
        q = delta / (x - iteration[iteration.length - 1].x).abs();
      }

      if (q >= 1) throw Exception("Метод не сходится");

      iteration.add(IterationSimpleIterations(x, xNext, _equation.f(x), _equation.iterationFunction(x), delta));
    } while (delta > _epsilon);

    return iteration;
  }

  bool _convergence(double x) {
    return _equation.firstDerivative(x) * _equation.secondDerivative(x) > 0;
  }

  int winner(String player1, String player2) {
    if (player1 == player2) return 0;
    if (player1 == "Бумага" && player2 == "") {

    }
    return 1;
  }
}

class IterationSimpleIterations implements IterationResult {
  double x;
  double xNext;
  double f;
  double g;
  double difference;

  IterationSimpleIterations(this.x, this.xNext, this.f, this.g, this.difference);
}