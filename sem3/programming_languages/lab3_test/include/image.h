#ifndef IMAGE
#define IMAGE

#include <stdint.h>

#define PIXEL_SIZE 3

typedef struct pixel {
    uint8_t b, g, r;
} pixel;

typedef struct image {
    uint64_t width, height;
    pixel* data;
} image;

#endif