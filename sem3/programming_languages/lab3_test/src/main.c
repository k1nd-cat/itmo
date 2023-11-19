#include "../include/console_io.h"
#include "../include/enums.h"
#include "../include/file_io.h"
#include "../include/image.h"
#include "../include/rotation.h"
#include "../include/serialization.h"
#include "../include/util.h"

#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char** argv) {
    if (argc < 4) {
        fprintf(stderr, "\n\n%s\n\n", validate_input_args_status_names[ARGS_ERROR]);
        fprintf(stderr, "Input format: ./image-transformer <source-image> <transformed-image> <angle>\n\n");
        return 1;
    }

    char* source = argv[1];
    char* out = argv[2];
    char* str_angle = argv[3];

    int16_t angle = 0;

    validate_angle_status read_angle_status = read_angle(str_angle, &angle);

    if (read_angle_status != ANGLE_VALID) {
        fprintf(stderr, "\n\n%s\n\n", validate_angle_status_names[read_angle_status]);
        fprintf(stderr, "Angle allowed values: 0, 90, -90, 180, -180, 270, -270\n\n");
        return 1;
    }

    FILE *src_file;
    file_proc_status open_file_status = read_file(source, &src_file);

    if (open_file_status != FILE_PROC_OK) {
        fprintf(stderr, "\n\n%s\n\n", file_proc_status_names[open_file_status]);
        return 1;
    }

    image* img;
    read_bmp_status read_bmp_status = from_bmp(src_file, &img);

    if (read_bmp_status != READ_BMP_OK) {
        clear_images_memory(1, img);
        fprintf(stderr, "\n\n%s\n\n", read_status_names[read_bmp_status]);
        return 1;
    };
    fclose(src_file);

    image* rotated;

    switch (abs((angle / 90) % 2))
    {
        case 0:
            rotated = ((angle/90) % 4 == 0) ? copy_image(img) : rotate_180(img);
            break;
        default:
            switch (abs(angle / 90))
            {
                case 1:
                    rotated = (angle < 0) ? rotate_90_reverse(img) : rotate_90(img);
                    break;
                default:
                    rotated = (angle < 0) ? rotate_90(img) : rotate_90_reverse(img);
                    break;
            }
            break;
    }

    FILE* out_file;
    open_file_status = write_file(out, &out_file);

    if (open_file_status != FILE_PROC_OK) {
        clear_images_memory(2, img, rotated);
        fprintf(stderr, "\n\n%s\n\n", file_proc_status_names[open_file_status]);
        return 1;
    }

    write_bmp_status write_bmp_status = to_bmp(out_file, rotated);

    if (write_bmp_status != WRITE_BMP_OK) {
        clear_images_memory(2, img, rotated);
        fprintf(stderr, "\n\n%s\n\n", write_status_names[write_bmp_status]);
        return 1;
    };
    fclose(out_file);
    clear_images_memory(2, img, rotated);

    return 0;
}