import '../../task/nonlinear_equation.dart';
import 'nonlinear_equation_solver.dart';

class ChordMethod implements NonlinearEquationSolver {
  final NonlinearEquation _equation;
  final Interval _interval;
  final double _epsilon;

  ChordMethod(this._equation, this._interval, this._epsilon);

  @override
  List<IterationChord> solve() {
    List<IterationChord> iteration = [];
    double a = _interval.a;
    double b = _interval.b;

    double x = a - ((b - a) / (_equation.f(b) - _equation.f(a)) * _equation.f(a));
    double f = _equation.f(x);
    iteration.add(IterationChord(_interval, x, f));
    int currentIteration = 0;
    do {
      if (a * x >= 0) {
        a = x;
      } else {
        b = x;
      }
      x = a - ((b - a) / (_equation.f(b) - _equation.f(a)) * _equation.f(a));
      f = _equation.f(x);
      iteration.add(IterationChord(Interval(a, b), x, f));
      currentIteration++;
    } while((iteration[currentIteration].x - iteration[currentIteration - 1].x).abs() > _epsilon && (iteration[currentIteration].interval.a - iteration[currentIteration].interval.b).abs() > _epsilon && (iteration[currentIteration].f).abs() > _epsilon);
    return iteration;
  }
}

class IterationChord implements IterationResult {
  Interval interval;
  double x;
  double f;

  IterationChord(this.interval, this.x, this.f);
}