package com.ardakazanci.jetpackcomposeplaygrounds

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Bundle
import android.text.TextPaint
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.ardakazanci.jetpackcomposeplaygrounds.ui.theme.JetpackComposePlaygroundsTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetpackComposePlaygroundsTheme {

            }
        }
    }
}

// Sample 1
@Composable
fun PongGame() {
    val racketWidthDp = 150f
    val racketHeightDp = 20f
    val ballRadiusDp = 10f
    val density = LocalDensity.current.density

    var racketPosition by remember { mutableFloatStateOf(200f) }
    var ballPosition by remember { mutableStateOf(Offset(100f, 100f)) }
    var ballVelocity by remember { mutableStateOf(Offset(8F, 9f)) }

    LaunchedEffect(key1 = true) {
        while (true) {
            withFrameMillis { _ ->
                ballPosition = ballPosition.copy(
                    x = ballPosition.x + ballVelocity.x,
                    y = ballPosition.y + ballVelocity.y
                )
            }
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {

        // Convert dimensions to pixels
        val racketWidth = racketWidthDp * density
        val racketHeight = racketHeightDp * density
        val ballRadius = ballRadiusDp * density

        // Check for collisions with the window edges
        if (ballPosition.x - ballRadius < 0f || ballPosition.x + ballRadius > size.width) {
            ballVelocity = ballVelocity.copy(x = -ballVelocity.x)
        }
        if (ballPosition.y - ballRadius < 0f || ballPosition.y + ballRadius > size.height) {
            ballVelocity = ballVelocity.copy(y = -ballVelocity.y)
        }

        // Check for collisions with the racket
        if (ballPosition.y + ballRadius >= size.height - racketHeight &&
            ballPosition.x in racketPosition..racketPosition + racketWidth
        ) {
            ballVelocity = ballVelocity.copy(y = -ballVelocity.y)
        }

        // Draw the racket
        drawRect(
            color = Color.Green,
            topLeft = Offset(racketPosition, size.height - racketHeight),
            size = androidx.compose.ui.geometry.Size(racketWidth, racketHeight)
        )

        // Draw the ball
        drawCircle(
            color = Color.Blue,
            center = ballPosition,
            radius = ballRadius
        )

        // Update the racket position to follow the ball
        racketPosition = min(max(ballPosition.x - racketWidth / 2, 0f), size.width - racketWidth)
    }
}


// Sample 2
@Composable
fun SnowFallAction() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF494949))) {
        SnowfallScreen()  // Snowflake Background

        Image(
            painter = painterResource(id = androidx.core.R.drawable.ic_call_answer), // your image set.
            contentDescription = "Cloudy Image",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )
    }
}

@Composable
fun SnowfallScreen() {
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    val snowflakes = remember {
        List(100) {
            Snowflake(
                canvasSize.width.toFloat(),
                canvasSize.height.toFloat()
            )
        }
    }

    LaunchedEffect(canvasSize) {
        while (true) {
            snowflakes.forEach {
                it.updatePosition(
                    canvasSize.width.toFloat(),
                    canvasSize.height.toFloat()
                )
            }
            delay(16)
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { newSize ->
                canvasSize = newSize
            }
    ) {
        snowflakes.forEach { snowflake ->
            drawCircle(
                color = Color.White,
                radius = snowflake.radius,
                center = Offset(snowflake.x.value, snowflake.y.value)
            )
        }
    }
}

class Snowflake(private val screenWidth: Float, private val screenHeight: Float) {
    var x = mutableStateOf(Random.nextFloat() * screenWidth)
    var y = mutableStateOf(Random.nextFloat() * screenHeight - screenHeight)
    var radius = Random.nextFloat() * 4f + 1f
    private var speedY = Random.nextFloat() * 3f + 1f

    fun updatePosition(screenWidth: Float, screenHeight: Float) {
        y.value += speedY

        // Reset snowflake
        if (y.value > screenHeight) {
            y.value = 0f
            x.value = Random.nextFloat() * screenWidth
        }
    }
}

// Sample 3
data class DataPoint(val x: Float, val y: Float)

val staticDataPoints = listOf(
    DataPoint(0f, 100f), DataPoint(1f, 150f), DataPoint(2f, 130f), DataPoint(3f, 170f),
    DataPoint(4f, 160f), DataPoint(5f, 180f), DataPoint(6f, 190f), DataPoint(7f, 200f),
    DataPoint(8f, 170f), DataPoint(9f, 160f), DataPoint(10f, 140f), DataPoint(11f, 150f),
    DataPoint(12f, 130f), DataPoint(13f, 120f), DataPoint(14f, 110f), DataPoint(15f, 130f),
    DataPoint(16f, 140f), DataPoint(17f, 150f), DataPoint(18f, 160f), DataPoint(19f, 180f)
)

@Composable
fun LineGraphScreen() {
    var startAnimation by remember { mutableStateOf(false) }
    val animatableProgress = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    val staticDataPoints = staticDataPoints

    Column {
        Button(onClick = {
            startAnimation = !startAnimation
            coroutineScope.launch {
                animatableProgress.snapTo(0f)
                animatableProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = TweenSpec(durationMillis = 2000)
                )
            }
        }) {
            Text("Start Anim")
        }
        LineGraph(dataPoints = staticDataPoints, animationProgress = animatableProgress.value)
    }
}

