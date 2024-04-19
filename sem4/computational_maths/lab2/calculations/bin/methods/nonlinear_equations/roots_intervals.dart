import '../../task/nonlinear_equation.dart';
import 'nonlinear_equation_solver.dart';

class RootsIntervals {
  final NonlinearEquation _equation;
  final List<Interval> _roots = [];
  final Interval _interval;

  RootsIntervals(this._interval, this._equation);

  Interval get interval => _interval;
  List<Interval> get roots => _roots;

  void findRootsIntervals() {
    for (double i = _interval.a + 1; i <= _interval.b; ++i) {
      if ((_equation.f(i - 1)) * _equation.f(i) < 0) {
        _roots.add(Interval(i - 1, i));
      }
    }
  }
}