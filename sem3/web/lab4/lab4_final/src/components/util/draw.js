const vector = 5;
const begin = 0;
const end = 340;
const centre = 170;
const mark = 30;

export function drawCanvas(ctx, r) {
    graph(ctx, r);
    axes(ctx);
}

export function drawPointByCursor(event, r) {
    const radius = mark * r;

    const canvas = event.target;
    const ctx = canvas.getContext('2d');

    const rect = canvas.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;

    const valueX = (x - centre) / mark;
    const valueY = (y - centre) / mark * -1;

    /*
    const valueR = radius / mark;
	canvasValues.push({ valueX, valueY });
	selectPoint({ valueX, valueY, valueR });
    */

	drawPoint(ctx, x, y, r, valueX, valueY);

    return { x: valueX, y: valueY };
}

export function drawPointByCoords(ctx, valueX, valueY, valueR) {
    const x = valueX * mark + centre;
    const y = valueY * mark * -1 + centre;
    const r = valueR * mark;
    drawPoint(ctx, x, y, valueR, valueX, valueY);
}

/*
export function drawPointsOnRadiusChange(ctx, ) {
const prevRadius = radius; 
radius = mark * parseFloat(this.value);
ctx.clearRect(0, 0, end, end);
graph();
axes();

  window.canvasValues.forEach((point) => {
    point.valueX = point.valueX * radius / prevRadius;
      point.valueY = point.valueY * radius / prevRadius;
    let x = point.valueX * mark + centre;
    let y = point.valueY * mark * -1 + centre;
    drawPoint(x, y, point.valueX, point.valueY);
});

window.results.forEach((point) => {
    let x = point.x * mark + centre;
    let y = point.y * mark * -1 + centre;
    drawPoint(x, y, point.x, point.y);
});
}
*/

function drawPoint(ctx, x, y, r, valueX, valueY) {
    ctx.beginPath();
    if (checkArea(valueX, valueY, r)) {
      ctx.strokeStyle = 'rgb(146,0,0)';
      ctx.fillStyle = 'rgb(146,0,0)';
  	} else {
      ctx.strokeStyle = 'rgb(0,68,146)';
      ctx.fillStyle = 'rgb(0,68,146)';
    }    ctx.moveTo(x, y);
    ctx.arc(x, y, 3, 2 * Math.PI, 0, false);
    ctx.fill();
    ctx.closePath();
}

function graph(ctx, r) {

    const radius = mark * r;

    ctx.clearRect(0, 0, end, end);

    ctx.beginPath();
    ctx.strokeStyle = 'rgb(0, 146, 146)';
    ctx.fillStyle = 'rgb(0, 146, 146)';
    // Сектор круга
    ctx.moveTo(centre, centre);
    ctx.arc(centre, centre, radius, 0, Math.PI / 2, false);
    // Прямоугольник
    ctx.fillRect(centre - radius / 2, centre, radius / 2, radius);
    // Треугольник
    ctx.moveTo(centre, centre);
    ctx.lineTo(centre, centre - radius);
    ctx.lineTo(centre - radius, centre);

    ctx.fill();

    ctx.stroke();
    ctx.closePath();
}

function axes(ctx) {

    ctx.beginPath();

    ctx.strokeStyle = 'rgb(0,118,118)';

    // line X

    ctx.moveTo(begin, centre);
    ctx.lineTo(end, centre);

    // vector X
    ctx.lineTo(end - vector,centre + vector);
    ctx.moveTo(end, centre);
    ctx.lineTo(end - vector,centre - vector);

    // line Y
    ctx.moveTo(centre, end);
    ctx.lineTo(centre, begin);

    // Vector Y
    ctx.lineTo(centre + vector, begin + vector);
    ctx.moveTo(centre, begin);
    ctx.lineTo(centre - vector, begin + vector);

    ctx.font = "italic 8pt Arial";
    ctx.fillStyle = 'rgb(0,118,118)';
    for (let i = 1; i < 10; ++i) {
        if (i - 5 !== 0) {
            ctx.moveTo(begin + mark * i, centre - 3);
            ctx.lineTo(begin + mark * i, centre + 3);
            ctx.fillText(i - 5, begin + mark * i - 5, centre + 13);
        }
    }

    for (let i = 1; i < 10; ++i) {
        if (5 - i != 0) {
            ctx.moveTo(centre + 3, mark * i);
            ctx.lineTo(centre - 3, mark * i);
            ctx.fillText(5 - i, centre - 13, mark * i + vector);
        }
    }

    ctx.fillText("0", centre - 13, centre + 13);

    ctx.closePath();
    ctx.stroke();
}

function checkArea(x, y, r) {
    return x >= -1 * r / 2 && x <= 0 && y >= -1 * r && y <= 0
        || x >= 0 && y <= 0 && x * x + y * y <= r * r
        || x <= 0 && y >= 0 && y <= x + r;
}
