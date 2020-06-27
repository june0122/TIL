var gl;

function testGLError(functionLastCalled) {
    /* gl.getError returns the last error that occurred using WebGL for debugging */
    var lastError = gl.getError();

    if (lastError != gl.NO_ERROR) {
        alert(functionLastCalled + " failed (" + lastError + ")");
        return false;
    }
    return true;
}

function initialiseGL(canvas) {
    try {
        // Try to grab the standard context. If it fails, fallback to experimental
        gl = canvas.getContext("webgl") || canvas.getContext("experimental-webgl");
        gl.viewport(0, 0, canvas.width, canvas.height);
    } catch (e) {}

    if (!gl) {
        alert("Unable to initialise WebGL. Your browser may not support it");
        return false;
    }
    return true;
}

function createCubePosColor(sx, sy, sz) {
    var vertexData = [-0.5 * sx, -0.5 * sy, 0.5 * sz, 0.1, 0.8, 0.5, 1.0, // v1
        -0.5 * sx, 0.5 * sy, 0.5 * sz, 0.1, 0.8, 0.5, 1.0, // v2
        0.5 * sx, 0.5 * sy, 0.5 * sz, 0.1, 0.8, 0.5, 1.0, // v3

        0.5 * sx, -0.5 * sy, 0.5 * sz, 0.1, 0.8, 0.5, 1.0, // v8
        0.5 * sx, 0.5 * sy, 0.5 * sz, 0.1, 0.8, 0.5, 1.0, // v3
        -0.5 * sx, -0.5 * sy, 0.5 * sz, 0.1, 0.8, 0.5, 1.0, // v1

        0.5 * sx, 0.5 * sy, -0.5 * sz, 0.3, 1.0, 1.0, 1.0, // v4
        0.5 * sx, -0.5 * sy, -0.5 * sz, 0.3, 1.0, 1.0, 1.0, // v5
        -0.5 * sx, 0.5 * sy, -0.5 * sz, 0.3, 1.0, 1.0, 1.0, // v6

        0.5 * sx, -0.5 * sy, -0.5 * sz, 0.3, 1.0, 1.0, 1.0, // v5
        -0.5 * sx, 0.5 * sy, -0.5 * sz, 0.3, 1.0, 1.0, 1.0, // v6
        -0.5 * sx, -0.5 * sy, -0.5 * sz, 0.3, 1.0, 1.0, 1.0, // v7

        0.5 * sx, 0.5 * sy, 0.5 * sz, 0.2, 1.0, 0.8, 1.0, // v3
        0.5 * sx, 0.5 * sy, -0.5 * sz, 0.2, 1.0, 0.8, 1.0, // v4
        0.5 * sx, -0.5 * sy, -0.5 * sz, 0.2, 1.0, 0.8, 1.0, // v5

        0.5 * sx, 0.5 * sy, 0.5 * sz, 0.2, 1.0, 0.8, 1.0, // v3
        0.5 * sx, -0.5 * sy, -0.5 * sz, 0.2, 1.0, 0.8, 1.0, // v5
        0.5 * sx, -0.5 * sy, 0.5 * sz, 0.2, 1.0, 0.8, 1.0, // v8

        -0.5 * sx, 0.5 * sy, 0.5 * sz, 0.2, 0.9, 0.8, 1.0, // v2
        0.5 * sx, 0.5 * sy, 0.5 * sz, 0.2, 0.9, 0.8, 1.0, // v3
        -0.5 * sx, 0.5 * sy, -0.5 * sz, 0.2, 0.9, 0.8, 1.0, // v6

        0.5 * sx, 0.5 * sy, 0.5 * sz, 0.2, 0.9, 0.8, 1.0, // v3
        0.5 * sx, 0.5 * sy, -0.5 * sz, 0.2, 0.9, 0.8, 1.0, // v4
        -0.5 * sx, 0.5 * sy, -0.5 * sz, 0.2, 0.9, 0.8, 1.0, // v6

        -0.5 * sx, -0.5 * sy, 0.5 * sz, 0.2, 0.6, 0.5, 1.0, // v1
        -0.5 * sx, -0.5 * sy, -0.5 * sz, 0.3, 1.0, 1.0, 1.0, // v7
        0.5 * sx, -0.5 * sy, 0.5 * sz, 0.5, 1.0, 0.5, 1.0, // v8

        0.5 * sx, -0.5 * sy, -0.5 * sz, 0.2, 0.6, 0.5, 1.0, // v5
        -0.5 * sx, -0.5 * sy, -0.5 * sz, 0.3, 1.0, 1.0, 1.0, // v7
        0.5 * sx, -0.5 * sy, 0.5 * sz, 0.5, 1.0, 0.5, 1.0, // v8

        -0.5 * sx, -0.5 * sy, 0.5 * sz, 0.3, 1.0, 0.6, 1.0, // v1
        -0.5 * sx, 0.5 * sy, 0.5 * sz, 0.3, 1.0, 0.6, 1.0, // v2
        -0.5 * sx, -0.5 * sy, -0.5 * sz, 0.3, 1.0, 0.6, 1.0, // v7

        -0.5 * sx, 0.5 * sy, 0.5 * sz, 0.3, 1.0, 0.6, 1.0, // v2
        -0.5 * sx, 0.5 * sy, -0.5 * sz, 0.3, 1.0, 0.6, 1.0, // v6
        -0.5 * sx, -0.5 * sy, -0.5 * sz, 0.3, 1.0, 0.6, 1.0 // v7
    ];

    return vertexData;
}

