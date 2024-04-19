abstract class NonlinearEquationSolver {
  List<IterationResult> solve();
}

class IterationResult {
}

class Interval {
  double a;
  double b;

  Interval(this.a, this.b);

  @override
  String toString() {
    return "[$a, $b]";
  }
}