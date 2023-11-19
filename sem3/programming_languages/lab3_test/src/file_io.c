#include "../include/enums.h"

#include <stdio.h>

file_proc_status read_file(char* filename, FILE** f) {
    if ((*f = fopen(filename, "rb")) == NULL) return FILE_PROC_ERROR;
    return FILE_PROC_OK;
}

file_proc_status write_file(char* filename, FILE** f) {
    if ((*f = fopen(filename, "wb")) == NULL) return FILE_PROC_ERROR;
    return FILE_PROC_OK;
}

file_proc_status close_file(FILE* f) {
    if (fclose(f) != 0) return FILE_PROC_ERROR;
    return FILE_PROC_OK;
}