@Composable
fun LineGraph(
    dataPoints: List<DataPoint>,
    animationProgress: Float,
    strokeWidth: Float = 5f,
    pointRadius: Float = 8f
) {
    val gridColor = Color.Gray.copy(alpha = 0.3f)
    val gridStrokeWidth = 1f
    val textPaint = TextPaint().apply {
        color = android.graphics.Color.BLACK
        textSize = 30f
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        val graphWidth = size.width
        val graphHeight = size.height

        // Grid & Text Drawing
        val horizontalLineCount = 4
        val verticalLineCount = dataPoints.size

        for (i in 1 until horizontalLineCount) {
            val y = graphHeight / horizontalLineCount * i
            drawLine(gridColor, Offset(0f, y), Offset(graphWidth, y), gridStrokeWidth)
        }

        for (i in 0 until verticalLineCount) {
            val x = (graphWidth / (dataPoints.size - 1)) * i
            drawLine(gridColor, Offset(x, 0f), Offset(x, graphHeight), gridStrokeWidth)
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(
                    dataPoints[i].x.toString(),
                    x,
                    graphHeight - 10f, // padding text
                    textPaint
                )
            }
        }

        // Graph draw
        val path = Path()
        val maxIndex = (dataPoints.size - 1) * animationProgress

        for (i in 0..maxIndex.toInt()) {
            val point = dataPoints[i]
            val scaledX = (point.x / (dataPoints.size - 1)) * graphWidth
            val scaledY = graphHeight - (point.y / 200f) * graphHeight

            if (i == 0) {
                path.moveTo(scaledX, scaledY)
            } else {
                path.lineTo(scaledX, scaledY)
            }

            drawCircle(
                color = Color.Black,
                radius = pointRadius,
                center = Offset(scaledX, scaledY)
            )
        }

        val gradient = Brush.horizontalGradient(
            colors = listOf(Color.Blue, Color.Red),
            startX = 0f,
            endX = graphWidth
        )

        drawPath(
            path = path,
            brush = gradient,
            style = Stroke(width = strokeWidth)
        )
    }
}

// Sample 4
@Composable
fun CustomBarChartWithAnimation() {
    val maxValue = 5
    val values = listOf(1, 2, 3, 4, 5)
    var startAnimation by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        // Button to start the animation.
        Button(
            onClick = { startAnimation = true },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Show Level")
        }

        // Iterate through the values to create each bar.
        values.forEach { value ->

            val currentPercentage = if (startAnimation) value.toFloat() / maxValue else 0f

            val animatedPercentage = animateFloatAsState(
                targetValue = currentPercentage,
                animationSpec = tween(durationMillis = 1000),
                label = "Sample Animation"
            )


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {

                Text(
                    text = "$value",
                    fontSize = 18.sp,
                    modifier = Modifier.width(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Background of the bar - gray
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .weight(1f)
                        .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                ) {
                    // Animated fill of the bar - green
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(animatedPercentage.value * 100.dp) // Width is animated.
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF00885F), Color(0xFF00885F))
                                ),
                                shape = RoundedCornerShape(12.dp) // Rounded corners for the filled part.
                            )
                    )
                }
            }
        }
    }
}

