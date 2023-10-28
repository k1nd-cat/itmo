const schedule = document.getElementById("schedule"),
    ctx = schedule.getContext('2d');

let vector = 5;
let begin = 0;
let end = 300;
let centre = 150;
let mark = 30;
let radius = mark * 2;

window.canvasValues = [];

setTimeout(() => {
    graph();
    axes();
}, 100);

let r = document.querySelectorAll('input[type="radio"]');
for(let radio in r) {
    r[radio].onclick = function() {
        radius = mark * parseFloat(this.value);
        ctx.clearRect(0, 0, end, end);
        graph();
        axes();
    }
}

function axes() {


    ctx.beginPath();

        ctx.strokeStyle = 'rgb(0,118,118)';
        // ctx.fillStyle = 'rgb(165,0,19)';
        // ctx.strokeStyle = 'rgb(165,0,19)';

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

        for (let i = 1; i < 9; ++i) {
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

function graph() {
    ctx.beginPath();
    ctx.strokeStyle = 'rgb(0, 146, 146)';
    ctx.fillStyle = 'rgb(0, 146, 146)';

    // ctx.fillStyle = 'rgb(0, 146, 146)';
    // ctx.strokeStyle = 'rgb(0, 146, 146)';
    // Сектор круга
    ctx.moveTo(centre, centre);
    ctx.arc(centre, centre, radius, 3 * Math.PI / 2, 0, false);
// Прямоугольник
    ctx.fillRect(centre - radius / 2, centre - radius, radius / 2, radius);
// Треугольник
    ctx.moveTo(centre, centre);
    ctx.lineTo(centre, centre + radius / 2);
    ctx.lineTo(centre + radius / 2, centre);

    ctx.fill();

    ctx.stroke();
    ctx.closePath();
}

function getCursorPosition(canvas, event) {
    const rect = canvas.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;
    
    const valueX = (x - centre) / mark;
    const valueY = (y - centre) / mark * -1;

	canvasValues.push({ valueX, valueY });

    ctx.beginPath();
    ctx.strokeStyle = 'rgb(0,68,146)';
    ctx.fillStyle = 'rgb(0,68,146)';
    ctx.moveTo(x, y);
    ctx.arc(x, y, 3, 2 * Math.PI, 0, false);
    ctx.fill();
    ctx.closePath();
}

const canvas = document.querySelector('canvas')
canvas.addEventListener('mousedown', function(e) {
    if (checkDrawingR())
        getCursorPosition(canvas, e)
})
