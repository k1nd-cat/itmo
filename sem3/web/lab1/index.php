<?php
session_start();

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
        $result = true;

    return $result;
}

function getPointResult($x, $y, $r) {
    if ($r == 1 || $r == 1.5 || $r == 2 || $r == 2.5 || $r == 3) {
        $result = checkPointInArea($x, $y, $r);
        echo '<script>window._x = ', $x, ';', 'window._y = ', $y, ';', 'window._r = ', $r, ';', '</script>';

        // Добавление данных в таблицу
        addToTable($x, $y, $r, $result);
    }
}

if (isset($_POST['button_clear'])) {
    unset($_SESSION['cache']);
} else {
    if (isset($_POST['input_x']) && isset($_POST['input_y']) && isset($_POST['value_r'])) {

        $x = $_POST['input_x'];
        $y = $_POST['input_y'];
        $r = $_POST['value_r'];
        $xFlag = false;
        $yFlag = false;
        $rFlag = false;

        $x = str_replace(",", ".", $x);
        $y = str_replace(",", ".", $y);

        if (is_numeric($x))
            if ($x > -3 && $x < 5)
                $xFlag = true;
            else
                echo '<script>alert(\'not valid value X\')</script>';

        if ($xFlag)
            if (is_numeric($y))
                if ($y > -3 && $y < 3)
                    $yFlag = true;
                else
                    echo '<script>alert(\'not valid value Y\')</script>';

        if ($xFlag && $yFlag)
            for ($i = 0; $i < count($r); $i++) {
                if (!($r[$i] == 1 || $r[$i] == 1.5 || $r[$i] == 2 || $r[$i] == 2.5 || $r[$i] == 3)) continue;

                getPointResult($x, $y, $r[$i]);
                $rFlag = true;
            }

        if (!$rFlag && $xFlag && $yFlag)
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

include('file.html');