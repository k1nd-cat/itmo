#include "../include/image.h"

#include <stdarg.h>
#include <stddef.h>
#include <stdlib.h>

void clear_images_memory(int count, ...) {
    va_list list;

    va_start(list, count);
    for (size_t j = 0; j < count; j++) {
        image* curr_image = va_arg(list, image*);
        free(curr_image->data);
        free(curr_image);
    }
    va_end(list);
}