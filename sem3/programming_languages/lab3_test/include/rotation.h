#ifndef ROTATION
#define ROTATION

#include "image.h"
#include <inttypes.h>
#include <stdbool.h>

image* rotate_90(image * const source);
image* rotate_90_reverse(image* source);
image* rotate_180(image* source);
image* copy_image(image* source);

#endif