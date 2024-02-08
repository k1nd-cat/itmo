#include <iostream>
#include <vector>

using namespace std;

int main() {
    int n;
    double e;
    int m;
    vector<vector<float>> a;
    vector <float> b;
    cout << "Enter the size of the square matrix: ";
    cin >> n;
    cout << "Enter the calculation error: ";
    cin >> e;
    cout << "Enter the maximum allowed number of iterations: ";
    cin >> m;
    cout << "Enter the coefficients of the matrix: ";
    a.resize(n);
    for (int i = 0; i < n; ++i) {
        a[i].resize(n);
        for (int j = 0; j < n; ++j)
            cin >> a[i][j];
    }

    cout << "Enter the right parts of the system equations: ";
    b.resize(n);
    for (int i = 0; i < n; ++i) cin >> b[i];

    int iterationNumber = 1;
    double currentCalculationError = 0;
    for (int i = 0; i < n; ++i) {
        double s = 0;
        for (int j = 0; j < i - 1; ++j) {

        }
    }
    return 0;
}
