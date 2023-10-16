let flag = true;

function check_input() {
    // let button = document.getElementById("check");
    let x = document.getElementById("input_x").value;
    let y = document.getElementById("input_y").value;
    // let r = document.getElementsByName("value_r");
    // button.visible = true;

    try {
        x = Number(x);
        y = Number(y);

/*        if (typeof r == "undefined") {
            alert("Значение не валидно");
            return false;
        }*/
        // r = Number(r);
        if (!(-5 <= x && x <= 3) || !(-3 <= y && y <= 3)) {
            // button.visible = false;
            alert("Значение не валидно");
            return false;
        }
    } catch (ex) {
        return false;
    }

    return true;
}