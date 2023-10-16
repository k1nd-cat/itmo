function fullValidation() {
    return buttonClearClicked || validValues;
}

let buttonClearClicked = false;
let validValues = false;

function isButtonClearClicked() {
    buttonClearClicked = true;
}

function isValidateValues() {
    let x = document.getElementById("input_x");
    let y = document.getElementById("input_y");
    let r = document.querySelectorAll('input[type="checkbox"]');

    validValues = !(!checkInputValue(x, -5, 3)
        || !checkInputValue(y, -3, 3)
        || !checkR(r));
}

function checkInputValue(inputValue, bottomValue, topValue){
    let value = inputValue.value;
    value = value.replace(",", ".");

    if (isNaN(value) || value == null || value === "") {
        inputValue.setCustomValidity("Введите число");
        inputValue.reportValidity();
        return false;
    }

    let floatValue = parseFloat(value);
    if ((floatValue <= bottomValue) || (floatValue >= topValue)){
        inputValue.setCustomValidity("Число находится за пределами диапазона");
        inputValue.reportValidity();
        return false;
    }

    inputValue.setCustomValidity("");
    return true;
}

function checkR(valueR) {
    for (let value of valueR)
        if (value.checked){
            valueR[0].setCustomValidity("");
            return true;
        }
    valueR[0].setCustomValidity("Выберите значение R");
    return false;
}