
function isValidateValues() {
    let y = document.getElementById("input_y");
    let x = document.querySelectorAll('input[type="checkbox"]');
    let r = document.querySelectorAll('input[type="radio"]');

    let inputValues = document.getElementById('values');
    inputValues.value = window.canvasValues.map((pair) => `${pair.valueX}|${pair.valueY}`).join(';');

    return checkX(x) && checkY(y) && checkR(r);
}

function checkX(valueX) {
    for (let value of valueX)
        if (value.checked){
            valueX[0].setCustomValidity("");
            valueX[0].reportValidity();
            return true;
        }
    if (window.canvasValues.length) {
		clearCustomValidity();
		return true;
	}
    valueX[0].setCustomValidity("Выберите значение X");
    valueX[0].reportValidity();
    return false;
}

function checkY(valueY){
    let value = valueY.value;
    value = value.replace(",", ".");

	const emptyY = isNaN(value) || value == null || value === "";

    if (!window.canvasValues.length && emptyY) {
        valueY.setCustomValidity("Введите число");
        valueY.reportValidity();
        return false;
    }

	if (!emptyY) {
	    let floatValue = parseFloat(value);
	    if ((floatValue <= -3) || (floatValue >= 3)){
	        valueY.setCustomValidity("Число Y находится за пределами диапазона");
	        valueY.reportValidity();
	        return false;
	    }
	} else {
		clearCustomValidity();
	}

    valueY.setCustomValidity("");
    valueY.reportValidity();
    return true;
}

function checkDrawingR() {
    let r = document.querySelectorAll('input[type="radio"]');
    if (!checkR(r)) {
        r[0].setCustomValidity("Выберите радиус, чтобы выбрать точку");
        r[0].reportValidity();
        return false;
    }

    return true;
}

function checkR(valueR) {
    for (let value of valueR)
        if (value.checked) {
            valueR[0].setCustomValidity("");
            valueR[0].reportValidity();
            return true;
        }
    valueR[0].setCustomValidity("Выберите значение R");
    valueR[0].reportValidity();
    return false;
}

function clearCustomValidity() {
    let x = document.querySelectorAll('input[type="checkbox"]');
    let y = document.getElementById("input_y");
    let r = document.querySelectorAll('input[type="radio"]');
    x[0].setCustomValidity("");
    x[0].reportValidity();
    y.setCustomValidity("");
    y.reportValidity();
    r[0].setCustomValidity("");
    r[0].reportValidity();
}
