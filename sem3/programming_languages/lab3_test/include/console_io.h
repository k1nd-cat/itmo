#ifndef CONSOLE_IO
#define CONSOLE_IO

#include "enums.h"
#include <inttypes.h>

validate_filename_status check_valid_filename(char* filename);
validate_angle_status read_angle(char* angle, int16_t* res);

#endif