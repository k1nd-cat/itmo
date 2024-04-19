import '../../task/nonlinear_equation.dart';
import 'nonlinear_equation_solver.dart';

class NewtonMethod implements NonlinearEquationSolver {
  final NonlinearEquation _equation;
  final Interval _interval;
  final double _epsilon;

  NewtonMethod(this._equation, this._interval, this._epsilon);

  @override
  List<IterationNewton> solve() {
    List<IterationNewton> iteration = [];
    double a = _interval.a;
    double b = _interval.b;
    double x = a;
    if (!_convergence(x)) throw ArgumentError("Не выполнено условие сходимости");
    double f = _equation.f(x);
    double firstDerivative = _equation.firstDerivative(x);
    iteration.add(IterationNewton(_interval, x, f, firstDerivative));
    int currentIteration = 0;
    do {
      if (a * x >= 0) {
        a = x;
      } else {
        b = x;
      }

      x = iteration[currentIteration].x - (iteration[currentIteration].f / iteration[currentIteration].firstDerivative);
      double f = _equation.f(x);
      double firstDerivative = _equation.firstDerivative(x);
      iteration.add(IterationNewton(Interval(a, b), x, f, firstDerivative));
    } while((iteration[currentIteration].x - iteration[currentIteration - 1].x).abs() > _epsilon && (iteration[currentIteration].f).abs() > _epsilon && (iteration[currentIteration].f / iteration[currentIteration].firstDerivative).abs() > _epsilon);

    return iteration;
  }

  bool _convergence(double x) {
    return _equation.firstDerivative(x) * _equation.secondDerivative(x) > 0;
  }

}

class IterationNewton implements IterationResult {
  Interval interval;
  double x;
  double f;
  double firstDerivative;

  IterationNewton(this.interval, this.x, this.f, this.firstDerivative);
}