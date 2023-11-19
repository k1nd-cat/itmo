const char* file_proc_status_names[2] = {
        "FILE_PROC_OK",
        "FILE_PROC_ERROR"
};

const char* read_status_names[5] = {
        "READ_BMP_OK",
        "READ_BMP_INVALID_SIGNATURE",
        "READ_BMP_INVALID_BITS",
        "READ_BMP_INVALID_HEADER",
        "READ_BMP_NO_ENOUGH_MEMORY"
};

const char* write_status_names[2] = {
        "WRITE_OK",
        "WRITE_ERROR"
};

const char* validate_filename_status_names[2] = {
        "FILENAME_VALID",
        "FILENAME_ERROR"
};

const char* validate_input_args_status_names[2] = {
        "ARGS_VALID",
        "ARGS_ERROR"
};

const char* validate_angle_status_names[3] = {
        "ANGLE_VALID",
        "ANGLE_FORMAT_ERROR",
        "ANGLE_VALUE_ERROR"
};