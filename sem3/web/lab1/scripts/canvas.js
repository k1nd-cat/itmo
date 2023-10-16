var schedule = document.getElementById("schedule"),
    ctx = schedule.getContext('2d');
    ctx.strokeStyle = 'rgb(0,165,165)';
    ctx.fillStyle = 'rgb(0,165,165)';

begin = 5;
end = 295;
centre = 150;
radius = 100;

// Сектор круга
ctx.moveTo(centre, centre);
ctx.arc(centre, centre, radius, 3 * Math.PI / 2, 0, false);
// Прямоугольник
ctx.fillRect(centre, centre, radius, radius / 2);
// Треугольник
ctx.moveTo(centre, centre);
ctx.lineTo(centre, centre - radius / 2);
ctx.lineTo(centre - radius, centre);

ctx.fill();

ctx.beginPath();

if (window._x) {
    x = centre + Number(radius * Number(window._x) / Number(window._r));
    // x = 10;
    y = centre + (-1) * Number(radius * Number(window._y) / Number(window._r));
    ctx.strokeStyle = 'rgb(0,68,146)';
    ctx.fillStyle = 'rgb(0,68,146)';
    ctx.moveTo(x, y);
    ctx.arc(x, y, 3, 2 * Math.PI, 0, false);
    ctx.fill();
    ctx.closePath();
    // alert(window._x);
}

ctx.beginPath();
ctx.strokeStyle = 'rgb(0, 146, 146)';

// line X
ctx.moveTo(begin, centre);
ctx.lineTo(end, centre);

// vector X
ctx.lineTo(end - begin,centre + begin);
ctx.moveTo(end, centre);
ctx.lineTo(end - begin,centre - begin);

// line Y
ctx.moveTo(centre, end - radius / 2);
ctx.lineTo(centre, begin);

// Vector Y
ctx.lineTo(centre + begin, begin + begin);
ctx.moveTo(centre, begin);
ctx.lineTo(centre - begin, begin + begin);

ctx.moveTo(centre + radius / 2, centre - 3);
ctx.lineTo(centre + radius / 2, centre + 3);

ctx.moveTo(centre + radius, centre - 3);
ctx.lineTo(centre + radius, centre + 3);

ctx.moveTo(centre - radius / 2, centre - 3);
ctx.lineTo(centre - radius / 2, centre + 3);

ctx.moveTo(centre + radius, centre - 3);
ctx.lineTo(centre + radius, centre + 3);

ctx.moveTo(centre + 3, centre - radius / 2);
ctx.lineTo(centre - 3, centre - radius / 2);

ctx.moveTo(centre + 3, centre - radius);
ctx.lineTo(centre - 3, centre - radius);

ctx.closePath();
ctx.stroke();
