const canvas = document.getElementById("hexagonCanvas");
const context = canvas.getContext("2d");

const centerX = canvas.width / 2;
const centerY = canvas.height / 2;
const radius = 60;
const speed = 0.003 ; // 0.001 seconds per frame
const sides = 6;
const angleIncrement = (Math.PI * 2) / sides;


function drawHexagon(x, y, radius, sides, color, angle) {
    context.beginPath();
    context.moveTo(x + radius * Math.cos(angle), y + radius * Math.sin(angle));

    for (let i = 1; i <= sides; i++) {
        const currentAngle = angle + angleIncrement * i;
        context.lineTo(
            x + radius * Math.cos(currentAngle),
            y + radius * Math.sin(currentAngle)
        );
    }

    context.closePath();
    context.fillStyle = color;
    context.fill();
}

function animate() {
    context.clearRect(0, 0, canvas.width, canvas.height);
    const time = Date.now() * speed; // Current time in seconds
    const x = [];
    const y = [];

    for (let i = 0; i < 3; i++) {
        x[i] = centerX +  Math.cos(time + (Math.PI * 2) / 3 * i) * radius * 2;
        y[i] = centerY +  Math.sin(time + (Math.PI * 2) / 3 * i) * radius ;
    }
    drawHexagon(x[0], y[0], radius, sides, "magenta", time);

    const yellowAngle = time + (Math.PI * 2) / 3; // 120 degrees phase shift
    drawHexagon(x[1], y[1], radius, sides, "yellow", yellowAngle);

    const cyanAngle = time + ((Math.PI * 2) / 3) * 2; // 240 degrees phase shift
    drawHexagon(x[2], y[2], radius, sides, "cyan", cyanAngle);

    requestAnimationFrame(animate);
}

animate();

