function multiplyMatrix() {
    var matrix1 = [
        [1],
        [2],
        [3],
        [4]
    ];
    var matrix2 = [
        [5],
        [6],
        [7],
        [8]
    ];

    for (var i = 0; i < 4; i++) {
        for (var j = 0; j < 4; j++) {
            matrix[i][j] =
                a[i][0] * b[0][j] +
                a[i][1] * b[1][j] +
                a[i][2] * b[2][j] +
                a[i][3] * b[3][j];
        }
    }
}