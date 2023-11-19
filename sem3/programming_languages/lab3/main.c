#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// TODO: disc is full

typedef struct params
{
    char *input; // bmp inputFile
    char *output; // result dir
    int max_iter; // iterations
    int dump_freq; // frequency of dumps
} params;

//params appparams = {"step0_4.bmp", ".", 50, 1}; // test
params appparams = {"", "", 50, 1};

const short POINT = 1;
const short NO_POINT = 0;

typedef struct header
{
    unsigned int offset; // position: 10
    unsigned int width; // position: 18
    unsigned int height; // position: 22
    unsigned short bitsByPixel; // amount of bits per pixel
    char *fileHeader; // header till data begin
} header;

header bmpHeader;

/**
 * Get params structure from command line.
 */
void readParams(int argc, char *argv[]) {
    int argIndex = 1;
    char *name = "";
    for (; argIndex < argc; argIndex++) {
        if (strlen(name) == 0) {
            name = argv[argIndex];
        } else {
            char *value = argv[argIndex];
            if (strcmp(name, "--input") == 0) {
                appparams.input = value;
            } else if (strcmp(name, "--output") == 0) {
                appparams.output = value;
            } else if (strcmp(name, "--max_iter") == 0) {
                appparams.max_iter = atoi(value);
            } else if (strcmp(name, "--dump_freq") == 0) {
                appparams.dump_freq = atoi(value);
            }
            name = "";
        }
    }
}

/**
 * Check that params are ok.
 */
int checkParams() {
    if (strlen(appparams.input) == 0 || strlen(appparams.output) == 0) {
        printf("ERROR: wrong params\nUsage:\n--input - path to bmp inputFile with begin state (mandatory)\n--output - path to output dir (mandatory)\n--max_iter - amount of iterations\n--dump_freq - frequency of dumps");
        return 0;
    }
    return 1;
}

/**
 * Read header of inputFile.
 */
int readHeader(FILE *file) {
    fseek(file, 0, SEEK_SET);
    unsigned short type;
    if (fread(&type, sizeof(short), 1, file) == 0) return 0;
    if (type != 0x4d42) {
        printf("ERROR: input inputFile is not bmp");
        return 0;
    }
    if (fseek(file, 8, SEEK_CUR) != 0) return 0;
    if (fread(&bmpHeader.offset, sizeof(int), 1, file) == 0) return 0;
    if (fseek(file, 4, SEEK_CUR) != 0) return 0;
    if (fread(&bmpHeader.width, sizeof(int), 1, file) == 0) return 0;
    if (fread(&bmpHeader.height, sizeof(int), 1, file) == 0) return 0;
    if (fseek(file, 2, SEEK_CUR) != 0) return 0;
    if (fread(&bmpHeader.bitsByPixel, sizeof(short), 1, file) == 0) return 0;
    // printf("HEADER: Offset: %d, Width: %d, Height: %d, BitsByPixel: %d\n", bmpHeader.offset, bmpHeader.width, bmpHeader.height, bmpHeader.bitsByPixel);
    if (bmpHeader.bitsByPixel != 1) {
        printf("ERROR: input inputFile is not mono bmp, please save with 1 bit per pixel");
        return 0;
    }
    bmpHeader.fileHeader = calloc(1, bmpHeader.offset);
    if (fseek(file, 0, SEEK_SET) != 0) return 0;
    if (fread(bmpHeader.fileHeader, 1, bmpHeader.offset, file) == 0) return 0;
    return 1;
}

void debugData(short **data) {
    int x = 0, y;
    for (; x < bmpHeader.height; x++) {
        printf("%d:  ", x);
        for (y = 0; y < bmpHeader.width; y++) {
            printf("%d  ", data[x][y]);
        }
        printf("\n--------------\n");
    }
    printf("\n");
}

/**
 * Read initial state of the game.
 */
void readData(FILE *file, short **data) {
    fseek(file, bmpHeader.offset, SEEK_SET);
    int x = 0, y = 0, readSymbols = 0;
    while (x < bmpHeader.height) {
        unsigned char symbol = fgetc(file);
        if (++readSymbols == 4) {
            readSymbols = 0;
        }

    // process symbol
        short bit;
        for (bit = 7; y < bmpHeader.width && bit >= 0; bit--) {
            data[x][y] = ((1 << bit) & symbol) > 0 ? POINT: NO_POINT;
            y++;
        }

        if (y == bmpHeader.width) {
        // read till 4 bytes in one line
            while (readSymbols < 4) {
                fgetc(file);
                readSymbols++;
            }

            readSymbols = 0;
            x++;
            y = 0;
        }
    }
}

/**
 * Calculate amount of neighbours.
 */
