import 'package:decimal/decimal.dart';

class Values {
  late int n;
  late double epsilon;
  late List<List<double>> a;
  late List<double> b;
  late List<double> x;
  late int M;
}

class Result {
  List<double>? result;
  bool isExist = true;
}