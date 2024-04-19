import 'mathematical_problem.dart';

class NonlinearEquation implements MathematicalProblem {
  final String _equation;
  late final double Function(double x) f;
  late final double Function(double x) firstDerivative;
  late final double Function(double x) secondDerivative;
  late final double Function(double x) iterationFunction;

  NonlinearEquation(this._equation);

  @override
  String get equation => _equation;
}
