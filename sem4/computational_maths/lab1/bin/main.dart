import 'dart:io';
import 'dart:math';
import 'gauss_seidel.dart';
import 'inputs.dart';
import 'values.dart';

void main() async {
  Input input = TestInput();

  int value = getValue();
  if (value == 1) {
    input = ConsoleInput();
  } else if (value == 2) {
    input = FileInput("C:/itmo/itmo/sem4/computational_maths/lab1/file4.in");
  } else {
    input = TestInput();
  }

  var values = await input.readData();
  var method = GaussSeidel();
  var result = Result();
  try {
    result = method.result(values!);
  } catch (e) {
    print("Ошибка в выполнении программы");
  }
  if (result.isExist) {
    print("Результат");
    for (int i = 0; i < result.result!.length; ++i) {
      stdout.write("x${i+1} = ${result.result![i]}\n");
    }
  } else {
    print("Условие сходимости не выполняется");
  }
}

int getValue() {
  int value;
  stdout.write("Если ввод данных через консоль, введите 1, через файл -- введите 2: ");
  try {
    value = int.parse(stdin.readLineSync()!);
    if (value != 1 && value != 2 && value != 3) throw e;
  } catch (e) {
    print("Повторите ещё раз.");
    value = getValue();
  }

  return value;
}