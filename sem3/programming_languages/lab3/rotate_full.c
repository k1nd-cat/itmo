#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <assert.h>

FILE* inputFile = NULL;

struct bmp_header {
    uint16_t bfType;
    uint32_t  bfileSize;
    uint32_t bfReserved;
    uint32_t bOffBits;
    uint32_t biSize;
    uint32_t biWidth;
    uint32_t  biHeight;
    uint16_t  biPlanes;
    uint16_t biBitCount;
    uint32_t biCompression;
    uint32_t biSizeImage;
    uint32_t biXPelsPerMeter;
    uint32_t biYPelsPerMeter;
    uint32_t biClrUsed;
    uint32_t  biClrImportant;
} bmpHeader;

struct image {
    uint64_t width, height;
    struct pixel* data;
};

/*  deserializer   */
enum read_status  {
    READ_OK = 0,
    INVALID_ARGUMENTS = 1,
    INVALID_INPUT_FILE = 2,
    INVALID_FILE = 3,
    READ_INVALID_SIGNATURE,
    READ_INVALID_BITS,
    READ_INVALID_HEADER
    /* коды других ошибок  */
};

void errorHandler(int code) {
    if (code == READ_OK) return;
    if (code == INVALID_ARGUMENTS) fprintf(stderr, "Error in command line arguments");
    else if (code == INVALID_INPUT_FILE) fprintf(stderr, "Error in input file path");
    else if (code == INVALID_FILE) fprintf(stderr, "Input file is not bmp or is not correct");
    exit(code);
}

enum read_status from_bmp( FILE* in, struct image* img );

/*  serializer   */
enum  write_status {
    WRITE_OK = 0,
    WRITE_ERROR
    /* коды других ошибок  */
};

enum write_status to_bmp( FILE* out, struct image const* img );

struct input_params {
    char* inputFilePath;
    char* outputFilePath;
    int angle;
} inputParams;

int readParams(int argc, char* argv[]) {
    if (argc != 4) return INVALID_ARGUMENTS;
    inputParams.inputFilePath = argv[1];
    inputParams.outputFilePath = argv[2];
    inputParams.angle = strtol(argv[3], NULL, 10);

    return 0;
}

int file_opener() {
    inputFile = fopen(inputParams.inputFilePath, "rb");
    if (inputFile == NULL) return INVALID_INPUT_FILE;

    return 0;
}

int readHeader(FILE* file) {
    fseek(file, 0, SEEK_SET);
    unsigned short type;
    if (fread(&type, sizeof(short), 1, inputFile) == 0) return 0;
    if (type != 0x4d42) return INVALID_FILE;
    bmpHeader.bfType = type;
    if (fseek(file, 8, SEEK_CUR) != 0) return INVALID_FILE;
//    bmpHeader.bOffBits

}

void bmp_reader() {

}



int main(int argc, char* argv[]) {
    printf("%s\n%s\n%i\n", inputParams.inputFilePath, inputParams.outputFilePath, inputParams.angle);

    return 0;
}