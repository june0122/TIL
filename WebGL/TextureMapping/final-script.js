/* (CC-NC-BY) Junseop Lim 2020
    WebGL 1.0 Tutorial - HTML image texture mapping & Phong Shading tutorial */

var gl;

var KaVal = 0.0;
var KdVal = 0.5;
var KsVal = 0.0;
var KshVal = 1.0;
var redVal = 1.0;
var greenVal = 1.0;
var blueVal = 1.0;

rotY = 0.0;
flag_npot = 0;
flag_animation = 1;
flag_delete_texture = 0;

function toggleAnimation() {
    flag_animation ^= 1;
}

function toggleNPOT() {
    flag_npot ^= 1;

    if (flag_npot) {
        alert('NPOT is enabled.');
    } else {
        alert('NPOT is disabled.\n\nOnly power of two textures are supported.');
    }
}

function toggleDeleteTexture() {
    flag_delete_texture ^= 1;
}

// Verify that the value is a power of two
function isPowerOfTwo(x) {
    return (x & (x - 1)) == 0;
}

function testGLError(functionLastCalled) {
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

var vertexData = [
    // Backface (RED/WHITE) -> z = 0.5
    -0.5, -0.5, -0.5, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, -1.0,
    0.5, 0.5, -0.5, 1.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0, -1.0,
    0.5, -0.5, -0.5, 1.0, 0.0, 0.0, 1.0, 1.0, -0.0, 0.0, 0.0, -1.0, -0.5, -0.5, -0.5, 1.0, 0.0, 0.0, 1.0, -0.0, -0.0, 0.0, 0.0, -1.0, -0.5, 0.5, -0.5, 1.0, 0.0, 0.0, 1.0, -0.0, 1.0, 0.0, 0.0, -1.0,
    0.5, 0.5, -0.5, 1.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0, -1.0,
    // Front (BLUE/WHITE) -> z = 0.5      
    -0.5, -0.5, 0.5, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0,
    0.5, 0.5, 0.5, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 1.0,
    0.5, -0.5, 0.5, 0.0, 0.0, 1.0, 1.0, 1.0, -0.0, 0.0, 0.0, 1.0, -0.5, -0.5, 0.5, 0.0, 0.0, 1.0, 1.0, -0.0, -0.0, 0.0, 0.0, 1.0, -0.5, 0.5, 0.5, 0.0, 0.0, 1.0, 1.0, -0.0, 1.0, 0.0, 0.0, 1.0,
    0.5, 0.5, 0.5, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 1.0,
    // LEFT (GREEN/WHITE) -> z = 0.5     
    -0.5, -0.5, -0.5, 0.0, 1.0, 0.0, 1.0, -0.0, -0.0, -1.0, 0.0, 0.0, -0.5, 0.5, 0.5, 0.0, 1.0, 0.0, 1.0, 1.0, 1.0, -1.0, 0.0, 0.0, -0.5, 0.5, -0.5, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, -1.0, 0.0, 0.0, -0.5, -0.5, -0.5, 0.0, 1.0, 0.0, 1.0, -0.0, -0.0, -1.0, 0.0, 0.0, -0.5, -0.5, 0.5, 0.0, 1.0, 0.0, 1.0, -0.0, 1.0, -1.0, 0.0, 0.0, -0.5, 0.5, 0.5, 0.0, 1.0, 0.0, 1.0, 1.0, 1.0, -1.0, 0.0, 0.0,
    // RIGHT (YELLOE/WHITE) -> z = 0.5    
    0.5, -0.5, -0.5, 1.0, 1.0, 0.0, 1.0, -0.0, -0.0, 1.0, 0.0, 0.0,
    0.5, 0.5, 0.5, 1.0, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0,
    0.5, 0.5, -0.5, 1.0, 1.0, 0.0, 1.0, 1.0, 0.0, 1.0, 0.0, 0.0,
    0.5, -0.5, -0.5, 1.0, 1.0, 0.0, 1.0, -0.0, -0.0, 1.0, 0.0, 0.0,
    0.5, -0.5, 0.5, 1.0, 1.0, 0.0, 1.0, -0.0, 1.0, 1.0, 0.0, 0.0,
    0.5, 0.5, 0.5, 1.0, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0,
    // BOTTON (MAGENTA/WHITE) -> z = 0.5 
    -0.5, -0.5, -0.5, 1.0, 0.0, 1.0, 1.0, -0.0, -0.0, 0.0, -1.0, 0.0,
    0.5, -0.5, 0.5, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, -1.0, 0.0,
    0.5, -0.5, -0.5, 1.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0, -1.0, 0.0, -0.5, -0.5, -0.5, 1.0, 0.0, 1.0, 1.0, -0.0, -0.0, 0.0, -1.0, 0.0, -0.5, -0.5, 0.5, 1.0, 0.0, 1.0, 1.0, -0.0, 1.0, 0.0, -1.0, 0.0,
    0.5, -0.5, 0.5, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, -1.0, 0.0,
    // TOP (CYAN/WHITE) -> z = 0.5       
    -0.5, 0.5, -0.5, 0.0, 1.0, 1.0, 1.0, -0.0, -0.0, 0.0, 1.0, 0.0,
    0.5, 0.5, 0.5, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0, 0.0,
    0.5, 0.5, -0.5, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 1.0, 0.0, -0.5, 0.5, -0.5, 0.0, 1.0, 1.0, 1.0, -0.0, -0.0, 0.0, 1.0, 0.0, -0.5, 0.5, 0.5, 0.0, 1.0, 1.0, 1.0, -0.0, 1.0, 0.0, 1.0, 0.0,
    0.5, 0.5, 0.5, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0, 0.0
];

function initialiseBuffer() {
    gl.vertexBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, gl.vertexBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertexData), gl.STATIC_DRAW);

    var texture = gl.createTexture();
    gl.bindTexture(gl.TEXTURE_2D, texture);

    var image = new Image();
    image.src = "sunshine_1024.jpg";
    image.addEventListener('load', function() {
        // Now that the image has loaded make copy it to the texture.
        gl.bindTexture(gl.TEXTURE_2D, texture);
        gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, image);

        if (!flag_npot) {
            gl.generateMipmap(gl.TEXTURE_2D);
        }
    });

    const selectImage = document.getElementById('userImage');

    selectImage.addEventListener('change', function() {
        image.src = document.getElementById('userImage').files[0].name;
        image.onload = function() {
            // if image's width and height is not power of two, show the alert and restart the page.
            if (!flag_npot && (!isPowerOfTwo(this.width) || !isPowerOfTwo(this.height))) {
                alert('Your image size is ' + this.width + ' × ' + this.height + ' pixels.' +
                    '\n\nOpenGL ES 2.0 and WebGL have only limited NPOT support.' +
                    '\n\nPlease upload an image that meets the criteria.');
                location.reload(true);
            }
        }
    });

    return testGLError("initialiseBuffers and texture initialize");
}

