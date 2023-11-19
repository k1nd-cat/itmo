#include "../include/enums.h"
#include "../include/bmp.h"
#include "../include/image.h"

#include <stddef.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>

#define BMP_TYPE 0x4D42
#define BI_HEADER_SIZE 54
#define DIB_HEADER_SIZE 40
#define BI_PLANES_USED 1
#define BI_BITS_PER_PIXEL 24
#define BI_XPELS_PER_METER 2835
#define BI_YPELS_PER_METER 2835

// static void print_bmp_header_fields(struct bmp_header* header);
static uint8_t calc_padding(uint64_t width) { return 4 - ((width * PIXEL_SIZE) % 4);}
static bmp_header create_bmp_header(image const* img);

read_bmp_status from_bmp(FILE* in, image** const img) {
    bmp_header header;

    (*img) = malloc(sizeof(image));

    if (fread(&header, sizeof(bmp_header), 1, in) < 1)
        return READ_BMP_INVALID_HEADER;
    if (header.bfType != BMP_TYPE)
        return READ_BMP_INVALID_SIGNATURE;

    (*img)->width = (uint64_t) header.biWidth;
    (*img)->height = (uint64_t) header.biHeight;

    (*img)->data = malloc(sizeof(pixel) * (*img)->width * (*img)->height);
    if ((*img)->data == NULL) {
        free((*img)->data);
        return READ_BMP_NO_ENOUGH_MEMORY;
    }

    uint8_t padding = calc_padding((*img)->width);

    fseek(in, (long) header.bOffBits, SEEK_SET);
    size_t curr_pixel = 0;

    for (size_t i = 0; i < (*img)->height; i++) {
        for (size_t j = 0; j < (*img)->width; j++) {
            uint8_t colours[PIXEL_SIZE] = {0};
            fread(&colours, sizeof(uint8_t), PIXEL_SIZE, in);

            (*img)->data[curr_pixel++] = (pixel) {
                    .b = colours[0],
                    .g = colours[1],
                    .r = colours[2]
            };
        }
        fseek(in, (long) padding, SEEK_CUR);
    }
    return READ_BMP_OK;
}

write_bmp_status to_bmp(FILE* out, image const* img) {
    bmp_header header = create_bmp_header(img);

    if (fwrite(&header, sizeof(bmp_header), 1, out) != 1)
        return WRITE_BMP_ERROR;

    uint8_t padding = calc_padding(img->width);
    pixel* current_row;
    size_t row_size = img->width * PIXEL_SIZE;

    for (size_t i = 0; i < img->height; i++) {
        current_row = img->data + i * (img->width);

        if (fwrite(current_row, row_size, 1, out) != 1)
            return WRITE_BMP_ERROR;

        uint8_t zero_padding[3] = {0};

        if (fwrite(zero_padding, sizeof(uint8_t), padding, out) != padding)
            return WRITE_BMP_ERROR;
    }

    return WRITE_BMP_OK;
}

static bmp_header create_bmp_header(image const* img) {
    bmp_header header;
    size_t row_size = img->width * PIXEL_SIZE;
    size_t data_size = (row_size  + calc_padding(img->width)) * img->height;

    header.bfType = BMP_TYPE;
    header.bfileSize = data_size + BI_HEADER_SIZE;
    header.bfReserved = 0;
    header.bOffBits = BI_HEADER_SIZE;

    header.biSize = DIB_HEADER_SIZE;
    header.biWidth = img->width;
    header.biHeight = img->height;
    header.biPlanes = BI_PLANES_USED;
    header.biBitCount = BI_BITS_PER_PIXEL;
    header.biCompression = 0;
    header.biSizeImage = data_size;
    header.biXPelsPerMeter = BI_XPELS_PER_METER;
    header.biYPelsPerMeter = BI_YPELS_PER_METER;
    header.biClrUsed = 0;
    header.biClrImportant = 0;

    return header;
}