// Sample 5
@Composable
fun ScratchCardView() {
    val context = LocalContext.current
    var touchPoints by remember { mutableStateOf(listOf<Offset>()) }
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(Unit) {
        val originalBitmap = BitmapFactory.decodeResource(context.resources, androidx.core.R.drawable.ic_call_answer).copy( // your fill image
            Bitmap.Config.ARGB_8888, true)
        bitmap = originalBitmap.asImageBitmap()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        // Scratch Image
        Image(
            bitmap = ImageBitmap.imageResource(androidx.core.R.drawable.ic_call_decline_low), // your scratch image
            contentDescription = "Scratch Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        // Fill Image Canvas
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        touchPoints = touchPoints + change.position
                        bitmap?.let {
                            val modifiedBitmap = erase(it.asAndroidBitmap(), touchPoints, 30F)
                            bitmap = modifiedBitmap.asImageBitmap()
                        }
                    }
                }
        ) {
            bitmap?.let {
                drawImage(it, dstSize = IntSize(this.size.width.roundToInt(), this.size.height.roundToInt()) )
            }
        }
    }
}

// Brush Effect for Erase
fun erase(bitmap: Bitmap, touchPoints: List<Offset>, radius: Float): Bitmap {
    val paint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        isAntiAlias = true
        strokeWidth = radius
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    val canvas = android.graphics.Canvas(bitmap)
    val path = Path()

    touchPoints.forEachIndexed { index, point ->
        val adjustedX = point.x - bitmap.width / 4
        val adjustedY = point.y - bitmap.width / 4
        if (index == 0) {
            path.moveTo(adjustedX, adjustedY)
        } else {
            path.lineTo(adjustedX, adjustedY)
        }
    }

    canvas.drawPath(path.asAndroidPath(), paint)

    return bitmap
}

// Sample 6
@Composable
fun SnakeGame() {
    var snakeBody by remember { mutableStateOf(listOf(Offset(100f, 100f))) }
    var foodPosition by remember { mutableStateOf(randomOffset()) }
    var snakeDirection by remember { mutableStateOf(Offset(1f, 0f)) }
    val blockSize = with(LocalDensity.current) { 20.dp.toPx() }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(200)
            val newHead = snakeBody.first().copy(
                x = snakeBody.first().x + blockSize * snakeDirection.x,
                y = snakeBody.first().y + blockSize * snakeDirection.y
            )
            snakeBody = listOf(newHead) + snakeBody.dropLast(1)

            if (newHead.x >= foodPosition.x && newHead.x < foodPosition.x + blockSize &&
                newHead.y >= foodPosition.y && newHead.y < foodPosition.y + blockSize) {
                snakeBody = listOf(newHead) + snakeBody
                foodPosition = randomOffset()
            }
        }
    }

    Canvas(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                val (dragX, dragY) = dragAmount
                snakeDirection = when {
                    abs(dragX) > abs(dragY) -> if (dragX > 0) Offset(1f, 0f) else Offset(-1f, 0f)
                    else -> if (dragY > 0) Offset(0f, 1f) else Offset(0f, -1f)
                }
            }
        }) {
        snakeBody.forEach { bodyPart ->
            drawRect(
                color = Color.Green,
                topLeft = bodyPart,
                size = Size(blockSize, blockSize)
            )
        }

        drawRect(
            color = Color.Red,
            topLeft = foodPosition,
            size = Size(blockSize, blockSize)
        )
    }
}

fun randomOffset(): Offset {
    val random = Random.Default
    return Offset(
        x = random.nextInt(from = 0, until = 300).toFloat(),
        y = random.nextInt(from = 0, until = 600).toFloat()
    )
}

// Sample 7