var shaderProgram;

var elementData = [0, 1, 2, 3, 4, 5];

function initialiseBuffer() {

    gl.vertexBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, gl.vertexBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(createCubePosColor(1.0, 1.0, 1.0)), gl.STATIC_DRAW);

    gl.elementBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, gl.elementBuffer);
    gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(elementData), gl.STATIC_DRAW);

    return testGLError("initialiseBuffers");
}

function initialiseShaders() {

    var fragmentShaderSource = '\
			varying highp vec4 color; \
			void main(void) \
			{ \
				gl_FragColor = color; \
			}';

    gl.fragShader = gl.createShader(gl.FRAGMENT_SHADER);
    gl.shaderSource(gl.fragShader, fragmentShaderSource);
    gl.compileShader(gl.fragShader);
    // Check if compilation succeeded
    if (!gl.getShaderParameter(gl.fragShader, gl.COMPILE_STATUS)) {
        alert("Failed to compile the fragment shader.\n" + gl.getShaderInfoLog(gl.fragShader));
        return false;
    }

    // Vertex shader code
    var vertexShaderSource = '\
			attribute highp vec4 myVertex; \
			attribute highp vec4 myColor; \
			uniform mediump mat4 transformationMatrix; \
			uniform mediump mat4 virwMatrix; \
			uniform mediump mat4 projMatrix; \
			varying highp vec4 color;\
			void main(void)  \
			{ \
				gl_Position = transformationMatrix * myVertex; \
				color = myColor; \
			}';

    gl.vertexShader = gl.createShader(gl.VERTEX_SHADER);
    gl.shaderSource(gl.vertexShader, vertexShaderSource);
    gl.compileShader(gl.vertexShader);
    // Check if compilation succeeded
    if (!gl.getShaderParameter(gl.vertexShader, gl.COMPILE_STATUS)) {
        alert("Failed to compile the vertex shader.\n" + gl.getShaderInfoLog(gl.vertexShader));
        return false;
    }

    // Create the shader program
    gl.programObject = gl.createProgram();
    // Attach the fragment and vertex shaders to it
    gl.attachShader(gl.programObject, gl.fragShader);
    gl.attachShader(gl.programObject, gl.vertexShader);
    // Bind the custom vertex attribute "myVertex" to location 0
    gl.bindAttribLocation(gl.programObject, 0, "myVertex");
    gl.bindAttribLocation(gl.programObject, 1, "myColor");
    // Link the program
    gl.linkProgram(gl.programObject);
    // Check if linking succeeded in a similar way we checked for compilation errors
    if (!gl.getProgramParameter(gl.programObject, gl.LINK_STATUS)) {
        alert("Failed to link the program.\n" + gl.getProgramInfoLog(gl.programObject));
        return false;
    }

    gl.useProgram(gl.programObject);
    console.log("myVertex Location is: ", gl.getAttribLocation(gl.programObject, "myColor"));

    gl.enable(gl.DEPTH_TEST);

    return testGLError("initialiseShaders");
}

