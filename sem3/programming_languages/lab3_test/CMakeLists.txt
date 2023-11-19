cmake_minimum_required(VERSION 3.22)
project(lab3_test C)

set(CMAKE_C_STANDARD 99)

add_executable(lab3_test src/main.c include/bmp.h include/console_io.h include/enums.h include/file_io.h include/image.h include/rotation.h include/serialization.h include/util.h src/console_io.c src/enum_naming.c src/file_io.c src/rotation.c src/serialization.c src/util.c)
