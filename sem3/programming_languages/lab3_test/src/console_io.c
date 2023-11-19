#include "../include/enums.h"

#include <limits.h>
#include <stdbool.h>
#include <stddef.h>
#include <stdint.h>
#include <stdlib.h>

static int16_t rotation_allowed_angles[7] = {0, 90, -90, 180, -180, 270, -270};
static char forbidden_symbols[8] = {'\\', '/', ':', '*', '?', '<', '>', '|'};

static bool is_forbidden(char symb) {
    for (size_t i = 0; i < 8; i++) {
        if (forbidden_symbols[i] == symb) return true;
    }
    return false;
}

validate_filename_status check_valid_filename(char* filename) {
    for (size_t i = 0; filename[i] != '\0'; i++) {
        if (is_forbidden(filename[i])) return FILENAME_ERROR;
    }
    return FILENAME_VALID;
}

static bool angle_is_allowed(int16_t angle) {
    for (size_t i = 0; i < 7; i++) {
        if (rotation_allowed_angles[i] == angle) return true;
    }
    return false;
}

static bool convert_to_int(char* string, int16_t* num) {
    char* end;
    long res = strtol(string, &end, 10);

    if (end == string) return false;
    if (res == LONG_MAX || res == LONG_MIN) return false;
    if (res > INT_MAX || res < INT_MIN) return false;

    *num = (int16_t) res;
    return true;
}

validate_angle_status read_angle(char* angle, int16_t* res) {
    int16_t temp_res;
    if (!convert_to_int(angle, &temp_res)) return ANGLE_FORMAT_ERROR;
    if (!angle_is_allowed(temp_res)) return ANGLE_VALUE_ERROR;
    *res = temp_res;
    return ANGLE_VALID;
}