function initialiseShaders() {
    var fragmentShaderSource = '\
         precision mediump float;\
         varying highp vec4 color; \
         varying mediump vec2 texCoord;\
         varying highp vec3 v,n; \
         uniform sampler2D sampler2d;\
         uniform float Ka;\
         uniform float Kd;\
         uniform float Ks;\
         uniform float Ksh;\
         uniform float red;\
         uniform float green;\
         uniform float blue;\
         void main(void) \
         { \
            highp vec3 L, E, R; \
            highp vec3 light; \
            highp vec4 light_color; \
            highp float dist;\
            highp vec4 diffuse; \
            highp vec4 specular;\
            highp vec4 specular_color;\
            light = vec3(1.0, 1.0, +2.8); \
            light_color = vec4 (red, green, blue, 1.0); \
            specular_color = vec4 (1.0, 1.0, 1.0, 1.0); \
            L = normalize(light - v);\
            E = normalize(-v);\
            R = normalize(-reflect(L, n)); \
            normalize(light); \
            dist = distance(v, light); \
            dist = 2.0 / (dist*dist); \
            diffuse = light_color  * dist * max(dot(light, n), 0.0); \
            specular = specular_color * pow(max(dot(R,E), 0.05), Ksh); \
            gl_FragColor = Ka * color + Kd*diffuse + Ks*specular + texture2D(sampler2d, texCoord); \
            gl_FragColor.a = 1.0; \
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
         attribute highp vec2 myUV; \
         attribute highp vec3 myNormal; \
         uniform mediump mat4 vmMat; \
         uniform mediump mat4 pMat; \
         uniform mediump mat4 normalMat; \
         varying highp vec4 color;\
         varying mediump vec2 texCoord;\
         varying highp vec3 v; \
         varying highp vec3 n; \
         void main(void)  \
         { \
            n = vec3(normalMat * vec4(myNormal, 1.0)); \
            normalize(n); \
            v = vec3(vmMat * myVertex); \
            gl_Position = pMat * vec4(v, 1.0); \
            color = myColor ; \
            texCoord = myUV*1.0; \
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
    gl.bindAttribLocation(gl.programObject, 2, "myUV");
    gl.bindAttribLocation(gl.programObject, 3, "myNormal");
    // Link the program
    gl.linkProgram(gl.programObject);
    // Check if linking succeeded in a similar way we checked for compilation errors
    if (!gl.getProgramParameter(gl.programObject, gl.LINK_STATUS)) {
        alert("Failed to link the program.\n" + gl.getProgramInfoLog(gl.programObject));
        return false;
    }

    gl.useProgram(gl.programObject);

    return testGLError("initialiseShaders");
}

function renderScene() {
    var vmMat = [];
    var pMat = [];
    var normalMat = [];
    var vmMatLocation = gl.getUniformLocation(gl.programObject, "vmMat");
    var pMatLocation = gl.getUniformLocation(gl.programObject, "pMat");
    var normalMatLocation = gl.getUniformLocation(gl.programObject, "normalMat");

    if (flag_npot) {
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR);
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
    } else if (!flag_npot && flag_delete_texture) {
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR);
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR);
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.MIRRORED_REPEAT);
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.MIRRORED_REPEAT);
    }

    if (flag_animation) {
        rotY += 0.01;
    }

    if (flag_delete_texture) {
        // remove current texture by binding null texture
        gl.bindTexture(gl.TEXTURE_2D, null);
        alert('Texture image has been deleted.');
        flag_delete_texture = 0;
    }

    gl.clearColor(0.0, 0.0, 0.0, 1.0);
    gl.clearDepth(1.0); // Added for depth Test 
    gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT); // Added for depth Test 
    gl.enable(gl.DEPTH_TEST); // Added for depth Test 

    mat4.lookAt(vmMat, [0.0, 0.0, 2.0], [0.0, 0.0, 0.0], [0.0, 1.0, 0.0]);
    mat4.rotateY(vmMat, vmMat, rotY);
    mat4.rotateX(vmMat, vmMat, rotY * 2.2); // control rotation speed by multiplication
    mat4.identity(pMat);
    mat4.perspective(pMat, 3.14 / 3.0, 800.0 / 600.0, 0.5, 5);
    mat4.invert(normalMat, vmMat);
    mat4.transpose(normalMat, normalMat);

    gl.uniformMatrix4fv(vmMatLocation, gl.FALSE, vmMat);
    gl.uniformMatrix4fv(pMatLocation, gl.FALSE, pMat);
    gl.uniformMatrix4fv(normalMatLocation, gl.FALSE, normalMat);

    var KaLoc = gl.getUniformLocation(gl.programObject, "Ka");
    var KdLoc = gl.getUniformLocation(gl.programObject, "Kd");
    var KsLoc = gl.getUniformLocation(gl.programObject, "Ks");
    var KshLoc = gl.getUniformLocation(gl.programObject, "Ksh");
    var redLoc = gl.getUniformLocation(gl.programObject, "red");
    var greenLoc = gl.getUniformLocation(gl.programObject, "green");
    var blueLoc = gl.getUniformLocation(gl.programObject, "blue");

    if (KaLoc != -1) gl.uniform1f(KaLoc, KaVal);
    if (KdLoc != -1) gl.uniform1f(KdLoc, KdVal);
    if (KsLoc != -1) gl.uniform1f(KsLoc, KsVal);
    if (KshLoc != -1) gl.uniform1f(KshLoc, KshVal);
    if (redLoc != -1) gl.uniform1f(redLoc, redVal);
    if (greenLoc != -1) gl.uniform1f(greenLoc, greenVal);
    if (blueLoc != -1) gl.uniform1f(blueLoc, blueVal);



    if (!testGLError("gl.uniformMatrix4fv")) {
        return false;
    }

    gl.bindBuffer(gl.ARRAY_BUFFER, gl.vertexBuffer);
    gl.enableVertexAttribArray(0);
    gl.vertexAttribPointer(0, 3, gl.FLOAT, gl.FALSE, 48, 0);
    gl.enableVertexAttribArray(1);
    gl.vertexAttribPointer(1, 4, gl.FLOAT, gl.FALSE, 48, 12);
    gl.enableVertexAttribArray(2);
    gl.vertexAttribPointer(2, 2, gl.FLOAT, gl.FALSE, 48, 28);
    gl.enableVertexAttribArray(3);
    gl.vertexAttribPointer(3, 3, gl.FLOAT, gl.FALSE, 48, 36);

    if (!testGLError("gl.vertexAttribPointer")) {
        return false;
    }

    gl.drawArrays(gl.TRIANGLES, 0, 36);
    // gl.drawArrays(gl.LINE_STRIP, 0, 36); 
    if (!testGLError("gl.drawArrays")) {
        return false;
    }

    return true;
}

function main() {
    var canvas = document.getElementById("texture-canvas");

    if (!initialiseGL(canvas)) {
        return;
    }

    if (!initialiseBuffer()) {
        return;
    }

    if (!initialiseShaders()) {
        return;
    }

    // renderScene();
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