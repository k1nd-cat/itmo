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

include('file.html');