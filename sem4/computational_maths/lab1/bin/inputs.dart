import 'dart:io';
import 'dart:math';

import 'package:decimal/decimal.dart';

import 'values.dart';

abstract class Input {
  Values? readData() {
    stdout.write("Данная функция пока в разработке");
    return null;
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

  Decimal _inputEpsilon() {
    Decimal value;
    stdout.write("Введите погрешность вычислений: ");
    try {
      value = Decimal.parse(stdin.readLineSync()!);
    } catch (e) {
      print("Погрешность введена некорректно.");
      value = _inputEpsilon();
    }

    return value;
  }

  List<List<Decimal>> _inputA(int count) {
    List<List<Decimal>> value = [];
    stdout.write("Введите коэффициенты (строчка -- один ряд):\n");
    try {
      for (int i = 0; i < count; i++) {
        value.add([]);
        stdout.write("Введите ряд ${i + 1}: ");
        stdin.readLineSync()!.split(' ').forEach((element) {
          value[i].add(Decimal.parse(element));
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

  List<Decimal> _inputB(int count) {
    List<Decimal> value = [];
    stdout.write("Введите правые части в ряд через пробел: ");
    try {
      stdin.readLineSync()!.split(' ').forEach((element) {
        value.add(Decimal.parse(element));
      });
      if (value.length != count) throw e;
    } catch (e) {
      print("Правые части введены некорректно.");
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