import 'mathematical_problem.dart';

class NonlinearEquationsSystem implements MathematicalProblem {
  final String _equation;

  NonlinearEquationsSystem(this._equation);

  @override
  String get equation => _equation;
}