@Composable
fun RotatableDraggableImage() {
    var rotation by remember { mutableStateOf(0f) }
    var triangleScale by remember { mutableStateOf(1f) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val triangleSize = 100f * triangleScale

            val gradient = Brush.linearGradient(
                colors = listOf(Color.Black, Color.Gray),
                start = Offset(size.width / 2 - triangleSize, size.height / 2 + triangleSize),
                end = Offset(size.width / 2 + triangleSize, size.height / 2 + triangleSize)
            )

            val path = Path().apply {
                moveTo(size.width / 2, size.height / 2 - triangleSize)
                lineTo(size.width / 2 - triangleSize, size.height / 2 + triangleSize)
                lineTo(size.width / 2 + triangleSize, size.height / 2 + triangleSize)
                close()
            }

            drawPath(path, brush = gradient)
        }

        Image(
            painter = painterResource(id = androidx.core.R.drawable.ic_call_answer_video), // your image
            contentDescription = "Sample Image",
            modifier = Modifier
                .padding(top = 500.dp)
                .graphicsLayer(rotationZ = rotation)
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        val centerX = size.width / 2
                        val centerY = size.height / 2
                        val angle = -atan2(
                            change.position.x - centerX,
                            change.position.y - centerY
                        ) * (180f / Math.PI.toFloat())

                        rotation = (rotation + angle).roundToInt().toFloat()
                        triangleScale = abs(sin(Math.toRadians(rotation.toDouble()))).toFloat()
                    }
                }
        )
    }
}

// Sample 8

@Composable
fun FlipCard() {
    var rotationXDegrees by remember { mutableStateOf(0f) }
    var rotationYDegrees by remember { mutableStateOf(0f) }
    val rotationX by animateFloatAsState(
        targetValue = rotationXDegrees,
        animationSpec = tween(durationMillis = 1000)
    )
    val rotationY by animateFloatAsState(
        targetValue = rotationYDegrees,
        animationSpec = tween(durationMillis = 1000)
    )

    val dragGestureModifier = Modifier.pointerInput(Unit) {
        detectDragGestures { _, dragAmount ->
            rotationXDegrees += dragAmount.y * 0.1f
            rotationYDegrees -= dragAmount.x * 0.1f
        }
    }

    Card(
        modifier = Modifier
            .padding(top = 200.dp)
            .fillMaxWidth()
            .graphicsLayer {
                this.rotationX = rotationX
                this.rotationY = rotationY
                cameraDistance = 12 * density
            }
            .then(dragGestureModifier),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = androidx.core.R.drawable.ic_call_answer_low), // your image
                contentDescription = "Front Image"
            )
        }
    }
}

// Sample 9

@Composable
fun AnimatedHeartShape() {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        var clicked by remember { mutableStateOf(false) }
        val heartSize = 400.dp
        val heartSizePx = with(LocalDensity.current) { heartSize.toPx() }

        val infiniteTransition = rememberInfiniteTransition(label = "Sample rememberInfiniteTransition")
        val animatedStrokePhase = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = heartSizePx * 2,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ), label = "Sample AnimatedFloat"
        )

        val continuousScale = infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "Sample AnimatedFloat"
        )

        val disappearScale by animateFloatAsState(
            targetValue = if (clicked) 0f else 1f,
            animationSpec = tween(
                durationMillis = 500,
                easing = LinearOutSlowInEasing
            ), label = "Sample Float As State"
        )

        Canvas(
            modifier = Modifier
                .size(heartSize)
                .clickable { clicked = true }
        ) {
            val path = Path().apply {
                moveTo(heartSizePx / 2, heartSizePx / 5)
                cubicTo(heartSizePx * 3 / 4, 0f, heartSizePx, heartSizePx / 3, heartSizePx / 2, heartSizePx)
                cubicTo(0f, heartSizePx / 3, heartSizePx / 4, 0f, heartSizePx / 2, heartSizePx / 5)
                close()
            }

            val gradient = Brush.linearGradient(
                colors = listOf(Color.Red, Color(0xFF841C26), Color(0xFFBA274A)),
                start = Offset(0f, 0f),
                end = Offset(heartSizePx, heartSizePx)
            )

            val combinedScale = continuousScale.value * disappearScale

            scale(combinedScale, combinedScale, pivot = Offset(heartSizePx / 2, heartSizePx / 2)) {
                drawPath(path, gradient)

                val lineGradient = Brush.linearGradient(
                    colors = listOf(Color(0xFFFFE45E), Color(0xFFFF6392)),
                    start = Offset(0f, 0f),
                    end = Offset(heartSizePx, heartSizePx)
                )

                val pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(heartSizePx, heartSizePx),
                    phase = -animatedStrokePhase.value
                )

                drawPath(
                    path = path,
                    brush = lineGradient,
                    style = Stroke(width = 8.dp.toPx(), pathEffect = pathEffect)
                )
            }
        }
    }
}

