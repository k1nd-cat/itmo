import 'dart:io';
import 'dart:math';
import 'values.dart';

abstract class Input {
  Values? readData() {
    stdout.write("Данная функция пока в разработке");
    return null;
  }
}

class TestInput implements Input {
  @override
  Values? readData() {
    var values = Values();
    values.n = 3;
    values.epsilon = 0.01;
    values.a = [
      [2, 2, 10],
      [10, 1, 1],
      [2, 10, 1]
    ];
    values.b = [14, 12, 13];
    values.x = [1.4, 1.2, 1.3];
    values.M = 100;

    return values;

  }
}

class ConsoleInput extends Input {
  @override
  Values? readData() {
    var values = Values();
    values.n = _inputN();
    values.epsilon = _inputEpsilon();
    values.a = _inputA(values.n);
    values.b = _inputB(values.n);
    values.x = _inputX(values.n);
    values.M = _inputM();

    return values;
  }

  int _inputN() {
    int value;
    stdout.write("Введите порядок матрицы: ");
    try {
      value = int.parse(stdin.readLineSync()!);
    } catch (e) {
      print("Порядок матрицы введён некорректно.");
      value = _inputN();
    }

    return value;
  }

  double _inputEpsilon() {
    double value;
    stdout.write("Введите погрешность вычислений: ");
    try {
      value = double.parse(stdin.readLineSync()!);
    } catch (e) {
      print("Погрешность введена некорректно.");
      value = _inputEpsilon();
    }

    return value;
  }

  List<List<double>> _inputA(int count) {
    List<List<double>> value = [];
    stdout.write("Введите коэффициенты (строчка -- один ряд):\n");
    try {
      for (int i = 0; i < count; i++) {
        value.add([]);
        stdout.write("Введите ряд ${i + 1}: ");
        stdin.readLineSync()!.split(' ').forEach((element) {
          value[i].add(double.parse(element));
        });
        if (value[i].length != count) throw e;
      }

      if (value.length != count) throw e;
    } catch (e) {
      print("Коэффициенты введены некорректно.");
      value = _inputA(count);
    }

    return value;
  }

  List<double> _inputB(int count) {
    List<double> value = [];
    stdout.write("Введите правые части в ряд через пробел: ");
    try {
      stdin.readLineSync()!.split(' ').forEach((element) {
        value.add(double.parse(element));
      });
      if (value.length != count) throw e;
    } catch (e) {
      print("Правые части введены некорректно.");
      value = _inputB(count);
    }

    return value;
  }

  List<double> _inputX(int count) {
    List<double> value = [];
    stdout.write("Введите начальные приближения в ряд через пробел: ");
    try {
      stdin.readLineSync()!.split(' ').forEach((element) {
        value.add(double.parse(element));
      });
      if (value.length != count) throw e;
    } catch (e) {
      print("Начальные приближения введены некорректно.");
      value = _inputB(count);
    }

    return value;
  }

  int _inputM() {
    int value;
    stdout.write("Введите максимально допустимое число итераций: ");
    try {
      value = int.parse(stdin.readLineSync()!);
    } catch (e) {
      print("Максимально допустимое число итераций введёно некорректно.");
      value = _inputN();
    }

    return value;
  }
}