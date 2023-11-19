#ifndef ENUMS
#define ENUMS

extern char* file_proc_status_names[2];
extern char* read_status_names[5];
extern char* write_status_names[2];
extern char* validate_filename_status_names[2];
extern char* validate_input_args_status_names[2];
extern char* validate_angle_status_names[3];

typedef enum file_proc_status {
    FILE_PROC_OK,
    FILE_PROC_ERROR
} file_proc_status;

typedef enum read_bmp_status  {
    READ_BMP_OK,
    READ_BMP_INVALID_SIGNATURE,
    READ_BMP_INVALID_BITS,
    READ_BMP_INVALID_HEADER,
    READ_BMP_NO_ENOUGH_MEMORY
} read_bmp_status;

typedef enum write_bmp_status  {
    WRITE_BMP_OK,
    WRITE_BMP_ERROR
} write_bmp_status;

typedef enum validate_filename_status  {
    FILENAME_VALID,
    FILENAME_ERROR
} validate_filename_status;

typedef enum validate_input_args_status  {
    ARGS_VALID,
    ARGS_ERROR
} validate_input_args;

typedef enum validate_angle_status {
    ANGLE_VALID,
    ANGLE_FORMAT_ERROR,
    ANGLE_VALUE_ERROR
} validate_angle_status;


#endif