import 'dart:io';

import 'package:logger/logger.dart';

import 'method.dart';
import 'values.dart';

var log = Logger();

class GaussSeidel implements Method {

  @override
  Result result(Values values) {
    List<List<double>> beginMatrix = [...values.a];
    List<double> beginB = [...values.b];
    List<double> leftPart = [];
    var result = Result();
    print("Исходная матрица:");
    printCurrentMatrix(values.a, values.b);
    _diagonalDominance(values.a, values.b, values.x);
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

    for (int i = 0; i < beginMatrix.length; ++i) {
      leftPart.add(0);
      for (int j = 0; j < beginMatrix.length; ++j) {
        leftPart[i] += beginMatrix[i][j] * result.result![j];
      }
    }

    stdout.write("\n\n");
    for (int i = 0; i < leftPart.length; ++i) {
      stdout.write("${leftPart[i]} = ${beginB[i]} | Разница: ${(leftPart[i] - beginB[i]).abs()}\n");
    }
    stdout.write("\n\n");

    printCurrentMatrix(beginMatrix, beginB);

    return result;
  }

  bool _matrixNorm(List<List<double>> matrix) {
    double max = 0;
    for (int i = 0; i < matrix.length; ++i) {
      double rowSum = matrix[i].reduce((a, b) => a + b).abs();
      max = max >= rowSum ? max : rowSum;
    }

    if (max > 1) return false;
    return true;
  }

  void _diagonalDominance(List<List<double>> matrix, List<double> results, List<double> x) {
    var size = matrix.length;
    int maxRow;
    for (int i = 0; i < size; ++i) {
      maxRow = i;
      for (int j = i + 1; j < size; j++) {
        if (matrix[j][i].abs() > matrix[maxRow][i].abs()) {
          maxRow = j;
        }
      }
      _swap(matrix, i, maxRow);
      _swap(results, i, maxRow);
      _swap(x, i, maxRow);
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
    List<double>? previousX = [...values.x];
    for (int i = 0; i < values.M; ++i) {
      values.x = _oneIteration(values);

      print("Итерация ${i + 1}");
      for (int i = 0; i < values.x.length; ++i) {
        stdout.write("x${i+1} = ${values.x[i]}\n");
      }

      if (previousX != null) {
        print("ε = ${_currentEpsilon(values.x, previousX)}\n");
        if (_currentEpsilon(values.x, previousX) < values.epsilon) break;
      } else {
        print("");
      }

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
        if (j < i) {
          rowResult += values.a[i][j] * result[j];
        } else if (j > i) {
          rowResult += values.a[i][j] * values.x[j];
        }
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