var rotX = 0.0;
var rotY = 0.0;
var rotZ = 0.0;


function renderScene() {

    gl.clearColor(0.0, 0.0, 0.0, 1.0);

    gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

    var matrixLocation = gl.getUniformLocation(gl.programObject, "transformationMatrix");

    var rotationX = [
        1.0, 0.0, 0.0, 0.0,
        0.0, Math.cos(rotX), Math.sin(rotX), 0.0,
        0.0, -Math.sin(rotX), Math.cos(rotX), 0.0,
        0.0, 0.0, 0.0, 1.0
    ];

    var rotationY = [
        Math.cos(rotY), 0.0, -Math.sin(rotY), 0.0,
        0.0, 1.0, 0.0, 0.0,
        Math.sin(rotY), 0.0, Math.cos(rotY), 0.0,
        0.0, 0.0, 0.0, 1.0
    ];

    var rotationZ = [
        Math.cos(rotZ), Math.sin(rotZ), 0.0, 0.0, -Math.sin(rotZ), Math.cos(rotZ), 0.0, 0.0,
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    ];

    rotX += 0.01;
    rotY += 0.01;
    rotZ += 0.01;

    if (Math.sin(rotX) > 0) {
        gl.uniformMatrix4fv(matrixLocation, gl.FALSE, rotationY);

    } else {
        gl.uniformMatrix4fv(matrixLocation, gl.FALSE, rotationX);
    }

    if (!testGLError("gl.uniformMatrix4fv")) {
        return false;
    }

    gl.bindBuffer(gl.ARRAY_BUFFER, gl.vertexBuffer);
    gl.enableVertexAttribArray(0);
    gl.vertexAttribPointer(0, 3, gl.FLOAT, gl.FALSE, 28, 0);
    gl.enableVertexAttribArray(1);
    gl.vertexAttribPointer(1, 4, gl.FLOAT, gl.FALSE, 28, 12);
    // gl.vertexAttrib4f(1, Math.random(), 0.0, 1.0, 1.0);

    if (!testGLError("gl.vertexAttribPointer")) {
        return false;
    }

    gl.drawArrays(gl.TRIANGLES, 0, 12 * 3);
    console.log("Enum for Primitive Assumbly", gl.TRIANGLES, gl.TRIANGLE, gl.POINTS);
    if (!testGLError("gl.drawArrays")) {
        return false;
    }

    return true;
}

function updateRotation(index) {
    return function(event, ui) {
        var angleInDegrees = ui.value;
        var angleInRadians = angleInDegrees * Math.PI / 180;
        rotation[index] = angleInRadians;
        drawScene();
    };
}


function main() {
    var canvas = document.getElementById("helloapicanvas");

    if (!initialiseGL(canvas)) {
        return;
    }

    if (!initialiseBuffer()) {
        return;
    }

    if (!initialiseShaders()) {
        return;
    }

    // Render loop
    requestAnimFrame = (function() {
        return window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame ||
            function(callback) {
                window.setTimeout(callback, 1000, 60);
            };
    })();

    (function renderLoop() {
        if (renderScene()) {
            // Everything was successful, request that we redraw our scene again in the future
            requestAnimFrame(renderLoop);
        }
    })();
}