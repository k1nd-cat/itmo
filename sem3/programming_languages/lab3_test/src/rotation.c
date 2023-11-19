#include "../include/image.h"

#include <stdbool.h>
#include <stddef.h>
#include <stdint.h>
#include <stdlib.h>

static image* init_image(uint64_t width, uint64_t height, bool reverse_rotation) {
    image* res = malloc(sizeof(image));
    res->width =  reverse_rotation? height : width;
    res->height = reverse_rotation ? width : height;
    res->data = malloc(sizeof(pixel) * width * height);
    return res;
}

image* rotate_90(image* source) {
    image* result = init_image(source->width, source->height, true);

    for (size_t i = 0; i < result->height; i++) {
        for (size_t j = 0; j < result->width; j++) {
            result->data[i * result->width + j]
                    = source->data[source->width * (j + 1) - 1 - i];
        }
    }
    return result;
}

image* rotate_90_reverse(image* source) {
    image* result = init_image(source->width, source->height, true);

    for (size_t i = 0; i < result->height; i++) {
        for (size_t j = 0; j < result->width; j++) {
            result->data[i * result->width + j]
                    = source->data[source->width * (source->height - 1 - j) + i];
        }
    }
    return result;
}

image* rotate_180(image* source) {
    image* result = init_image(source->width, source->height, false);

    for (size_t i = 0; i < result->height; i++) {
        for (size_t j = 0; j < result->width; j++) {
            result->data[i * result->width + j]
                    = source->data[source->width * (source->height - 1 - i) + (source->width - 1 - j)];
        }
    }
    return result;
}

image* copy_image(image* source) {
    image* result = init_image(source->width, source->height, false);

    for (size_t i = 0; i < result->height; i++) {
        for (size_t j = 0; j < result->width; j++) {
            result->data[i * result->width + j]
                    = source->data[i * result->width + j];
        }
    }
    return result;
}