<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <title>Lab1_2629</title>
    <link href="styles.css" rel="stylesheet" type="text/css" />
</head>
<body>
<?php

session_start();

// unset($_SESSION['cache']);

if (!isset($_SESSION['cache'])) {
    $_SESSION['cache'] = array();
}

function addToTable($x, $y, $r, $result) {

    $obj = (object) [
        'x' => $x,
        'y' => $y,
        'r' => $r,
        'result' => $result,
    ];

    $_SESSION['cache'][] = $obj;
}

function checkPointInArea($x, $y, $r): bool {
    $result = false;

    //проверка на вхождение в область
    if ($x < 0 && $x > -1 * $r && $y > 0 && $y < $x / 2 + $r / 2 ||
        $x >= 0 && $y >= 0 && $x <= $r && $y <= $r && $y * $y + $x * $x <= $r * $r ||
        $x >= 0 && $x <= $r && $y <= 0 && $y >= -1 * $r / 2)
    {
        $result = true;
    }

    return $result;
}

function getPointResult($x, $y, $r) {
    if ($r == 1 || $r == 1.5 || $r == 2 || $r == 2.5 || $r == 3) {
        $flag = true;
        $result = checkPointInArea($x, $y, $r);

        echo '<script>window._x = ', $x, ';', 'window._y = ', $y, ';', 'window._r = ', $r, ';', '</script>';

        // Добавление данных в таблицу
        addToTable($x, $y, $r, $result);
    }
}

if (isset($_POST['input_x'])) {

    $x = $_POST['input_x'];
    $y = $_POST['input_y'];
    $r = $_POST['value_r'];
    $rFlag = false;

    if (!(isset($x) && isset($y) && isset($r))) {
        echo '<script>alert(\'not valid value\')</script>';
    } else {

        if (is_numeric($x) && is_numeric($y))
            if ($x >= -5 && $x <= 3 && $y >= -3 && $y <= 3)
                $flag = true;
            else
                echo '<script>alert(\'not valid values\')</script>';

        for ($i = 0; $i < count($r); $i++) {
            if (!($r[$i] == 1 || $r[$i] == 1.5 || $r[$i] == 2 || $r[$i] == 2.5 || $r[$i] == 3)) continue;

            getPointResult($x, $y, $r[$i]);
            $rFlag = true;
        }

        if (!$rFlag)
            echo '<script>alert(\'not valid value R\')</script>';
    }
}


$rows = explode('/', "");

function resultsTable()
{
    if (!isset($_SESSION['cache'])) return;

    $rowsLength = count($_SESSION['cache']);

    for ($i = $rowsLength - 1; $i >= 0; $i--) {
        $x = $_SESSION['cache'][$i]->x;
        $y = $_SESSION['cache'][$i]->y;
        $r = $_SESSION['cache'][$i]->r;
        $result = $_SESSION['cache'][$i]->result ? 'входит' : 'не входит';
        echo '<tr>';
        echo '<td>', $x, '</td><td>', $y, '</td><td>', $r, '</td><td>', $result, '</td>';
        echo '</tr>';
    }
}
?>
    <form action="index.php" method="post" onsubmit="return check_input()">
    <table class="table" border="0" cellpadding="0" cellspacing="0">
        <thead id="head">
        <tr>
            <th colspan="2" class = table>
                <div class="header-text">Трошкин Александр Евгеньевич P3216 2629</div>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr id="canvas">
            <td colspan="2"><canvas id='schedule' height="270px"></canvas></td>
        </tr>
        <tr class="values">
            <td class="values"><label for="input_x" id="label_x">Значение X, {-5 ... 3}</label></td>
            <td class="values_input"><input class="input_text" type="text" id="input_x" name="input_x"/></td>
        </tr>
        <tr class="values">
            <td class="values"><label for="input_y" id="label_y">Значение Y, {-3 ... 3}</label></td>
            <td class="values_input"><input class="input_text" type="text" id="input_y" name="input_y"/></td>
        </tr>
        <tr class="values">
            <td class="values"><label for="checkbox_r" id="label_r">Значение R, {-3 ... 3}</label></td>
            <td class="values_input">
                <label>
                    <input type="checkbox" name="value_r[]" id="r1" value="1">1
                </label>
                <label>
                    <input type="checkbox" name="value_r[]" id="r2" value="1.5">1.5
                </label>
                <label>
                    <input type="checkbox" name="value_r[]" id="r3" value="2">2
                </label>
                <label>
                    <input type="checkbox" name="value_r[]" id="r4" value="2.5">2.5
                </label>
                <label>
                    <input type="checkbox" name="value_r[]" id="r5" value="3">3
                </label>
            </td>
        </tr>
        <tr>
            <td colspan="2" id="button_str">
                <button type="submit" id="check" name="button">Проверить</button>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <table class="resul_table">
                    <thead id="result_head">
                        <td>X</td>
                        <td>Y</td>
                        <td>R</td>
                        <td>Попадание</td>
                    </thead>
                    <tbody id="table_out">
                        <?php
                        resultsTable($rows);
                        ?>
                    </tbody>
                </table>
            </td>
        </tr>
        </tbody>
    </table>
    </form>
    <script type="text/javascript" src="scripts/canvas.js"></script>
    <script type="text/javascript" src="scripts/validation.js"></script>
</body>
</html>