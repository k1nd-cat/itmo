import 'dart:ffi';
import 'dart:math';
import 'gauss_seidel.dart';
import 'inputs.dart';

void main() {
  Input input = TestInput();
  var values = input.readData();
  var method = GaussSeidel();
  method.result(values!);
}