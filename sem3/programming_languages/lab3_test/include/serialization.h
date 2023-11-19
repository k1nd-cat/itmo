#ifndef SERIALIZATION
#define SERIALIZATION

#include "bmp.h"
#include "enums.h"
#include "image.h"

#include <stdio.h>

read_bmp_status from_bmp(FILE* in, image** const img);
write_bmp_status to_bmp(FILE* out, image const* img);

#endif