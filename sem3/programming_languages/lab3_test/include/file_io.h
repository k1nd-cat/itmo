#ifndef FILE_IO
#define FILE_IO

#include "enums.h"
#include <stdio.h>

file_proc_status read_file(char* filename, FILE** f);
file_proc_status write_file(char* filename, FILE** f);
file_proc_status close_file(FILE* f);

#endif