// Sample 10

@Composable
fun TicTacToeGame() {
    val gridSize = 3
    val cellSizeDp = 200.dp / gridSize
    val cellSizePx = with(LocalDensity.current) { cellSizeDp.toPx() }
    var grid by remember { mutableStateOf(Array(gridSize) { CharArray(gridSize) { ' ' } }) }
    var currentPlayer by remember { mutableStateOf('X') }
    var winner by remember { mutableStateOf<Char?>(null) }
    var animationOffset by remember { mutableStateOf(0.dp) }

    LaunchedEffect(winner) {
        if (winner != null) {
            animationOffset = -300.dp
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(modifier = Modifier
            .size(200.dp)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val x = (offset.x / cellSizePx).toInt()
                    val y = (offset.y / cellSizePx).toInt()
                    if (grid[y][x] == ' ' && winner == null) {
                        grid = grid.copyOf().also { it[y][x] = currentPlayer }
                        currentPlayer = if (currentPlayer == 'X') 'O' else 'X'
                        winner = checkWinner(grid)
                    }
                }
            }) {

            val gradientBrush = Brush.linearGradient(
                colors = listOf(Color.Yellow, Color.Magenta)
            )


            for (i in 1 until gridSize) {
                drawLine(
                    brush = gradientBrush,
                    start = Offset(x = cellSizePx * i, y = 0f),
                    end = Offset(x = cellSizePx * i, y = 200.dp.toPx()),
                    strokeWidth = 4.dp.toPx()
                )
                drawLine(
                    brush = gradientBrush,
                    start = Offset(x = 0f, y = cellSizePx * i),
                    end = Offset(x = 200.dp.toPx(), y = cellSizePx * i),
                    strokeWidth = 4.dp.toPx()
                )
            }


            for (i in 0 until gridSize) {
                for (j in 0 until gridSize) {
                    val offsetX = cellSizePx * j
                    val offsetY = cellSizePx * i
                    when (grid[i][j]) {
                        'X' -> drawX(offsetX, offsetY, cellSizePx)
                        'O' -> drawO(offsetX, offsetY, cellSizePx)
                    }
                }
            }
        }

        AnimatedVisibility(visible = winner != null) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Winner: ${winner ?: ""}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

private fun checkWinner(grid: Array<CharArray>): Char? {

    for (row in grid) {
        if (row[0] != ' ' && row[0] == row[1] && row[0] == row[2]) {
            return row[0]
        }
    }

    for (col in 0..2) {
        if (grid[0][col] != ' ' && grid[0][col] == grid[1][col] && grid[0][col] == grid[2][col]) {
            return grid[0][col]
        }
    }

    if (grid[0][0] != ' ' && grid[0][0] == grid[1][1] && grid[0][0] == grid[2][2]) {
        return grid[0][0]
    }
    if (grid[0][2] != ' ' && grid[0][2] == grid[1][1] && grid[0][2] == grid[2][0]) {
        return grid[0][2]
    }

    if (grid.all { row -> row.all { cell -> cell != ' ' } }) {
        return 'D'
    }


    return null
}

private fun DrawScope.drawX(offsetX: Float, offsetY: Float, size: Float) {

    val gradientBrush = Brush.linearGradient(
        colors = listOf(Color.Black, Color.Cyan)
    )

    drawLine(
        brush = gradientBrush,
        start = Offset(offsetX, offsetY),
        end = Offset(offsetX + size, offsetY + size),
        strokeWidth = 14F
    )
    drawLine(
        brush = gradientBrush,
        start = Offset(offsetX + size, offsetY),
        end = Offset(offsetX, offsetY + size),
        strokeWidth = 14F
    )
}

private fun DrawScope.drawO(offsetX: Float, offsetY: Float, size: Float) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(Color.Blue, Color.Red)
    )

    drawCircle(
        brush = gradientBrush,
        radius = size / 2,
        center = Offset(offsetX + size / 2, offsetY + size / 2),
        style = Stroke(width = 6.dp.toPx())
    )
}

