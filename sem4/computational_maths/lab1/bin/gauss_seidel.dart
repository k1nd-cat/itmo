import 'dart:io';

import 'package:logger/logger.dart';

import 'method.dart';
import 'values.dart';

var log = Logger();

class GaussSeidel implements Method {

  @override
  Result result(Values values) {
    var result = Result();
    print("Исходная матрица:");
    printCurrentMatrix(values.a, values.b);
    _diagonalDominance(values.a, values.b, values.x);
    values.a = values.a.reversed.toList();
    values.b = values.b.reversed.toList();
    values.x = values.x.reversed.toList();
    print("Матрица после диагнализирования:");
    printCurrentMatrix(values.a, values.b);

    values.a = _expressX(values.a, values.b);
    if (!_matrixNorm(values.a)) {
      result.isExist = false;
      return result;
    }

    print("Приведем систему A𝑥 = 𝑏 к виду 𝑥 = 𝐶𝑥 + 𝑑 :");
    printCurrentMatrix(values.a, values.b);
    result = _iterations(values, result);

    return result;
  }

  bool _matrixNorm(List<List<double>> matrix) {
    double max = 0;
    for (int i = 0; i < matrix.length; ++i) {
      double rowSum = matrix[i].reduce((a, b) => a + b).abs();
      max = max > rowSum ? max : rowSum;
    }

    if (max > 1) return false;
    return true;
  }

  void _diagonalDominance(List<List<double>> matrix, List<double> results, List<double> x) {
    List<int> mainElement = List.filled(matrix.length, -1);
    for (int i = 0; i < matrix.length; ++i) {
      double maxValue = matrix[i].reduce((currentMax, next) => currentMax > next ? currentMax : next);
      double minValue = matrix[i].reduce((currentMin, next) => currentMin < next ? currentMin : next);
      maxValue = maxValue.abs() > minValue.abs() ? maxValue : minValue;
      int maxValueIndex = matrix[i].indexOf(maxValue);
      maxValue = maxValue.abs();
      double sum = matrix[i].reduce((a, b) => a.abs() + b.abs());
      sum -= maxValue.abs();
      if (sum < maxValue) {
        mainElement[i] = maxValueIndex;
      }
    }

    for (int i = 0; i < mainElement.length; ++i) {
      if (mainElement[i] == -1 || i == mainElement[i]) continue;
      _swap(matrix, i, mainElement[i]);
      _swap(results, i, mainElement[i]);
      _swap(x, i, mainElement[i]);
      if (mainElement[mainElement[i]] != -1) {
        mainElement[mainElement[i]] = i;
      }
    }
  }


    List<T> _swap<T>(List<T> matrix, int a, int b) {
    T intermediate = matrix[a];
    matrix[a] = matrix[b];
    matrix[b] = intermediate;
    return matrix;
  }

  List<List<double>> _expressX(List<List<double>> matrix, List<double> rightParts) {
    List<List<double>> result = [];
    for (int i = 0; i < rightParts.length; ++i) {
      result.add([]);
      for (int j = 0; j < rightParts.length; ++j) {
        double value;
        if (i == j) {
          double argument = matrix[i][i];
          result[i] = result[i].map((e) => e /= argument).toList();
          matrix[i] = matrix[i].map((e) => e /= argument).toList();
          rightParts[i] /= argument;
          value = 0;
        } else {
          value = 0 - matrix[i][j];
        }

        result[i].add(value);
      }
    }

    return result;
  }

  void printCurrentMatrix(List<List<double>> matrix, List<double> results) {
    for (int i = 0; i < matrix.length; ++i) {
      for (var element in matrix[i]) {
        stdout.write("$element ");
      }
      stdout.write("   ${results[i]} ");
      print("");
    }
    print("");
  }

  Result _iterations(Values values, Result result) {
    List<double>? previousX;
    for (int i = 0; i < values.M; ++i) {
      values.x = _oneIteration(values);
      if (previousX != null) {
        if (_currentEpsilon(values.x, previousX) < values.epsilon) break;
      }

      print("Итерация ${i + 1}");
      for (int i = 0; i < values.x.length; ++i) {
        stdout.write("x${i+1} = ${values.x[i]}\n");
      }
      print("");

      previousX = values.x;
    }

    result.result = values.x;
    return result;
  }

  List<double> _oneIteration(Values values) {
    List<double> result = [];
    for (int i = 0; i < values.a.length; ++i) {
      double rowResult = 0;
      for (int j = 0; j < values.x.length; ++j) {
        rowResult += values.a[i][j] * values.x[j];
      }
      rowResult += values.b[i];
      result.add(rowResult);
    }

    return result;
  }

  double _currentEpsilon(List<double> current, List<double> previous) {
    double max = 0;
    for (int i = 0; i < current.length; ++i) {
      double value = (current[i] - previous[i]).abs();
      if (value > max) max = value;
    }

    return max;
  }
}