int calcNeighbours(short **data, int x, int y) {
    int neighbours = 0, subX, subY;
    for (subX = x - 1; subX <= x + 1; subX++) {
        for (subY = y - 1; subY <= y + 1; subY++) {
            if (subX == x && subY == y) continue; // current point
            int nX = subX, nY = subY;
            if (nX == -1) nX = bmpHeader.height - 1; // take from other side
            else if (nX == bmpHeader.height) nX = 0; // take from other side
            if (nY == -1) nY = bmpHeader.width - 1; // take from other side
            else if (nY == bmpHeader.width) nY = 0; // take from other side
            if (data[nX][nY] == POINT) {
                neighbours++;
            }
        }
    }
    return neighbours;
}

/**
 * One game iteration.
 *
 * Any live cell with fewer than two live neighbours dies, as if by underpopulation.
 * Any live cell with two or three live neighbours lives on to the next generation.
 * Any live cell with more than three live neighbours dies, as if by overpopulation.
 * Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
 *
 * return 1 if game over (no changes or all pixels die).
 */
int gameIteration(short **data) {
    short **newData = calloc(sizeof(short*), bmpHeader.height);
    int x = 0, y, changed = 0, liveExists = 0;
    for (; x < bmpHeader.height; x++) {
        newData[x] = calloc(sizeof(short), bmpHeader.width);
        for (y = 0; y < bmpHeader.width; y++) {
            int neighbours = calcNeighbours(data, x, y);
            if (data[x][y] == POINT) {
                if (neighbours < 2 || neighbours > 3) {
                    newData[x][y] = NO_POINT;
                    changed = 1;
                } else {
                    newData[x][y] = POINT;
                    liveExists = 1;
                }
            } else { // NO_POINT
                if (neighbours == 3) {
                    newData[x][y] = POINT;
                    liveExists = 1;
                    changed = 1;
                } else {
                    newData[x][y] = NO_POINT;
                }
            }
        }
    }
    for (x = 0; x < bmpHeader.height; x++) {
        for (y = 0; y < bmpHeader.width; y++) {
            data[x][y] = newData[x][y];
        }
        free(newData[x]);
    }
    free(newData);
    int stop = changed == 0 && liveExists == 0 ? 1 : 0;
    return stop;
}

/**
 * Put data in memory.
 */
short **prepareData() {
    short **data = calloc(sizeof(short*), bmpHeader.height);
    int x = 0;
    for (; x < bmpHeader.height; x++) {
        data[x] = calloc(sizeof(short), bmpHeader.width);
    }
    return data;
}

/**
 * Clean data memory.
 */
void freeData(short **data) {
    int x = 0;
    for (; x < bmpHeader.height; x++) {
        free(data[x]);
    }
    free(data);
}

/**
 * Save state iteration to inputFile.
 */
void saveData(short **data, int iteration) {
    char *fileName = calloc(1, strlen(appparams.output) + 20);
    sprintf(fileName, "%s/step_%d.bmp", appparams.output, iteration);
    FILE *file = fopen(fileName, "wb"); // open inputFile for read
    free(fileName);
    fseek(file, 0, SEEK_SET);
    fwrite(bmpHeader.fileHeader, 1, bmpHeader.offset, file);
    int x = 0, y = 0, writtenSymbols = 0;
    while (x < bmpHeader.height) {
        unsigned char symbol = (char)0;
        short bit;
        for (bit = 7; y < bmpHeader.width && bit >= 0; bit--) {
            if (data[x][y] == POINT) {
                symbol = symbol | (1 << bit);
            }
            y++;
        }
        fputc(symbol, file);
        if (++writtenSymbols == 4) {
            writtenSymbols = 0;
        }
        symbol = (char)0;
        if (y == bmpHeader.width) {
            // write till 4 bytes in one line
            while (writtenSymbols < 4) {
                fputc((char)0, file);
                writtenSymbols++;
            }
            writtenSymbols = 0;
            x++;
            y = 0;
        }
    }
    fclose(file);
}

/**
 * Read data and execute iterations.
 */

void game() {
    FILE *file = fopen(appparams.input, "rb"); // open inputFile for read
    if (file == NULL) {
        printf("Cannot open inputFile: %s\n");
        exit(0);
    }
    if (readHeader(file) != 1) {
        fclose(file);
        return;
    }
    short **data = prepareData();
    readData(file, data);
    fclose(file);
    int iteration = 0;
    for (;;) {
        int stop = gameIteration(data);
        iteration++;
        if (stop == 0) {
            stop = appparams.max_iter > 0 && iteration == appparams.max_iter ? 1 : 0; // check if max iteration
        }
        if (iteration % appparams.dump_freq == 0 || stop == 1) {
            saveData(data, iteration);
        }
        if (stop == 1) break; // no changes or all die
    }
    freeData(data);
    free(bmpHeader.fileHeader);
}

int main(int argc, char *argv[]) {
    readParams(argc, argv);
    if (checkParams() == 0) exit(0);
    game();
}

